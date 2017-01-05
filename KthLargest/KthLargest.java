import java.util.Arrays;
import java.util.Comparator;

/**
 * TODO: Report the results of your time trials here.
 *  
 * @author <insert your name here>
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
    // TODO
    return 0;
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
    assert kthLargest1(a, 1) == 12;
    assert kthLargest1(a, 2) == 9;
    assert kthLargest1(a, 3) == 8;
    assert kthLargest1(a, 4) == 7;
  }
}
