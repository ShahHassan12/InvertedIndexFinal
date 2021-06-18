import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
import java.io.FileNotFoundException;

class Parser{
    public static ArrayList<Document> parse(File inputFile) {
        ArrayList<Document> docList = new ArrayList<Document>();
        
        try {
             int count=0;
            Scanner sc = new Scanner(inputFile);
            while (sc.hasNext()) {
               
                Document parsedDoc = new Document();
                String currLine = sc.nextLine();

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
                        String v2 =  currLine.substring(9,23);
                    }

                    currLine = sc.nextLine();
                    if (currLine.substring(0, 6).equals("<DATE>")) {
                        int date = Integer.parseInt(currLine.substring(6, 12));
                        parsedDoc.setDate(date);
                        String v3 = currLine.substring(6,12);
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

                    docList.add(parsedDoc);
                }
                
     
            }
            
            sc.close();
        } 

        catch (FileNotFoundException e) {
            System.out.println("File not Found!");
        }
        return docList;
    }
}

