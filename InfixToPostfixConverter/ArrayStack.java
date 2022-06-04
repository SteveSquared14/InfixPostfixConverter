import java.io.FileNotFoundException;
import java.util.EmptyStackException;
/**
 * Write a description of class ArrayStack here.
 * 
 * @author (Steven Duckett) 
 * @version (v1.0)
 */
public class ArrayStack<E> implements Stack{
    public static final int capacity = 20; //The capacity of the ArrayStack as specified in the assignment brief
    private Object[] stack; //A generic array of type object for storage
    private int top = -1; //Array index starts at 0, so the E at the top of the Stack will always be at position size()-1
   
    /**
     * Constructor for manually creating a new ArrayStack
     * @parameter the capacity as specified by the user
     */
    public ArrayStack(int newArrayStackCapacity){
        //Capacity of the array cannot be less than one, so if it is throw and error to let the user know
        if(capacity < 1){
            throw new IllegalArgumentException("Initial capacity of stack must be greater than 1!");
        }
        else{
            //If capacity is greater than one, make a new array of type E with the capacity specified by the user
            stack = new Object[newArrayStackCapacity];
        }
    }
    
    /**
     * Default constructor for ArrayStack with capacity of 20 as 
     * specified in the assignment brief
     */
    public ArrayStack()
    {
        this(capacity);               
    }
    
    /**
     * Checks if the stack is empty by returning true if top equals -1
     *  @return true if top equals -1, false otherwise
     */
    public boolean isEmpty(){
        //If the stack is empty, the top E will be at position -1
        //So this method checks to see if top == -1 and returns true if it is, indicating the stack is empty
        return top == -1;
    }
    
    /**
     * @return the size of the stack
     */
    public int size(){
        return (top + 1); //if top is equal to -1, then the the size of the stack must be top + 1 because the indexing starts at 0
    }
    
    /**
     * @param the object to be added to the top of the stack
     */
    public void push(Object e){
        //Before adding a new E on to the stack, it is best practice to check if the array has reached the 
        //maximum pre-allocated capacity because if it had, the array size needs to be doubled
        //However, this should never be the case for this application as the assignment brief specifies the maximum size/capacity of the 
        //user input is 20 characters (Which is the default capacity set in this class)
        if(top == stack.length -1){
            Object[] newArray = new Object[stack.length*2];
            System.arraycopy(stack, 0, newArray, 0, stack.length);
            stack = newArray;
        }
        else{
            //Index position of the object is equal to the value of top after it is pre-incremented
            stack[++top] = e;
        }
    }
    
    /**
     * Aka peek()
     * Returns the top E in the stack, which is at position top (-1)
     * If the stack is empty, then there is nothing to return so throw an EmptyStackException to let the user know
     */
    public Object top(){
        if(isEmpty()){
            //If the stack is empty, then there cannot be anything to return
            //so throw an error to inform the user
            throw new EmptyStackException();
        }
        else{
            //Return the top element from the stack by passing top as the index of the stack
            return stack[top];
        }
    }
    
    /**
     * Remove and turn the top element from the stack provided the stack is not empty
     * if the stack is empty, throw an EmptyStackException
     * @return the object removed from the stack
     */
    public Object pop(){
        if(isEmpty()){
            //If the stack is empty, then there is nothing to remove so throw an error to inform the user
            throw new EmptyStackException();
        }
        else{
            //First make a new object which is passed the object at the top of the stack
            Object topObject = stack[top];
            //Then decrease the size of the stack by one by pre-incrementing the value of top and then returning that value to the 
            //index of the array
            stack[top--] = null;
            //Now the array has been adjusted, return the object that was previously at the top
            return topObject;
        }
    }
}
