import java.awt.Color;
import java.util.Random;
import java.util.NoSuchElementException;

/**
 * @author (Christopher Sinders)
 * 
 * A ColorTable represents a dictionary of frequency counts, keyed on Color.
 * It is a simplification of Map<Color, Integer>. The size of the key space
 * can be reduced by limiting each Color to a certain number of bits per channel.
 */

/**
 * Implement this class, including whatever data members you need and all of the
 * public methods below. You may create any number of private methods if you
 * find them to be helpful. Replace all TODO comments with appropriate javadoc
 * style comments. Be sure to document all data fields and helper methods you
 * define.
 */

public class ColorTable {
	/**
	 * Counts the number of collisions during an operation.
	 */
	private static int numCollisions = 0;
	private int initialCapacity;
	private int bitsPerChannel;
	private int collisionStrategy;
	private double rehashThreshold;
	private int tableSize;
	private int currentSize = 0;
	private KeyValue[] table;

	/**
	 * Returns the number of collisions that occurred during the most recent get
	 * or put operation.
	 */
	public static int getNumCollisions() {
		return numCollisions;
	}

	public class Iterat implements Iterator {
		int r = 0;
		int g = 0;
		int b = 0;
		int step = (int) Math.pow(2, 8-bitsPerChannel);

		public boolean hasNext() {
			int finalIndex = (int) (step * (Math.pow(2, bitsPerChannel) - 1));
			// only if flag is true does it mean that there is no next one.
			boolean flag = (r == finalIndex && g == finalIndex && b == finalIndex)
					|| (r == 255 && b == 255 && g == 255);
			return !flag;

			// TO DO

		}

		public long next() {
			long ans = 0;
			if (this.hasNext()) {
				// System.out.println("I'm inside of hasNext()'s loop");
				// get the current answer
				if (table[getIndex(new Color(r, g, b))] == null) {
					// System.out.println("color was not in there");
					ans = 0;
				} else {
					ans = table[getIndex(new Color(r, g, b))].getCount();
					// System.out.println("The color was found");
				}
				// switch the rgb values to the next set
				if (g < b) {
					g += step;
					b = 0;
				} else if (r < g) {
					r += step;
					g = 0;
					b = 0;
				} else {
					b += step;
				}
			}
			// System.out.println("the new rgb is " + r + " " + g + " " + b);
			return ans;
		}
	}

	/**
	 * Constructs a color table with a starting capacity of initialCapacity.
	 * Keys in the color key space are truncated to bitsPerChannel bits. The
	 * collision resolution strategy is specified by passing either
	 * Constants.LINEAR or Constants.QUADRATIC for the collisionStrategy
	 * parameter. The rehashThrehold specifies the maximum tolerable load factor
	 * before triggering a rehash.
	 * 
	 * @throws RuntimeException
	 *             if initialCapacity is not in the range
	 *             [1..Constants.MAX_CAPACITY]
	 * @throws RuntimeException
	 *             if bitsPerChannel is not in the range [1..8]
	 * @throws RuntimeException
	 *             if collisionStrategy is not one of Constants.LINEAR or
	 *             Constants.QUADRATIC
	 * @throws RuntimeException
	 *             if rehashThreshold is not in the range (0.0..1.0] for a
	 *             linear strategy or (0.0..0.5) for a quadratic strategy
	 */

	public ColorTable(int initialCapacity, int bitsPerChannel, int collisionStrategy, double rehashThreshold) {
		this.initialCapacity = initialCapacity;
		this.bitsPerChannel = bitsPerChannel;
		this.collisionStrategy = collisionStrategy;
		this.rehashThreshold = rehashThreshold;
		if (initialCapacity < 1 || initialCapacity > Constants.MAX_CAPACITY) {
			throw new RuntimeException("initialCapacity is not between " + 1 + " and " + Constants.MAX_CAPACITY);
		}
		if (bitsPerChannel < 1 || bitsPerChannel > 8) {
			throw new RuntimeException("bitsPerChannel is not between " + 1 + " and 8");
		}
		if (collisionStrategy != Constants.LINEAR && collisionStrategy != Constants.QUADRATIC) {
			throw new RuntimeException("collisionStrategy is not one of Constants.LINEAR or Constants.QUADRATRIC");
		}
		if (collisionStrategy == Constants.LINEAR) {
			if (rehashThreshold < 0 || rehashThreshold >= 1) {
				throw new RuntimeException("rehashThreshold is not between 0 and 1 for a linear collision strategy");
			}
		} else {
			if (rehashThreshold < 0 || rehashThreshold > .5) {
				throw new RuntimeException("rehashThreshold is not between 0 and .5 for a quadratic collsion strategy");
			}
		}
		table = new KeyValue[initialCapacity];
		tableSize = initialCapacity;
	}

