package maventest3;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import maventest3.html;

import org.apache.lucene.analysis.en.EnglishAnalyzer;

public class Indexer {
	
private IndexWriter writer;
	
	public Indexer(String indexDirectoryPath) throws IOException {

        Directory indexDirectory = FSDirectory.open(Paths.get(indexDirectoryPath));
        // the EnglishAnalyzer uses PorterStemmer
        EnglishAnalyzer analyzer = new EnglishAnalyzer(); 

        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(OpenMode.CREATE_OR_APPEND);
        writer = new IndexWriter(indexDirectory, config);
    }
	
	 public void close() throws CorruptIndexException, IOException {
	        writer.close();
	 }
	 
	    public int createIndex(Path file) throws IOException {
	    	
	    	//Iterate Directory
	        Files.walkFileTree(file, new SimpleFileVisitor<Path>()
	        {

	            html hTMLHandler = new html();

	            @Override
	            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
	            {

	                if (!attrs.isDirectory())
	                {
	                    String fileName = file.getFileName().toString();
	                    
	                    // here we specify that filename must end with either html and txt 
	                    if (fileName.endsWith(".html") || fileName.endsWith(".htm") || fileName.endsWith(".txt"))
	                    {
	                        System.out.println(
	                              "Indexing " + fileName);
	                        Document document = hTMLHandler.getDocument(new FileInputStream(file.toFile()));
	                        writer.addDocument(document);
	                    }

	                }

	                return FileVisitResult.CONTINUE;
	            }
	        });
	        
	        return writer.numDocs();

	    }

}
