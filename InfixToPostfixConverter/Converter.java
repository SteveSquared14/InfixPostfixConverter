import java.util.Scanner;
/**
 * This class takes a user inputted expression which is in the infix form
 * and converts it to postfix form, which is then output to the user
 * 
 * @author (Steven Duckett) 
 * @version (v1.0)
 */
public class Converter
{
    private String userExpression; //The expression the user wants to convert
    private char[] userExpressionArray; //A char array created from the user expression, used for converting to postfix form
    private ArrayStack<Object> characterStack; //Stack1 for Dijkstra's two stack algorithm
    private ArrayStack<Object> operatorStack; //Stack2 for Dijkstra's two stack algorithm
    private ArrayStack<Object> leftParenthesesStack;
    private ArrayStack<Object> rightParenthesesStack;
    private Scanner userInputScanner;
    
    /**
     * Main method to be used with the .jar file exported from this project
     */
    public static void main(String[] args){
        Converter infixToPostfix = new Converter();
    }
    
    /**
     * Constructor for Converter class
     * No parameters required as the field is set using user input
     * which is located in a seperate method which is called in 
     * the Constructor body
     */
    public Converter()
    {
        userInputScanner = new Scanner("");
        //Instansiate a new ArrayStack for the characters in the user input expression
        characterStack = new ArrayStack<>();
        //Instansiate a new ArrayStack for the operators in the user input expression
        operatorStack = new ArrayStack<>();
        //Instansiate a new ArrayStack for the left parentheses in the user input expression
        leftParenthesesStack = new ArrayStack<>();
        //Instansiate a new ArrayStack for the right parentheses in the user input expression
        rightParenthesesStack = new ArrayStack<>();
        //Gets the users input and assigns it to the userExpression field
        getUserInput();
    }
    
    /**
     * Accessor method for the user expression
     * @return the current value assigned to the userExpression field
     */
    private String getUserExpression(){
        return userExpression;
    }
    
    /**
     * Mutator method for changing the userExpression field
     * @param the new expression 
     */
    private void setUserExpression(String changeUserExpression){
        userExpression = changeUserExpression;
    }
    
    /**
     * Makes a new scanner object that takes the user input which
     * is their expression
     */
    private void getUserInput(){
        userInputScanner = new Scanner(System.in);
        System.out.println("Expressions can only contain letters, operands and parentheses");
        System.out.println("Expressions must be less than 20 characters (parentheses included)");
        System.out.println("Type the expression you wish to convert (Or to exit, type exit) and press Enter:");
        String userInput = userInputScanner.nextLine();
        //If the user inputted exit, then close the scanner and stop the application
        if(userInput.equalsIgnoreCase("exit")){
            userInputScanner.close();
        }
        else{
            userExpression = userInput.toUpperCase();
            //Remove all spaces in the users input expression as these are not allowed as per the requirements specified in the brief
            userExpression.replace(" ", "");
            //If the length check returns false, close the scanner, set the expression to a default value, and inform the user of the error via the specified method
            if(!checkExpressionLength(userExpression)){
                userInputScanner.close();
                userExpression = "";
                expressionLengthError();
            }
            else{
                //Assign the users expression to an array ready for the conversion to postfix form
                userExpressionArray = userExpression.toCharArray();
                if(checkFullyParenthesised()){
                    //By this stage, all checking has been completed and passed. So run dijkstra's two stack algorithm on the users expression
                    dijkstrasTwoStackAlgorithm();
                }
                else{
                    //If the fully parenthesised check returns false, close the scanner and inform the user via the specified error method                    
                    userInputScanner.close();
                    parenthesesError();
                }
            }
        }
    }
    
    /**
     * The length of the expression cannot be more than 20 characters so
     * if it is, return false
     * Returns true otherwise
     * @param the expression to be checked
     */
    private boolean checkExpressionLength(String testExpression){
        Boolean output = true;
        if(testExpression.length() > 20){
            output = false;
        }
        return output;
    }
    
    /**
     * Calls to the two methods for checking if the user's expression is fully parenthesised, in the correct order
     * If the expression contains the correct number of parentheses, then check the order those parentheses are in
     * @return the boolean outcome. If both intermediate methods return true, the returns true. Returns false otherwise
     */
    private boolean checkFullyParenthesised(){
        boolean numberOfParentheses = checkNumberOfParentheses();
        boolean parenthesesOrder = checkParenthesesOrder();
        boolean returnValue = false;
        //If the number of parentheses is not correct then return false; with no need to check the order of the parentheses
        if(numberOfParentheses == true){
            if(parenthesesOrder == true){
                returnValue = true;
            }
        }
        return returnValue;
    }
    
