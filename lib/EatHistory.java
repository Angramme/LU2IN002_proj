package lib;

import java.util.LinkedList;
import java.util.ListIterator;

public class EatHistory {
    public static final int CHUNK_BUFFER_COUNT = 10;
    private LinkedList<EatHistoryChunk> chunks;
    private String path;

    public EatHistory(String path){
        this.path = path;
        chunks = new LinkedList<EatHistoryChunk>();
    }

    private void addChunk(EatHistoryChunk chunker){
        if(chunker.getBeginTimestamp() < chunks.getFirst().getBeginTimestamp()){
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

    protected void loadChunks(int start, int end){

    }
}
