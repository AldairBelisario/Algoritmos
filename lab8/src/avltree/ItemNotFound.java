package avltree;

@SuppressWarnings("serial")
public class ItemNotFound extends Exception{
	public ItemNotFound(String msg) {
		super(msg);
	}
	public ItemNotFound() {
		super();
	}
}
