import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
/**
 * A document object that will be created on the base of the documents retrieved
 * from the Financial Times corpus
 * 
 * @author Shah Hassan Syed
 */
class Document implements Comparable<Document> {
    //all the meta data of the document
    String profile;
    String docID;
    Posting posting;
    int date;
    String headLine;
    String byLine;
    String dateLine;
    String publisher;
    String page;
    String parsedText;
    String companies;
    String countries;
    String industries;
    String types;    
    //ArrayList to hold tokens
    LinkedList<String> txtTokens = new LinkedList<String>();
    //the hashmap that will hold all the postings of words in this document along with their vector calculations
    HashMap<String, Posting> docIndex = new HashMap<>();
    //the L2 norm of this document
    double L2Norm = 0.0;
    //the cosSim of this document according to the query
    double cosSim = 0.0;
    /**
     *Construct a document object
     */
    public Document(){
        this.profile = "";
        this.docID = "";
        this.posting = new Posting();
        this.date = 0000;
        this.headLine = "";
        this.byLine = "";
        this.dateLine = "";
        this.publisher = "";
        this.page = "";
        this.parsedText = "";
        this.txtTokens = null;      
    }

    /**
     * Set the docID to the user defined docID
     */
    public void setDocID(String docID){
        this.docID=docID;
    }

    /**
     *Return the doc ID of this document
     */
    public String getDocID(){
        return this.docID;
    }

    /**
     * Set the profile of this document to the user defined profile
     */
    public void setProfile(String prof){
        this.profile = prof;
    }

    /**
     * Set the date of this document to the user defined date
     */
    public void setDate(int date){
        this.date = date;
    }

    /**
     * Set the headLine of this document to the user defined headLine
     */
    public void setHeadLine(String hd){
        this.headLine = hd;
    } 

    /**
     * Set the byLine of this document to the user defined byLine
     */
    public void setByLine(String bl){
        this.byLine = bl;
    }

    /**
     * Set the dateLine of this document to the user defined dateLine
     */
    public void setDateLine(String dl){
        this.dateLine = dl;   
    }

    /**
     * Set the text of this document to the user defined text
     */
    public void setText(String txt){
        this.parsedText=txt;
    }

    /**
     * Set the publisher of this document to the user defined publisher
     */
    public void setPub(String publisher){
        this.publisher = publisher;
    }

    /**
     * Set the page of this document to the user defined page
     */
    public void setPage(String page){
        this.page = page;
    }

    /**
     * Set tokens list of the document to the user defined token List
     */
    public void setTokens(LinkedList<String> normalTokens){
        this.txtTokens = normalTokens;
    }

    /**
     * Set the countries of this document to the user defined countries
     */
    public void setCountries(String cn){
        this.countries = cn;
    }

    /**
     * Set the companies of this document to the user defined companies
     */
    public void setCompanies(String co){
        this.companies = co;
    }

    /**
     * Set the industries of this document to the user defined industries
     */
    public void setIndustries(String in){
        this.industries = in;
    }

    /**
     * Set the types of this document to the user defined types
     */
    public void setTypes(String tp){
        this.types = tp;
    }

    /**
     * get the text of this document
     */
    public String getText(){
        return this.parsedText;
    }
    
    /**
     * get the tokens of the terms in this document
     */ 
    public LinkedList<String> getTokens(){
        return txtTokens;
    }    

    /**
     * Add a position of a term in the hashmap of postings
     */
    public void addTermPos(String word, int pos){
        //if term already exists then do not create a a new posting
        //just add the position of the term and increase frequency
        if(docIndex.containsKey(word)){
            docIndex.get(word).addPosition(pos);  
            docIndex.get(word).addFrequency();
        }
        //else make a new posting for the word and add to the document hashmap
        else{
            Posting wordPost = new Posting(word, this.docID,this, 1, pos);
            docIndex.put(word, wordPost);
        }
    }   

    
    /**
     * Method to caluclate the L2 norm of the document
     */
    public void calculateL2Norm(){
        Iterator termIterator = docIndex.values().iterator();
        double sum = 0.0;
        while(termIterator.hasNext()){
            Posting currWord = (Posting)termIterator.next();
            sum += Math.pow(currWord.getWtf(),2);         
        }
        this.L2Norm = Math.sqrt(sum);
    }

    /**
     * returns the L2 norm of the document
     */
    public double getL2(){
        return this.L2Norm;   
    }

    /**
     * Method to caluclate the Cosine Similarity Score of the document
     */
    public void calculateCosSim(HashMap<String, Posting> hm, String term){
         double cosSimi = 0.0;       

        Posting p = this.docIndex.get(term);
        double nw = p.getNW();
        cosSimi += nw * hm.get(term).getWtf();

        //add cosine similarity based on this term to the already existing cosine similarity
        this.cosSim += cosSimi;
    }

    /**
     * get the cosSim of this document according to the query
     */
    public double getCosSim(){
        return this.cosSim;
    }

    /**
     * Overriding the compareTo method to sort documents according to the cosine similarity
     */
    public int compareTo(Document doc){
        if(this.cosSim > doc.cosSim){
            return 1;
        }
        else if(this.cosSim < doc.cosSim){
            return -1;
        }
        else{
            return 0;
        }
    }
}