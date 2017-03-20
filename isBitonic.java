import static org.junit.Assert.*;

/*
 * People who contributed
 *  Christopher Sinders - Wrote initial version
 * 
 */

public class isBitonic {

	public static Boolean test(Integer[] array, int low, int high) {
		if (array == null) {
			// System.out.println("The array was null");
			return false;
		}
		if (low < 0 || high >= array.length) {
			throw new IndexOutOfBoundsException(
					"Either low is less than 0 or high is greater than the size of the array");
		}

		if (low + 4 > high) {
			// Can only be bitonic if the array has 4 or less members
			// System.out.println("The size of the subarray was less than 4");
			return true;
		}
		// number of times the array switches direction
		int switches = 0;
		// false = decreasing, true = increasing
		boolean wasIncreasing = array[low] < array[low + 1];
		for (int pos = low; pos < array.length - 1 && switches <= 2; pos++) {
			// check to see if it is increasing
			if (array[pos] < array[pos + 1]) {
				// check if it previously was not increasing
				if (!wasIncreasing) {
					// System.out.println("Switching at index " + pos + " and "
					// + (pos + 1));
					switches++;
					wasIncreasing = !wasIncreasing;
				}
			} else if (array[pos] > array[pos + 1]) {
				// Check to see if it was previously increasing
				if (wasIncreasing) {
					// System.out.println("Switching at index " + pos + " and "
					// + (pos + 1));
					switches++;
					wasIncreasing = !wasIncreasing;
				}
			}
			// if array[pos] == array[pos+1] do nothing
			// System.out.println("switches is " + switches);

		}

		return switches <= 2;

	}

	public static void main(String[] args) {

		// test cases
		Integer[] array = new Integer[5];
		for (int i = 0; i < array.length; i++) {
			array[i] = i;
		}
		// low == high
		assertTrue(test(array, 0, 0));
		// low > high
		assertTrue(test(array, 1, 0));
		// low + 1 == high
		assertTrue(test(array, 0, 1));
		// n == 1
		Integer[] a1 = new Integer[1];
		a1[0] = 1;
		assertTrue(test(array, 0, 1)); // n == 2
		a1 = new Integer[2];
		a1[0] = 1;
		a1[1] = 2;
		assertTrue(test(array, 0, 2));

		// Initial test
		array = new Integer[5];
		array[0] = 1;
		array[1] = 2;
		array[2] = 1;
		array[3] = 2;
		array[4] = 1;
		assertFalse(test(array, 0, 4));

		// test for all same
		for (int i = 0; i < array.length; i++)
			array[i] = 1;
		assertTrue(test(array, 0, 4));

		// test for large increasing
		array = new Integer[100];
		for (int i = 0; i < array.length; i++) {
			array[i] = i;
		}
		assertTrue(test(array, 0, array.length - 1));
		// test for large decreasing
		for (int i = 100; i < array.length; i--) {
			array[array.length - i] = i;
		}
		assertTrue(test(array, 0, array.length - 1));

		System.out.println("All tests sucessful");

	}

}
