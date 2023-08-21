package application;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BPTreeTest {
  
  private BPTree<Integer,Integer> testBPTree1;
  private BPTree<Integer,Integer> testBPTree2;
  
  @Before
  public void setUp() throws Exception {
    BPTree<Integer, Integer> testBPTree1 = new BPTree<Integer,Integer>(3);
    BPTree<Integer, Integer> testBPTree2 = new BPTree<Integer,Integer>(3);
  }

  @After
  public void tearDown() throws Exception {
    testBPTree1 = null;
    testBPTree2 = null;
  }

  @Test
  public void test() {
    fail("Not yet implemented");
  }

}
