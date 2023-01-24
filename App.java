package maventest3;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.Similarity;



public class App {

	public static void main(String[] args) throws ParseException {
		 if (!(args.length > 3)) {
	            System.out.println("Arguments are missing");
	            System.exit(0);
	        }

	        try {
	            Path documentFolder = Paths.get(args[0]); // html file location
	            Path indexFolder = Paths.get(args[1]); // xml file location
	            String rankingModel = args[2]; // VS or OK ranking model
	            String query = args[3]; // Qeuery

	            //Index Documents in folder
	            Indexer indexer = new Indexer(indexFolder.toString());
	            int numIndexed;
	            //Calculate start time
	            long startTime = System.currentTimeMillis();
	            numIndexed = indexer.createIndex(documentFolder);
	            //Calculate endtime
	            long endTime = System.currentTimeMillis();
	            System.out.println(numIndexed + " File indexed, time taken: " + (endTime - startTime) + " ms");
	            indexer.close();

	            //Perform search
	            Searcher searcher = new Searcher(indexFolder, getSimilarity(rankingModel));
	            // hits variable will calculate how many hits we will get when we search a specific query
	            TopDocs hits = searcher.search(query);
	            //Print how many documents have the query we searched for and the calculated time for the process
	            System.out.println(hits.totalHits + " documents found. Time :" + (endTime - startTime));
	            
	            int rank = 1;
	            
	           for (ScoreDoc scoreDoc : hits.scoreDocs) {
	                Document doc = searcher.getDocument(scoreDoc);
	               //for each document indexed the program will print the document with the higher rank till the lowest rank  
	               System.out.println("Rank: " +  rank + doc.get("title"));
	               System.out.println("File: " + scoreDoc);
	               System.out.println("");
	               
	               rank++;
	            }

	        } catch ( IOException ex) {
	            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
	        }

	    }
	
	private static Similarity getSimilarity(String similarityType) {
		//this method when called with either VS or OK gives the user the ranking model that is desired
        Similarity similarity = null;
        // VS RankingModel
        if ("VS".equalsIgnoreCase(similarityType)) {
            similarity = new ClassicSimilarity();
        }
        // OK RankingModel
        if ("OK".equalsIgnoreCase(similarityType)) {
            similarity = new BM25Similarity();
        }
        return similarity;
    }

	

}
