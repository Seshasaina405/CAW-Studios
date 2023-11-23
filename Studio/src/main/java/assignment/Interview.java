package assignment;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.Assert;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class Interview {
	public static void main(String[] args) throws InterruptedException, ParseException, IOException {
		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(Duration.ofMinutes(1));
		driver.manage().window().maximize();
		driver.get("https://testpages.herokuapp.com/styled/tag/dynamic-table.html");
		driver.findElement(By.xpath("//summary[text()='Table Data']")).click();
		driver.findElement(By.id("jsondata")).clear();
		driver.findElement(By.id("jsondata")).click();
		driver.findElement(By.id("jsondata")).sendKeys("[{\"name\" : \"Bob\", \"age\" : 20, \"gender\": \"male\"}, {\"name\": \"George\", \"age\" : 42, \"gender\": \"male\"}, {\"name\":\r\n"
				+ "\"Sara\", \"age\" : 42, \"gender\": \"female\"}, {\"name\": \"Conor\", \"age\" : 40, \"gender\": \"male\"}, {\"name\":\r\n"
				+ "\"Jennifer\", \"age\" : 42, \"gender\": \"female\"}]");
		driver.findElement(By.id("refreshtable")).click();
		driver.findElement(By.id("dynamictable"));
		List<Object> ExpectedList = new ArrayList<Object>();
		JSONParser p = new JSONParser();
		String json="[{\"name\" : \"Bob\", \"age\" : 20, \"gender\": \"male\"}, {\"name\": \"George\", \"age\" : 42, \"gender\": \"male\"}, {\"name\":\r\n"
				+ "\"Sara\", \"age\" : 42, \"gender\": \"female\"}, {\"name\": \"Conor\", \"age\" : 40, \"gender\": \"male\"}, {\"name\":\r\n"
				+ "\"Jennifer\", \"age\" : 42, \"gender\": \"female\"}]";
		JSONArray jarray = (JSONArray) p.parse(json);
		
		for(int i=0; i<jarray.size(); i++) {
			JSONObject jobject = (JSONObject) jarray.get(i);			
//			System.out.println(jobject);
			ExpectedList.add(jobject);
		}
		System.out.println(ExpectedList);
		
		List<WebElement> rowElements = driver.findElements(By.xpath("//table[@id='dynamictable']/tr"));
				
		for(int i=1; i<rowElements.size(); i++) {
			List<WebElement> columnElements = rowElements.get(i).findElements(By.tagName("td"));			
			Map<String,Object> hashMap = new HashMap<String, Object>();	        
		    for(int j=0; j<columnElements.size(); j++) {		    	
		    	if(j==0) {	    		
		    	hashMap.put("name",  columnElements.get(j).getText());
		    	}
		    	if(j==1) {
		    		hashMap.put("age", Integer.parseInt( columnElements.get(j).getText()));
		    	}	
		    	if(j==2) {
		    		hashMap.put("gender",  columnElements.get(j).getText());
		    	}
		}
		    JSONObject jsonObject = new JSONObject(hashMap);		       
	        System.out.println(jsonObject.toJSONString());
	        assertJsonObjectsEqual(jsonObject.toString(), ExpectedList.get(i-1).toString());
		}
		
		
		driver.quit();
        
	}
	private static void assertJsonObjectsEqual(String json1, String json2) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode node1 = objectMapper.readTree(json1);
        JsonNode node2 = objectMapper.readTree(json2);
    
        Assert.assertEquals(node1, node2, "JSON objects are not equal irrespective of order");
    }
	
        }

