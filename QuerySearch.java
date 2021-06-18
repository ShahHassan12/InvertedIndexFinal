import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.IOException;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Collections;
import java.io.FileNotFoundException;
import java.util.LinkedList;
/**
 * The query class; reads in the query from the user and decides how to parse and
 * which function to further call
 *
 * Michael Scanlon & Shah Hassan Syed
 */
class QuerySearch{
    //a hashmap to hold all the terms in the query for computation
    public static HashMap<String, Posting> queryPost; 
    /**
     * The main method to run a query search
     *
     * Michael Scanlon & Shah Hassan Syed
     */
    public static void query(){
        //array that will contain the token of a single term search or a boolean search
        String[] queryTokens;

        //Array that will contain the tokens of the phrase search query
        String[] phraseQTokens;

        Scanner qr = new Scanner(System.in);
        System.out.println();

        //identify what type of query do you want to run
        String type = "";

        //identify what type search model do you want to use (Boolean or Vector)

        ArrayList<Document> docList = new ArrayList<Document>();
        ArrayList<Document> phraseList;       

        ArrayList<Document> simDocs;
        while(!type.equalsIgnoreCase("EXIT")){
            System.out.println("Type in 'BATCH' or 'SINGLE' to run the type of query OR type in 'EXIT' to exit the system");
            type = qr.nextLine();

            //this is an important prompt because 

            if(type.equalsIgnoreCase("Single")){                

                System.out.println("Enter the query to be searched");
                String query = qr.nextLine();

                //Create a hashmap for all the query terms for computational purposes
                queryPost = new HashMap<>();

                simDocs = new ArrayList<Document>();
                if(query.substring(0,1).equals("\"") && query.substring(query.length()-1).equals("\"")){
                    phraseQTokens = Tokenizer.phraseTokenize(query); 
                    phraseList = new ArrayList<Document>();
                    boolean stp = false;
                    for(String s:phraseQTokens){
                        if(Normaliser.stopWord(s)==true){
                            stp = true;
                        }
                    }

                    //if there are stop words in the phrase
                    if(stp == true){
                        ArrayList<String> qPhrase = new ArrayList<String>();

                        //we will the positions of the terms in the document
                        //to compute consecutive term occurences 
                        LinkedList<Integer> pos = new LinkedList<Integer>();  

                        //add each of the tokens from the query to the hashmap for computational purposes
                        for(String q: phraseQTokens){
                            qPhrase.add(q);  
                            if(queryPost.containsKey(q)){
                                queryPost.get(q).addFrequency();
                            }
                            else{
                                Posting p = new Posting(q,"query", null, 1, 0);
                                queryPost.put(q,p);
                            }
                        } 

                        //calculate the weighted Tf of each term in the query
                        for(Posting p: queryPost.values()){
                            p.weightedTf = Math.sqrt(1+Math.log10(p.frequency));                    
                        }

                        //since it is a phrase, search for all the documents that contains all the terms in the phrase
                        //this will narrow down the documents for further search
                        docList = andSearch(qPhrase, true);

                        //Seach for the occurences of the terms in a consecutive manner for the phrase search
                        for(Document d: docList){
                            String searchWord = phraseQTokens[0];
                            pos = InverterIndexSW.index.get(searchWord).returnPositions(d.getDocID());
                            for(int p = 0; p < pos.size(); p++){
                                int j = 0; 
                                while(j<phraseQTokens.length-1){
                                    String searchWord2 = phraseQTokens[j+1];
                                    PostingList pToken = InverterIndexSW.index.get(phraseQTokens[j+1]);
                                    int qPos = pos.get(p)+j+1;
                                    if(pToken.isAtPosition(d.getDocID(),qPos)){
                                        j++;
                                        if(j==phraseQTokens.length-1 && !phraseList.contains(d)){
                                            phraseList.add(d);
                                        }

                                    }  
                                    else{
                                        j = phraseQTokens.length;  
                                    }
                                }

                            }
                        }
                        docList = phraseList;                        

                    }//if there are no stop words in the phrase, do the same search but in the non-stopWord index instead

                    else{
                        ArrayList<String> qPhrase = new ArrayList<String>();            
                        LinkedList<Integer> pos = new LinkedList<Integer>();                
                        for(String q: phraseQTokens){
                            qPhrase.add(q);  
                            if(queryPost.containsKey(q)){
                                queryPost.get(q).addFrequency();
                            }
                            else{
                                Posting p = new Posting(q,"query",null, 1, 0);
                                queryPost.put(q,p);
                            }
                        } 
                        for(Posting p: queryPost.values()){
                            p.weightedTf = Math.sqrt(1+Math.log10(p.frequency));                    
                        }

                        docList = andSearch(qPhrase, true);

                        for(Document d: docList){
                            String searchWord = phraseQTokens[0];
                            pos = InverterIndex.index.get(searchWord).returnPositions(d.getDocID());
                            for(int p = 0; p < pos.size(); p++){
                                int j = 0; 
                                while(j<phraseQTokens.length-1){
                                    String searchWord2 = phraseQTokens[j+1];
                                    PostingList pToken = InverterIndex.index.get(phraseQTokens[j+1]);
                                    int qPos = pos.get(p)+j+1;
                                    if(pToken.isAtPosition(d.getDocID(),qPos)){
                                        j++;
                                        if(j==phraseQTokens.length-1 && !phraseList.contains(d)){
                                            phraseList.add(d);
                                        }

                                    }  
                                    else{
                                        j = phraseQTokens.length;  
                                    }
                                }

                            }
                        }
                        docList = phraseList;                    
                    }        

                }
                //If the phrase is not entered in the query along with qoutation, then query will be parsed to find
                //any boolean term, if no boolean term is found then OR is considered default
                else{
                    queryTokens = Tokenizer.booleanTokenize(query);          
                    for(String q: queryTokens){
                        if(!q.equals("AND") && !q.equals("OR") && !q.equals("NOT")){
                            if(queryPost.containsKey(q)){
                                queryPost.get(q).addFrequency();
                            }
                            else{
                                Posting p = new Posting(q,"query",null, 1, 0);
                                queryPost.put(q,p);
                            }
                        }
                    }

                    //calculate the weighted Tf of each term in the query
                    for(Posting p: queryPost.values()){
                        p.weightedTf = Math.sqrt(1+Math.log10(p.frequency));                    
                    }

                    //search for boolean terms and send them in for that boolean search
                    for(int i = 0; i < queryTokens.length; i++){
                        //search for AND
                        if(queryTokens[i].equals("AND") && i != queryTokens.length-1 && i!= 0){
                            ArrayList<String> qTokens = new ArrayList<String>();
                            qTokens.add(queryTokens[i+1]);
                            qTokens.add(queryTokens[i-1]);
                            boolean stp = false;
                            for(String q: qTokens){
                                if(Normaliser.stopWord(q)){
                                    stp= true;
                                }
                            }

                            docList = andSearch(qTokens, stp);
                            i = queryTokens.length;
                        }
                        //search for OR
                        else if(queryTokens[i].equals("OR")&& i != queryTokens.length-1 && i!= 0){
                            ArrayList<String> qTokens = new ArrayList<String>();
                            qTokens.add(queryTokens[i+1]);
                            qTokens.add(queryTokens[i-1]);  
                            boolean stp = false;
                            for(String q: qTokens){
                                if(Normaliser.stopWord(q)){
                                    stp= true;
                                }                           
                            }  

                            docList = orSearch(qTokens, stp);
                            i = queryTokens.length;
                        }
                        //search for NOT
                        else if(queryTokens[i].equals("NOT") && i != queryTokens.length-1){                    
                            boolean stp = false;                
                            if(Normaliser.stopWord(queryTokens[i+1])){
                                stp= true;
                            }                       

                            docList = notSearch(queryTokens[i+1], stp);
                            i = queryTokens.length;
                        }
                        //OR is considered default
                        else{
                            ArrayList<String> qTokens = new ArrayList<String>();
                            qTokens.add(queryTokens[i]);
                            boolean stp = false;
                            for(String q: qTokens){
                                if(Normaliser.stopWord(q)){
                                    stp= true;
                                }
                            }  
                            docList = orSearch(qTokens, stp);

                        }
                    }
                }
                System.out.println();          

                //Send the computed document list to the printer class along with the number of results to be 
                //printed to terminal, the type of system required to run, vector or boolean
                Printer.write(docList, "TREC", 1000, "boolean", "BooleanOutput.txt");
                //the system will also write the results to a file, the file can be specified by the user
                Printer.write(docList, "TREC", 1000, "vector", "VectorOutput.txt"); 
                

                System.out.println("Boolean results succesfully written to BooleanOutput.txt");
                System.out.println("Vector results succesfully written to VectorOutput.txt.");

                System.out.println("\n<<CAUTION: RESULTS FROM PREVIOUS SEARCH WILL BE OVERWRITTEN IN THE FILES IN THE NEXT SEARCH>>\n");
            }
            //If the users chooses the batch option this part will be executed
            else if (type.equalsIgnoreCase("BATCH")){    

                System.out.println("\nPlease enter the file name, containing the batch queries, along with the extension\n");
                String batchFile = qr.nextLine(); 
                BatchSearch(batchFile);         

            }
            //If the users choosed to exit the system
            else if (type.equalsIgnoreCase("EXIT")){

            }
            else{
                //if an invalid option for the search type is entered
                System.out.println("INVALID OPTION! \nPlease enter the right choice!\n\n");                   
            }
        }
    }

