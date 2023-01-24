package maventest3;

import java.io.IOException;
import java.nio.file.Path;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

// This class searches the documents for the specific Query
public class Searcher {
	
	IndexSearcher indexSearcher;
    QueryParser queryParser;
    Query query;
    
    public Searcher(Path indexDirectoryPath) throws IOException {

          Directory indexDirectory = FSDirectory.open(indexDirectoryPath);
          IndexReader reader = DirectoryReader.open(indexDirectory);

          indexSearcher = new IndexSearcher(reader);
          queryParser = new QueryParser("body", new EnglishAnalyzer());
          
    }
    
    public Searcher(Path indexDirectoryPath, Similarity similarity) throws IOException {
        this(indexDirectoryPath);
        indexSearcher.setSimilarity(similarity);

    }
    
    public TopDocs search(String searchQuery) throws IOException, ParseException {
    	// search the documents with the specific query
          query = queryParser.parse(searchQuery);
          return indexSearcher.search(query, 10);
    }
    
    public Document getDocument(ScoreDoc scoreDoc) throws CorruptIndexException, IOException {
          return indexSearcher.doc(scoreDoc.doc);
    }

}

