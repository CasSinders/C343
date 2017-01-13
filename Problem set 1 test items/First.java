public class First {
  static Integer[] has = new Integer[10];
  static int tableSize = 10;
  static int tableEntries = 0;
  
  public static void LinearHash(int x) {
    tableEntries++;
    if (tableEntries == tableSize) {
      ResizeTable();
    }
    int i = 0;
    while (has[(((7 -(x % 7)) + i) % tableSize)] != null) {
      i++;
    }
    has[(((7 -(x % 7)) + i) % tableSize)] = x; 
  }
  public static void ResizeTable() {
    int j = 4;
    while (!isPrime((4 * j) + 3)) {
      j++;
    }
    Integer[] temp = new Integer[(4 * j)+3];
    for (int i = 0; i < has.length; i++) {
      temp[i] = has[i];
    }
    tableSize = temp.length;
    has = temp;
  }
  private static Boolean isPrime(int x) {
    for (int i = 2; i < x; i++) {
      if ((x % i) == 0) {
        return false;
      }
    }
    return true;
  }
  public static void QuadHash(int x) {
    tableEntries++;
    if (tableEntries == tableSize) {
      ResizeTable();
    }
    int i = 0;
    while ( has[(int) (((7 -(x  % 7)) + Math.pow(i, 2)) % tableSize)] != null) {
      i++;
    }
    has[(int) (((7 -(x  % 7)) + Math.pow(i, 2)) % tableSize)] = x; 
  }
  public static void PrintArray() {
    for (int i = 0; i < has.length; i++) {
      System.out.print(has[i] + ", ");
    }
    System.out.println();
  }
  public static void main(String[] args) {
    Integer[] test = new Integer[7];
    test[0] = 4371;
    test[1] = 1323;
    test[2] = 6173;
    test[3] = 4199;
    test[4] = 4344;
    test[5] = 9679;
    test[6] = 1989;
    for (int i = 0; i < test.length; i++) {
      LinearHash(test[i]);
    }
    PrintArray();
    has = new Integer[10];
    tableSize = 10;
    tableEntries = 0;
    for (int i = 0; i < test.length; i++) {
      QuadHash(test[i]);
    }
    PrintArray();
    has = new Integer[10];
    tableSize = 10;
    tableEntries = 0;
    Integer[] test2 = new Integer[14];
    int j = 0;
    for (int i = 0; i < test2.length; i++) {
      if (j == test.length) {
        j = 0;
      }      
      test2[i] = test[j];
      j++;
    }
    for (int i = 0; i < test2.length; i++) {
      LinearHash(test2[i]);
    }
    PrintArray();
    has = new Integer[10];
    tableSize = 10;
    tableEntries = 0;
    for (int i = 0; i < test2.length; i++) {
      QuadHash(test2[i]);
    }
    PrintArray();
  }
}