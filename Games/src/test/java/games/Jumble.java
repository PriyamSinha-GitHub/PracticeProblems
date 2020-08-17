//Author: 	Priyam SInha
//Date: 	10/08/2020
package games;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import io.restassured.response.Response;
import io.restassured.response.ResponseBody;

public class Jumble {
		HashMap<Integer,ResponseBody> hm = new HashMap<Integer, ResponseBody>();
		
		
		static final String url = "https://api.dictionaryapi.dev/api/v1/entries/en/";
		static final int statusOK = 200;
		static final int statusTryAgain = 429;
		static final Logger logger = Logger.getLogger(Jumble.class);
		static final String LOG_PROPERTIES_FILE = "log4j.properties";
		//Execute game Jumble
		//Game allows you to enter a word, it would check word's validity.
		//Description: It would again ask for a jumbled up word possible with provided word
		//If your answer is correct it display success message else a failure message.
		public HashMap<Integer,String> executeJumbleGame(String myWord) throws InterruptedException, IOException 
		{
			Jumble jm = new Jumble();
			Permutations pm = new Permutations();
			ArrayList<String> validWords = new ArrayList<String>();
			String message = null;
			int iPassed = 0;
			//  BasicConfigurator.configure();
			//  Enter word
			//	System.out.println("Enter a word:");
			//	Scanner sc = new Scanner(System.in);
			//	String myWord = sc.nextLine();
			
			logger.info("Word in question is: "+myWord);
			
			//Get status code for this word from google disctionary api
			int statusCode = jm.checkValidWord(myWord);
			logger.info("Status of the dictionary service for "+myWord+" is "+statusCode);
			
			//If status code is SUCCESS , check for all permutations and valid words possible
			//Take another input of jumbled word from user, and check if its in valid words found.
			if (statusCode==statusOK)
			{
				//sort the letters of the word
				char[] sortedChars = jm.sortLetters(myWord);
				String sortedString = new String();
				for(char c:sortedChars) sortedString = sortedString+c;
				
				//get all permutations of the word
				Set<String> possibleWords = pm.permutationFinder(sortedString);
				logger.info("All possible combinations for "+myWord+ "is "+possibleWords);

				//for each permutation check validity from web service
				ArrayList<String> tryAgain = new ArrayList<String>();
				for(String word:possibleWords)
				{
					int status = 0;					
					int k = 0;
					Thread.sleep(5000);
					status = jm.checkValidWord(word);
					if(status==statusOK)
					{
						if(!myWord.equalsIgnoreCase(word))
						{
							validWords.add(word);
							//System.out.println("Word:"+word);
						}
					}
					else if(status==statusTryAgain)
					{
						k=k+1;
						tryAgain.add(word);
					}
				}
				
				int count=0;
				while(!tryAgain.isEmpty() & count<3)
				{
					count=count+1;
					int size = tryAgain.size();
					for(int i=0;i<size;i++)
					{
						String word = tryAgain.get(i);					
						int status = 0;					
						int k = 0;
						Thread.sleep(5000);
						status = jm.checkValidWord(word);
						if(status==statusOK)
						{
							if(!myWord.equalsIgnoreCase(word))
							{
								validWords.add(word);
								if(!tryAgain.isEmpty())
								{
									tryAgain.remove(word);
									size = tryAgain.size();
								}
								System.out.println("Word:"+word);
							}
						}
						if(status!=statusTryAgain)
						{
							if(!tryAgain.isEmpty())
							{
								tryAgain.remove(word);
								size = tryAgain.size();
							}
						}
					}
				}
			
				System.out.println(tryAgain);
				if(!tryAgain.isEmpty())
				{
					logger.info("Please note: You cannot enter as a jumbled word - "+tryAgain);
				}
				
				//Enter the jumbled up word
				//new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
				System.out.println("Enter a jumbled word for "+myWord+" : ");
				Scanner sc1 = new Scanner(System.in);
				String myJumbledWord = sc1.nextLine();
				logger.info("User entered "+myJumbledWord+ " for "+myWord);
				
				//Verify if jumbled word entered by user is valid jumbled word for the word.
				if(validWords.contains(myJumbledWord))
					{
						//System.out.println("The Jumbled Word entered is one of the correct entries.");
						message= "The Jumbled Word entered  '"+myJumbledWord+"' is one of the correct entries.";					
						iPassed= 1;
						
					}
				else 
					{
						//System.out.println("The Jumbled Word entered is incorrect");
						message = "The Jumbled Word  '"+myJumbledWord+"' entered is incorrect entry for '"+myWord+"'";					
						iPassed=0;
					}
				logger.info(message);
				validWords = jm.sortStrings(validWords.toArray());
				logger.info("All valid jumbled words for "+myWord+ "is "+validWords);
			}
			else if(statusCode==0)
			{
				message= "Please check your interner connetion - Word Service could not be reached";
				logger.error(message);
				//System.out.println("Please check your interner connetion - Word Service could not be reached");
			}
			else if(statusCode==404)
			{
				message= "Word/Service not found";
				logger.error(message);				
				//System.out.println("Sorry - the game is not working. Error in Dictionary Service used.");
			}
			else if(statusCode==429)
			{
				message= "Service is busy - cannot process the sent request - Please try again in some time";
				logger.error(message);		
				//System.out.println("Sorry - the game is not working. Error in Dictionary Service used.");
			}
			else
			{
				message="Exception - "+statusCode+" occured";
				logger.error(message);
			}
			HashMap<Integer,String> hm = new HashMap<Integer,String>();
			hm.put(iPassed, message);
			return hm;				
		}
		
