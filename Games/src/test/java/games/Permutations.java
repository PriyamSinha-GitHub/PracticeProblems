package games;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Permutations 
{
	
		//Function to get a set of all permutations of strings possible from a string
		//Returns a set of strings
	    public Set<String> permutationFinder(String str) 
	    {
	        Set<String> perm = new HashSet<String>();
	        //Handling error scenarios
	        if (str == null) {
	            return null;
	        } else if (str.length() == 0) {
	            perm.add("");
	            return perm;
	        }
	        char initial = str.charAt(0); // first character
	        String rem = str.substring(1); // Full string without first character
	        Set<String> words = permutationFinder(rem);
	        for (String strNew : words) {
	            for (int i = 0;i<=strNew.length();i++){
	                perm.add(charInsert(strNew, initial, i));
	            }
	        }
	        return perm;
	    }

	    //Function to create a word made out of three substrings
	    //Returns a string
	    public static String charInsert(String str, char c, int j) 
	    {
	        String begin = str.substring(0, j);
	        String end = str.substring(j);
	        return begin + c + end;
	    }
	    
	    //Execute to get permutations of given string
	    public static void main(String[] args) 
	    {
	    	Permutations pm = new Permutations();
	    	Scanner sc = new Scanner(System.in);
	    	String str = sc.nextLine();
	    	Set<String> set = pm.permutationFinder(str);
	        System.out.println("\nPermutations for " + str + " are: \n" + set);
	    }
	}
