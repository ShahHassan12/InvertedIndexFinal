
import java.util.ArrayList;
class Tokenizer{
  
  public static String[] tokenize(Document doc){

    String originalTxt = doc.getText();
   
    originalTxt = originalTxt.replaceAll("\\'s", "");

    String[] tokens = originalTxt.replaceAll("[^a-zA-Z0-9 ]", "").toLowerCase().split("\\s+");
    
    return tokens;
  }

   public static String[] phraseTokenize(String s){

    String originalTxt = s;
   
    originalTxt = originalTxt.replaceAll("\\'s", "");
    
    String[] tokens = originalTxt.replaceAll("[^a-zA-Z0-9 ]", "").toLowerCase().split("\\s+");
    
    return tokens;
  }
  
     public static String[] booleanTokenize(String s){

    String originalTxt = s;
   
    originalTxt = originalTxt.replaceAll("\\'s", "");
    
    String[] tokens =  originalTxt.replaceAll("[^a-zA-Z0-9 ]", "").split("\\s+");
    
    for(int i = 0; i < tokens.length; i++){
        if(!tokens[i].equals("AND") && !tokens[i].equals("OR") && !tokens[i].equals("NOT")){
            tokens[i] = tokens[i].toLowerCase();
        }
    }
    
    return tokens;
  }
  
}