import java.util.HashMap;

/**
 * Created by Tom on 02/04/2017.
 */
public class Endpoints {
    private class Videos{
        private int delay, requests, id;

        private Videos(int id, int requests, int delay){
            this.id = id;
            this.requests = requests;
            this.delay = delay;
        }

        public int getDelay(){
            return delay;
        }

        public  int getRequests(){
            return requests;
        }

        public int getId(){
            return id;
        }
    }

    private int[] cacheLatency;
    private int datacenterLatency;
    private Videos[] videos;

    Endpoints(int datacenterLatency, int numCaches, int numVideos) {
        this.datacenterLatency = datacenterLatency;
        cacheLatency = new int[numCaches];
        videos = new Videos[numVideos];
    }

    void setCache(int cacheId, int latency) {
        cacheLatency[cacheId] = latency;
    }

    void addVideoRequest(int videoId, int numberOfRequests) {
        videos[videoId] = new Videos(videoId, numberOfRequests, datacenterLatency);
    }

    void setVideoRequest(int videoId, int cacheId) {
        videos[videoId].delay=cacheLatency[cacheId];
    }

    int getCacheLatency(int cacheId){
        return cacheLatency[cacheId];
    }

    boolean isConnectedToCache(int cacheId){
        return cacheLatency[cacheId]!=0;
    }

    int getLatencySaved(int videoId, int cacheId){
        int numRequests = videos[videoId].requests;
        return numRequests * videos[videoId].delay - numRequests * cacheLatency[cacheId];
    }

    boolean hasVideoRequests(int videoId){
        return videos[videoId]!=null;
    }

    String printEndpoint() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("» Datacenter latency: ").append(datacenterLatency).append("\n");
        int x=0;
        for(int cache : cacheLatency) {
            if(cache!=0){
                stringBuilder.append("  Cache ").append(x).append(", latency ").append(cache).append("ms\n");
            }
            x++;

        }
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    public double getScore(){
        double scoreForEndpoint=0;
        for(int x=0;x<videos.length;x++){
            if(hasVideoRequests(x)){
                scoreForEndpoint += (datacenterLatency - videos[x].delay) * videos[x].requests;
            }
        }
        return scoreForEndpoint;
    }
}