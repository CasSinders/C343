import java.util.Arrays;

/**
 * TODO: Report the results of your time trials here.
 * 
 * Time trials for values up to 10^8 
 * 1000 integers is = 0 
 * 10000 integers is = 0 
 * 100000 integers is = 2 
 * 1000000 integers is = 0 
 * 10000000 integers is = 5 
 * 100000000 integers is = 52
 * 
 * The function runs at Big O of n.  
 *   This is because each index in the array is visted one time.  
 * 
 * @author <Christopher Sinders>
 */

public class RotateArray {
  
  /**
   * Takes an array of integers and rotates the array elements in place
   * one position to the right. The last element wraps around to the
   * beginning of the array.
   */
  
  public static void rotateRight(int[] a) {
    // create a temp variable to hold the value of the last member of the array.
    int temp = a[a.length-1];
    // slide the entire array down one.
    for (int i = a.length-1; i > 0; i--) {
      a[i] = a[i-1];
    }
    // place the temp variable at the front to wrap-around.
    a[0] = temp;
  }
  
  public static void main(String[] args) {
    //Initial tests
    int[] a;
    a = new int[] { 2, 4, 6, 8, 10 };
    System.out.println("before: a = " + Arrays.toString(a));
    rotateRight(a);
    System.out.println(" after: a = " + Arrays.toString(a));
    
    a = new int[] { 0, 3, 7 };
    System.out.println("before: a = " + Arrays.toString(a));
    rotateRight(a);
    System.out.println(" after: a = " + Arrays.toString(a));
    
    // Additional required tests.
    for (int arraySize = 1000; arraySize <= Math.pow(10,8); arraySize*=10) {
      a = new int[arraySize];
      for (int i = 0; i < arraySize; i++) {
        a[i] = (int) (Math.random() * arraySize);
      }
      long startTime = System.currentTimeMillis();
      rotateRight(a);
      long endTime = System.currentTimeMillis();
      long duration = endTime - startTime;
      System.out.println("Time on rotateRight for " + arraySize + " integers is = " + 
                         duration);
    }
  }
}