	/**
	 * 
	 * Returns the number of bits per channel used by the colors in this table.
	 */
	public int getBitsPerChannel() {
		return bitsPerChannel;
	}

	/**
	 ** 
	 * Returns the frequency count associated with color. Note that colors not
	 * explicitly represented in the table are assumed to be present with a
	 * count of zero. Uses Util.pack() as the hash function.
	 */
	public long get(Color color) {
		int index = getIndex(color);
		if (table[index] == null) {
			return 0;
		} else {
			return table[index].getCount();
		}
	}

	/**
	 * 
	 * Associates the count with the color in this table. Do nothing if count is
	 * less than or equal to zero. Uses Util.pack() as the hash function.
	 */
	public void put(Color color, long count) {
		int index = getIndex(color);
		if (table[index] == null) {
			currentSize++;
			table[index] = new KeyValue(Util.pack(color, bitsPerChannel), count);
			if (getLoadFactor() >= rehashThreshold) {
				rehash();
			}
		}
		table[index] = new KeyValue(Util.pack(color, bitsPerChannel), count);
	}

	/**
	 * 
	 * Increments the frequency count associated with color. Note that colors
	 * not explicitly represented in the table are assumed to be present with a
	 * count of zero.
	 */
	public void increment(Color color) {
		int index = getIndex(color);
		if (table[index] == null) {
			currentSize++;
			table[index] = new KeyValue(Util.pack(color, bitsPerChannel), 1);
			if (getLoadFactor() >= rehashThreshold) {
				rehash();
			}
			// System.out.println(currentSize);
		} else {
			table[index].incrementCount();
		}
	}

	/**
	 * Returns the load factor for this table.
	 */
	public double getLoadFactor() {
		return ((double) currentSize) / tableSize;
	}

	/**
	 * Returns the size of the internal array representing this table.
	 */
	public int getCapacity() {
		return tableSize;
	}

	/**
	 * Returns the number of key/value associations in this table.
	 */
	public int getSize() {
		return currentSize;
	}

	/**
	 * Returns true iff this table is empty.
	 */
	public boolean isEmpty() {
		return currentSize == 0;
	}

	/**
	 * Increases the size of the array to the smallest prime greater than double
	 * the current size that is of the form 4j + 3, and then moves all the
	 * key/value associations into the new array.
	 * 
	 * Hints: -- Make use of Util.isPrime(). -- Multiplying a positive integer n
	 * by 2 could result in a negative number, corresponding to integer
	 * overflow. You should detect this possibility and crop the new size to
	 * Constants.MAX_CAPACITY.
	 * 
	 * @throws RuntimeException
	 *             if the table is already at maximum capacity.
	 */
	private void rehash() {
		// System.out.println("inside of rehash");
		if (tableSize == Constants.MAX_CAPACITY) {
			throw new RuntimeException("Reached max size of the table");
		}
		int i = 0;
		while (((4 * i) + 3) < tableSize * 2) {
			i++;
		}
		while (!Util.isPrime(((4 * i) + 3))) {
			i++;
		}
		int newSize = (4 * i) + 3;
		if (newSize < 0) {
			// array overflowed so set it to max capacity.
			newSize = Constants.MAX_CAPACITY;
		}
		// System.out.println("the new size is " + newSize);
		// Create a new table of the old size and then resize the old table.
		KeyValue[] tempTable = new KeyValue[tableSize];
		tableSize = newSize;
		// Since rehashing the table, starting value of currentSize is 0
		currentSize = 0;
		for (int j = 0; j < tempTable.length; j++) {
			tempTable[j] = table[j];
		}
		table = new KeyValue[newSize];

		// Rehash everything from the tempTable into the resized one.
		for (int j = 0; j < tempTable.length; j++) {
			if (tempTable[j] != null) {
				put(Util.unpack(tempTable[j].getKey(), bitsPerChannel), tempTable[j].getCount());
			}
		}
	}

	/**
	 * Returns an Iterator that marches through each color in the key color
	 * space and returns the sequence of frequency counts.
	 */
	public Iterator iterator() {
		return new Iterat();
	}

