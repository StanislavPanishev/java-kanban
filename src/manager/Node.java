package manager;

class Node<T> {

    protected T data;
    protected Node<T> next;
    protected Node<T> prev;

    public Node(Node<T> prev, T data, Node<T> next) {
        this.data = data;
        this.next = null;
        this.prev = null;
    }
}