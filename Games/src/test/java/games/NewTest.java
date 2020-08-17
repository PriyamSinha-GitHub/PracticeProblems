package games;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;
import org.testng.xml.XmlSuite;

public class NewTest implements IReporter {
	
	//output
	HashMap<Integer,String> hm = new HashMap<Integer, String>();
	String reportPath = "file:\\\\\\"+System.getProperty("user.dir")+"\\test-output\\emailable-report.html";
	String logPath = "file:\\\\\\"+System.getProperty("user.dir")+"\\logs\\log4j.log";
	boolean validInput = false;
	
	@Test(dataProvider = "data") //check input
	public void checkInput(String word) throws AssertionError
	{		
			if(checkNumbersInString(word))
			{
				validInput = false;
				Reporter.log("Input Word: "+word+" is alphanumeric");
				Assert.assertTrue(validInput);
			}
			else
			{
				validInput = true;
				Reporter.log("Input Word: "+word+" is a string");
				Assert.assertTrue(validInput);
			}
				
	}
	
  @Test(dependsOnMethods = "checkInput", dataProvider = "data") //play game
  public void executeGame(String word)
  {
	  //end program if vaid input not provided
	  if(!validInput)
	  {		   
		    System.exit(0);
	  }
		  
	  //for valid input play game - store results in hm hashmap and display proper message in report
	  Jumble jm = new Jumble();
	  try
	  {
		 
			  hm = jm.executeJumbleGame(word);
			  if(!hm.isEmpty())
			  {
				  if(hm.keySet().toArray()[0].equals(0))
				  {
					  Reporter.log(hm.get(hm.keySet().toArray()[0]));
					  Assert.assertTrue(false);
					  
				  }
				  else
				  {
					  Reporter.log(hm.get(hm.keySet().toArray()[0]));
					  Assert.assertTrue(true);
				  }					
			  }
	  }
	  catch(Exception e)
	  {
		  System.out.println("Sorry - Game is not functional as of now - "+"Please try later");
		  e.printStackTrace();
	  }
  }
  
  @Test(dependsOnMethods = "executeGame") //verify output messages
  public void checkOutput()
  {
	  if(hm.isEmpty())
	  {
		  Assert.assertTrue(false, "There is no output/jumbled word for this word");
	  }
	  if(hm.keySet().toArray()[0].equals(0))
	  {
		  if(hm.get(hm.keySet().toArray()[0]).indexOf("incorrect entry")>0)
		  {
			  System.out.println("Failed Report - Correct Message");
			  //Reporter.log(hm.get(hm.keySet().toArray()[0]));
			  Reporter.log("Correct Message displayed for failed test");
			  Assert.assertTrue(true, "Correct Message displayed for failed test");			  
			  
		  }
		  else
		  {
			  //Reporter.log(hm.get(hm.keySet().toArray()[0]));
			  Assert.assertTrue(false, "Incorrect Message displayed for failed test");
			  Reporter.log("Incorrect Message displayed for failed test");
		  }
	  }
	  if(hm.keySet().toArray()[0].equals(1))
	  {
		  if(hm.get(hm.keySet().toArray()[0]).indexOf("correct entries")>0)
		  {
			  System.out.println("Passed Report - Correct Message");
			  //Reporter.log(hm.get(hm.keySet().toArray()[0]));
			  Reporter.log("Correct Message displayed for passed test");
			  Assert.assertTrue(true, "Correct Message displayed for passed test");			  			  
		  }
		  else
		  {
			  //Reporter.log(hm.get(hm.keySet().toArray()[0]));
			  Reporter.log("Incorrect Message displayed for failed test");
			  Assert.assertTrue(false, "Incorrect Message displayed for passed test");
			  
		  }
	  }
  }
	  @DataProvider
	  public Object[][] data() {
	    return new Object[][] {
	      new Object[] {"art"}
	    };
  }
	 @AfterSuite
	 public void display()
	 {
		 System.out.println("Please find logs at: "+logPath);
		 System.out.println("Please find report at: "+reportPath);
	 }
  public void generateReport(List<XmlSuite> arg0, List<ISuite> arg1, String arg2) 
  {
	
	
  }

  public boolean checkNumbersInString(String myWord)
	{
			String regex = ".*\\d.*";  
			if( myWord.matches(regex) )
				return true;
			else
				return false;
	}

}
