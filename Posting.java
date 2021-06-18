import java.util.ArrayList;
import java.util.LinkedList;
/**
 * This class holds the object of posting that will be used to host positions of a word in
 * a single document
 */
public class Posting 
{
    //the term for which the posting exists
    public String term = "";
    //the document ID of this posting
    public String docID;
    //the entire document of this posting
    public Document doc;
    //the frequency of the term in this document
    public int frequency;    
    //the positions at which the term exsts
    public LinkedList<Integer> listPositions;
    //the weightedTf of this word according to the document
    public double weightedTf = 0.0;    
    //the normalised weight of the term according to the document
    public double normWeight = 0.0;

    /**
     * Constrctor of a posting 
     */
    public Posting(){
        this.docID = "";  
        this.frequency = 0;
        listPositions = new LinkedList<Integer>();
    }

    /**
     * Constructor of a posting with predefinded parameters
     */
    public Posting(String term,String id, Document doc, int freq, int position){
        this.term = term;
        this.docID = id;
        this.doc = doc;
        this.frequency = freq;  
        listPositions = new LinkedList<Integer>();
        listPositions.add(position);
    }

    /**
     * Set doc ID of the posting
     */
    public void setDocID(String docID){
        this.docID = docID;   
    }

    /**
     * Retrun doc ID of the posting
     */
    public String getDocID(){
        return this.docID;   
    }

    /**
     * set the frequency of the posting to the user defined frequency
     */
    public void setFrequency(int freq){
        this.frequency = freq;
    }

    /**
     * Increase the frequency of the term by 1
     */
    public void addFrequency(){
        this.frequency++;
    }

    /**
     * return frequency of the term
     */
    public int getFrequency(){
        return this.frequency;
    }
    
    /**
     * add a position to the position list of the term
     */
    public void addPosition(int p){
        this.listPositions.add(p);     
    }

    /**
     * Return the position list
     */
    public LinkedList<Integer> getPositions(){
        return this.listPositions;
    }

    /**
     * Method to check if the word exists at a certain position in the document
     */
    public boolean isAtPosition(int i){
        if(listPositions.contains(i)){
            return true;
        }
        return false;
    }

    /**
     * This method will calculate the weighted Tf of a word
     * 
     * param sw - if the word is stop word or no, to accordingly search in the right index
     */
    public void calculateWeightedTf(boolean sw){
        if(sw){
            double idf = InverterIndexSW.index.get(this.term).getIdf();
            double wtf = (1+Math.log10(this.frequency))*(idf);
            this.weightedTf = wtf;
        }
        else{
            double idf = InverterIndex.index.get(this.term).getIdf();
            double wtf = (1+Math.log10(this.frequency))*(idf);
            this.weightedTf = wtf;
        }
    }

    /**
     * Return the weighted tf of the term
     */
    public double getWtf(){
        return this.weightedTf;
    }

    /**
     * Method to calculate the norm weight of the word
     */
    public void calculateNormWeight(double l2){
        double normWeight = (double)(weightedTf/l2);
        this.normWeight = normWeight;
    }

    /**
     * Return the normalized weight of the term
     */
    public double getNW(){
        return this.normWeight;
    }
}