    /**
     * For an expression to be fully parenthesised, each operator must be surrounded by a pair of parentheses. Thus, if the total number of
     * parentheses in the expression divided by the total number of operators in the expression equals 2 then we know the expression inputted
     * by the user contains the correct number of parentheses
     * @return the boolean outcome of the check, true if numberOfParentheses/numberOfOperators==2, false otherwise
     */
    private boolean checkNumberOfParentheses(){
        boolean returnValue = false;
        int numberOfOperators = 0;
        int numberOfParentheses = 0;
        for(int i=0; i<userExpressionArray.length; i++){           
            //Assigns the character at position i in the array to the variable, ready for checking
            char charToCheck = userExpressionArray[i];
            //Checks if the character is an open or close parentheses, if so increment the corresponding variable
            if(charToCheck == '(' || charToCheck == ')' || charToCheck == '[' || charToCheck == ']' || charToCheck == '{' || charToCheck == '}'){
                numberOfParentheses += 1;
            }
            //checks if the character is an operator, if so increment the corresponding variable
            else if(charToCheck == '+' || charToCheck == '-' || charToCheck == '/' || charToCheck == '*' || charToCheck == '^'){
                numberOfOperators += 1;
            }
            //Then check to see if number of parentheses divided by number of operators equals 2, as discussed above
        }
        if(numberOfParentheses/numberOfOperators == 2){
            returnValue = true;
        }
        return returnValue;
    }
      
    /**
     * For an expression to be fully parenthesised, each opening parentheses must be followed
     * by a closing parentheses of the same type. So a ( must be followed by  ), but a ( cannot
     * be followed by a } or a ], for example
     * @return true if all parentheses are in correct order, returns false otherwise
     */
    private boolean checkParenthesesOrder(){
        //Now that we have confirmed the expression contains the correct number of parentheses, we need to make sure that
        //they are correctly ordered and paired in the expression. I.e. ( must be followed by )
        boolean returnValue = false;
        for(int i=0; i<userExpressionArray.length; i++){           
            //Assigns the character at position i in the array to the variable, ready for checking
            char charToCheck = userExpressionArray[i];
            //checks if the character is an open parentheses, if so push it on to the releant stack
            if(charToCheck == '('){
                leftParenthesesStack.push((Object)charToCheck);
            }
            else if(charToCheck == ')'){
                if(leftParenthesesStack.isEmpty() || leftParenthesesStack.top() == "("){
                    returnValue = false;
                }
                else{
                    leftParenthesesStack.pop();
                    returnValue = true;
                }
            }
        }
        return returnValue;
    }  

    /**
     * Outputs an error message to the user related to incorrect parentheses/parenthesising
     */
    private void parenthesesError(){
        System.out.println();
        System.out.println("Error! Expression must be fully parenthesised");
    }
    
    /**
     * Outputs an error message to the user related to the expression being the wrong length
     */
    private void expressionLengthError(){
        System.out.println();
        System.out.println("Error! Expression must be less than 20 characters");
    }
    
    /**
     * Outputs an error message related to incorrect operators being entered
     */
    private void invalidOperatorError(){
        System.out.println();
        System.out.println("Error! Expression must only contain the following operators: + * /");
    }
    
    /**
     * Dijkstra's Two Stack Algorithm, used to convert the users expression to postfix form
     */
    private void dijkstrasTwoStackAlgorithm(){
        int i=0;
        String expressionToOutput = "";
        //For each character of the userExpressionArray, do the following as long as the value of i
        //is less than the length of the user inputted expression (userExpression length = userExpressionArray length):
        if(userExpression.contains("-") || userExpression.contains("^")){
            invalidOperatorError();
        }    
        else{
            for(i=0; i<userExpressionArray.length; i++){           
                //Assigns the character at position i in the array to the variable, ready for checking
                char charToCheck = userExpressionArray[i];
                //According to the application requirements, the expression must only contain + / * operators
                //so if it contains - or ^ then throw an error to inform the user            
    
                //Checks if the character is an open parentheses, if so ignore it. As per dijkstra's two stack algorithm
                if(charToCheck == '('){
                }
                //Checks if the character is a letter between A-Z, if it is then it gets pushed on to the characterStack
                else if((charToCheck >= 'A' && charToCheck <= 'Z')){
                    characterStack.push((Object)charToCheck);
                }
                //Checks if the character is an operator, if so it pushes it on the the operatorStack as per Dijkstra's two stack algorithm            
                else if(charToCheck == '+' || charToCheck == '/' || charToCheck == '*'){
                    operatorStack.push((Object)charToCheck);            
                }
                //If the character being scanned is a right operand, the top two objects from the characterStack are popped along with the
                //the top object from the operatorStack. These are then concantenated and pushed back on to the characterStack
                //as according to Dijkstra's Two Stack Algorithm
                else if(charToCheck == ')'){
                    Object firstTerm = characterStack.pop();
                    Object secondTerm = characterStack.pop();
                    Object firstOperator = operatorStack.pop();
                    Object concatTerm = secondTerm.toString() + firstTerm.toString() + firstOperator.toString();
                    characterStack.push(concatTerm);
                }
                //If none of the above conditions execute, then character is not valid in the expression so inform the user
                else{
                    System.out.println(charToCheck + " is not a recognised character.");
                }
            }
            //The following is used to print the final outcome of dijkstra's two stack algorithm (the postfix conversion) to the user
            System.out.println();
            System.out.println("The postfix conversion of your expression is:");
            System.out.println(characterStack.pop().toString());
        }
    }
}