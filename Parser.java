import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
import java.io.FileNotFoundException;

/**
 * This parser is specifically built to parse Financial Times newspaper corpus
 * with a standardized formar which contains any number of news articles.
 * Further on seperates meta data and text body of a news article within the corpus 
 * and makes a Document object out of each article.
 *
 * @author Shah Hassan Syed
 * @version 2021
 */

class Parser{

    /**
     *The main class parse; takes in a single input as a text file
     *and goes through different checks to seperate the meta data
     *from the text body of the news article.
     * 
     * @param inputFile an input file; a Financial Times corpus that has
     * multiple newswire articles
     * @return an arraylist of document objects;
     */
    public static ArrayList<Document> parse(File inputFile) {

        //initialize an arraylist of type Document
        ArrayList<Document> docList = new ArrayList<Document>();
        
        //only proceed if the input is a valid text file
        try {
            Scanner sc = new Scanner(inputFile);
            while (sc.hasNext()) {
               
                //create a document object for each article
                Document parsedDoc = new Document();
                String currLine = sc.nextLine();

                //use conditionals to check what type of meta data is being dealt with
                if (currLine.equals("<DOC>")) {
                    currLine = sc.nextLine();
                    if (currLine.substring(0, 7).equals("<DOCNO>")) {                    
                        int i = 0;
                        String docLine = currLine.substring(7);
                        String docID = "";
                        while(docLine.charAt(i) != '<'){
                         docID += docLine.charAt(i);
                         i++;
                        }                       
                       
                        parsedDoc.setDocID(docID);                     
                        
                    }

                    currLine = sc.nextLine();
                    if (currLine.substring(0, 9).equals("<PROFILE>")) {
                        parsedDoc.setProfile(currLine.substring(9, 23));
                    }

                    currLine = sc.nextLine();
                    if (currLine.substring(0, 6).equals("<DATE>")) {
                        int date = Integer.parseInt(currLine.substring(6, 12));
                        parsedDoc.setDate(date);
                    }
                    sc.nextLine();
                    currLine = sc.nextLine();

                    if (currLine.equals("<HEADLINE>")) {
                        currLine = sc.nextLine();
                        String headLine = "";
                        headLine = headLine + currLine + " ";
                        currLine = sc.nextLine();
                        while (!currLine.equals("</HEADLINE>")) {
                            headLine = headLine + currLine + " ";
                            currLine = sc.nextLine();
                        }
                        currLine = sc.nextLine();
                        parsedDoc.setHeadLine(headLine);
                    }

                    if (currLine.equals("<BYLINE>")) {
                        currLine = sc.nextLine();
                        String byLine = "";
                        byLine = byLine + currLine +" ";
                        currLine = sc.nextLine();
                        while (!currLine.equals("</BYLINE>")) {
                            byLine = byLine + currLine + " ";
                            currLine = sc.nextLine();
                        }
                        currLine = sc.nextLine();
                        parsedDoc.setByLine(byLine);
                    }

                    if (currLine.equals("<DATELINE>")) {
                        currLine = sc.nextLine();
                        String dateLine = "";
                        dateLine = dateLine + currLine +" ";
                        currLine = sc.nextLine();
                        while (!currLine.equals("</DATELINE>")) {
                            dateLine = dateLine + currLine + " ";
                            currLine = sc.nextLine();
                        }
                        currLine = sc.nextLine();
                        parsedDoc.setDateLine(dateLine);
                    }

                    //after storing all the meta data, check for the text body of the article
                    if (currLine.equals("<TEXT>")) {
                        currLine = sc.nextLine();
                        String text = "";
                        text = text + currLine + " ";
                        currLine = sc.nextLine();
                        while (!currLine.equals("</TEXT>")) {
                            text = text + currLine + " ";
                            currLine = sc.nextLine();
                        }
                        parsedDoc.setText(text);
                    }
                    
                    currLine = sc.nextLine();
                    boolean pubFound = false;
                    
                    //Check if there is more meta data after the text body.
                    //This depends upon case by case basis
                    while(pubFound==false){
                        if(currLine.equals("<XX>")){
                            currLine = sc.nextLine();
                            currLine = sc.nextLine();
                            currLine = sc.nextLine();
                            switch(currLine.substring(0,4)){
                                case "<CN>":
                                String cn = currLine.substring(4);  
                                currLine = sc.nextLine();
                                while(!currLine.equals("</CN>")){                                    
                                 cn += currLine;
                                 currLine = sc.nextLine();
                                }
                                parsedDoc.setCountries(cn);                            
                                break;

                                case "<CO>":
                                String co = currLine.substring(4);  
                                currLine = sc.nextLine();
                                while(!currLine.equals("</CO>")){                                    
                                 co += currLine;
                                 currLine = sc.nextLine();
                                }
                                parsedDoc.setCompanies(co); 
                                break;

                                case "<IN>":
                                String in = currLine.substring(4);  
                                currLine = sc.nextLine();
                                while(!currLine.equals("</IN>")){                                    
                                 in += currLine;
                                 currLine = sc.nextLine();
                                }
                                parsedDoc.setIndustries(in); 
                                break;

                                case "<TP>":
                                parsedDoc.setTypes(currLine.substring(4));
                                String tp = currLine.substring(4);  
                                currLine = sc.nextLine();
                                while(!currLine.equals("</TP>")){                                    
                                 tp += currLine;
                                 currLine = sc.nextLine();
                                }
                                parsedDoc.setCountries(tp); 
                                break;                                
                            }
                            currLine = sc.nextLine();
                        }
                        else{
                            pubFound =true;
                        }
                    }

                    if (currLine.substring(0, 5).equals("<PUB>")) {
                        String pub = currLine.substring(5, 24);
                        parsedDoc.setPub(pub);
                    }
                    sc.nextLine();
                    currLine = sc.nextLine();

                    if (currLine.equals("<PAGE>")) {
                        currLine = sc.nextLine();
                        parsedDoc.setPage(currLine);
                    }
                    if (sc.hasNext()) {
                        sc.nextLine();
                        sc.nextLine();
                    }

                    //once everything is set, add the Document to the arraylist
                    docList.add(parsedDoc);
                }
                
     
            }
            
            sc.close();
        } 

        //Throw an exception if the text file not found
        catch (FileNotFoundException e) {
            System.out.println("File not Found!");
        }

        //return the arraylist of Documents once scanner reached the end of the file
        return docList;
    }
}

