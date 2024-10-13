import java.util.ArrayList;
import java.util.Collections;
import java.io.FileWriter;
import java.io.BufferedWriter;  
import java.io.IOException;
/**
 * Printer class is responsible for printing the output of the search
 * New formats can be plugged into the class.
 *
 * @author Shah Hassan Syed
 */
public class Printer
{
    //team name can be plugged in and changed
    private static String teamName = "ShahHassanSyed";

    /**
     * The Print class will print the results to the console
     * 
     * param docs - the arrayList that will contain all the documents from the result
     * param format - the format of the ouput (here we are only using TREC)
     * param type - the type of model to be used in the search
     * param num - the number of results to be printed (the number is ignored for boolean search print)
     * param qID - the ID of the query being searched
     */
    public static void print(ArrayList<Document> docs,String format, int num, String type){     
        //only TREC output format is supported by the system as of now
        if(format.equals("TREC")){
            //for vector search
            if(type.equals("vector")){

                if(docs.isEmpty()){
                    System.out.println("No documents found for your query !");
                }
                else{
                    //sort the documents in descending order
                    Collections.sort(docs, Collections.reverseOrder());
                    //if more than 1000 documents are found
                    if(docs.size() >= num){
                        for(int i = 0; i < num; i++){
                            System.out.println("0 " + "1 " + docs.get(i).getDocID() + " " + (i+1) + " " + docs.get(i).getCosSim() + " " + teamName);

                        }
                    }
                    //if less than 1000 documents are found
                    else{
                        
                        for(int i = 0; i < docs.size(); i++){
                            System.out.println("0 " + "1 " + docs.get(i).getDocID() + " " + (i+1) + " " + docs.get(i).getCosSim() + " " + teamName);

                        }
                    }
                }
            }
            //for boolean search
            if(type.equals("boolean")){
                if(docs.isEmpty()){
                    System.out.println("No documents found for your query !");
                }
                else{
                    for(int i = 0; i < docs.size(); i++){
                        System.out.println("0 " + "1 " + docs.get(i).getDocID() + " " + (i+1) + " " + 1.0 + " " + teamName);
                    }
                }
            }
        }
    }

    /**
     * The Write class will write the results to a file, overwriting previous ones
     * 
     * param docs - the arrayList that will contain all the documents from the result
     * param format - the format of the ouput (here we are only using TREC)
     * param num - the number of results to be printed (the number is ignored for boolean search print)
     * param type - the type of model to be used in the search
     * param fileName - the name of the file that you want the reluts to be appended
     * param qID - the ID of the query being searched
     */
    public static void write(ArrayList<Document> docs,String format, int num, String type, String fileName){

        try{
            //initialize a new object to write to file
            FileWriter fw = new FileWriter(fileName);

            //only TREC output format is supported by the system as of now
            if(format.equals("TREC")){
                //for vector search
                if(type.equals("vector")){
                    if(docs.isEmpty()){
                        System.out.println("\nNo documents found to be printed!");
                    }
                    else{
                        //sort the documents in descending order
                        Collections.sort(docs, Collections.reverseOrder());
                        if(docs.size() >= num){
                            for(int i = 0; i < num; i++){
                                fw.write("0 " + "1 " + docs.get(i).getDocID() + " " + (i+1) + " " + docs.get(i).getCosSim() + " " + teamName + "\n");

                            }
                        }
                        else{
                            for(int i = 0; i < docs.size(); i++){
                                fw.write("0 " + "1 " + docs.get(i).getDocID() + " " + (i+1) + " " + docs.get(i).getCosSim() + " " + teamName + "\n");

                            }
                        }
                        fw.close();                    
                    }
                }
                //for boolean search
                if(type.equals("boolean")){
                    if(docs.isEmpty()){
                        System.out.println("\nNo documents found to be printed!");
                    }
                    else{

                        for(int i = 0; i < docs.size(); i++){
                            fw.write("0 " + "1 " + docs.get(i).getDocID() + " " + (i+1) + " " + 1.0 + " " + teamName + "\n");
                        }

                    }
                    fw.close();                 
                }

            }

        }
        catch(IOException e){

        }
    }

    /**
     * The append class will append the results to a file without overwriting any previous
     * 
     * param docs - the arrayList that will contain all the documents from the result
     * param format - the format of the ouput (here we are only using TREC)
     * param num - the number of results to be printed (the number is ignored for boolean search print)
     * param type - the type of model to be used in the search
     * param fileName - the name of the file that you want the reluts to be appended
     */
    public static void append(ArrayList<Document> docs,String format, int num, String type, String fileName, String qID){

        try{
            //initialize a new object to write to file
            FileWriter fw = new FileWriter(fileName, true);               
            //only TREC output format is supported by the system as of now
            if(format.equals("TREC")){
                //for vector search
                if(type.equals("vector")){
                    if(docs.isEmpty()){
                        System.out.println("\nNo documents found to be printed!");
                    }
                    else{
                        //sort the documents in descending order
                        Collections.sort(docs, Collections.reverseOrder());
                        if(docs.size() >= num){
                            for(int i = 0; i < num; i++){
                               
                                fw.write(qID + " 1 " + docs.get(i).getDocID() + " " + (i+1) + " " + docs.get(i).getCosSim() + " " + teamName + "\n");

                            }
                            fw.close();
                        }
                        else{
                            for(int i = 0; i < docs.size(); i++){                                
                                fw.write(qID + " 1 " + docs.get(i).getDocID() + " " + (i+1) + " " + docs.get(i).getCosSim() + " " + teamName + "\n");

                            }
                            fw.close();

                        }

                    }

                }
                //for boolean search
                if(type.equals("boolean")){
                    if(docs.isEmpty()){
                        System.out.println("\nNo documents found to be printed!");
                    }
                    else{
                        for(int i = 0; i < docs.size(); i++){
                            fw.append(qID + " 1 " + docs.get(i).getDocID() + " " + (i+1) + " " + 1.0 + " " + teamName + "\n");
                        }
                        fw.close();
                    }

                }
            }
        }
        catch(IOException e){

        }
    }
}
