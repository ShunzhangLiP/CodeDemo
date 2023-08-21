package application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/**
 * Implementation of a B+ tree to allow efficient access to many different indexes of a large data
 * set. BPTree objects are created for each type of index needed by the program. BPTrees provide an
 * efficient range search as compared to other types of data structures due to the ability to
 * perform log_m N lookups and linear in-order traversals of the data items.
 * 
 * @author sapan (sapan@cs.wisc.edu) Shunzhang Li(sli568@wisc.edu)
 *
 * @param <K> key - expect a string that is the type of id for each item
 * @param <V> value - expect a user-defined type that stores all data for a food item
 */
public class BPTree<K extends Comparable<K>, V> implements BPTreeADT<K, V> {

  // Root of the tree
  private Node root;

  // Branching factor is the number of children nodes
  // for internal nodes of the tree
  private int branchingFactor;


  /**
   * Public constructor
   * 
   * @param branchingFactor
   */
  public BPTree(int branchingFactor) {
    if (branchingFactor <= 2) {
      throw new IllegalArgumentException("Illegal branching factor: " + branchingFactor);
    }
    this.branchingFactor = branchingFactor;
    root = new LeafNode();
  }



  @Override
  /**
   * insert the key value pair to the root
   */
  public void insert(K key, V value) {
    root.insert(key, value);
  }