    /**
     * The batch search will be based off an external query file that will be
     * read in and parsed
     * 
     * @param model - the model to be used for the search
     * @param batchFile - name of the file that contain queries
     * 
     * Michael Scanlon & Shah Hassan Syed
     */
    public static void BatchSearch(String batchFile){    
        //initialize a new query term hashmap to overwrite the previous terms
        queryPost = new HashMap<>();
        try{
            ArrayList<Query> queryList = BatchQuery.parse(batchFile); 
            //we can specify the file to which we want to write the results to
            String booleanFile = "BooleanResults.txt";
            String vectorFile = "VectorResults.txt";

            //for each query in the batch we will compute results individually
            for(Query q: queryList){  
                //take out the query from each Query obkject and make tokens of it
                String[] qPTokens = Tokenizer.phraseTokenize(q.getQuery());
                ArrayList<String> searchArray =  new ArrayList<String>();
                //we will send all the tokens for the OR search
                for(String s : qPTokens){
                    searchArray.add(s);
                    //Add all the tokens to the hashmap for computational purposes
                    if(queryPost.containsKey(s)){
                        queryPost.get(s).addFrequency();
                    }
                    else{
                        Posting p = new Posting(s,"query", null, 1, 0);
                        queryPost.put(s,p);
                    }
                }
                for(Posting p: queryPost.values()){
                    p.weightedTf = Math.sqrt(1+Math.log10(p.frequency));                    
                }

                /**
                 * IMPORTANT!
                 * BATCH QUERY FUNCTIONALITY
                 * In the function below we can modify wether we want to search through the stopWord
                 * index or the one without stop words using the second paramerter of orSearch method
                 * (True will search through stopWord index, while false will not)   
                 */

                Boolean stopWord = false;

                ArrayList<Document> docs = orSearch(searchArray,stopWord);
                //we will send the documents to be printed to an external user specified file            
                Printer.append(docs, "TREC", 1000, "boolean", booleanFile, q.getDocID());  
                Printer.append(docs, "TREC", 1000, "vector", vectorFile, q.getDocID());

                //we do not want to add frequency of previous queries
                queryPost.clear();
                //we need to clear the CosSimilarity of previous query Search
                if(stopWord){
                    for(Document d: InverterIndexSW.parsedDoc){
                        d.cosSim = 0.0;                    
                    }
                } 
                else{
                    for(Document d: InverterIndex.nswDocs){
                        d.cosSim = 0.0;
                    }
                }
            }
            
            System.out.println("Boolean results succesfully appened to " + booleanFile);
            System.out.println("Vector results succesfully appened to " + vectorFile);

            System.out.println("\n<<CAUTION: RESULTS WILL BE APPENDED TO THE PREVIOUS RESULTS FILE IN THE NEXT SEARCH>>\n");
            
        }
        catch(Exception e){
            System.out.println("RESTARTING PROCESS!\n");   
        }
    }

