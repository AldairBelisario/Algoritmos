package avltree;

import bstree.BSTree;
import bstree.ItemDuplicated;

public class TestAVL {

    public static void main(String[] args) throws ItemDuplicated, bstree.ItemNotFound {
    	// Creación del árbol BST
    	BSTree<Integer> bst = new BSTree<>();
    	bst.insert(5);
    	bst.insert(2);
    	bst.insert(8);
    	bst.insert(1);
    	bst.insert(4);
    	bst.insert(7);
    	bst.insert(10);

    	int result = bst.search(7);
		System.out.println("BST - Resultado de la busqueda: " + result);

    	// Creación del árbol AVL
    	AVLTree<Integer> avl = new AVLTree<>();
    	avl.insert(5);
    	avl.insert(2);
    	avl.insert(8);
    	avl.insert(1);
    	avl.insert(4);
    	avl.insert(7);
    	avl.insert(10);

    	Integer result1 = avl.search(10);
		if (result1 != null) {
		    System.out.println("Elemento encontrado: " + result1);
		} else {
		    System.out.println("Elemento no encontrado.");
		}
    }
}
