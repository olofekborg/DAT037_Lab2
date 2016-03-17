import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Implementation of priority queue that uses a binary heap
 * and a hashmap for fast lookups (modify-key)
 */
public class PriorityQueue<E> {

    Comparator<? super E> comp; // compares objects in the queue
    HashMap<E, Integer> map; //maps an object to its index in the queue
    ArrayList<E> queue; // stores the binary tree


    /**
     * Constructor which takes a comparator
     *
     * @param comp comparator which is used to decide the order in the queue
     */
    public PriorityQueue(Comparator<? super E> comp) {
        map = new HashMap<>();
        queue = new ArrayList<>();
        this.comp = comp;
    }


    /**
     * Tries to add an object to the queue. Fails if object already present
     * (no duplicates allowed)
     *
     * @param obj object to insert
     * @return False if object already existed, otherwise true
     */
    public boolean insert(E obj) {
        if (findIndex(obj) != null) {
            return false;
        }
        queue.add(obj);
        map.put(obj, lastIndex());
        bubbleUp(lastIndex());
        return true;
    }

    /**
     * Remove and return the item at the top of the queue
     * Restructure the queue to keep a binary heap
     *
     * @return top item
     */
    public E poll() {
        if (queue.size() == 0) {
            return null;
        }
        E oldRoot = queue.get(0);
        if (lastIndex()!=0) {
            E newRoot = queue.remove(lastIndex());
            map.put(newRoot, 0);
            queue.set(0, newRoot);
            bubbleDown(0);
        } else {
            queue.remove(0);
        }
        map.remove(oldRoot);
        return oldRoot;
    }

    /**
     * Return the item at the top of the queue
     *
     * @return top item
     */
    public E peak() {
        if (queue.size() == 0) {
            return null;
        }
        return queue.get(0);
    }

    /**
     * Replaces an object and restructures the queue
     *
     * @param oldObj the object to be modified
     * @param newObj the new object
     * @return True replacement was succesful (otherwise false)
     */
    public boolean modifyKey(E oldObj, E newObj) {
        //kolla om objektet finns
        Integer index = findIndex(oldObj);
        if (index == null)
            return false;
        set(index, newObj);
        bubbleDown(index);
        bubbleUp(findIndex(newObj));
        return true;
    }

    // DEBUG
    @Override
    public String toString() {

        StringBuffer s = new StringBuffer();

        if (queue.size() == 0)
            return "Empty";

        int mod = 2;
        int increment = 4;

        for (int i = 1; i < queue.size(); i++) {

            if (i == mod) {
                mod += increment;
                increment = increment * 2;
            }
        }

        int max = mod;

        mod = 2;
        increment = 4;

        append_spaces(max, s);
        s.append(queue.get(0));
        s.append('\n');
        append_spaces((max - mod), s);

        for (int i = 1; i < queue.size(); i++) {

            s.append(queue.get(i));

            if (i == mod) {

                s.append('\n');

                mod += increment;
                increment = increment * 2;
                append_spaces((max - mod), s);

            } else {
                if ((i - 1) % 2 == 0)
                    s.append("__");
                else
                    s.append("  ");
            }
        }
        return s.toString();
    }

    /**
     * @param n
     * @param s
     */
    private void append_spaces(int n, StringBuffer s) {
        for (int i = 0; i < n; i++) {
            s.append(' ');
        }
    }

    /**
     * Starts at index i and if the element is smaller than its
     * parent it swaps with it and repeats the process for the next parent.
     *
     * @param index to start at
     */
    private void bubbleUp(int index) {
        E obj = queue.get(index);
        int parentI = (index - 1) / 2;
        while (parentI >= 0) {
            E parent = queue.get(parentI);
            if (comp.compare(obj, parent) >= 0) {
                break;
            }
            //switch places
            set(index, parent);
            set(parentI, obj);
            //calculate new indexes
            index = parentI;
            parentI = (index - 1) / 2;
        }
    }

    /**
     * Starts at index i and if the element is larger than any of its
     * children it swaps with the smallest child (and repeats).
     *
     * @param index to start at
     */
    private void bubbleDown(int index) {
        E obj = queue.get(index);

        int leftChildI = 2 * index + 1;
        int rightChildI = 2 * index + 2;

        while (rightChildI <= lastIndex()) {
            E leftChild = queue.get(leftChildI);
            E rightChild = queue.get(rightChildI);

            //check which child is smallest
            if (comp.compare(leftChild, rightChild) <= 0) {
                //if left was smallest, compare to it
                if (comp.compare(obj, leftChild) > 0) {
                    set(leftChildI, obj);
                    set(index, leftChild);
                    //set new index
                    index = leftChildI;
                } else {
                    //the queue is ordered
                    return;
                }
            } else {
                //if right was smallest, compare to it
                if (comp.compare(obj, rightChild) > 0) {
                    //swap
                    set(rightChildI, obj);
                    set(index, rightChild);
                    //set new index
                    index = rightChildI;
                } else {
                    //the queue is ordered
                    return;
                }
            }
            //calculate child indexes
            leftChildI = 2 * index + 1;
            rightChildI = 2 * index + 2;
        }
        //even if we dont have a rightChild, we could have a leftChild
        if (leftChildI == lastIndex()) {
            E leftChild = queue.get(leftChildI);
            if (comp.compare(obj, leftChild) > 0) {
                set(leftChildI, obj);
                set(index, leftChild);
            }
        }
    }

    /**
     * Sets an object to the given position in the queu
     * and updates the hashmap accordingly
     *
     * @param i   new index
     * @param obj object to set
     */
    private void set(int i, E obj) {
        queue.set(i, obj);
        map.put(obj, i);

    }

    /**
     * Returns the index of the given object (or null if not in array)
     */
    private Integer findIndex(E obj) {
        return map.get(obj);
    }

    private int lastIndex() {
        return queue.size() - 1;
    }
}

