package recognition03;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.watson.developer_cloud.service.security.IamOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifiedImages;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyOptions;


public class Recognition03_lib 
{
	VisualRecognition service;
	IamOptions iamOptions;
	
	public Recognition03_lib()
	{	
		service = new VisualRecognition("2018-03-19");
		iamOptions = new IamOptions.Builder()
		  .apiKey("1618101")
		  .build();
		service.setIamCredentials(iamOptions);
	}
	
	public ClassifiedImages getRecognition(String flie)
	{
		InputStream imagesStream = null;
		try {
			imagesStream = new FileInputStream(flie);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ClassifyOptions classifyOptions = new ClassifyOptions.Builder()
		  .imagesFile(imagesStream)
		  .imagesFilename(flie)
		  .threshold((float) 0.6)
		  .build();
		ClassifiedImages result = service.classify(classifyOptions).execute();
		return result;
	}
	
	public void getJson(ClassifiedImages result)
	{
		MySQL my_sql = new MySQL(); 
		
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = null;
		try {
			node = mapper.readTree(String.valueOf(result));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String class1 = node.get("images").get(0).get("classifiers").get(0).get("classes").get(0).get("class").toString();
		double score1 = node.get("images").get(0).get("classifiers").get(0).get("classes").get(0).get("score").asDouble();
		String class2 = node.get("images").get(0).get("classifiers").get(0).get("classes").get(1).get("class").toString();
		double score2 = node.get("images").get(0).get("classifiers").get(0).get("classes").get(1).get("score").asDouble();
		String class3 = node.get("images").get(0).get("classifiers").get(0).get("classes").get(2).get("class").toString();
		double score3 = node.get("images").get(0).get("classifiers").get(0).get("classes").get(2).get("score").asDouble();
		
		my_sql.updateImage( class1 , score1 , class2 , score2 , class3 , score3 );
		System.out.println(result);
	}
}
