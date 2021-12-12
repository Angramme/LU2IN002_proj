package lib;

import java.util.LinkedList;
import java.util.ListIterator;
import java.io.File;

public class EatHistory {
    public static final int CHUNK_BUFFER_COUNT = 5;
    private LinkedList<EatHistoryChunk> chunks;
    private File path;

    public EatHistory(File path){
        this.path = path;
        chunks = new LinkedList<EatHistoryChunk>();

        loadLatestChunks();
    }

    private void addChunk(EatHistoryChunk chunker){
        if(chunks.size() == 0){
            chunks.add(chunker);
        }else if(chunker.getBeginTimestamp() < chunks.getFirst().getBeginTimestamp()){
            chunks.addFirst(chunker);
        }else if(chunker.getBeginTimestamp() > chunks.getLast().getBeginTimestamp()){
            chunks.addLast(chunker);
        }else{
            ListIterator<EatHistoryChunk> iter = chunks.listIterator();
            while(chunker.getBeginTimestamp() < iter.next().getBeginTimestamp()){
                iter.add(chunker);
            }
        }
    }

    protected void loadLatestChunks(){
        for(File chf : path.listFiles()){            
            try{
                addChunk(new EatHistoryChunk(chf));
            }catch(Exception ex){
                System.out.println("wtf"+ex);
                ex.printStackTrace();
            }
            if(chunks.size() > CHUNK_BUFFER_COUNT) chunks.removeFirst();
        }
    }

    public void addDataPoint(long timestamp, SimpleFood food) throws Exception {
        if(chunks.size() == 0){
            EatHistoryChunk chnk = new EatHistoryChunk(new File(path.getAbsolutePath() + "/" + timestamp+".chunk"));
            chnk.addDataPoint(timestamp, food);
            addChunk(chnk);
        }else if(timestamp >= chunks.getLast().getBeginTimestamp()){
            if(!chunks.getLast().addDataPoint(timestamp, food)){
                EatHistoryChunk chnk = new EatHistoryChunk(new File(path.getAbsolutePath() + "/" + timestamp+".chunk"));
                chnk.addDataPoint(timestamp, food);
                addChunk(chnk);
            }
        }else if(timestamp >= chunks.get(CHUNK_BUFFER_COUNT-2).getBeginTimestamp()){
            EatHistoryChunk beforelast = chunks.get(CHUNK_BUFFER_COUNT-2);
            EatHistoryChunk.DataPoint entry = beforelast.popLast();
            beforelast.addDataPoint(timestamp, food);
            addDataPoint(entry.timestamp, entry.food);
        }else{
            throw new Exception("cannot add data point! reason : too far back in history!");
        }
        sync();
    }

    public void sync() throws Exception {
        for(EatHistoryChunk chk : chunks){
            chk.sync();
        }
    }

    // protected void loadChunks(int start, int end){

    // }
}
