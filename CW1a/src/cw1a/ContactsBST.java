package cw1a;// or whatever

/**
 *
 * @author Robert Szczyglowski
 * template for use of students by D Lightfoot 2025-03-18
 */
 
public  class ContactsBST implements  IContactDB {
    
   private class Node {
      Contact data;
      Node left, right;
      private Node(Contact m) { data = m; left = null; right = null; } 
   }
   private Node root; // this is the tree
   private int numEntries;
   private int totalVisited; // for logging total nodes visited
            
   public ContactsBST() {
        System.out.println("Binary Search Tree");
        clearDB();
        resetTotalVisited();
   }
           
    /**
     * Empties the database.
     * @pre true
     */
    @Override
    public void clearDB() {
       root = null; // garbage collector will tidy up
       numEntries = 0;
    }
    
    /**
     * Determines whether a contact's name exists as a key inside the database
     * @pre name is not null and not empty string or all blanks
     * @param name the contact name (key) to locate
     * @return true iff the name exists as a key in the database
     */
    @Override
    public boolean containsName(String name){
        assert name != null && !name.trim().equals("");
        return get(name) != null;
    }
        
    private Contact retrieve (Node tree, String name) {
		// base case is an empty tree
                if (tree == null) {
                    return null;
                }
                
                System.out.println("Visiting node: " + tree.data.getName()); // logging node visit
                
                if (name.equals(tree.data.getName())) { // check if name matches current node
                    return tree.data;
                }
                
                if (name.compareTo(tree.data.getName()) < 0) { // search left or right subtree recursively depending on comparison
                    return retrieve(tree.left, name);
                }
                
                else {
                    return retrieve(tree.right, name);
                }
            
	}
    
    private Contact retrieveWithCount(Node tree, String name, int[] count){ // count nodes visited during retrieval
        // base case is an empty tree again
        if (tree == null) {
            return null;
        }
        
        count[0]++; // increment counter
        System.out.println("Visiting node: " + tree.data.getName()); // logging node visit again
        
        // following is a bit of a repeat, so no comments necessary as far as I'm aware.
        
        if (name.equals(tree.data.getName())) {
            return tree.data;
        }
        
        if (name.compareTo(tree.data.getName()) < 0) {
            return retrieveWithCount(tree.left, name, count);
        }
        else {
            return retrieveWithCount(tree.right, name, count);
        }
        
    }
    
	/**
     * Returns a Contact object mapped to the supplied name.
     * @pre name not null and not empty string or all blanks
     * @param name The Contact name (key) to locate
     * @return the Contact object mapped to the key name if the name
        exists as key in the database, otherwise null
     */
    @Override
    public Contact get(String name){ 
       // assert statements here
       
       assert name != null : "Name cannot be null";
       assert !name.trim().equals("") : "Name cannot be empty or all blank spaces";
       
       System.out.println("Searching for contact name: " + name);
       
       int[] nodeCount = {0}; // tracking nodes
       Contact result = retrieveWithCount(root, name, nodeCount);
       
       totalVisited += nodeCount[0]; // add current node count to total node count
       System.out.println("Contact name: " + name + ", nodes visited: " + nodeCount[0]); // logging
       
       return result;
    }
    
    /**
     * Returns the number of members in the database
     * @pre true
     * @return number of members in the database. 
     */
    @Override
    public int size() {return numEntries;}
	
    /**
     * Determines if the database is empty or not.
     * @pre true
     * @return true iff the database is empty
     */
    @Override
    public boolean isEmpty() { return size() == 0; }
	
     private Node insert(Node tree, Contact contact, int[] count) {
	   // to do
        String name = contact.getName();
        
        if (tree == null) { // if empty tree, create root node
            numEntries++;
            return new Node(contact);
        }
        
        count[0]++; // increment node visit count
        System.out.println("Visiting node: " + tree.data.getName()); // logging
        
        if (name.equals(tree.data.getName())) {
            tree.data = contact;
            return tree;
        }
        if (name.compareTo(tree.data.getName()) < 0) {
            tree.left = insert(tree.left, contact, count);
        }
        else{
            tree.right = insert(tree.right, contact, count);
        }
        return tree; // not really
     }
  
