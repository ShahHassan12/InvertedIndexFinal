
/**
 * This class will hold the query if it is coming from a file
 */
public class Query
{
    //the doc ID of the query
    public String docID;
    //the actual query to be searched
    public String query;    

    /**
     * Constructor of Query class 
     */
    public Query()
    {
        this.docID = "";
        this.query = "";        
    }
    
    /**
     * Constructor of Query class that will take in user defined parameters
     */
    public Query(String id, String q)
    {
        this.docID = id;
        this.query = q;        
    }

    /**
     * set the id to a user defined ID 
     */
    public void setDocID(String id){
        this.docID = id;   
    }

    /**
     * set the query to a user defined query
     */
    public void setQuery(String query){
        this.query = query;   
    }

    /**
     * Return the queryID 
     */
    public String getDocID(){
        return this.docID;   
    }

    /**
     * Returns the query 
     */
    public String getQuery(){
        return this.query;   
    }

}
