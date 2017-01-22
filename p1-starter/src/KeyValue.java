public class KeyValue {
	private int key;
	private long count;

	/*
	 * Constructor for Class
	 */
	public KeyValue(int key, long count) {
		this.key = key;
		this.count = count;
	}

	/*
	 * Return the key
	 */
	public int getKey() {
		return key;
	}
	/*
	 * return the count
	 */

	public long getCount() {
		return count;
	}
	/*
	 * Set the key
	 */

	public void setKey(int key) {
		this.key = key;
	}
	/*
	 * set the count to a particular amount
	 */

	public void setCount(long count) {
		this.count = count;
	}
	/*
	 * increment the count value
	 */

	public void incrementCount() {
		count++;
	}
}
