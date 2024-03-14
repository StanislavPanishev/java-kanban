package manager;

import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private final MyLinkedList<Task> browsingHistory;
    private final Map<Integer, Node<Task>> nodes;

    public InMemoryHistoryManager() {
        browsingHistory = new MyLinkedList<>();
        nodes = new HashMap<>();
    }

    public static class MyLinkedList<T> {
        private Node<T> head;
        private Node<T> tail;
        private int size = 0;

        private Node<T> linkLast(T task) {
            final Node<T> oldTail = tail;
            final Node<T> newNode = new Node<>(oldTail, task, null);
            if (size == 0) {
                head = newNode;
            } else {
                oldTail.next = newNode;
            }
            tail = newNode;
            size++;
            return newNode;
        }

        public List<T> getTasks() {
            List<T> list = new ArrayList<>();
            Node<T> node = head;
            while (node != null) {
                list.add(node.data);
                node = node.next;
            }
            return list;
        }

        private void removeNode(Node<T> node) {
            if (node == null) {
                return;
            }
            if (size == 1) {
                head = null;
                tail = null;
            }
            Node<T> prevNode = node.prev;
            Node<T> nextNode = node.next;
            if (prevNode != null) {
                prevNode.next = nextNode;
            } else {
                head = nextNode;
            }
            if (nextNode != null) {
                nextNode.prev = prevNode;
            } else {
                tail = prevNode;
            }
            size--;
        }
    }

    @Override
    public List<Task> getHistory() {
        return browsingHistory.getTasks();
    }

    @Override
    public void add(Task task) {
        final int id = task.getId();
        if (nodes.containsKey(id)) {
            remove(id);
        }
        final Node<Task> node = browsingHistory.linkLast(task);
        nodes.put(id, node);
    }

    @Override
    public void remove(int id) {
        final Node<Task> node = nodes.remove(id);

        if (node != null)
            browsingHistory.removeNode(node);
    }
}
