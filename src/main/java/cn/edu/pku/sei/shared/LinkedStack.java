package cn.edu.pku.sei.shared;

import java.util.NoSuchElementException;

/**
* A stack is a special list of elements in which
* elements are added and removed at one end.
* A stack interface with common stack operations:
**/

interface PureStack<E>
{
  int size();
  boolean isEmpty();

// Adds an element to the top.
  void push(E elemeSnt);

// Removes and returns the top element.
   E pop();

// Returns the top element without removing it.
   E peek();
}

/**
*
* A simple class that implements the PureStack interface with linked nodes.
* Each method of the class takes O(1) time.
*
a**/


public class LinkedStack<E> implements PureStack<E>
{
  private class SNode
  {
    public E data;
    public SNode link;
  }

  private SNode top;    // refers to the top node of the stack.
  private int numItems; // number of elements in the stack.

  public LinkedStack()
  {
    top = null;
    numItems = 0;
  }

  public int size()
  {
    return numItems;
  }

  public boolean isEmpty()
  {
     return numItems == 0;
  }

  public void push(E element)
  {
    SNode toAdd = new SNode();
    toAdd.data = element;
    toAdd.link = top;
    top = toAdd;
    numItems++;
  }

  public E pop()
  {
    if ( top == null )
      throw new NoSuchElementException();
    E returnVal = top.data;
    top = top.link;
    if ( numItems <= 0 )
      throw new RuntimeException("An incorrect number of elements");
    numItems--;
    return returnVal;
  }

  public E peek()
  {
    if ( top == null )
      throw new NoSuchElementException();
    return top.data;
  }
} // LinkedStack
