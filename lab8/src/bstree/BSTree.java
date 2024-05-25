package bstree;

public class BSTree<E extends Comparable<E>> implements LinkBST<E> {

    protected class Node {
        public E data;
        public Node left, right;

        public Node(E data) {
            this.data = data;
            this.left = null;
            this.right = null;
        }
    }

    protected Node root;

    public BSTree() {
        this.root = null;
    }

    @Override
    public boolean isEmpty() {
        return this.root == null;
    }

    @Override
    public void insert(E x) throws ItemDuplicated {
        root = insertHelper(root, x);
    }

    private Node insertHelper(Node root, E x) throws ItemDuplicated {
        if (root == null) {
            root = new Node(x);
            return root;
        }
        if (x.compareTo(root.data) < 0) {
            root.left = insertHelper(root.left, x);
        } else if (x.compareTo(root.data) > 0) {
            root.right = insertHelper(root.right, x);
        } else {
            throw new ItemDuplicated("El elemento ya existe");
        }
        return root;
    }

    @Override
    public void remove(E x) throws ItemNotFound {
        root = removeHelper(root, x);
    }

    private Node removeHelper(Node root, E x) throws ItemNotFound {
        if (root == null) {
            throw new ItemNotFound("El arbol esta vacio");
        }
        if (x.compareTo(root.data) < 0) {
            root.left = removeHelper(root.left, x);
        } else if (x.compareTo(root.data) > 0) {
            root.right = removeHelper(root.right, x);
        } else {
            if (root.left == null) {
                return root.right;
            } else if (root.right == null) {
                return root.left;
            }
            root.data = findMin(root.right).data;
            root.right = removeHelper(root.right, root.data);
        }
        return root;
    }

    private Node findMin(Node root){
        while (root.left != null) {
            root = root.left;
        }
        return root;
    }

    public E findMinValue() throws ItemNotFound {
        if (root == null) {
            throw new ItemNotFound("El arbol esta vacio");
        }
        Node current = root;
        while (current.left != null) {
            current = current.left;
        }
        return current.data;
    }
   
    @Override
    public E search(E x) throws ItemNotFound {
        Node result = searchHelper(root, x);
        if (result == null) {
            throw new ItemNotFound("El elemento no existe");
        }
        return result.data;
    }

    private Node searchHelper(Node root, E x) {
        if (root == null || root.data.compareTo(x) == 0) {
            return root;
        }
        if (x.compareTo(root.data) < 0) {
            return searchHelper(root.left, x);
        }
        return searchHelper(root.right, x);
    }
    
    public void iterativePreOrden() {
        Node current = root;

        while (current != null) {
            System.out.print(current.data + " "); 
            if (current.left != null) {
                Node predecessor = current.left;

                while (predecessor.right != null && predecessor.right != current) {
                    predecessor = predecessor.right;
                }

                if (predecessor.right == null) {
                    predecessor.right = current;
                    current = current.left;
                    predecessor.right = null;
                    current = current.right;
                }
                current = current.right;
            }
        }
    }
    
    public int countNodes() {
        return countNodesHelper(root);
    }
    
    private int countNodesHelper(Node node) {
        if (node == null) {
            return 0;
        }
        int leftCount = countNodesHelper(node.left);
        int rightCount = countNodesHelper(node.right);
        return 1 + leftCount + rightCount;
    }
}
