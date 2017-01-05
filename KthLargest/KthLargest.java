import java.util.Arrays;
import java.util.Comparator;


/**
 * TODO: Report the results of your time trials here.
 * 
 * kthLargest1 is providing significantly faster results than version 2 for me.  Anywhere between 5-10 times faster
 * for the array of size 10000.  I believe this is because of the fact that version 2 has to iterate over the 
 * array every time new values are added, where as version 1 only has to iterate over it during the sort process.
 * An idea for increasing the speed of version 2 would be to use binary search instead of linear to find the 
 * index to insert at.  
 *  
 * @author <Christopher Sinders>
 */

public class KthLargest {
  
  /**
   * Weiss (pg 1) Suppose you have a group of N numbers and would like to
   * determine the kth largest. This is known as the selection problem.
   * 
   * One way to solve this problem would be to read the N numbers into an array,
   * sort the array in decreasing order by some simple algorithm such as
   * bubblesort, and then return the element in position k.
   */
  
  public static int kthLargest1(int[] a, int k) {
    assert a != null;
    int n = a.length;
    assert n > 0 && k <= n;
    Integer[] b = new Integer[n];
    for (int i = 0; i < n; i++)
      b[i] = a[i];
    Arrays.sort(b, new Comparator<Integer>() {
      public int compare(Integer x, Integer y) {
        return y.compareTo(x);
      }
    });
    return b[k - 1];
  }
  
  
  /**
   * Weiss (pg 1) A somewhat better algorithm might be to read the first k
   * elements into an array and sort them (in decreasing order). Next, each
   * remaining element is read one by one. As a new element arrives, it is
   * ignored if it is smaller that the kth element in the array. Otherwise, it
   * is placed in its correct spot in the array, bumping one element out of the
   * array. When the algorithm ends, the element in the kth position is returned
   * as the answer.
   */
  
  public static int kthLargest2(int[] a, int k) {
    assert a != null;
    int n = a.length;
    assert n > 0 && k <= n;
    Integer[] b = new Integer[k];
    
    // Place the first k number elements into the array
    for (int i = 0; i < k; i++){
      b[i] = a[i];
    }
    // Sort the elements in descending order
    Arrays.sort(b, new Comparator<Integer>() {
      public int compare(Integer x, Integer y) {
        return y.compareTo(x);
      }
    });
    // Read through the remaining elements
    for (int nextElement = k; nextElement < n; nextElement++) {
      if (a[nextElement] > b[k-1]) {
        // the next element is bigger than my smallest element, so insert it into the array
        int insertAt = 0;
        // Find out where to insert the new element
        while (b[insertAt] > a[nextElement]) {
          insertAt++;
        }
        // Shift everything from where we are inserting, up the array
        for (int i = k-1; i > insertAt; i--) {
          b[i] = b[i-1];
        }
        // insert the new element into the array
        b[insertAt] = a[nextElement];
      }
    }
    return b[k-1];
  }
  /**
   * To enable asserts in Eclipse: 
   * 1. On Windows, go to menu Window. On a Mac, go to menu Eclipse. 
   * 2. Go to Preferences. 
   * 3. Choose Java, and then "Installed JREs" from the left panel. 
   * 4. Select your JRE, and then click the Edit... button in the right panel. 
   * 5. Enter -ea in the "Default VM arguments" field.
   */
  
  public static void main(String[] args) {
    int[] a;
    a = new int[] { 8, 7, -3, 9, 2, 1, -5, 4, 12, -2 };
    // intital asserts
    assert kthLargest1(a, 1) == 12;
    assert kthLargest1(a, 2) == 9;
    assert kthLargest1(a, 3) == 8;
    // Added Additional asserts
    assert kthLargest1(a, 4) == 7;
    assert kthLargest1(a, 5) == 4;
    assert kthLargest1(a, 6) == 2;
    assert kthLargest1(a, 7) == 1;
    assert kthLargest1(a, 8) == -2;
    assert kthLargest1(a, 9) == -3;
    // Inserts for KthLargest2
    assert kthLargest2(a, 1) == 12;
    assert kthLargest2(a, 2) == 9;
    assert kthLargest2(a, 3) == 8;
    assert kthLargest2(a, 4) == 7;
    assert kthLargest2(a, 5) == 4;
    assert kthLargest2(a, 7) == 1;
    assert kthLargest2(a, 8) == -2;
    assert kthLargest2(a, 9) == -3;
    
//   In main, create an array containing 10,000 random integers. 
//   Run each method to find the median value in the array. 
//   (Note that the median for a group of n numbers is the element that is the n/2 largest.) 
//   Use System.currentTimeMillis() to compute the running time for each algorithm.
//   Report your results in comments at the top of your file. 
    
    int arraySize = 10000;
    int[] randomArray = new int[arraySize];
    for (int i = 0; i < arraySize; i++) {
      randomArray[i] = (int) (Math.random() * arraySize);
    }
    //Test for kthLargest1
    long startTime = System.currentTimeMillis();
    kthLargest1(randomArray,arraySize/2);
    long endTime = System.currentTimeMillis();
    long duration = endTime - startTime;
    System.out.println("Time on kthLargest1 for " + arraySize + " integers is = " + 
                       duration);
    
    //Test for kthLargest2
    startTime = System.currentTimeMillis();
    kthLargest2(randomArray,arraySize/2);
    endTime = System.currentTimeMillis();
    duration = endTime - startTime;
    System.out.println("Time on kthLargest2 for " + arraySize + " integers is = " + 
                       duration);
  }
}