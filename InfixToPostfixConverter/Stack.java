
/**
 * Write a description of class Stack here.
 * 
 * @author (Steven Duckett) 
 * @version (v1.0)
 */
public interface Stack<E>{
    /**
     * Return the size of the stack, returns null if Stack is empty
     */
    int size();
    
    /**
     * Return true if the stack is empty, return false otherwise
     */
    boolean isEmpty();
    
    /**
     * Push the object, e, on to the top of the stack
     * @param the Object e, of generic type Element, to be pushed on to the stack
     */
    void push(E e);
    
    /**
     * Removes the top element from the list AND returns it
     * Returns null if stack empty
     */
    E pop();
    
    /**
     * Aka peek()
     * Returns the top element of the stack, but DOES NOT remove it fom the stack
     * Returns null if the stack is empty
     */
    E top();
}
