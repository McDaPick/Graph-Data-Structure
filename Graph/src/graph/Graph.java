
    package graph;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open
 */

/**
 *
 * @author Reeves
 */
public class Graph implements java.io.Serializable {

    ArrayList<Vertex> nodes;
    ArrayList<String> links;
    ArrayList<String> rootpages;
    Soup S;
    public static final int maxSites = 2000;
    public int count = 0;
    public static ArrayList<String> siteList = new ArrayList<>();
    private ArrayList<ArrayList<SiteData>> wordslist = new ArrayList<>();

    public Graph(){
        nodes = new ArrayList<>();
        rootpages = new ArrayList<>();
        S = new Soup();
        
    }

    // Computes the minimum path to all edges that can be reached from
    // the source website, using the weight determined upon writing
    public void minPath(String source) {
                PriorityQueue<Vertex> eq = new PriorityQueue<Vertex>(11, new Comparator<Vertex>() {
    public int compare(Vertex x, Vertex y){
        if(x.minDistance > y.minDistance){
            return -1;
        }
        if(x.minDistance < y.minDistance){
            return 1;
        }
        return 0;
    }
        });
                
        Queue q = new LinkedList();
        Stack path = new Stack();
        Vertex src = null;
        for (Vertex n : nodes) {
        System.out.println(n.name);   
            if (source.equals(n.name)) {
                
                src = n;
            }
        }
        src.minDistance = 0;
        eq.add(src);

        while (!eq.isEmpty()) {
            Vertex n = (Vertex) eq.poll();
            if (n.adjacencies != null) {
                for (Edge e : n.adjacencies) {

                    Vertex edge = e.target;
                    double weight = e.weight;
                    double distancesofar = n.minDistance + weight;
                    if (distancesofar < edge.minDistance) {
                        eq.remove(edge);

                        edge.minDistance = distancesofar;
                        edge.previous = n;
                        eq.add(edge);
                    }

                }
            }
        }

    }

    public Stack getPathTo(String target) {
        Vertex tmp = null;
        Stack q = new Stack();
        for (Vertex n : nodes) {
            if (n.name.equals(target)) {
                tmp = n;
            }
        }
        while (tmp != null) {
            q.add(tmp);
            tmp = tmp.previous;
            
        }
        return q;
    }
    
    public void clearPrev(){
        for(Vertex n : nodes){
            n.previous = null;
        }
    }
    
    //Writes graph to text and serializable file to be persistant and ensure
    // faster run time
    public void writeGraph(String s) throws IOException, Exception {
        FileWriter fw = new FileWriter("sites.txt", true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter out = new PrintWriter(bw);

        FileWriter fw2 = new FileWriter("sitepaths.txt", true);
        BufferedWriter bw2 = new BufferedWriter(fw2);
        PrintWriter out2 = new PrintWriter(bw2);

        OutputStream file = new FileOutputStream("wordslist.ser");
        OutputStream os = new BufferedOutputStream(file);
        ObjectOutput output = new ObjectOutputStream(os);
        //siteList.add(s);
        ArrayList<SiteData> he = S.getWords(s);
        ArrayList<ArrayList<SiteData>> hlist = S.getEdgesAndWords(s);
        writeGraph(he, hlist, out, out2, output, 0);
        out.close();
        out2.close();
        output.close();
    }
    
    //Writes graph to text and serializable file to be persistant and ensure
    // faster run time
    public void writeGraph(ArrayList<SiteData> current, ArrayList<ArrayList<SiteData>> links, PrintWriter pw1, PrintWriter pw2, ObjectOutput out, int rcount) throws IOException, Exception {
        ArrayList<ArrayList<SiteData>> hlist = new ArrayList<>();
        if (current != null) {
            if (!siteList.contains(current.get(0))) {

                siteList.add(current.get(0).url);
                out.writeObject(current);
                
            }
            pw1.println(current.get(0).url);

            for (ArrayList<SiteData> he : links) {
                double cval = compareSites(current, he);
                if (cval < 1.0) {
                    pw2.println(current.get(0).url + " " + he.get(0).url + " " + cval);
                }
            }
            if (rcount < 3) {
                for (ArrayList<SiteData> he : links) {
                    if (!siteList.contains(he.get(0).url)) {
                        writeGraph(he, S.getEdgesAndWords(he.get(0).url), pw1, pw2, out, ++rcount);
                    }
                }
            } else {
                for (ArrayList<SiteData> he : links) {
                    if (!siteList.contains(he.get(0).url)) {
                        pw1.println(he.get(0).url);
                        //System.out.println("hey");
                        out.writeObject(he);
                    }
                }

            }
        }
    }
    // indicates which websites have been added as nodes as to not repeat nodes
    ArrayList<String> edgenodes = new ArrayList<String>();
    
    // After writing the edges to files, reads the files and creates a graph
    //
    public void loadEdges() {
        try {
            FileReader fr1 = new FileReader(new File("sites.txt"));
            BufferedReader br2 = new BufferedReader(fr1);
            FileReader fr = new FileReader(new File("sitepaths.txt"));
            BufferedReader br = new BufferedReader(fr);
            String line;
            ArrayList<String> nodelist = new ArrayList<String>();
            while ((line = br2.readLine()) != null) {
                nodes.add(new Vertex(line, new ArrayList<Edge>()));
                nodelist.add(line);
            }
            br2.close();
            int count = -1;
            while ((line = br.readLine()) != null) {
                Vertex node = null;
                String tar = null;
                Vertex target = null;
                for (Vertex n : nodes) {
                    if (n.name.equals(line.split(" ")[0])) {
                        node = n;
                        tar = line.split(" ")[1];
                    }
                }
                for (Vertex n : nodes) {
                    if (n.name.equals(tar)) {
                        target = n;
                    }
                }

                node.adjacencies.add(new Edge(target, 1 - Double.parseDouble(line.split(" ")[2])));
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private int[] array1;
    private int[] array2;

    
    // Similiarity metric used to weigh the graph (Cosine Similarity function)
    public double compareSites(ArrayList<SiteData> site1, ArrayList<SiteData> site2) throws Exception {
        array1 = new int[site1.size()];
        array2 = new int[array1.length];
        int c = 0;
        for (SiteData sd : site1) {

            for (SiteData sd2 : site2) {
                if (sd.word.equals(sd2.word)) {
                    array1[c] = sd.getCount();
                    array2[c] = sd2.getCount();
                }
            }
            c++;
        }
        return cosine(array1, array2);
    }

    public double cosine(int[] a, int[] b) {
        int top = 0;
        double bot = 0;
        for (int i = 0; i < a.length; i++) {
            top += a[i] * b[i];
        }
        bot = Math.sqrt(sumOfSquares(a)) * Math.sqrt(sumOfSquares(b));
        return top / bot;
    }

    public int sumOfSquares(int[] a) {
        int sum = 0;
        for (int i = 0; i < a.length; i++) {
            sum += a[i] * a[i];
        }
        return sum;
    }
    
   public static void main(String Args[]) throws Exception{
        Graph g = new Graph();
        g.writeGraph("https://en.wikipedia.org/wiki/Super_Smash_Bros.");
    }
}


