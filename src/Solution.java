import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Tom on 02/04/2017.
 */
public class Solution {
    private File inputFile, outputFile;
    private int numRequests, numCaches, cacheServerCapacity;
    private Endpoints[] endpoints;
    int[] videos;
    List<Vector<Integer>> videosInCache;

    public Solution(String inputString) throws IOException{
        inputFile = new File("input/" + inputString);
        outputFile = new File("ouput/solution.txt");
    }

    public void init() throws IOException{
        String line;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile)));
        line = bufferedReader.readLine();
        // Input test: // System.out.println("line 1: " + line);
        String[] arraySizes = line.split(" ");

        videos = new int[Integer.parseInt(arraySizes[0])];
        endpoints =  new Endpoints[Integer.parseInt(arraySizes[1])];
        numRequests =  Integer.parseInt(arraySizes[2]);
        numCaches =  Integer.parseInt(arraySizes[3]);
        cacheServerCapacity = Integer.parseInt(arraySizes[4]);

        line = bufferedReader.readLine();
        // Input test: // System.out.println("video sizes: " + line);
        String[] data = line.split(" ");

        for(int i = 0; i < videos.length; i++) {
            videos[i] = Integer.parseInt(data[i]);
        }

        line = bufferedReader.readLine();
        data = line.split(" ");

        // load endpoints
        int datacenterLatency;
        int numberOfCaches;
        for(int i = 0; i < endpoints.length; i++) {
            // Input test: // System.out.println("Endpoint " + i + ": " + line);
            datacenterLatency = Integer.parseInt(data[0]);
            numberOfCaches = Integer.parseInt(data[1]);

            endpoints[i] = new Endpoints(datacenterLatency, numCaches, videos.length);
            for(int j = 0; j < numberOfCaches; j++) {
                line = bufferedReader.readLine();
                // Input test: // System.out.println("    cache " + j + ": " + line);
                data = line.split(" ");
                endpoints[i].setCache(Integer.parseInt(data[0]), Integer.parseInt(data[1]));
            }
            line = bufferedReader.readLine();
            data = line.split(" ");
        }

        // Add video requests
        int numberOfRequests;
        int videoNumber;
        int endpoint;

        for(int x=0;x<numRequests;x++){
            // Input test: // System.out.println("Video requests " + j + ": " + line);
            data = line.split(" ");
            videoNumber = Integer.parseInt(data[0]);
            endpoint = Integer.parseInt(data[1]);
            numberOfRequests = Integer.parseInt(data[2]);
            endpoints[endpoint].addVideoRequest(videoNumber, numberOfRequests);
            line = bufferedReader.readLine();
        }

        //printData();
    }

    private void printData() {
        System.out.println("videos | Count: " + videos.length + " | ");
        for(int i = 0; i < endpoints.length; i++) {
            System.out.println("-- Endpoint " + i + " --");
            System.out.println(endpoints[i].printEndpoint());
        }
        System.out.println("cacheServers | Count: " + numCaches + " | ");
        System.out.println("cacheServerCapacity | Count: " + cacheServerCapacity);
    }

    public void solve(){
        videosInCache = new ArrayList<Vector<Integer>>(numCaches);

        for(int x=0;x<numCaches;x++) { //Iterates through each cache
            double[] latencySavedPerVideo = new double[videos.length];    //Stores
            for (int y = 0; y < videos.length; y++) {
                latencySavedPerVideo[y] = 0;
                for (int z = 0; z < endpoints.length; z++) {
                    if (endpoints[z].isConnectedToCache(x) && endpoints[z].hasVideoRequests(y)) {
                        latencySavedPerVideo[y] += endpoints[z].getLatencySaved(y, x);
                    }
                }
            }
            videosInCache.add(new Vector<>());

            boolean full = false;
            int cacheSpaceTaken = 0;
            System.out.println("Cache " + x + ": Capacity " + cacheServerCapacity);
            double totalSaved =0;
            while (!full) {
                full = true;
                int bestVidIndex = -1;
                double saved = 0;
                for (int y = 0; y < latencySavedPerVideo.length; y++) {
                    if(bestVidIndex<=-1&&(videos[y] + cacheSpaceTaken) <= cacheServerCapacity && latencySavedPerVideo[y] >0){
                        saved = latencySavedPerVideo[y];
                        bestVidIndex = y;
                        full = false;
                    }
                    else if (latencySavedPerVideo[y] >= saved && (videos[y] + cacheSpaceTaken) <= cacheServerCapacity && latencySavedPerVideo[y] >0) {
                        if (latencySavedPerVideo[y] == saved) {
                            if (videos[y] <= videos[bestVidIndex]) {
                                saved = latencySavedPerVideo[y];
                                bestVidIndex = y;
                            }
                        } else {
                            saved = latencySavedPerVideo[y];
                            bestVidIndex = y;
                        }
                        full = false;
                    }
                }
                if(full == false){
                    videosInCache.get(x).add(bestVidIndex);
                    for(int y=0;y<endpoints.length;y++){
                        if(endpoints[y].hasVideoRequests(bestVidIndex)&&endpoints[y].isConnectedToCache(x)){
                            endpoints[y].setVideoRequest(bestVidIndex, x);
                        }
                    }
                    cacheSpaceTaken += videos[bestVidIndex];
                    System.out.println(bestVidIndex + "\t" + cacheSpaceTaken + "\t" + saved);
                    latencySavedPerVideo[bestVidIndex] = -1;
                    totalSaved += saved;
                }
            }
            System.out.println("Space used:\t" + cacheSpaceTaken + "\t\t" + totalSaved);
        }
    }
}
