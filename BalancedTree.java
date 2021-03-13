public class BalancedTree {
    Node root;

    public BalancedTree() {
        this.root = new Node();
        this.root.size = 0;
    }

    private void init(Key key, Value value){
        this.root = new Node(key, value);
    }

    /**
     * Function is used to update attributes of parent's node according to it's children.
     * update key, size, min_k, max_k, sum - see elaboration at Node class
     * @param x: current node to update
     */
    private void updateKey(Node x){

        x.key = x.left.key.createCopy();
        int s = x.left.size;
        x.min_k = x.left.min_k.createCopy();
        x.sum = x.left.sum.createCopy();
        if (x.middle != null){
            x.key = x.middle.key.createCopy();
            s+= x.middle.size;
            x.sum.addValue(x.middle.sum.createCopy());
            x.max_k = x.middle.max_k.createCopy();
        }
        if (x.right != null){
            x.key = x.right.key.createCopy();
            s += x.right.size;
            x.sum.addValue(x.right.sum.createCopy());
            x.max_k = x.right.max_k.createCopy();
        }
        x.size =  s;

    }

    /**
     * Function is used to update children of node and attributes of the node itself
     * @param x parent
     * @param l left child
     * @param m middle child
     * @param r right child
     */
    private void setChildren(Node x, Node l, Node m, Node r){


        x.left = l;
        x.middle = m;
        x.right = r;
        l.p = x;
        if (m != null){
            m.p = x;
        }
        if (r != null){
            r.p =x;
        }
        updateKey(x);
    }

    /**
     * Function is used to insert node into correct position and split parents if the new tree doesn't comply
     * with 2-3 definition
     * @param x parent of node
     * @param z node to insert
     * @return y- new parent if split is done, null - no split was necessary
     */
    private Node Insert_And_Split(Node x, Node z){
        Node l = x.left;
        Node m = x.middle;
        Node r = x.right;
        if (r == null){
            if (z.key.compareTo(l.key)<0){
                setChildren(x,z, l, m);
            }
            else if (z.key.compareTo(m.key)<0){
                setChildren(x, l , z, m);
            }
            else {
                setChildren(x, l, m, z);
            }
            return null;
        }
        Node y = new Node();
        if (z.key.compareTo(l.key)<0){
            setChildren(x, z, l, null);
            setChildren(y, m, r, null);
        }
        else if(z.key.compareTo(m.key)<0){
            setChildren(x, l, z, null);
            setChildren(y, m, r, null);
        }
        else if(z.key.compareTo(r.key)<0){
            setChildren(x, l, m, null);
            setChildren(y, z, r, null);
        }
        else{
            setChildren(x, l, m, null);
            setChildren(y, r, z, null);
        }
        return y;
    }

    /**
     * This function is used to check if node is a leaf
     * @param n node to be checked
     * @return true - if node is a leaf, false - otherwise
     */
    private Boolean isLeaf(Node n){

        if (n.left == null){

            return true;
        }
        return false;
    }

    /**
     * This function is used to insert node with given key and value.
     * @param key
     * @param value
     */
    public void insert(Key key, Value value){
        if (this.root == null || this.root.key == null) {
            init(key.createCopy(), value.createCopy());
            return;
        }

        Node z = new Node(key, value);
        Node y = this.root;
        if (this.root.left == null) {
            Node w = new Node();
            if (this.root.key.compareTo(z.key) > 0) {
                setChildren(w, z, this.root, null);
            } else {
                setChildren(w, this.root, z, null);
            }
            this.root = w;

            return;
        }

        while (!isLeaf(y)){
            if(z.key.compareTo(y.left.key)<0){
                y=y.left;
            }
            else if(z.key.compareTo(y.middle.key)<0){
                y=y.middle;
            }
            else if(y.right!=null){
                y=y.right;
            }
            else {
                y=y.middle;
            }
        }
        Node x = y.p;
        z= Insert_And_Split(x,z);
        while (x != this.root){
            x= x.p;
            if(z!=null){
                z= Insert_And_Split(x,z);
            }
            else {
                updateKey(x);
            }

        }
        if (z!=null){
            Node w = new Node();
            setChildren(w, x, z,null);
            this.root = w;
        }

    }

    /**
     * This function is used as aux to delete node. Try to borrow child from another node or merge.
     * @param y node to modify
     * @return z- new parent if one created after deletion
     */
    private Node Borrow_or_Merge(Node y){
        Node z = y.p;
        if(y == z.left){
            Node x = z.middle;
            if(x.right!= null){
                setChildren(y, y.left, x.left, null);
                setChildren(x, x.middle, x.right, null);
            }
            else{
                setChildren(x, y.left, x.left, x.middle);
                y = null;
                setChildren(z, x, z.right, null);
            }
            return z;
        }
        if(y == z.middle){
            Node x = z.left;
            if(x.right!= null){
                setChildren(y, x.right, y.left, null);
                setChildren(x, x.left, x.middle, null);
            }
            else{
                setChildren(x, x.left, x.middle, y.left);
                y = null;
                setChildren(z, x, z.right, null);
            }
            return z;
        }
        else{
            Node x = z.middle;
            if (x.right != null){
                setChildren(y, x.right, y.left, null);
                setChildren(x, x.left, x.middle, null);
            }
            else{
                setChildren(x, x.left, x.middle, y.left);
                y = null;
                setChildren(z, z.left, x, null);
            }
            return z;
        }
    }

    /** This function is used as aux to delete node. Delete node while maintaining 2-3 tree.
     *
     * @param x - node to delete
     */
    private void delete_node(Node x){
        Node y = x.p;
        if (x == this.root){
            this.root = null;
            return;
        }
        if(x == y.left){
            setChildren(y, y.middle, y.right, null);
        }
        else if(x == y.middle){
            setChildren(y, y.left, y.right, null);
        }
        else{
            setChildren(y, y.left, y.middle, null);
        }
        x = null;
        while (y!=null){
            if(y.middle == null){
                if(y!= this.root){
                    y= Borrow_or_Merge(y);
                }
                else{
                    this.root = y.left;
                    y.left.p = null;
                    y = null;
                    return;
                }
            }
            else{
                updateKey(y);
                y=y.p;
            }
        }
    }

    /**
     * This function calls aux function, trying to delete node with given key. If no matching key found do nothing.
     * @param key of node to delete
     */
    public void delete (Key key){
        Node x = search(this.root, key);
        if(x!= null){
            delete_node(x);
        }
        return;
    }


    /**
     * This function is used as aux to search a given key in a node in tree
     * @param x root
     * @param key to search
     * @return x - node found, null - if no node is found
     */
    private Node search(Node x, Key key){
        if(this.root == null){
            return null;
        }
        if(isLeaf(x)){
            if (x.key.compareTo(key) == 0){
                return x;
            }
            else {
                return null;
            }
        }
        if (key.compareTo(x.left.key) <= 0){
            return search(x.left, key);
        }
        else if (key.compareTo(x.middle.key) <= 0){
            return  search(x.middle, key);
        }
        else if(x.right != null){
            return search(x.right, key);
        }
        else{
            return null;
        }
    }

    /**
     * This function is used to search key in tree.
     * @param key to search
     * @return if found - value of key, otherwise null
     */
    public Value search(Key key){
        Node x = search(this.root, key);
        if(x!=null){
            return x.value.createCopy();
        }
        return null;
    }


    /**
     * This function calculates statistical order of given key if found.
     * @param key
     * @return rank - statistical order
     */
    public int rank(Key key) {
        int rank = 1;
        Node x = search(this.root, key);
        if(x == null){
            return 0;
        }
        Node y = x.p;
        while( y!= null){
            if(x == y.middle){
                rank = rank + y.left.size;
            }
            else if(x == y.right){
                rank = rank + y.left.size + y.middle.size;;
            }
            x = y;
            y = y.p;
        }
        return rank;
    }


    /**
     * This aux function is used to find key of node matching given statistical order.
     * @param x root of tree
     * @param i statistical order
     * @return copy of found key
     */
    private Key select(Node x, int i){
        if(x == null || this.root == null || x.size < i){
            return null;
        }
        if(isLeaf(x)){
            return x.key.createCopy();
        }
        int s_left = x.left.size;
        int s_left_middle = x.middle.size + x.left.size;
        if(i<= s_left){
            return select(x.left, i);
        }
        else if(i <= s_left_middle){
            return select(x.middle, i-s_left);
        }
        return select(x.right, i-s_left_middle);
    }

    /**
     * This main function is used to find key of node matching given statistical order, using aux.
     * @param i statistical order
     * @return key of found node
     */
    public Key select(int i) {
        if(i <= 0){
            return null;
        }
        return select(this.root, i);
    }

    /**
     * This function is used as aux to sum over values of leaves in k1,k2 interval.
     * if current node is partially contained in interval we want to check sum attribute of children.
     * @param x current node
     * @param k1 lower key
     * @param k2 upper key
     * @return required sum
     */

    private Value sum_of_inter(Node x, Key k1, Key k2){
        Value sum=null;
        if(x == null){
            return null;
        }

        if(x.min_k.compareTo(k1) >=0 && x.max_k.compareTo(k2) <=0){
            return x.sum.createCopy();
        }

        if(is_partial(x.left, k1, k2)){
            Value tmp_sum = sum_of_inter(x.left, k1,k2);
            if (tmp_sum != null) {
                if (sum == null) {
                    sum = tmp_sum;
                } else {
                    sum.addValue(tmp_sum);
                }
            }
        }
        if(is_partial(x.middle, k1, k2)){
            Value tmp_sum = sum_of_inter(x.middle, k1,k2);
            if (tmp_sum != null) {
                if (sum == null) {
                    sum = tmp_sum;
                } else {
                    sum.addValue(tmp_sum);
                }
            }
        }
        if(is_partial(x.right, k1, k2)){
            Value tmp_sum = sum_of_inter(x.right, k1,k2);
            if (tmp_sum != null){
                if(sum == null){
                    sum = tmp_sum;
                }
                else {
                    sum.addValue(tmp_sum);
                }
            }
        }
        return sum;
    }


    /**
     *This function is used to check if current node's interval is partially contained in k1,k2 interval
     * @param x node to check
     * @param k1 lower key boundary
     * @param k2 upper key boundary
     * @return true - if partially contained, false otherwise
     */
    private boolean is_partial(Node x, Key k1, Key k2){
        if(x == null) return false;

        if (x.max_k.compareTo(k1) <0 || x.min_k.compareTo(k2)>0){
            return false;
        }
        return true;
    }

    /**
     * This function is used to sum values of leaves in interval of keys.
     * @param key1 lower key boundary
     * @param key2 upper key boundary
     * @return required sum
     */
    public Value sumValuesInInterval(Key key1, Key key2){
        if(this.root == null){
            return null;
        }
        if(key1.compareTo(key2) > 0){
            return null;
        }
        return sum_of_inter(this.root, key1, key2);
    }



}
