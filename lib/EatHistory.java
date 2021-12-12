package lib;

import java.util.LinkedList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;
import java.io.File;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class EatHistory {
    interface ChunkEach{
        public void run(EatHistoryChunk chunk);
    }

    public static final int CHUNK_BUFFER_COUNT = 5;
    private LinkedList<EatHistoryChunk> chunks;
    private File path;

    public EatHistory(File path){
        this.path = path;
        chunks = new LinkedList<EatHistoryChunk>();

        loadLatestChunks();
    }

    public long getBeginTimestamp(){
        return chunks.getFirst().getBeginTimestamp();
    }
    public long getEndTimestamp(){
        return chunks.getLast().getEndTimestamp();
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
                Pattern pt = Pattern.compile("([0-9]+)\\.chunk");
                Matcher mc = pt.matcher(chf.getName());

                if(!mc.matches()){
                    System.out.println(String.format(" ! warning: Invalid file present in %s", path.getAbsolutePath()));
                    continue;
                }
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

    protected void loadChunks(long start, long end) throws Exception {
        for(File f : path.listFiles()){
            Pattern pt = Pattern.compile("([0-9]+)\\.chunk");
            Matcher mc = pt.matcher(f.getName());

            if(!mc.matches()){
                System.out.println(String.format(" ! warning: Invalid file present in %s", path.getAbsolutePath()));
                continue;
            }
            long ts = Long.parseLong(mc.group(1));
            if(ts >= start && ts <= end){
                EatHistoryChunk removed = null;
                if(ts < getBeginTimestamp()){
                    removed = chunks.getLast();
                    chunks.removeLast();
                    EatHistoryChunk chunker = new EatHistoryChunk(f);
                    chunks.addFirst(chunker);
                }else if(ts > getEndTimestamp()){
                    removed = chunks.getFirst();
                    chunks.removeFirst();
                    EatHistoryChunk chunker = new EatHistoryChunk(f);
                    chunks.addLast(chunker);
                } // else : its already loaded!

                if(removed != null){
                    if(removed.getBeginTimestamp() >= start && removed.getBeginTimestamp() <= end){
                        throw new Exception("cannot load the entire interval into memory! (too big) ");
                    }
                }
            }
        }
    }

    public void eachDataPoint(EatHistoryChunk.DataPointEach func){
        ChunkEach F = (EatHistoryChunk chunk) -> {
            chunk.eachDataPoint(func);
        };
        eachChunk(F);
    }

    public void eachDataPoint(long start, long end, EatHistoryChunk.DataPointEach func) throws Exception {
        ChunkEach F = (EatHistoryChunk chunk) -> {
            chunk.eachDataPoint(start, end, func);
        };
        eachChunk(start, end, F);
    }

    public void eachChunk(ChunkEach func){
        for(EatHistoryChunk chk : chunks){
            func.run(chk);
        }
    }
    public void eachChunk(long start, long end, ChunkEach func) throws Exception {
        HashSet<Long> called = new HashSet<Long>();
        if(start < getEndTimestamp() && end > getBeginTimestamp()){
            if(chunks.getFirst().getBeginTimestamp() >= start){
                Iterator<EatHistoryChunk> it = chunks.iterator();
                while(it.hasNext()){
                    EatHistoryChunk chk = it.next();
                    if(chk.getBeginTimestamp() > end) break;
                    func.run(chk);
                    called.add(chk.getBeginTimestamp());
                }
            }else{
                Iterator<EatHistoryChunk> it = chunks.descendingIterator();
                while(it.hasNext()){
                    EatHistoryChunk chk = it.next();
                    if(chk.getEndTimestamp() < start) break;
                    func.run(chk);
                    called.add(chk.getBeginTimestamp());
                }
            }
        }
        for(File f : path.listFiles()){
            Pattern pt = Pattern.compile("([0-9]+)\\.chunk");
            Matcher mc = pt.matcher(f.getName());

            if(!mc.matches()){
                System.out.println(String.format(" ! warning: Invalid file present in %s", path.getAbsolutePath()));
                continue;
            }
            long ts = Long.parseLong(mc.group(1));

            if(ts >= start && ts <= end && !called.contains(ts)){
                EatHistoryChunk chk = new EatHistoryChunk(f);
                func.run(chk);
            }
        }
    }
}
