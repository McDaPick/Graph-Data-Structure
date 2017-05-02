
package graph;

import java.io.Serializable;

/**
 *
 * @author mnielsen
 */
public class Holder  implements Serializable  {
    String url;
    String nextUrl;
    Double sim;
    public Holder(String url, String nextUrl, Double sim){
        this.url = url;
        this.nextUrl = nextUrl;
        this.sim = sim;
}
    
    public String getAll(){
        return ("First Url : " + url  + " Next Url : "+ nextUrl + " Similarity : " + sim);
    }
}