  /*
   * (non-Javadoc)
   *  do the rangesearch at root
   * @see BPTreeADT#rangeSearch(java.lang.Object, java.lang.String)
   */
  @Override
  public List<V> rangeSearch(K key, String comparator) {
    List rangeList = new ArrayList<V>();
    if (!comparator.contentEquals(">=") && !comparator.contentEquals("==")
        && !comparator.contentEquals("<="))
      return rangeList;
    return root.rangeSearch(key, comparator);
  }


  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    Queue<List<Node>> queue = new LinkedList<List<Node>>();
    queue.add(Arrays.asList(root));
    StringBuilder sb = new StringBuilder();
    while (!queue.isEmpty()) {
      Queue<List<Node>> nextQueue = new LinkedList<List<Node>>();
      while (!queue.isEmpty()) {
        List<Node> nodes = queue.remove();
        sb.append('{');
        Iterator<Node> it = nodes.iterator();
        while (it.hasNext()) {
          Node node = it.next();
          sb.append(node.toString());
          if (it.hasNext())
            sb.append(", ");
          if (node instanceof BPTree.InternalNode)
            nextQueue.add(((InternalNode) node).children);
        }
        sb.append('}');
        if (!queue.isEmpty())
          sb.append(", ");
        else {
          sb.append('\n');
        }
      }
      queue = nextQueue;
    }
    return sb.toString();
  }


  /**
   * This abstract class represents any type of node in the tree This class is a super class of the
   * LeafNode and InternalNode types.
   * 
   * @author sapan
   */
  private abstract class Node {

    // List of keys
    List<K> keys;
    boolean hasPromotion;
    Node promotedNode;

    /**
     * Package constructor
     */
    Node() {
      keys = new ArrayList<K>();
    }

    /**
     * Inserts key and value in the appropriate leaf node and balances the tree if required by
     * splitting
     * 
     * @param key
     * @param value
     */
    abstract void insert(K key, V value);

    /**
     * Gets the first leaf key of the tree
     * 
     * @return key
     */
    abstract K getFirstLeafKey();

    /**
     * Gets the new sibling created after splitting the node
     * 
     * @return Node
     */
    abstract Node split();

    /*
     * (non-Javadoc)
     * 
     * @see BPTree#rangeSearch(java.lang.Object, java.lang.String)
     */
    abstract List<V> rangeSearch(K key, String comparator);

    /**
     * return true if number of keys in this node is more >= branching factor
     * 
     * @return boolean
     */
    abstract boolean isOverflow();

    // abstract boolean hasPromotion();
    /**
     * custom method that returns the promoted node if split
     * @return
     */
    abstract Node getPromotion();

    public String toString() {
      return keys.toString();
    }

  } // End of abstract class Node

  /**
   * This class represents an internal node of the tree. This class is a concrete sub class of the
   * abstract Node class and provides implementation of the operations required for internal
   * (non-leaf) nodes.
   * 
   * @author sapan
   */
  private class InternalNode extends Node {

    // List of children nodes
    List<Node> children;
    InternalNode promotedNode;  // the promoted node if this node has to split
    
    /**
     * Package constructor
     */
    InternalNode() {
      super();
      children = new ArrayList<Node>();
      promotedNode = null;
    }

    /**
     * @see BPTree.Node#getFirstLeafKey() check if the first children in the children list is a
     *      leafNode return it if yes else recursively call getFirstLeafKey of the first children
     *      internal node
     */
    K getFirstLeafKey() {
      if (!(children.get(0) instanceof BPTree.LeafNode)) {  //if it is an internal node
        return children.get(0).getFirstLeafKey();       //get the first key
      }
      return children.get(0).keys.get(0);       //get the first leafnode's first key
    }

    /**
     * (non-Javadoc)
     * 
     * @see BPTree.Node#isOverflow()
     */
    boolean isOverflow() {
      // TODO : Complete
      return keys.size() >= branchingFactor;
    }

    /**
     * (non-Javadoc) have not implement a way to store values with same value in the tree, will
     * think about this LeafNode should handle the initiation of split?
     * 
     * @see BPTree.Node#insert(java.lang.Comparable, java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    void insert(K key, V value) {
      promotedNode = null;
      int numKeys = keys.size();
      for (int i = 0; i < numKeys; i++) {
        if (key.compareTo(keys.get(i)) < 0) { //traverse through the key list, find the first node
                                                //that is larger than the inserted key
          children.get(i).insert(key, value);
          if (children.get(i).getPromotion() != null) {
            InternalNode temp = new InternalNode();
            temp = (InternalNode) children.get(i).getPromotion();
            keys.addAll(i, children.get(i).getPromotion().keys);
            children.remove(i);
            children.addAll(i, temp.children);
          }
          break;
        }
        if (i == numKeys - 1) {             //if the inserted key is the largest
          children.get(numKeys).insert(key, value);
          if (children.get(numKeys).getPromotion() != null) {
            keys.addAll(children.get(numKeys).getPromotion().keys);
            children.addAll(((InternalNode) (children.get(numKeys).getPromotion())).children);
            children.remove(numKeys);
          }
          break;
        }
      }
      if (isOverflow()) {                   //if this node is overflow
        split();
      }
    }

    /**
     * (non-Javadoc) 
     * 
     * @see BPTree.Node#split()
     */
    Node split() {
      InternalNode sibling = new InternalNode();
      int promoteNum = branchingFactor / 2; //find the first node that is 
      promotedNode = new InternalNode();  
      for (int i = promoteNum + 1; i < keys.size(); i++) {  //add second half to the sibling
        sibling.keys.add(keys.get(i));
      }
      for (int i = children.size() / 2; i < children.size(); i++) {
        sibling.children.add(children.get(i));
      }
      promotedNode.keys.add(keys.get(promoteNum));
      keys.remove(promoteNum);                  //remove the middle key
      int numKeys = keys.size();
      for (int i = numKeys; i > promoteNum; i--) {  //remove the keys and children that 
                                                        //is to siblings
        keys.remove(i - 1);
      }
      int numChildren = children.size() - 1;
      for (int i = numChildren; i > numChildren / 2; i--) {
        children.remove(i);
      }
      promotedNode.children.add(this);
      promotedNode.children.add(sibling);
    //  promotedNode.keys.add(sibling.getFirstLeafKey());
      if (root.equals(this)) {          //change root if root split
        root = this.promotedNode;
      }
      return sibling;
    }

    /**
     * have not implement == case (non-Javadoc)
     * 
     * @see BPTree.Node#rangeSearch(java.lang.Comparable, java.lang.String)
     */
    List<V> rangeSearch(K key, String comparator) {
      List<V> rangeList = new ArrayList<V>();
      if (key == null || comparator == null) {  //return empty list
        return rangeList;
      }
      if (comparator.equals("<=")|| comparator.equals("==")) {  //find the maximum number that is
                                                                   //smaller than target
        if (keys.get(0).compareTo(key) > 0) {
          rangeList.addAll(children.get(0).rangeSearch(key, comparator));
        } else {
          for (int i = keys.size() - 1; i > -1; i--) {
            if (key.compareTo(keys.get(i)) >= 0) {
              rangeList.addAll(children.get(i + 1).rangeSearch(key, comparator));
              break;
            }
          }
        }
      } else if (comparator.equals(">=")) {             //find the minimum number that is
                                                          //larger than target
        if (keys.get(keys.size() - 1).compareTo(key) < 0) {
          rangeList.addAll(children.get(keys.size() - 1).rangeSearch(key, comparator));
        } else {
          for (int i = 0; i < keys.size(); i++) {
            if (key.compareTo(keys.get(i)) <= 0) {
              rangeList.addAll(children.get(i).rangeSearch(key, comparator));
              break;
            }
          }
        }
      }      
      return rangeList;
    }

    @Override
    /**
     *Non-javadoc
     *see BPTree.Node#getPromotion() 
     */
    Node getPromotion() {
      return promotedNode;
    }

  } // End of class InternalNode


  /**
   * This class represents a leaf node of the tree. This class is a concrete sub class of the
   * abstract Node class and provides implementation of the operations that required for leaf nodes.
   * 
   * @author sapan
   */
  private class LeafNode extends Node {

    // List of values
    List<V> values;

    // Reference to the next leaf node
    LeafNode next;

    // Reference to the previous leaf node
    LeafNode previous;

    //promoted node if split
    InternalNode promotedNode;
    /**
     * Package constructor
     */
    LeafNode() {
      super();
      values = new ArrayList<V>();
      next = null;
      previous = null;
    }


    /**
     * (non-Javadoc)
     * 
     * @see BPTree.Node#getFirstLeafKey()
     */
    K getFirstLeafKey() {
      return keys.get(0);
    }

    /**
     * (non-Javadoc)
     * 
     * @see BPTree.Node#isOverflow()
     */
    boolean isOverflow() {
      return keys.size() >= branchingFactor;
    }

    /**
     * (non-Javadoc)
     * 
     * @see BPTree.Node#insert(Comparable, Object)
     */
    @SuppressWarnings("unchecked")
    void insert(K key, V value) {
      promotedNode = null;
      if (keys.isEmpty()) {
        keys.add(key);
        values.add(value);
        return;
      }
      for (int i = 0; i < keys.size(); i++) {   //find the first key that is larger than target
        if (key.compareTo(keys.get(i)) <= 0) {
          keys.add(i, key);
          values.add(i, value);
          break;
        }
        if (i == keys.size() - 1) {         //insert at the end if larger than the entire key list
          keys.add(key);
          values.add(value);
          break;
        }
      }
      if (isOverflow()) {
        next = (LeafNode) split();
      }
    }

    /**
     * (non-Javadoc) unsure how to store the internal node value
     * 
     * @see BPTree.Node#split()
     */
    Node split() {
      if (isOverflow()) {
        LeafNode sibling = new LeafNode();
        InternalNode promotion = new InternalNode();
        int promoteNum = branchingFactor / 2;
        promotion.keys.add(keys.get(promoteNum));
        for (int i = promoteNum; i < keys.size(); i++) {    //add second half to the sibling
          sibling.keys.add(keys.get(i));
          sibling.values.add(values.get(i));
        }
        for (int i = keys.size() - 1; i > promoteNum - 1; i--) {    //remove the second half
          keys.remove(i);
          values.remove(i);
        }
//        this.next = sibling;
//        this.next.previous = sibling;
//        LeafNode tempNode = this;
        sibling.previous = this;
        sibling.next = this.next;
        if(this.next!=null) {
        this.next.previous=sibling;
        }
        this.next = sibling;
        promotedNode = promotion;
        promotedNode.children.add(this);
        promotedNode.children.add(sibling);
        if (root.equals(this)) {    //update root if needed
          root = promotedNode;
        }
        return sibling;
      }
      return null;
    }

    Node getPromotion() {
      return promotedNode;
    }

    /**
     * (non-Javadoc)
     * 
     * @see BPTree.Node#rangeSearch(Comparable, String)
     */
    List<V> rangeSearch(K key, String comparator) {
      List<V> rangeList = new ArrayList<V>();
      int startIndex = 0;
      if (key == null || comparator == null) {
        return rangeList;
      }
      if (comparator.equals("<=")) {    //find the maximum value smaller than the target
        for (int i = keys.size() - 1; i > -1; i--) {
          if (key.compareTo(keys.get(i)) >= 0) {
            startIndex = i;
            break;
          }
        }
        for (int i = startIndex; i > -1; i--) {
          if (keys.get(i).compareTo(key) <= 0)
            rangeList.add(values.get(i));
        }
        if (previous != null) {         //go through the node before this
          rangeList.addAll(previous.rangeSearch(key, comparator));
        }
      } else if (comparator.equals(">=")) {        //find the minimum value larger than the target
        for (int i = 0; i < keys.size(); i++) {
          if (key.compareTo(keys.get(i)) <= 0) {
            startIndex = i;
            break;
          } else {
            startIndex = i;
          }
        }
        for (int i = startIndex; i < keys.size(); i++) {    
          if (keys.get(i).compareTo(key) >= 0)
            rangeList.add(values.get(i));
        }
        if (next != null) {     //check for the node after this
          rangeList.addAll(next.rangeSearch(key, comparator));
        }
      }
       else if (comparator.equals("==")) {          
          for (int i = 0; i < keys.size(); i++) {       //check for every key that is equal
            if (key.compareTo(keys.get(i)) == 0) {
              rangeList.add(values.get(i));
            }
          }
          if (previous != null) {    //since it starts the same condition as <=, so check previous 
            rangeList.addAll(previous.rangeSearch(key, comparator));
          }
        }
//      }

      return rangeList;
    }



  } // End of class LeafNode


  /**
   * Contains a basic test scenario for a BPTree instance. It shows a simple example of the use of
   * this class and its related types.
   * 
   * @param args
   */
  public static void main(String[] args) {
    // create empty BPTree with branching factor of 3
    BPTree<Double, Double> bpTree = new BPTree<>(3);

    // create a pseudo random number generator
    Random rnd1 = new Random();
    Double[] dd = {0.0d, 0.5d, 0.2d, 0.8d,0.1d,0.3d,0.6d,0.7d,0.9d};
    // some value to add to the BPTree
    // build an ArrayList of those value and add to BPTree also
    // allows for comparing the contents of the ArrayList
    // against the contents and functionality of the BPTree
    // does not ensure BPTree is implemented correctly
    // just that it functions as a data structure with
    // insert, rangeSearch, and toString() working.
    List<Double> list = new ArrayList<>();
    for (int i = 0; i < 10; i++) { 
      Double j = dd[rnd1.nextInt(9)];
      list.add(j);
      bpTree.insert(j, j);
      System.out.println("\n\nTree structure:\n" + bpTree.toString());
    }
    List<Double> filteredValues = bpTree.rangeSearch(0.0d, "==");
    System.out.println("Filtered values: " + filteredValues.toString());
  }

} // End of class BPTree
