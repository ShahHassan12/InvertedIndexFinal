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
 * This index does not support stop words.
 * 
 * parsedDocs - the docs read from one file of the corpus
 * index - the index that will hold the postingLists of each word
 * allDocs - all the documents read from the entire corpus will be parsed
 *           and stored here
 * docCount - the total number of documents the corpus holds
 */
class InverterIndex {
    //parsedDocs will contain
    static ArrayList<Document> parsedDoc = new ArrayList<Document>();    
    
    //index will hold all the terms along with their postingLists
    static HashMap<String, PostingList> index = new HashMap<>();
    
    //allDocs will hold all the documents that are present in the entire corpus
    //so they can be used later on.
    static ArrayList<Document> allDocs = new ArrayList<Document>();
    
    //these docs will contain the postings of all the non-stop word index postings
    static ArrayList<Document> nswDocs = new ArrayList<Document>();
    
    public static int docCount = 0;
    /**
     * The main method to provide the functionality of parsing documents, creating index and calculations
     * 
     * corpusDir - the directory that hold the corpus documents
     */
    public static void main(String fileName) {       
        File corpusDir = new File(fileName);
        File[] corpus = corpusDir.listFiles();

        for(File mainFile:corpus){    
            parsedDoc = Parser.parse(mainFile);
            for (Document d : parsedDoc) {
                //add to all Docs so we dont have to parse again for the stop words inverter index
                allDocs.add(d);                
                docCount++;
                String[] tokenArray = Tokenizer.tokenize(d);
                LinkedList<String> currTokens = new LinkedList<String>();                
                //Only the words that are not stopwords will be added to index              
                for (String s : tokenArray) {
                    if (Normaliser.stopWord(s) == true) {
                    } 
                    else {
                        currTokens.add(s);     
                    }        
                }  
                //set the tokens seperated to the tokens of the document
                d.setTokens(currTokens);
                //add the modified doc for futher computational purposed
                nswDocs.add(d);
            } 
        }
        
        for(Document d: nswDocs) {         
            for (int i = 0; i < d.getTokens().size(); i++)                
            {        
                String word = d.getTokens().get(i);
                //Only create one posting list per term, if it already exists, then just add the posting
                //to the posting list
                if(index.containsKey(word)){
                    Posting thisPosting =  new Posting(word, d.getDocID(),d, 1, i);                        
                    index.get(word).foundWord(thisPosting, i);
                    //Add the posting to the document too, for calculation purposes
                    d.addTermPos(word,i);
                }
                else{          
                    Posting thisPosting =  new Posting(word,d.getDocID(),d, 1, i);
                    PostingList docList = new PostingList(word,1,thisPosting);                   
                    index.put(word, docList);
                    //Add the posting to the document too, for calculation purposes
                    d.addTermPos(word,i);
                }
            }
        } 
        
        //Once all the documents are parsed and posting are set, here all the calculation methods
        //are called on each single documents and the postings in there.
        for(Document d: nswDocs){
            for(Posting p: d.docIndex.values()){
                p.calculateWeightedTf(false);
            }
            d.calculateL2Norm();
            for(Posting p: d.docIndex.values()){
                p.calculateNormWeight(d.L2Norm);
            }
        }

        //Printing this means that the index excluding the stopword has been successfully constructed
        System.out.println("___________________________________________________________");
        System.out.println("INITIALIZED INDEX EXCLUDING STOP WORDS (Documents = " + docCount + ")");
        System.out.println("___________________________________________________________");

    }

    /**
     * This method will return the number of document the corpus holds
     */
    public static int getCorpusCount(){
        return docCount;
    }
}

