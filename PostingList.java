import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * This is a postingList class that will be used to hold a the postings of a word
 * through out all the document
 *
 * @author Shah Hassan Syed
 */
class PostingList{
    //the actual word for which the postingList exists
    String term = "";

    //the frequency of the word in the whole Corpus
    int totalFrequency;

    //the postings for the word in each document individually
    ArrayList<Posting> postings = new ArrayList<Posting>();

    //the idf weight of the word throghout the corpus
    double idf = 0.0;    

    /**
     * Constructor for postingList 
     */
    public PostingList(){
        this.totalFrequency = 0;        
    }

    /**
     * Constructor with predefined parameters
     */
    public PostingList(String term, int freq){
        this.term = term;
        this.totalFrequency = freq;        
    }

    /**
     * Constructor with predefined params
     */
    public PostingList(String term,int freq, Posting p){
        this.term = term;
        this.totalFrequency = freq;
        this.postings.add(p);
        updateIdf();
    }

    /**
     * returns frequency of the word in the corpus
     */
    public int getFrequency(){
        return this.totalFrequency;
    }

    /**
     * method to update the postingList if the word already exists is the index, 
     * so no new PostingList is created
     */
    public void foundWord(Posting p, int position){
        this.totalFrequency++;
        boolean iBool =false;
        int i = 0;
        while(i<postings.size() && iBool == false){
            if(postings.get(i).getDocID().equals(p.getDocID())){
                postings.get(i).addFrequency();
                postings.get(i).addPosition(position);
                iBool = true;
            }
            else{
                i++;
                if(i == postings.size()){
                    postings.add(p);
                    iBool = true;
                }
            }          
        }
        //update the idf of each word right after it is found each time
        updateIdf();
    }

    /**
     * Returns posting associated with 
     */
    public ArrayList<Posting> getPostings(){
        return this.postings;
    }

    /**
     * Return an arrayList that holds all the positions where the word occurs in a certain document
     * 
     * param docID - which document to look for the position
     */
    public LinkedList<Integer> returnPositions(String docID){
        LinkedList<Integer> positions = new LinkedList<Integer>();
        for(Posting p: postings){
            if(p.getDocID().equals(docID)){
                positions = p.getPositions();
            }
        }
        return positions;
    }

    /**
     * method to know if there exists a word at a certain position in the document
     * Best used for the phrase Search
     * 
     * param docID -  the document in which to search
     * param i - the position to check for the existence of the word
     */
    public boolean isAtPosition(String docID, int i){       
        //check in all the Postings one by one
        for(Posting p: postings){
            if(p.getDocID().equals(docID)){
                //if the list of positions  for a certain word contains i, that means the word exists
                if(p.listPositions.contains(i)){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Method to check if a document exists in the postingList, 
     * alternatively checking if the word is contained in a certain document
     * 
     * param docID - the document to be checked for
     */
    public boolean containsDoc(String docID){
        for(Posting p: postings){
            if(p.getDocID().equals(docID)){
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the term for which the postingList exists
     */
    public String getTerm(){
        return this.term;   
    }

    /**
     * Method that will calculate idf of the word according to the certain instance
     */
    public void updateIdf(){
        double n = InverterIndex.getCorpusCount();
        double nt = (double)(postings.size());
        this.idf = Math.log10(n/nt);
    }

    /**
     * Returns the idf of this word, for which the posting list exists
     */
    public double getIdf(){
        return this.idf;   
    }

}