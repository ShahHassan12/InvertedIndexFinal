import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileNotFoundException;
/**
 * This class will parse batch query and send it for computation
 *
 * @author Michael Scanlon & Shah Hassan Syed
 */
public class BatchQuery
{
    /**
     * This method will parse batch query and return an ArrayList of each term
     *
     */
    public static ArrayList<Query> parse(String fileName){
        ArrayList<Query> qList = new ArrayList<Query>();
        File batchDir = new File(fileName);
        try{
            Scanner qs = new Scanner(batchDir);
            String currentLine = "";            
            while(qs.hasNext()){
                //A new string for each query that we add
                Query thisQ = new Query();
                String query = "";
                //keep looking for the docno
                while(!currentLine.equals("<DOCNO>")){              
                    currentLine = qs.nextLine();                    
                }               
                currentLine = qs.nextLine();
                thisQ.setDocID(currentLine);
                currentLine = qs.nextLine();
                
                
                //Once we get past the meta data of the query, we append the query into a single varaible
                //we keep on appending until next query is found                          
                while(!currentLine.equals("<DOCNO>") && qs.hasNext()){                    
                    currentLine = qs.nextLine();                    
                    //we only need to add lines that are not blank
                    if(currentLine.length() != 0 && !currentLine.equals("<DOCNO>")){
                        query +=" " + currentLine;
                    }                    
                }
                thisQ.setQuery(query);
                qList.add(thisQ);
            }
            return qList;
        }catch(FileNotFoundException e){
            System.out.println("FILE DOES NOT EXIST!");
        }

        return null;
    }
}
