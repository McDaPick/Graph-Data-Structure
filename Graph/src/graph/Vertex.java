package graph;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * @author mnielsen
 */
public class Vertex implements Comparable<Vertex>, Serializable {

    public final String name;
    public ArrayList<Edge> adjacencies = new ArrayList<Edge>();
    public double minDistance;
    public Vertex previous;

    Vertex(String argName) {
        name = argName;
    }

    Vertex(String line, ArrayList<Edge> arrayList) {
        name = line;
        adjacencies = arrayList;
    }
    
    public ArrayList<Edge>  getAdj(){
        return this.adjacencies;
    }

    public String toString() {
        return name;
    }

    public int compareTo(Vertex other) {
        return Double.compare(minDistance, other.minDistance);
    }

    public void getURL() throws IOException {
        File input = new File("/tmp/input.html");
        Document doc = Jsoup.parse(input, "UTF-8", "http://example.com/");

    }

    public static void computePaths(Vertex source) {
        source.minDistance = 0.;
        PriorityQueue<Vertex> vertexQueue = new PriorityQueue<Vertex>();
        vertexQueue.add(source);

        while (!vertexQueue.isEmpty()) {
            Vertex u = vertexQueue.poll();

            // Visit each edge exiting u
             for (int i = 0; i < u.adjacencies.size(); i++) {
                Edge e = u.adjacencies.get(i);
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


    public static void getLinks() throws IOException {
        Document doc = Jsoup.connect("http://en.wikipedia.org/wiki/Boston").timeout(5000).get();

        Element intro = doc.select("table.infobox + p").first();
        while (intro.tagName().equals("p")) {
    //here you will get an Elements object which you can
            //iterate through to get the links in the intro

            System.out.println(intro.select("a"));
            intro = intro.nextElementSibling();
        }

        for (Element h2 : doc.body().select("h2")) {
            if (h2.select("span").size() == 2) {
                if (h2.select("span").get(1).text().equals("Geography")) {
                    Element nextsib = h2.nextElementSibling();
                    while (nextsib != null) {
                        if (nextsib.tagName().equals("p")) {
                            //here you will get an Elements object which you
                            //can iterate through to get the links in the 
                            //geography section
                            System.out.println(nextsib.select("a"));
                            nextsib = nextsib.nextElementSibling();
                        } else if (nextsib.tagName().equals("h2")) {
                            nextsib = null;
                        } else {
                            nextsib = nextsib.nextElementSibling();
                        }
                    }
                }
            }
        }
    }
}