    /**
     * Inserts an Contact object into the database, with the key of the supplied
     * contact's name.
     * Note: If the name already exists as a key, then then the original entry
     * is overwritten. 
     * This method must return the previous associated value 
     * if one exists, otherwise null
     * 
     * @pre contact not null and contact name not empty string or all blanks
     */
    @Override
    public Contact put(Contact contact){
        
        assert contact != null : "Contact cannot be null";
        assert contact.getName() != null : "Contact name cannot be null";
        assert !contact.getName().trim().equals("") : "Contract name cannot be empty or all blank spaces";
        
        
        Contact returned = get(contact.getName());
        
        System.out.println("Adding contact name: " + contact.getName());
        int[] nodeCount = {0};
        root = insert(root, contact, nodeCount);
        
        totalVisited += nodeCount[0];
        System.out.println("Contact name: " + contact.getName() + ", nodes visited: " + nodeCount[0]);
        
        
        return returned;
    }
    
   /**
     * Removes and returns a contact from the database, with the key
     * the supplied name.
     * @param name The name (key) to remove.
     * @pre name not null and name not empty string or all blanks
     * @return the removed contact object mapped to the name, or null if
     * the name does not exist.
     */
    @Override
    public  Contact remove(String name){        
    /* based on Object-Oriented Programming in Oberon-2
       Hanspeter Mössenböck Springer-Verlag 1993, page 78
       transcribed into Java by David Lightfoot
    */
        assert name != null : "Name cannot be null";
        assert !name.trim().equals("") : "Name cannot be empty or all blank spaces";

        System.out.println("Removing contact name: " + name);

        int nodeCount = 0;


        Node parent = null, del, p = null, q = null;
        Contact result;
	del = root;
        
	while (del != null && !del.data.getName().equals(name)) {
            nodeCount++;
            System.out.println("Visiting node: " + del.data.getName());
            
            
            
            parent = del;
            if (name.compareTo(del.data.getName()) < 0)
                del = del.left;
            else 
                del = del.right;
	}// del == null || del.data.getName().equals(name))
    if(del != null) { // node to delete found
        nodeCount++;
        System.out.println("Visiting node: " + del.data.getName());
        
        // find the pointer p to the node to replace del 
        if (del.right == null) p = del.left;
        else if (del.right.left == null) {
            p = del.right; p.left = del.left;
        } else {
            p = del.right;
            
            while (p.left != null) {
                q = p; p = p.left;
                nodeCount++;
                System.out.println("Visiting node: " + p.data.getName());
            }
            q.left = p.right; p.left = del.left; p.right = del.right;
            }
            if(del == root) root = p;
            else if (del.data.getName().compareTo(parent.data.getName()) < 0) 
                parent.left = p;
            else parent.right = p;
           numEntries--;
            result = del.data;
        }
        else result = null;
        
        totalVisited += nodeCount;
        System.out.println("Contact name: " + name + ", nodes visited: " + nodeCount);
        
        return result;
    } // delete
    
   private void traverse(Node tree) {
      // to do 
      if (tree != null) {
          traverse(tree.left);
          System.out.println(tree.data.toString());
          traverse(tree.right);
      }
   }
   
    /**
     * Prints the names and affiliations of all the members in the database in 
     * alphabetic order.
     * @pre true
     */
    @Override
    public void displayDB(){
        System.out.println("Displaying contacts in alphabetical order: ");
        if (isEmpty()){
            System.out.println("Database is empty");
        }
        else{
            traverse(root);
        }
    }
    @Override
    public int getNumEntries() {
        return numEntries;
    }
    @Override
    public int getTotalVisited() {
        return totalVisited;
    
    }
    @Override
    public void resetTotalVisited() {
        totalVisited = 0;
    }
}

