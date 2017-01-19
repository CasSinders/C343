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
 * TODO
 * 
 * Implement this class, including whatever data members you need and all of the
 * public methods below. You may create any number of private methods if you find
 * them to be helpful. Replace all TODO comments with appropriate javadoc style 
 * comments. Be sure to document all data fields and helper methods you define.
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
   * Returns the number of collisions that occurred during the most recent get or
   * put operation.
   */
  public static int getNumCollisions() {
    return numCollisions;
  }

  /**
   * Constructs a color table with a starting capacity of initialCapacity. Keys in
   * the color key space are truncated to bitsPerChannel bits. The collision resolution
   * strategy is specified by passing either Constants.LINEAR or Constants.QUADRATIC for
   * the collisionStrategy parameter. The rehashThrehold specifies the maximum tolerable load 
   * factor before triggering a rehash.
   * 
   * @throws RuntimeException if initialCapacity is not in the range [1..Constants.MAX_CAPACITY]
   * @throws RuntimeException if bitsPerChannel is not in the range [1..8]
   * @throws RuntimeException if collisionStrategy is not one of Constants.LINEAR or Constants.QUADRATIC
   * @throws RuntimeException if rehashThreshold is not in the range (0.0..1.0] for a
   *                             linear strategy or (0.0..0.5) for a quadratic strategy
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
		  if (rehashThreshold < 0 || rehashThreshold > 1) {
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
   * Done
   * 
   * Returns the number of bits per channel used by the colors in this table.
   */
  public int getBitsPerChannel() {
    return bitsPerChannel;
  }

  /**
   * I believe this is good, do testing.
   * 
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
   * TODO
   * 
   * Associates the count with the color in this table. Do nothing if count is less than
   * or equal to zero. Uses Util.pack() as the hash function.
   */
  public void put(Color color, long count) {
  /**
   * increment the current size of the array.
   */
	  currentSize++;
	  if (getLoadFactor()  > rehashThreshold) {
		  rehash();
	  }
	  int index = getIndex(color);
	  table[index] = new KeyValue(color, count);
  }

  /**
   * Done
   * 
   * Increments the frequency count associated with color. Note that colors not
   * explicitly represented in the table are assumed to be present with a
   * count of zero.
   */
  public void increment(Color color) {
	int index = getIndex(color);
	if (table[index] == null) {
		table[index] = new KeyValue(color, 1);
		currentSize++;
	} else {
		table[index].incrementCount();
	}
  }

  /**
   * TODO
   * 
   * Returns the load factor for this table.
   */
  public double getLoadFactor() {
    return ((double) currentSize) / tableSize;
  }

  /**
   * TODO
   * 
   * Returns the size of the internal array representing this table.
   */
  public int getCapacity() {
    return tableSize;
  }

  /**
   * TODO
   * 
   * Returns the number of key/value associations in this table.
   */
  public int getSize() {
    return currentSize;
  }

  /**
   * TODO
   * 
   * Returns true iff this table is empty.
   */
  public boolean isEmpty() {
	return currentSize == 0;
  }

  /**
   * TODO
   * 
   * Increases the size of the array to the smallest prime greater than double the 
   * current size that is of the form 4j + 3, and then moves all the key/value 
   * associations into the new array. 
   * 
   * Hints: 
   * -- Make use of Util.isPrime().
   * -- Multiplying a positive integer n by 2 could result in a negative number,
   *    corresponding to integer overflow. You should detect this possibility and
   *    crop the new size to Constants.MAX_CAPACITY.
   * 
   * @throws RuntimeException if the table is already at maximum capacity.
   */
  private void rehash() { 
	  // System.out.println("inside of rehash");
	  if (tableSize == Constants.MAX_CAPACITY) {
		  throw new RuntimeException("Reached max size of the table");
	  }
	  // find out what value of i to start with
	  int i = ((tableSize * 2) - 3) / 4;
	  // it has to have a minimum value of 1
	  i = Math.max(i, 1);
	  // System.out.println("initial value of i is " + i);
	  
	  while(!Util.isPrime( ((4 * i) + 3)) ) {
		  i++;
	  }
	  int newSize = (4 * i) + 3;
	  if (newSize < 0) {
		  newSize = Constants.MAX_CAPACITY;
	  }
	  // System.out.println("the new size is " + newSize);
	  /* Create a new instance of ColorTable.  Hash everything from the old ColorTable into the new one.
	   * Set the current instance of ColorTable to the new instance.
	   */
	  KeyValue[] tempTable = new KeyValue[newSize];
	  for (int j = 0; j < tableSize; j++) {
		  int index = 0;
		  if (table[j] != null) {
			  int hash = Util.pack(table[j].getKey(), bitsPerChannel);
			  int collision = 0;
			  // Do Linear Strategy
			  if (collisionStrategy == Constants.LINEAR) {
				  index = (hash + collision) % newSize;
				  while(tempTable[index] != null) {
					  collision++;
					  index = (hash + collision) % newSize;
				  }
				  tempTable[index] = table[j];
				  // Since it is not Linear do Quadratic
			  } else { 
				  index = (hash + (int) Math.pow(collision,2)) % newSize;
				  while(tempTable[index] != null) {
					  collision++;
					  index = (hash + (int) Math.pow(collision, 2)) % newSize;
				  }
				  tempTable[index] = table[j];
			  }
		  }
	  }
	 table = tempTable;
	 tableSize = newSize;
  }

  /**
   * TODO
   * 
   * Returns an Iterator that marches through each color in the key color space and
   * returns the sequence of frequency counts.
   */
  public Iterator iterator() {
	  class Iterat implements Iterator {
		  long current = 0;
		  int r = 0;
		  int g = 0;
		  int b = 0;
		  int currentChannel = bitsPerChannel;
		  public boolean hasNext() {
			// TO DO 
			  
			  
			  
			  
		  }
		  public long next() {
			  if(this.hasNext()) {
				  long ans = 0;
				  // get the current answer
				  if (table[getIndex(new Color(r,g,b))] == null) {
					  ans = 0;
				  } else {
					  ans = table[getIndex(new Color(r,g,b))].getCount();
				  }
				  // switch the rgb values to the next set
				  
				  
				  
				  
				  //
			  }
			  return ans;
		  }
	  }
    return new Iterat();
  }
  
  /*
   * Finds the index of a color, or the next available index
   */
  public int getIndex(Color color) {
	  // System.out.println("testing " + getLoadFactor() + " > " + rehashThreshold);
	  if (getLoadFactor() > rehashThreshold) {
		  rehash();
	  }
	  // System.out.println("Inside of getIndex");
	  int hash = Util.pack(color, bitsPerChannel);
	  numCollisions = 0;
	  int collision = 0;
	  int index = 0;
	  // Use the Linear strategy
	  if (collisionStrategy == Constants.LINEAR) {
		  index = (hash + collision) % tableSize;
		  while(table[index] != null) {
			  // System.out.println("returning " + index);
			  if (table[index].getKey().equals(color)) {
				  // System.out.println(index);
				  return index;
			  }
			  collision++;
			  numCollisions++;
			  index = (hash + collision) % tableSize;
		  }
		  // System.out.println(index);
		  return index;
	  } else {
		  // Since it is not Linear, it must be quadratic
		  index = (hash + (int) Math.pow(collision, 2)) % tableSize;
		  // System.out.println(index);
		  while(table[index] != null) {
			 // System.out.println("testing if " + table[index].getKey() + " is == " + color);
			  if (table[index].getKey().equals(color)) {
		//		  System.out.println("The colors matched");
				  // System.out.println("found index " + index);
				  // System.out.println(index);
				  return index;
			  }
			  collision++;
			  numCollisions++;
			  index = (hash + (int) Math.pow(collision, 2)) % tableSize;
			  // // System.out.println("now trying index" + index);
		  }
		  // System.out.println("found index " + index);
		  return index;
	  }
  }

  /**
   * TODO
   * 
   * Returns a String representation of this table.
   */
  public String toString() {
	  String answer = "[";
	  for (int i = 0; i < tableSize; i++) {
		  if (table[i] != null) {
			  answer = answer + i + ":" + Util.pack(table[i].getKey(),bitsPerChannel) + "," + table[i].getCount() + ", ";
		  }
	  }
	  answer = answer + "]";
    return answer;
  }

  /**
   * TODO
   * 
   * Returns the count in the table at index i in the array representing the table.
   * The sole purpose of this function is to aid in writing the unit tests.
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
     //System.out.println("on increment " + i);
      table.increment(new Color(data[i]));
    }
     System.out.println("capacity: " + table.getCapacity()); // Expected: 7
     System.out.println("size: " + table.getSize());         // Expected: 3
    
     System.out.println();
    ColorTable test = new ColorTable(3, 6, Constants.QUADRATIC, .49);
    int[] testData = new int[] { 32960, 32960, 32960, 32960 };
    for (int i = 0; i < testData.length; i++) {
    	test.increment(new Color(testData[i]));
    }
    
    
    
    /* The following automatically calls table.toString().
       Notice that we only include non-zero counts in the String representation.
       
       Expected: [3:2096,2, 5:67632,1, 6:6257,1]
       
       This shows that there are 3 keys in the table. They are at positions 3, 5, and 6.
       Their color codes are 2096, 67632, and 6257. The code 2096 was incremented twice.
       You do not have to mimic this format exactly, but strive for something compact
       and readable.
       */
     System.out.println(table);
     System.out.println(test);
  }
}
