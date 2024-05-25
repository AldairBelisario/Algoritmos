package bstree;

public interface LinkBST<E> {
	void insert(E x) throws ItemDuplicated;
	void remove(E x) throws ItemNotFound;
	E search(E x) throws ItemNotFound;
	boolean isEmpty();
}