		//Function to get word details like nout/verb
		//Returns String Array of word detailsCollection
		public  ArrayList<String> getWordDetails(String word)
		{
				RESTServices rest = new RESTServices();
				ArrayList<String> actualMap=new ArrayList<String>();
				try 
				{
					
					hm = rest.getResponse(url+word);
					actualMap = rest.parseJsonArrary(hm);
				}
				catch(Exception e)
				{
					System.out.println(e);
				}
				System.out.println("Keys:"+actualMap.size());
				return actualMap;
		}
		
		//Function to check validity of the word
		//Returns 200 if valid word
		public  int checkValidWord(String word)
		{
			RESTServices rest = new RESTServices();
			int statusCode = 0;
			try 
			{				
				statusCode = rest.getStatusCode(url+word);				
			}
			catch(Exception e)
			{
				System.out.println(e);
			}
			return statusCode;

		}
		
		//Function to sort letters of an array
		//Returns sorted char array
		public char[] sortLetters(String myWord)
		{
			char[] chr = new char[myWord.length()];
			chr = myWord.toCharArray();
			//sort array
			for(int i=0;i<myWord.length()-1;i++)
			{
				if(chr[i]<chr[i+1])
				{
					char temp = chr[i];
					chr[i] = chr[i+1];
					chr[i+1] = temp;
				}
			}
			return chr;
			
		}
		
		//Function to sort strings of an array
		//Returns sorted string array
		public ArrayList<String> sortStrings(Object[] objects)
		{
			int size = objects.length;
			System.out.println(objects[0].toString());
			//sort array
	        //Sorting the strings
	        for (int i = 0; i < size; i++) 
	        {
	            for (int j = i + 1; j < size; j++) 
	            { 
	                if (objects[i].toString().compareTo(objects[j].toString()) > 0) 
	                {
	                    Object temp = objects[i];
	                    objects[i] = objects[j];
	                    objects[j] = temp;
	                }
	            }
	        }
	        ArrayList<String> sortedArr = new ArrayList<String>();
	        for(Object myWord:objects)
	        {
	        	sortedArr.add(myWord.toString());
	        }
			return sortedArr;
			
		}

}
