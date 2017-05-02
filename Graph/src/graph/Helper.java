/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

/**
 *
 * @author mnielsen
 */
public class Helper {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Vertex A = new Vertex("Apples");
        Vertex B = new Vertex("B");
        Vertex D = new Vertex("D");
        Vertex F = new Vertex("F");
        Vertex K = new Vertex("K");
        Vertex J = new Vertex("J");
        Vertex M = new Vertex("M");
        Vertex G = new Vertex("G");
        Vertex O = new Vertex("O");
        Vertex P = new Vertex("P");
        Vertex R = new Vertex("R");
        Vertex Z = new Vertex("Z");
        Vertex ZZ = new Vertex("E");

        // set the edges and weight
        A.adjacencies.add(new Edge(B, 8));
        A.adjacencies.add(new Edge(D, 8));
        B.adjacencies.add(new Edge(D, 11));
        D.adjacencies.add(new Edge(B, 11));
        D.adjacencies.add(new Edge(O, 3));
        F.adjacencies.add(new Edge(K, 23));
        K.adjacencies.add(new Edge(O, 40));
//        J.adjacencies = new Edge[]{new Edge(K, 25)};
//        M.adjacencies = new Edge[]{new Edge(R, 8)};
//        O.adjacencies = new Edge[]{new Edge(Z, 4)};
//        P.adjacencies = new Edge[]{new Edge(Z, 18)};
//        R.adjacencies = new Edge[]{new Edge(P, 15)};
//        Z.adjacencies = new Edge[]{new Edge(P, 18)};
//        ZZ.adjacencies = new Edge[]{new Edge(O, 20)};
        
        
        
        computePaths(A); // run Dijkstra
        System.out.println("Distance to " + O + ": " + O.minDistance);
        List<Vertex> path = getShortestPathTo(D);
        System.out.println("Path: " + path);
    }

    public static void computePaths(Vertex source) {
        source.minDistance = 0.;
        PriorityQueue<Vertex> vertexQueue = new PriorityQueue<Vertex>();
        vertexQueue.add(source);

       //System.out.println(vertexQueue);
        while (!vertexQueue.isEmpty()) {
            Vertex u = vertexQueue.poll();

            //      System.out.println(u);
            // Visit each edge exiting u
            for (int i = 0; i < u.adjacencies.size(); i++) {
                //         System.out.println(u.adjacencies[i].weight);
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

}
