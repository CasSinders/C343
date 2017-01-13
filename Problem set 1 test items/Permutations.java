public class Permutations {
  /**    * Print all permutations of the given string.   
    * 
    * @author (Christopher Sinders)  
    */ 
  public static void permute(String str) {
    permute(str.toCharArray(), 0, str.length()-1); 
  }
  public static void permute(char[] str, int low, int high) {
    if (low == high) {
      System.out.println(String.valueOf(str)); 
    } else {
      for (int i = low; i <= high; i++) {
        swap(str, low, i);
        permute(str, low+1, high);
        swap(str, low, i);
      }
    }
  }
  public static void swap(char[] c, int x, int y) {
    char temp = c[x];
    c[x] = c[y];
    c[y] = temp;
  }
  public static void main(String[] args) { 
    permute("abcd");    
  }
}