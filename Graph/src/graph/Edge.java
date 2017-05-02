package graph;

/**
 *
 * @author mnielsen
 */
public class Edge {

    public final Vertex target;
    public final double weight;

     Edge(Vertex argTarget, double argWeight) {
        target = argTarget;
        weight = argWeight;
    }

     public String returnEdges(){
         return ("The distance to " + target + " is " + weight);
                
     }
   
}


