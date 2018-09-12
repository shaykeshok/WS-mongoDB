package hello;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

//RestController
//אומר לשרת להעביר את הנתונים בעזרת JSON
//RequestMapping("/value")
//כאשר אני פונה לשרת עם הערך הוא יודע לפנות לפונקציה שמבצעת את הדבר הזה
//RequestParam
//מבקש פרמטר

@RestController
public class GreetingController {
	private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();
    public MongoClient mongo;
    public DB db ;
	public DBCollection table ;
    
    
	public GreetingController(){//, DBCollection table){
		this.mongo = new MongoClient("localhost", 27017);
		this.db = mongo.getDB("test");
		this.table = db.getCollection("user");
	}
	
    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(counter.incrementAndGet(),
                            String.format(template, name));
    }
    
    @RequestMapping("/shok")
    public String shok(@RequestParam(value="name", defaultValue="World") String name,@RequestParam(value="num", defaultValue="1182") int num) {
    	String ans="defualt";
    	if(name.equals("world"))
    		ans="shayke shok" + ", " + "Elkana";
    	else
    		if(name.equals("Dov"))
    			ans= "Dov " + 55;
    		else
    			ans= "nobody"+ " "+ name+ num*5;
    	return ans;
    }

    @RequestMapping("/insertDB")
    public String insertDB(@RequestParam(value="name", defaultValue="World") String name,
    		//,@RequestParam(value="dbname", defaultValue="test") String dbname,
    		@RequestParam(value="age", defaultValue="0") int age) {
   
		/**** Insert ****/
		// create a document to store key and value
		BasicDBObject document = new BasicDBObject();
		document.put("name", name);
		document.put("age", age);
		document.put("createdDate", new Date());
		table.insert(document);
		return "success";
    }
    
    	@RequestMapping(method=RequestMethod.POST,path="/updateDB")
   	    public String updateDB(@RequestParam(value="oldname", defaultValue="shayke") String oldname,
   	    		//@RequestParam(value="dbname", defaultValue="test") String dbname,
   	    		@RequestParam(value="newname", defaultValue="update-shayke") String newname) {
    	
      			
   			/**** Update ****/
   			// search document where name="shayke" and update it with new values
   			BasicDBObject query = new BasicDBObject();
   			query.put("name", oldname);

   			BasicDBObject newDocument = new BasicDBObject();
   			newDocument.put("name", newname);

   			BasicDBObject updateObj = new BasicDBObject();
   			updateObj.put("$set", newDocument);
   			
   			this.table.update(query, updateObj);
   			return "success";
   	 }
    @RequestMapping("/findName")
    public String findName(@RequestParam(value="name") String name){//,@RequestParam(value="dbname", defaultValue="test") String dbname) {
	
   
	    	/**** Find and display ****/
			BasicDBObject searchQuery = new BasicDBObject();
			searchQuery.put("name", "shayke");
		
			DBCursor cursor = this.table.find(searchQuery);
			
			while (cursor.hasNext()) {
				System.out.println(cursor.next());
			}
			return "success";
		
	    }
    @RequestMapping("/gedAllDB")
    public List gedAllDB(){
			List<String> dbs = this.mongo.getDatabaseNames();
		return dbs;
    }
    
    @RequestMapping("/gedAllCollections")
    public Set gedAllCollections(){//@RequestParam(value="dbname", defaultValue="test") String dbname){
	
		Set<String> db2 = this.db.getCollectionNames(); 
		return db2;
    }
    
    @RequestMapping("/dropTable")
    public String dropTable(@RequestParam(value="tblname") String tblname){//,@RequestParam(value="dbname", defaultValue="test") String dbname){
  
    	if(this.db.collectionExists(tblname)){
    		this.table = db.getCollection(tblname);
    		table.drop();
    		this.table=db.getCollection("user");
    		return "drop success";
    	}
    	return "table not exist";
    }
 
    
    @PostMapping("/insertPerson")
	public String insertPerson(@RequestBody String newperson, Long id) {

    	
		/**** Insert ****/
	  //create a document to store key and value
		BasicDBObject document = new BasicDBObject();
		document.put("name", newperson);
		document.put("id", id);
		document.put("createdDate", new Date());
		table.insert(document);
		return "success";
	}
    
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public void test(@RequestBody HashMap<String,Object> str) {
    	//Map<String, Object> mymap=new HashMap<>();
    	
    	BasicDBObject document = new BasicDBObject();

    	for ( Map.Entry<String, Object> entry : str.entrySet()) {
    	    String key = entry.getKey();
    	    Object value = entry.getValue();
    	    document.put(key,value);
    	}
	
		table.insert(document);
		
		
    	//mymap.put("key", "value");
    	//mymap.put("name","mordy arnon");
    	//HashMap<String, Object>innerMap=new HashMap<>();
    	//innerMap.put("innerkey",Integer.valueOf(23));
		//mymap.put("data",innerMap);
		//return mymap; 
    }
}