    /**
     * The and search will allow multiple queries to be searched using the and operator
     * to get the document list that only contains the union of all the queries
     * 
     * Takes in an arrayList of terms to be searched
     * Takes in a boolean that tells if the query contains a stop word or not
     * Takes in a type of search wether it is vector or boolean 
     * 
     * Michael Scanlon & Shah Hassan Syed
     */
    public static ArrayList<Document> andSearch(ArrayList<String> qArray, boolean st){
        ArrayList<Document> docList;
        ArrayList<Document> andList = new ArrayList<Document>();
        ArrayList<Posting> postingDocs;
        if(st){
            for(int d=0; d<qArray.size(); d++){                
                String q = qArray.get(d);

                if(InverterIndexSW.index.containsKey(q)){
                    postingDocs = InverterIndexSW.index.get(q).getPostings();
                    docList  =  new ArrayList<Document>();

                    //we will keep adding document to the list in a way, that only
                    //the documents that contain all the queries will remain
                    for(int i = 0; i<postingDocs.size(); i++){              
                        Posting currPosting = postingDocs.get(i);
                        String docID = currPosting.getDocID();
                        if(d>0){
                            if(andList.contains(currPosting.doc)){                                                              
                                docList.add(currPosting.doc);
                            }
                        }
                        andList.add(currPosting.doc);                    
                    }   

                    if(d>0){
                        andList = docList;
                    }
                }else{
                    andList.clear();
                    return andList;
                }

            }

        }
        //if the query does not contain stopWord it will be searched here
        else{
            for(int d=0; d<qArray.size(); d++){                
                String q = qArray.get(d);

                if(InverterIndex.index.containsKey(q)){
                    postingDocs = InverterIndex.index.get(q).getPostings();
                    docList  =  new ArrayList<Document>();
                    for(int i = 0; i<postingDocs.size(); i++){              
                        Posting currPosting = postingDocs.get(i);
                        String docID = currPosting.getDocID();
                        //we will keep adding document to the list in a way, that only
                        //the documents that contain all the queries will remain
                        if(d>0){

                            if(andList.contains(currPosting.doc)){                                                             
                                docList.add(currPosting.doc);
                            }
                        }
                        andList.add(currPosting.doc);                    
                    }   

                    if(d>0){
                        andList = docList;
                    }
                }else{
                    andList.clear();
                    return andList;
                }

            }
        }
        return andList;
    }

