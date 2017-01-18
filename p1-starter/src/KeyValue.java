import java.awt.Color;
public class KeyValue {
	private Color key;
	private long count;
	/* Constructor for Class
	 */
	public KeyValue(Color key, long count) {
		this.key = key;
		this.count = count;
	}
	/* Get/Set for key and count
	 */
	public Color getKey() {
		return key;
	}
	
	public long getCount() {
		return count;
	}
	
	public void setKey(Color key) {
		this.key = key;
	}
	
	public void setCount(long count) {
		this.count = count;
	}
	public void incrementCount() {
		count++;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}
}
