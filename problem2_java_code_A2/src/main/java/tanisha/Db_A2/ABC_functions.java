
/**

    This class provides functions to fetch news articles based on a given keyword using NewsApiClient.
    It also writes the top 5 news article titles and content to a file.
     * Reference: “Documentation,” News API. [Online]. Available: https://newsapi.org/docs. [Accessed: 30-Mar-2023].
	 * Reference: KwabenBerko. (n.d.)." Kwabenberko/News-API-Java: An wrapper for newsapi.org.," GitHub. Retrieved March 30, 2023, from https://github.com/KwabenBerko/News-API-Java 
	 * Reference: "MongoDB Atlas Manual," MongoDB Atlas Manual. [Online]. Available: https://www.mongodb.com/docs/manual/. [Accessed: 30-Mar-2023]. 
    
    
        */

package tanisha.Db_A2;
import com.kwabenaberko.newsapilib.*;
import com.kwabenaberko.newsapilib.models.request.EverythingRequest;
import com.kwabenaberko.newsapilib.models.request.SourcesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;
import com.kwabenaberko.newsapilib.models.response.SourcesResponse;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.request.EverythingRequest;


import java.io.*;
import java.util.*;

import org.bson.Document;

/**
 * Constructor for ABC_functions class.
 * @param key_to_get_news - A string representing the keyword for which news articles need to be fetched
 */

public class ABC_functions {
	private String keyword;
	public ABC_functions(String key_to_get_news) {
		this.keyword = key_to_get_news;
	}
	
/**
	 * This method fetches news articles from NewsAPI based on a given keyword and writes the top 5 article titles and content to a file.
	 * The method uses the NewsApiClient library to fetch news articles.
	 * The method also creates an ArrayList of ArrayLists to store the title and content of the top 5 articles.
	 * Finally, the method calls the write_news_to_file method to write the article titles and content to a file.
	 
 */
	
public void get_news(){
    	
        NewsApiClient newsApiClient = new NewsApiClient("API key");
        
        newsApiClient.getEverything(
        		  new EverythingRequest.Builder()
        		          .q(this.keyword)
        		          .build(),
        		  new NewsApiClient.ArticlesResponseCallback() {
        		      @Override
        		      public void onSuccess(ArticleResponse response) {
        		    	  ArrayList<ArrayList<String>> TitleContentList = new ArrayList<>();
        		    	  for (int i = 1; i <= 5; i++) {
        		          ArrayList<String> news = new ArrayList<>();
        		          news.add(response.getArticles().get(i).getTitle());
        		          news.add(response.getArticles().get(i).getContent()); 
        		          TitleContentList.add(news);
        		    	  }
        		    	  write_news_to_file(TitleContentList);
        		    	  
        		      }

        		      @Override
        		      public void onFailure(Throwable throwable) {
        		          System.out.println(throwable.getMessage());
        		      }
        		  }
        		);
}

/**

Writes news to a file with a given delimiter.

@param TitleContentList an ArrayList of ArrayLists of strings that contains the title and content of the news.
*/

public void write_news_to_file(ArrayList<ArrayList<String>> TitleContentList){
	
	/** Define the delimiter to use as a separator */
    String delimiter = ":::";
    
    /** Define the filename to write to */
    String filename = this.keyword.replaceAll("[^a-zA-Z0-9\\.]+", "_") + ".txt";

    /** Write the 2D list to the file */
    try {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        for (ArrayList<String> row : TitleContentList) {
            for (int i = 0; i < row.size(); i++) {
                writer.write(row.get(i));
                if (i < row.size() - 1) {
                    writer.write(delimiter);
                }
            }
            writer.newLine();
        }
        writer.close();
        System.out.println("Successfully wrote News to file!");
        Clean_Transform_file_data();
    } catch (IOException e) {
        System.out.println("An error occurred while writing the file.");
        e.printStackTrace();
    }    
}

/**

Inserts a record to MongoDB using the specified collection name and data parts.
@param collectionName the name of the MongoDB collection to insert data to
@param parts an array of strings containing the data to insert
*/

public void inserRecordToMongoDB(String collectionName, String[] parts) {
	String connectionString = "mongodb+srv://CREDENTIALS.hxoakca.mongodb.net/test?retryWrites=true&w=majority";
    ConnectionString connString = new ConnectionString(connectionString);
    MongoClientSettings settings = MongoClientSettings.builder()
        .applyConnectionString(connString)
        .build();

    MongoClient mongoClient = MongoClients.create(settings);
    MongoDatabase database = mongoClient.getDatabase("myMongoNews");
    MongoCollection<Document> collection = database.getCollection(collectionName);
    System.out.println("Connected to " + database.getName() + " database successfully.");
    
    /** inserting record in mongo db */
    
   Document doc1 = new Document("Title", parts[0]).append("Content", parts[1]);      
    InsertOneResult result1 = collection.insertOne(doc1);
    System.out.println("Inserted a document with the following id: " 
      + result1.getInsertedId().asObjectId().getValue());
}

/**

This method cleans the data from a file and transforms it into a format suitable for a collection.

The cleaned data is written to a text file with the same name as the collection name.
*/

public void Clean_Transform_file_data() {
	/** Define the delimiter to use as a separator */
    String delimiter = ":::";
    String collectionName = this.keyword.replaceAll("[^a-zA-Z0-9\\.]+", "_");
    /** Define the filename to write to */
    String filename = this.keyword.replaceAll("[^a-zA-Z0-9\\.]+", "_") + ".txt";
    String line;
    
    String text_file_clean = "";
    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(delimiter);
          for (int i = 0; i < parts.length; i++) {
          	parts[i] = parts[i];
              /** Remove special characters */
          	parts[i] = parts[i].replaceAll("[^a-zA-Z0-9 ]", "");
              /** Remove emoticons */
              parts[i] = parts[i].replaceAll("[\\uD83C-\\uDBFF\\uDC00-\\uDFFF]+", "");
              /** Remove URLs */
              parts[i] = parts[i].replaceAll("(https?://\\S+\\s*)|(www\\.\\S+\\s*)", "");
          }
          
          if(parts.length > 1) {
          inserRecordToMongoDB(collectionName, parts);}
          
          text_file_clean += String.join(delimiter, parts) + "\n";
        }
        if (text_file_clean.endsWith("\n")) {
        	text_file_clean = text_file_clean.substring(0, text_file_clean.length() - 1);
        }
        reader.close();
        
        
    } catch (IOException e) {
        System.out.println("An error occurred while reading the file.");
        e.printStackTrace();
    }
    
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
        writer.write(text_file_clean);
    } catch (IOException e) {
        System.out.println("An error occurred while writing to the file.");
        e.printStackTrace();
    }
}

}
