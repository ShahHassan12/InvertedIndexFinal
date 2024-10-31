Authors: Shah Hassan Syed

To contruct the inverted index, run the IRSystem.java. It requires a single argument,
which must be a folder that includes all the files to be parsed.

Typical Construct Time for the given Financial Times Folder (15 mb) : 6-7 minutes

Note: As of now the system can only search:
     - multi-word phrases (has to be in qoutation)
      e.g "to be or not to be"
     - AND boolean operator
     - OR boolean operator
     - NOT operator operator
     - Single non-phrase word operator	
     - A batch query file
     
 System Using machine learning formulas to score words, phrases and documents. 
 The following are the formulas the the index uses:
 
 idf score

By default the System uses OR operator if no qoutation found or no other boolean 
operator found

Once both inverter indexes are successfully constructed it will print a message:
________________________________________
INITIALIZED INDEX EXCLUDING STOP WORDS 
________________________________________

________________________________________
INITIALIZED INDEX INCLUDING STOP WORDS 
________________________________________

Once both inverted indexes are initialized, user will be prompted to type in
the type of query whether it is batch query search or single query search

If user chooses batch, then the user will be prompted to enter the name 
of the file that contains the queries. The file name must be written 
along with the extension, .txt or others etc.

To change the search option between searching through Stop Words index or not,
in the batchSearch method in QuerySearch class, and switch the stopWord variable
True means stop word index search
False means non-stop word index search



!!IMPORTANT!!
Due to the creation of many objects, the system might throw OutOfMemory Exception
if ran multiple time in a single session (this does not mean running multiple queries
in a single run), depending upon your machines allocated heap space to JVM

If the system throws such exception, one solution is to increase your default heap.
Else if the system does not throw exception in one index conruction, it might in the second, becuase
the heap is not cleared. Another solution to this is exiting your jvm or IDE before constructing again.

      
