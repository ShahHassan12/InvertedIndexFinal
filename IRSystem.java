import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.IOException;
import java.util.HashMap;
import java.util.Arrays;
/**
 * The main runner of the Information Retrieval Systems
 *
 * @author Michael Scanlon & Shah Hassan Syed
 */
public class IRSystem
{
    public static void main(String args[]){
        if (args.length != 1) {
            System.err.println("Corpus Directory required as parameter.");
            System.exit(1);
        }

        //read in the directory from the user
        String corpusDir = args[0];

        //Initialize the inverter index excluding the stop words
        InverterIndex.main(args[0]);

        //Initialize the inverter index including the stop words
        InverterIndexSW.main(args[0]);

        //call in the query method
        QuerySearch.query();

    }
}
