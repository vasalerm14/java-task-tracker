package managers;

import interfaces.HistoryManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import tasks.Task;

public class InMemoryHistoryManager implements HistoryManager {
    private HashMap<Integer, Node> history = new HashMap<>();

    private Node first;
    private Node last;

    private class Node {
        Node prev;
        Node next;
        Task task;

        public Node(Task task) {
            this.task = task;
        }
    }

    @Override
    public void add(Task task) {

        if (task == null) {
            return;
        }

        Node existingNode = history.get(task.getIdentification());
        if (existingNode != null) {
            removeNode(existingNode);
        }

        linkLast(task);
    }

    @Override
    public void remove(int id) {
        removeNode(history.get(id));
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    private void linkLast(Task task) {
        Node newNode = new Node(task);
        if (last == null) {
            first = newNode;
        } else {
            newNode.prev = last;
            last.next = newNode;
        }
        last = newNode;
        history.put(task.getIdentification(), newNode);

    }

    private ArrayList<Task> getTasks() {
        ArrayList<Task> tasksList = new ArrayList<>();
        Node current = first;

        while (current != null) {
            tasksList.add(current.task);
            current = current.next;
        }

        return tasksList;
    }

    private void removeNode(Node removeNode) {
        if (removeNode == null) {
            return;
        }

        Node prev = removeNode.prev;
        Node next = removeNode.next;

        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
        }

        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
        }

        history.remove(removeNode.task.getIdentification());
    }

}
