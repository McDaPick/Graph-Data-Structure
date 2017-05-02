package graph;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import org.jsoup.Jsoup;
import java.io.Serializable;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class urlGrabber implements Serializable {

    ArrayList<Holder> ListOfHolders = new ArrayList<Holder>();
    HashMap<String, Vertex> vertexMap = new HashMap();
    String[] bigUrl = new String[500];

    public void urlGrabber(String url) throws IOException, FileNotFoundException, ClassNotFoundException {

        Elements links = new Elements();
        Document doc = Jsoup.connect(url).get();
        try {
            doc = Jsoup.connect(url).get();
            links = doc.select("a");
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] temp = new String[10]; //Should use ArrayList for this due to unknown number of URLS, I'm just giving an example
        int counter = 0;

        vertexMap.put(url, new Vertex(url));

        if (links != null) {
            for (Element link : links) {
                String temporary = link.attr("href");
                if (temporary.contains("/wiki/") && !temporary.contains(".org") && !temporary.contains(".jpg") && !temporary.contains(".PNG") && counter < 10) {
                    vertexMap.put(temporary, new Vertex(temporary));
                    String theUrl = "https://en.wikipedia.org" + temporary;

                    vertexMap.put(theUrl, new Vertex(theUrl));
                    temp[counter] = theUrl;

                    Document u1 = Jsoup.connect(url).get();
                    Document u2 = Jsoup.connect(theUrl).get();

                    String x1 = u1.body().text();
                    String x2 = u2.body().text();

                    Cosine_Similarity cs = new Cosine_Similarity();
                    double sim = cs.Cosine_Similarity_Score(x1, x2);
//                    vertexMap.get(url).adjacencies = new Edge[]{new Edge(vertexMap.get(theUrl), sim)};
//                    vertexMap.get(theUrl).adjacencies = new Edge[]{new Edge(vertexMap.get(url), sim)};
                    Holder hold = new Holder(url, theUrl, sim);

                    ListOfHolders.add(hold);
                    
                    counter++;

                }
                
                
            }
            
//            writeToFile(ListOfHolders);
//            readFromFile();
        }
        //  for (int i = 0; i < ListOfHolders.size(); i++) {
             // System.out.println(ListOfHolders.get().url );
        //}
          // urlGrabber1(temp);
    }

    public void urlGrabber1(String[] url) throws IOException, FileNotFoundException, ClassNotFoundException {

        for (int i = 0; i < url.length; i++) {
            Elements links = new Elements();

            try {
                Document doc = Jsoup.connect(url[i]).get();
                links = doc.select("a");
            } catch (IOException e) {
                e.printStackTrace();
            }

            String[] temp = new String[10]; //Should use ArrayList for this due to unknown number of URLS, I'm just giving an example
            int counter = 0;
            if (links != null) {
                for (Element link : links) {
                    String temporary = link.attr("href");
                    if (temporary.contains("/wiki/") && !temporary.contains(".org") && !temporary.contains(".jpg") && !temporary.contains(".PNG") && counter < 10) {
                        String theUrl = "https://en.wikipedia.org" + temporary;

                        vertexMap.put(theUrl, new Vertex(theUrl));
                        temp[counter] = theUrl;

                        Document u1 = Jsoup.connect(url[i]).get();
                        Document u2 = Jsoup.connect(theUrl).get();

                        String x1 = u1.body().text();
                        String x2 = u2.body().text();

                        Cosine_Similarity cs = new Cosine_Similarity();
                        double sim = cs.Cosine_Similarity_Score(x1, x2);

                        Holder hold = new Holder(url[i], theUrl, sim);

                        ListOfHolders.add(hold);

                        counter++;
                    }
                }
            }
            urlGrabber2(temp);
        }

    }

    public void urlGrabber2(String[] url) throws IOException, FileNotFoundException, ClassNotFoundException {

        for (int i = 0; i < url.length; i++) {
            Elements links = new Elements();

            try {
                Document doc = Jsoup.connect(url[i]).get();
                links = doc.select("a");
            } catch (IOException e) {
                e.printStackTrace();
            }

            String[] temp = new String[10]; //Should use ArrayList for this due to unknown number of URLS, I'm just giving an example
            int counter = 0;
            if (links != null) {
                for (Element link : links) {
                    String temporary = link.attr("href");
                    if (temporary.contains("/wiki/") && !temporary.contains(".org") && !temporary.contains(".jpg") && !temporary.contains(".PNG") && counter < 10) {
                        String theUrl = "https://en.wikipedia.org" + temporary;

                        vertexMap.put(theUrl, new Vertex(theUrl));
                        temp[counter] = theUrl;

                        Document u1 = Jsoup.connect(url[i]).get();
                        Document u2 = Jsoup.connect(theUrl).get();

                        String x1 = u1.body().text();
                        String x2 = u2.body().text();

                        Cosine_Similarity cs = new Cosine_Similarity();
                        double sim = cs.Cosine_Similarity_Score(x1, x2);
                      
                         Holder hold = new Holder(url[i], theUrl, sim);
                         
                         if(ListOfHolders.size() < 500){
                             ListOfHolders.add(hold);
                         } 
                        counter++;

                    }
                }
               
            }
        }        
        writeToFile(ListOfHolders);
        readFromFile();
    }
    

    public void writeToFile(ArrayList<Holder> x) {
        try {
            FileOutputStream fos
                    = new FileOutputStream("graph.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(x);
            oos.close();
            fos.close();
            System.out.printf("Data saved in graph.ser");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void readFromFile() throws FileNotFoundException, IOException, ClassNotFoundException {
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
