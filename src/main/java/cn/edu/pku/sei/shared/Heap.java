package cn.edu.pku.sei.shared;

import java.util.List;
import java.util.ArrayList;
import java.util.NoSuchElementException;

interface PurePriorityQueue<E>
{
  int size();
  boolean isEmpty();
  void add(E element);
  // Returns a high-priority element without removing it.
  E getMin();
  // Removes a high-priority element.
  E removeMin();
}

public class Heap<E extends Comparable<? super E>>
   implements PurePriorityQueue<E>
{
  private static final int INIT_CAP = 10;
  private ArrayList<E> list;

  public Heap()
  {
    list = new ArrayList<E>(INIT_CAP);
  }

  public Heap(int aSize)
  {
     if ( aSize < 1 )
	throw new IllegalStateException();
     list = new ArrayList<E>(aSize);
  }

  // Builds a heap from a list of elements.
  public Heap(List<E>  aList)
  {
     int j, k;
     int len = aList.size();
     if ( len < 1 )
	throw new IllegalArgumentException();
     list = new ArrayList<E>(len);
     for ( E t : aList )
        list.add(t);
     if ( len < 2 ) return;
     j = (len - 2) / 2; // j is the largest index of an internal node with a child.
     for ( k = j; k >= 0; k-- )
        percolateDown(k);
  }  // O(n) time

  public int size()
  {
    return list.size();
  }

  public boolean isEmpty()
  {
    return list.isEmpty();
  }

  public void add(E element)
  {
    if ( element == null )
      throw new NullPointerException("add");
    list.add(element); // append it to the end of the list
    percolateUp(); // move it up to the proper place
  }

  // move the last element up to the proper place.
  private void percolateUp()
  {
     int child = list.size() - 1; // last element in the list
     int parent;
     while ( child > 0 )
     {
       parent = (child - 1) / 2; // use the (j-1)/2 formula
       if ( list.get(child).compareTo(list.get(parent)) >= 0 )
          break;
       swap(parent, child);
       child = parent;
     }
  }

  private void swap(int parent, int child)
  {
    E tmp = list.get(parent);
    list.set( parent, list.get(child) );
    list.set(child, tmp);
  }

  public E getMin()
  {
    if ( list.isEmpty() )
      throw new NoSuchElementException();
    return list.get(0);
  }

  public E removeMin()
  {
    if ( list.isEmpty() )
      throw new NoSuchElementException();
    E minElem = list.get(0); // get the min element at the root
    list.set(0, list.get(list.size() - 1) ); // copy the last element to the root
    list.remove( list.size() - 1 ); // remove the last element from the list
    if ( ! list.isEmpty() )
     percolateDown(0); // move the element at the root down to the proper place
    return minElem;
  }

  // Move the element at index start down to the proper place.
  private void percolateDown(int start)
  {
    if ( start < 0 || start >= list.size() )
      throw new RuntimeException("start < 0 or >= n");
    int parent = start;
    int child = 2 * parent + 1; // use the 2*i+1 formula
    while ( child < list.size() )
    {
      if ( child + 1 < list.size() &&
           list.get(child).compareTo(list.get(child + 1)) > 0 )
          child++; // select the smaller child
      if ( list.get(child).compareTo(list.get(parent)) >= 0 )
          break; // reach the proper place
      swap(parent, child);
      parent = child;
      child = 2 * parent + 1;
    }
  }

  // Heap Sort
  public static <E extends Comparable<? super E>> void heapSort(List<E> aList)
  {
    if ( aList.isEmpty() ) return;
    Heap<E> aHeap = new Heap<E>(aList);
    aList.clear();
    while ( ! aHeap.isEmpty() )
      aList.add( aHeap.removeMin() );
  } // repeatedly remove the smallest element in the heap and add it to the list.

} // Heap
