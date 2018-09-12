package hello;

import java.net.UnknownHostException;
import java.util.Date;



import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;

public class MongoConnect {
	
	 
	 public static void main(String[] args) {

		   try {

			/**** Connect to MongoDB ****/
			MongoClient mongo = new MongoClient("localhost", 27017);

			/**** Get database ****/
			// if database doesn't exists, MongoDB will create it for you
			DB db = mongo.getDB("test2");
			//DB db= mongo.getDatabase("test");
			
			
			/**** Get collection / table from 'test' ****/
			// if collection doesn't exists, MongoDB will create it for you
			DBCollection table = db.getCollection("user");

			/**** Insert ****/
			// create a document to store key and value
			BasicDBObject document = new BasicDBObject();
			document.put("name", "shayke");
			document.put("age", 30);
			document.put("createdDate", new Date());
			table.insert(document);

			/**** Find and display ****/
			BasicDBObject searchQuery = new BasicDBObject();
			searchQuery.put("name", "shayke");

			DBCursor cursor = table.find(searchQuery);
			
			while (cursor.hasNext()) {
				System.out.println(cursor.next());
			}

			/**** Update ****/
			// search document where name="shayke" and update it with new values
			BasicDBObject query = new BasicDBObject();
			query.put("name", "shayke");

			BasicDBObject newDocument = new BasicDBObject();
			newDocument.put("name", "shayke-updated");

			BasicDBObject updateObj = new BasicDBObject();
			updateObj.put("$set", newDocument);
			
			table.update(query, updateObj);

			/**** Find and display ****/
			BasicDBObject searchQuery2 
			    = new BasicDBObject().append("name", "shayke-updated");

			DBCursor cursor2 = table.find(searchQuery2);

			while (cursor2.hasNext()) {
				System.out.println(cursor2.next());
			}

			/**** Done ****/
			System.out.println("Done");

		//    } catch (UnknownHostException e) {
	//		e.printStackTrace();
		    } catch (MongoException e) {
			e.printStackTrace();
		    }

		  }
		}
