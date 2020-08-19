# PracticeProblems
## Games - Jumble
Jumble Word is a game that allows users to give a jumbled up word for a given word. The Game will not only let the user know if he was correct/wrong, but also would give solution with all possible valid jumbled up words possible of that word. 
### Technologies Used
*	Java
*	TestNG
*	Maven
*	REST
### Using
* Run through TESTNG xml (via eclipse/Jenkins..)
* A Word Challenge would be thrown based on the word given to the program from the TestNG Data Provider
*	If the WORD given in Data Provider is not valid, the game would not be executed
*	If Word Is valid, you will be asked to give a jumbled up word for this word
*	Once you enter this word, code would check if the word entered is from the list of valid jumbled up word it has found
*	If your answer is correct it gives a successful message
*	If your answer is incorrect it gives a failure message
### Details
*	Classes
    *  Jumble – All methods related to jumble game are present in this class
        *	executeJumbleGame – Method to execute the game
        *	sortStrings – Sort the strings in an array
        * checkValidWord – Check if a word is present in dictionary using webservice methods
        *	sortLetters – sort letters of a string
        *	getWordDetails – get more info of the word (not used in this program)
      *  RESTful Services – All methods related to web service that checks validity of the word
          * getStatusCode – returns status code, returned by dictionary webservice
          * getResponnse – returns the response received from webservice (no used)
          *	pasrseJsonArray – parse the json to get details (not used)
          *	comparehashMaps – compares two hashmaps (not used)
     *	Permutations – This class contains methods to give all possible combinations of a given any word or sequence of letters.
          *	permutationFinder – get a set of all permutations of a string
          *	charInster – forms a string of three substrings
### Contact
If you want to contact me you can reach me at priyam.sinha@ymail.com.