	/*
	 * Finds the index of a color, or the next available index for the color
	 * according to hashing strategy
	 */
	public int getIndex(Color color) {
		// System.out.println("testing " + getLoadFactor() + " > " +
		// rehashThreshold);
		if (getLoadFactor() >= rehashThreshold) {
			rehash();
		}
		// System.out.println("Inside of getIndex");
		int hash = Util.pack(color, bitsPerChannel);
		numCollisions = 0;
		int collision = 0;
		int index = getNextIndex(hash, collision);
		// Use the Linear strategy
		while (table[index] != null) {
			// System.out.println("returning " + index);
			if (table[index].getKey() == hash) {
				// System.out.println(index);
				return index;
			}
			collision++;
			numCollisions++;
			index = getNextIndex(hash, collision);
		}
		// System.out.println(index);
		return index;
	}

	/*
	 * Returns the nextIndex to check based on how many collisions there have
	 * been
	 */
	public int getNextIndex(int hash, int collision) {
		int index = 0;
		if (collisionStrategy == Constants.LINEAR) {
			index = (hash + collision) % tableSize;
		} else {
			// The table must be using Quadratic Collisions
			index = (hash + (int) Math.pow(collision, 2)) % tableSize;
		}
		return index;
	}

	/**
	 * Returns a String representation of this table.
	 */
	public String toString() {
		String answer = "[";
		for (int i = 0; i < tableSize; i++) {
			if (table[i] != null) {
				answer = answer + i + ":" + table[i].getKey() + "," + table[i].getCount() + ", ";
			}
		}
		answer = answer + "]";
		return answer;
	}

	/**
	 * Returns the count in the table at index i in the array representing the
	 * table. The sole purpose of this function is to aid in writing the unit
	 * tests.
	 */
	public long getCountAt(int i) {
		if (table[i] != null) {
			return table[i].getCount();
		} else {
			return 0;
		}
	}

	/**
	 * Simple testing.
	 */
	public static void main(String[] args) {
		ColorTable table = new ColorTable(3, 6, Constants.QUADRATIC, .49);
		int[] data = new int[] { 32960, 4293315, 99011, 296390 };
		for (int i = 0; i < data.length; i++) {
			// System.out.println("on increment " + i);
			table.increment(new Color(data[i]));
		}
		System.out.println("capacity: " + table.getCapacity()); // Expected: 7
		System.out.println("size: " + table.getSize()); // Expected: 3

		System.out.println();
		ColorTable test = new ColorTable(3, 6, Constants.QUADRATIC, .49);
		int[] testData = new int[] { 32960, 32960, 32960, 32960 };
		for (int i = 0; i < testData.length; i++) {
			test.increment(new Color(testData[i]));
		}
		/*
		 * Color[] testEqual = new Color[4]; for (int i = 0; i < data.length;
		 * i++) { testEqual[i] = new Color (data[i]); }
		 * System.out.println("testing if all these colors are equal"); for (int
		 * i = 0; i < testEqual.length; i++) { for (int j = i+1; j <
		 * testEqual.length; j++) { System.out.println("Testing if index " + i +
		 * " is equal to index " + j);
		 * System.out.println(testEqual[i].equals(testEqual[j])); } } int[]
		 * testHash = new int[4]; for (int i = 0; i < data.length; i++) {
		 * testHash[i] = Util.pack(testEqual[i], 6); }
		 * System.out.println("testing if the hash values are equal"); for (int
		 * i = 0; i < testHash.length; i++) { for (int j = i+1; j <
		 * testHash.length; j++) { System.out.println("Testing if index " + i +
		 * " is equal to index " + j); System.out.println(testHash[i] ==
		 * testHash[j]); } }
		 */

		/*
		 * The following automatically calls table.toString(). Notice that we
		 * only include non-zero counts in the String representation.
		 * 
		 * Expected: [3:2096,2, 5:67632,1, 6:6257,1]
		 * 
		 * This shows that there are 3 keys in the table. They are at positions
		 * 3, 5, and 6. Their color codes are 2096, 67632, and 6257. The code
		 * 2096 was incremented twice. You do not have to mimic this format
		 * exactly, but strive for something compact and readable.
		 */
		System.out.println(table);
		System.out.println(test);
		Color[] iteratorTest = { new Color(0, 0, 0), new Color(0, 0, 128), new Color(0, 128, 0), new Color(0, 128, 128),
				new Color(128, 0, 0), new Color(128, 0, 128), new Color(128, 128, 0), new Color(128, 128, 128) };
		test = new ColorTable(3, 8, Constants.LINEAR, .99);
		for (int i = 0; i < iteratorTest.length; i++) {
			test.increment(iteratorTest[i]);
		}
		System.out.println(test);
		System.out.println("capacity: " + test.getCapacity()); // Expected: 11
		System.out.println("size: " + test.getSize()); // Expected: 8
		Iterator iteratorTestor = test.iterator();
	}
}