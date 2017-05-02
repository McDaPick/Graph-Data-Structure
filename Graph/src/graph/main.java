package graph;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.PriorityQueue;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class main {

    public static void main(String[] args) throws IOException, FileNotFoundException, ClassNotFoundException {
//        readFromFile();

        //   urlGrabber wow = new urlGrabber();
        Cosine_Similarity cs1 = new Cosine_Similarity();

        //  wow.urlGrabber("https://en.wikipedia.org/wiki/List_of_Nintendo_64_games");
//        int counter = 1;
//        for (int i = 0; i < wow.ListOfHolders.size(); i++) {
//            System.out.println(counter + " " + wow.ListOfHolders.get(i).url);
//            counter++;
//        }
        FileInputStream fis = new FileInputStream("graph.ser");
        BufferedInputStream bis = new BufferedInputStream(fis);
        ObjectInputStream ois = new ObjectInputStream(bis);
        ArrayList<Holder> obj = (ArrayList<Holder>) ois.readObject();
        ois.close();

        HashMap<String, Vertex> vertexMap = new HashMap();

        vertexMap.put(obj.get(0).url, new Vertex(obj.get(0).url));

        for (int i = 0; i < obj.size(); i++) {
            vertexMap.put(obj.get(i).nextUrl, new Vertex(obj.get(i).nextUrl));

            vertexMap.get(obj.get(0).url).adjacencies.add(new Edge(vertexMap.get(obj.get(i).nextUrl), (obj.get(i).sim)));
            vertexMap.get(obj.get(i).url).adjacencies.add(new Edge(vertexMap.get(obj.get(0).nextUrl), (obj.get(0).sim)));

        }

//        vertexMap.put(obj.get(0).url, new Vertex(obj.get(0).url));
//        vertexMap.put(obj.get(1).nextUrl, new Vertex(obj.get(1).nextUrl));
//        vertexMap.put(obj.get(2).nextUrl, new Vertex(obj.get(2).nextUrl));
//        vertexMap.put(obj.get(3).nextUrl, new Vertex(obj.get(3).nextUrl));
//        vertexMap.put(obj.get(4).nextUrl, new Vertex(obj.get(4).nextUrl));
//
//        vertexMap.get(obj.get(0).url).adjacencies.add(new Edge(vertexMap.get(obj.get(1).nextUrl), (obj.get(1).sim)));
//        vertexMap.get(obj.get(0).url).adjacencies.add(new Edge(vertexMap.get(obj.get(2).nextUrl), (obj.get(2).sim)));
//        vertexMap.get(obj.get(0).url).adjacencies.add(new Edge(vertexMap.get(obj.get(3).nextUrl), (obj.get(3).sim)));
//        vertexMap.get(obj.get(0).url).adjacencies.add(new Edge(vertexMap.get(obj.get(4).nextUrl), (obj.get(4).sim)));
//        vertexMap.get(obj.get(0).url).adjacencies.add(new Edge(vertexMap.get(obj.get(5).nextUrl), (obj.get(5).sim)));
//        vertexMap.get(obj.get(0).url).adjacencies.add(new Edge(vertexMap.get(obj.get(6).nextUrl), (obj.get(6).sim)));
        computePaths(vertexMap.get(obj.get(2).url));
        List<Vertex> path = getShortestPathTo(vertexMap.get(obj.get(3).nextUrl));
        System.out.println("Path: " + path);

    }

    public static void computePaths(Vertex source) {
        source.minDistance = 0;
        PriorityQueue<Vertex> vertexQueue = new PriorityQueue<Vertex>();
        vertexQueue.add(source);

        //System.out.println(vertexQueue);
        while (!vertexQueue.isEmpty()) {
            Vertex u = vertexQueue.poll();

            //      System.out.println(u);
            // Visit each edge exiting u
            for (Edge e : u.adjacencies) {
                Vertex v = e.target;
                double weight = e.weight;
                double distanceThroughU = u.minDistance + weight;
                if (distanceThroughU < v.minDistance) {
                    vertexQueue.remove(v);
                    v.minDistance = distanceThroughU;
                    v.previous = u;
                    vertexQueue.add(v);
                }
            }
        }
    }

    public static List<Vertex> getShortestPathTo(Vertex target) {
        List<Vertex> path = new ArrayList<Vertex>();
        for (Vertex vertex = target; vertex != null; vertex = vertex.previous) {
            path.add(vertex);
        }

        Collections.reverse(path);
        return path;
    }

    public static void readFromFile() throws FileNotFoundException, IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream("graph.ser");
        BufferedInputStream bis = new BufferedInputStream(fis);
        ObjectInputStream ois = new ObjectInputStream(bis);
        ArrayList<Holder> obj = (ArrayList<Holder>) ois.readObject();
        ois.close();
        for (int i = 0; i < obj.size(); i++) {
            System.out.println(obj.get(i).getAll());
        }

    }

}
