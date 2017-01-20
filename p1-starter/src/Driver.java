/**
 * TODO
 * @author (Christopher Sinders)
 */

public class Driver {
  
  private static int numCollisions;
  
  /**
   *  
   * Return the ColorTable associated with this image, assuming the color key space
   * is restricted to bitsPerChannel. Increment numCollisions after each increment.
   */
  public static ColorTable vectorize(Image image, int bitsPerChannel) {
	  image = image.quantize(bitsPerChannel);
	  ColorTable table = new ColorTable(5,bitsPerChannel, Constants.LINEAR, .8);
	  int height = image.getHeight();
	  int width = image.getWidth();
	  for (int x = 0; x < width; x++) {
		  for (int y = 0; y < height; y++) {
			  table.increment(image.getColor(x, y));
			  numCollisions++;
		  }
	  }
    return table;
  }

  /**
   * 
   * Return the result of running Util.cosineSimilarity() on the vectorized images.
   * 
   * Note: If you compute the similarity of an image with itself, it should be close to 1.0.
   */
  public static double similarity(Image image1, Image image2, int bitsPerChannel) {
	  double ans = 0;
	  ColorTable table1 = vectorize(image1, bitsPerChannel);
	  ColorTable table2 = vectorize(image2, bitsPerChannel);
	  ans = Util.cosineSimilarity(table1, table2);
	  return ans;
  }

  /**
   * Uses the Painting images and all 8 bitsPerChannel values to compute and print 
   * out a table of collision counts.
   */
  public static void allPairsTest() {
    Painting[] paintings = Painting.values();
    int n = paintings.length;
    for (int y = 0; y < n; y++) {
      for (int x = y + 1; x < n; x++) {
        System.out.println(paintings[y].get().getName() + 
            " and " + 
            paintings[x].get().getName() + ":");
        for (int bitsPerChannel = 1; bitsPerChannel <= 8; bitsPerChannel++) {
          numCollisions = 0;
          System.out.println(String.format("   %d: %.2f %d", 
              bitsPerChannel,
              similarity(paintings[x].get(), paintings[y].get(), bitsPerChannel),
              numCollisions));
        }
        System.out.println();
      }
    }
  }

  /**
   * Simple testing
   */  
  public static void main(String[] args) {
    System.out.println(Constants.TITLE);
    Image mona = Painting.MONA_LISA.get();
    Image starry = Painting.STARRY_NIGHT.get();
    Image christina = Painting.CHRISTINAS_WORLD.get();
    Image degas = Painting.BLUE_DANCERS.get();
    System.out.println("It looks like all three test images were successfully loaded.");
    System.out.println("mona's dimensions are " + 
        mona.getWidth() + " x " + mona.getHeight());
    System.out.println("starry's dimenstions are " + 
        starry.getWidth() + " x " + starry.getHeight());
    System.out.println("christina's dimensions are " + 
        christina.getWidth() + " x " + christina.getHeight());
    // allPairsTest();
    System.out.println(similarity(mona,mona, 8));
    int experiment = 4;
    Image[] testArray = new Image[4];
    testArray[0] = mona;
    testArray[1] = starry;
    testArray[2] = christina;
    testArray[3] = degas;
    for (int i = 0; i < testArray.length; i++) {
    	double similarity = similarity(testArray[2],testArray[i],4);
    	System.out.println("Testing the Starry Night vs " + "testArray[" + i + "]");
    	System.out.println("The number of collisions is " + numCollisions);
    	System.out.println("The Similarity is " + similarity);
    	System.out.println();
    }
    
    
    
    
  }
}
