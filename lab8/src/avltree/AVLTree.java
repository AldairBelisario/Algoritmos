package avltree;

import bstree.BSTree;
import java.util.LinkedList;
import java.util.Queue;

public class AVLTree <E extends Comparable<E>> extends BSTree<E>{
	class NodeAVL extends Node {
		protected int bf;

		public NodeAVL(E data) {
			super(data);
			this.bf = 0;
		}

		@Override
		public String toString() {
			return data + " (bf: " + bf + ")";
		}
	}

	private boolean height;

	public void insert(E x) {
		this.height = false;
		try {
			this.root = insert(x, (NodeAVL)this.root);
		} catch (ItemDuplicated e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	protected Node insert(E x, NodeAVL node) throws ItemDuplicated {
		NodeAVL fat = node;
		if(node == null) {
			this.height = true;
			fat = new NodeAVL(x);
		} else {
			int resC = node.data.compareTo(x);
			if(resC == 0) {
				throw new ItemDuplicated(x + " ya se encuentra en el arbol...");
			}
			if(resC < 0) {
				fat.right = insert(x, (NodeAVL)node.right);
				if(this.height) {
					switch(fat.bf) {
					case -1:
						fat.bf = 1;
						this.height = false;
						break;
					case 0:
						fat.bf = 1;
						this.height = true;
						break;
					case 1:
						fat = balanceToLeft(fat);
						this.height = false;
						break;
					}
				}
			}
			else {
				fat.left = insert(x, (NodeAVL)node.left);
				switch (fat.bf) {
				case 1:
					fat.bf = 0;
					this.height = false;
					break;
				case 0:
					fat.bf = -1;
					break;
				case -1:
					fat = balanceToRight(fat);
					this.height = false;
					break;
				}
			}
		}
		return fat;
	}



	private NodeAVL balanceToLeft(NodeAVL node) {
		NodeAVL hijo = (NodeAVL)node.right;
		switch(hijo.bf) {
		case 1:
			node.bf = 0;
			hijo.bf = 0;
			node = rotateSL(node);
			break;
		case -1:
			NodeAVL nieto = (NodeAVL)hijo.left;
			switch(nieto.bf) {
			case -1: node.bf = 0; hijo.bf = 1; break;
			case 0: node.bf = 0; hijo.bf = 0; break;
			case 1: node.bf = 1; hijo.bf = 0; break;
			}
			nieto.bf = 0;
			node.right = rotateSR(hijo);
			node = rotateSL(node);
		}
		return node;
	}

	private NodeAVL balanceToRight(NodeAVL node) {
		NodeAVL hijo = (NodeAVL) node.left;
		switch (hijo.bf) {
		case -1:
			node.bf = 0;
			hijo.bf = 0;
			node = rotateSR(node);
			break;
		case 1:
			NodeAVL nieto = (NodeAVL) hijo.right;
			switch (nieto.bf) {
			case -1:
				node.bf = 1;
				hijo.bf = 0;
				break;
			case 0:
				node.bf = 0;
				hijo.bf = 0;
				break;
			case 1:
				node.bf = 0;
				hijo.bf = -1;
				break;
			}
			nieto.bf = 0;
			node.left = rotateSL(hijo);
			node = rotateSR(node);
		}
		return node;
	}


	private NodeAVL rotateSL(NodeAVL node) {
		NodeAVL p = (NodeAVL)node.right;
		node.right = p.left;
		p.left = node;
		node = p;
		return node;
	}

	private NodeAVL rotateSR(NodeAVL node) {
		NodeAVL p = (NodeAVL)node.left;
		node.left = p.right;
		p.right = node;
		node = p;
		return node;
	}

	public E search(E x) {
		try {
			NodeAVL result = searchHelper((NodeAVL) root, x);
			if (result == null) {
				throw new ItemNotFound("El elemento no existe");
			}
			return result.data;
		} catch (ItemNotFound e) {
			System.out.println("Error: " + e.getMessage());
			return null; 
		}
	}

	private NodeAVL searchHelper(NodeAVL root, E x) throws ItemNotFound {
		if (root == null || root.data.compareTo(x) == 0) {
			return root;
		}
		if (x.compareTo(root.data) < 0) {
			return searchHelper((NodeAVL) root.left, x);
		}
		return searchHelper((NodeAVL) root.right, x);
	}



	@Override
	public void remove(E x) {
		try {
			this.root = removeHelper((NodeAVL) this.root, x);
		} catch (ItemNotFound e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	private NodeAVL removeHelper(NodeAVL node, E x) throws ItemNotFound {
		if (node == null) {
			throw new ItemNotFound("El elemento no existe en el arbol");
		}

		int cmp = x.compareTo(node.data);
		if (cmp < 0) {
			node.left = removeHelper((NodeAVL) node.left, x);
			node = balanceAfterRemoval(node, 1); 
		} else if (cmp > 0) {
			node.right = removeHelper((NodeAVL) node.right, x);
			node = balanceAfterRemoval(node, -1); 
		} else {
			if (node.left == null) {
				return (NodeAVL) node.right;
			} else if (node.right == null) {
				return (NodeAVL) node.left;
			}

			NodeAVL min = (NodeAVL) findMin(node.right);
			node.data = min.data;
			node.right = removeHelper((NodeAVL) node.right, min.data);
			node = balanceAfterRemoval(node, -1); 
		}
		return node;
	}

	private NodeAVL findMin(Node node) {
		while (node.left != null) {
			node = node.left;
		}
		return (NodeAVL) node;
	}

	private NodeAVL balanceAfterRemoval(NodeAVL node, int balanceFactorAdjustment) {
		node.bf += balanceFactorAdjustment;
		if (node.bf == 2) {
			NodeAVL leftChild = (NodeAVL) node.left;
			if (leftChild.bf >= 0) {
				node = rotateSR(node);
				if (leftChild.bf == 0) {
					node.bf = 1;
					((NodeAVL) node.right).bf = -1;
				} else {
					node.bf = 0;
					((NodeAVL) node.right).bf = 0;
				}
			} else {
				node = balanceToRight(node);
			}
		} else if (node.bf == -2) {
			NodeAVL rightChild = (NodeAVL) node.right;
			if (rightChild.bf <= 0) {
				node = rotateSL(node);
				if (rightChild.bf == 0) {
					node.bf = -1;
					((NodeAVL) node.left).bf = 1;
				} else {
					node.bf = 0;
					((NodeAVL) node.left).bf = 0;
				}
			} else {
				node = balanceToLeft(node);
			}
		}
		return node;
	}


	//Recorrido por Amplitud
	public void recorridoAmplitud() {
		if (root == null) {
			return;
		}

		Queue<NodeAVL> queue = new LinkedList<>();
		queue.add((NodeAVL) root);

		while (!queue.isEmpty()) {
			NodeAVL node = queue.poll();
			System.out.print(node.data + " ");

			if (node.left != null) {
				queue.add((NodeAVL) node.left);
			}
			if (node.right != null) {
				queue.add((NodeAVL) node.right);
			}
		}
	}
}
