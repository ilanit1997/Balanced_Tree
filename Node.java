
public class Node {
    /**
     * This class is used to initiate attributes of nodes
     * sum - sum of values in tree rooted at node
     * size - number of children a node has
     * max_k - max key among leaves of tree rooted at current node
     * min_k - min key among leaves of tree rooted at current node
     * min_k, max_k is considered interval of keys of leaves
     */
    Node left;
    Node middle;
    Node right;
    Node p;
    Key key;
    Value value;
    int size = 1;
    Value sum;
    Key max_k;
    Key min_k;



    public Node(){};

    public Node(Key key, Value value){
        this.key = key.createCopy();
        this.value = value.createCopy();
        this.sum = value.createCopy();
        this.max_k = key.createCopy();
        this.min_k = key.createCopy();

    }
}
