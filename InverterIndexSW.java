import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.LinkedList;
import java.io.IOException;
import java.util.HashMap;
import java.util.Arrays;

/**
 * The Inverter Index class is responsible for making the document index to
 * hold postingList of each word in the document.
 * This index does support stop words.
 * 
 * parsedDocs - the docs read from one file of the corpus
 * index - the index that will hold the postingLists of each word
 * allDocs - all the documents read from the entire corpus will be parsed
 *           and stored here
 * docCount - the total number of documents the corpus holds
 * 
 * @author - Shah Hassan Syed
 */
class InverterIndexSW {
    static ArrayList<Document> parsedDoc = new ArrayList<Document>();
    static ArrayList<String> allTokens = new ArrayList<String>();
    static HashMap<String, PostingList> index = new HashMap<>();
    static ArrayList<Document> allDocs = new ArrayList<Document>();
    public static void main(String fileName) {        
        File corpusDir = new File(fileName);
        File[] corpus = corpusDir.listFiles();
        int docCount = 0;
        //we can re use the parsed documents from the inverter index
        parsedDoc = InverterIndex.allDocs;
        for (Document d : parsedDoc) {
            docCount++;
            String[] tokenArray = Tokenizer.tokenize(d);
            LinkedList<String> currTokens = new LinkedList<String>();
            //all the words including the stop words will be loaded onto the index
            //
            int i = 0;
            for (String s : tokenArray) {                 
                currTokens.add(s);
                if(index.containsKey(s)){                   
                    Posting thisPosting =  new Posting(s, d.getDocID(),d,1, i);                        
                    index.get(s).foundWord(thisPosting, i);                   

                    d.addTermPos(s,i);

                }
                else{          
                    Posting thisPosting =  new Posting(s,d.getDocID(), d, 1, i);
                    PostingList docList = new PostingList(s,1,thisPosting);                   
                    index.put(s, docList);
                    d.addTermPos(s,i);
                }
                i++;
            }  
            d.setTokens(currTokens);                
        }
 

        //Once all the documents are parsed and posting are set, here all the calculation methods
        //are called on each single documents and the postings in there.
        for(Document d: parsedDoc){
            for(Posting p: d.docIndex.values()){
                if(Normaliser.stopWord(p.term)){
                    p.calculateWeightedTf(true);
                }else{
                    p.calculateWeightedTf(false);
                }                
            }
            d.calculateL2Norm();
            for(Posting p: d.docIndex.values()){
                p.calculateNormWeight(d.L2Norm);
            }
        }
        
        //Printing this means that the index including the stopword has been successfully constructed
        System.out.println("___________________________________________________________");
        System.out.println("INITIALIZED INDEX INCLUDING STOP WORDS (Documents = " + docCount + ")");
        System.out.println("___________________________________________________________");
    }
}