    /**
     * The or search will allow multiple queries to be searched using the or operator
     * to get the document list that contains either of queries
     * 
     * Takes in an arrayList of terms to be searched
     * Takes in a boolean that tells if the query contains a stop word or not
     * 
     * Michael Scanlon & Shah Hassan Syed
     */
    public static ArrayList<Document> orSearch(ArrayList<String> qArray, boolean st){
        ArrayList<Document> docList =  new ArrayList<Document>();
        ArrayList<Posting> postingDocs;
        if(st){
            //if the query contains stopWord it will be searched here
            for(String q: qArray){
                if(InverterIndexSW.index.containsKey(q)){
                    postingDocs = InverterIndexSW.index.get(q).getPostings();
                    for(int i = 0; i<postingDocs.size(); i++){
                        Posting currPosting = postingDocs.get(i);
                        String docID = currPosting.getDocID();                        
                        currPosting.doc.calculateCosSim(queryPost,q);                            
                        if(!docList.contains(currPosting.doc)){                         

                            docList.add(currPosting.doc);
                        }
                    }
                }
            }
        }
        else{
            //if the query does not contain stopWord it will be searched here
            for(String q: qArray){
                if(InverterIndex.index.containsKey(q)){
                    postingDocs = InverterIndex.index.get(q).getPostings();
                    for(int i = 0; i<postingDocs.size(); i++){
                        Posting currPosting = postingDocs.get(i);
                        String docID = currPosting.getDocID();                        
                        currPosting.doc.calculateCosSim(queryPost,q);                            
                        if(!docList.contains(currPosting.doc)){                            
                            docList.add(currPosting.doc);
                        }
                    }
                }

            }   

        }
        return docList;
    }

    /**
     * The not search will only give out the documents in ArrayList that do not contain the query
     * 
     * Takes in a String of the term that is going to be searched for
     * Takes in a boolean that tells if the query contains a stop word or not
     * 
     * Michael Scanlon & Shah Hassan Syed
     */
    public static ArrayList<Document> notSearch(String q, boolean st){
        //the resulting documents after the search will be put in here
        ArrayList<Document> docList =  new ArrayList<Document>();     

        //the documents that do contain the query will be here
        ArrayList<Document> containsList =  new ArrayList<Document>();
        if(st){
            //if the query contains stopWord it will be searched here
            if(InverterIndexSW.index.containsKey(q)){
                PostingList wordP= InverterIndexSW.index.get(q);
                for(Posting p : wordP.getPostings()){
                    containsList.add(p.doc);
                }
                for (Document d: InverterIndexSW.allDocs){
                    if(!containsList.contains(d)){
                        docList.add(d);   
                    }
                }
            }

            else{
                //if the index does not contains the term at all, that means all the documents are the result of not Search
                for(Document d : InverterIndexSW.allDocs){
                    String docID = d.getDocID();
                    docList.add(d);                 
                }
            }
        }
        else{
            //if the query does not contain stopWord it will be searched here
            if(InverterIndex.index.containsKey(q)){
                PostingList wordP= InverterIndex.index.get(q);
                for(Posting p : wordP.getPostings()){
                    containsList.add(p.doc);
                }
                for (Document d: InverterIndex.allDocs){
                    if(!containsList.contains(d)){
                        docList.add(d);   
                    }
                }
            }
            //if the index does not contains the term at all, that means all the documents are the result of not Search
            else{
                for(Document d : InverterIndex.allDocs){
                    String docID = d.getDocID();
                    docList.add(d);              
                }
            }
        }
        return docList;
    }

}
