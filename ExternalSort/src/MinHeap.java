/**
 * @author ryanjt5
 * @version 4/14/20
 *
 */
public class MinHeap {

    private int size;
    private final int maxSize = 4096;
    private Record[] heap;


    /**
     * creates a new minHeap of size 4096
     */
    public MinHeap() {
        size = 0;
        heap = new Record[maxSize];

    }


    /**
     * get current size of heap
     * 
     * @return current size of heap
     */
    public int size() {
        return size;
    }


    /**
     * gets the array backing the heap
     * 
     * @return the heap array
     */
    public Record[] getArray() {
        return heap;
    }


    /**
     * gets a record from a given position
     * 
     * @param p
     *            given position
     * @return the record and position p
     */
    public Record get(int p) {
        return heap[p];
    }



    /**
     * checks if the heap is full
     * it has a max size of 4096 records
     * 
     * @return true if the heap is full
     */
    public boolean isFull() {
        return size == maxSize;
    }


    /**
     * checks if the heap is empty
     * 
     * @return true if the is heap empty
     */
    public boolean isEmpty() {
        return size == 0;
    }


    /**
     * gets the position of the right child of a given position
     * 
     * @param p
     *            parent of the child we're looking for
     * @return the position right child of the value at position p
     */
    public int getRight(int p) {
        if (p >= (size - 1) / 2) {
            return -1;
        }
        else {
            return 2 * p + 2;
        }
    }


    /**
     * gets the position of the left child of a given position
     * 
     * @param p
     *            parent of the child we're looking for
     * @return the position of left child of the value at position p
     */
    public int getLeft(int p) {
        if (p >= (size) / 2) {
            return -1;
        }
        else {
            return 2 * p + 1;
        }
    }


    /**
     * gets parent of given child
     * 
     * @param p
     *            position of given child
     * @return the position of the given child's parent
     */
    public int parent(int p) {
        if (p <= 0) {
            return -1;
        }
        else {
            return (p - 1) / 2;
        }
    }


    /**
     * swaps 2 objects in a given array
     * 
     * @param hp
     *            array
     * @param ob1
     *            position of first object to be swapped
     * @param ob2
     *            position of 2nd object to swap
     */
    private void swap(Record[] hp, int ob1, int ob2) {
        Record temp = heap[ob1];
        hp[ob1] = heap[ob2];
        hp[ob2] = temp;
    }


    /**
     * insert a new record into the heap
     * 
     * @param rec
     *            record to be inserted
     * @return true if successful insert false otherwise
     *         a value won't be inserted if the heap is full
     */
    public boolean insert(Record rec) {
        if (size >= maxSize) {
            return false;
            // heap is full
        }
        else { // proceed with regular insert
            int curr = size++;
            heap[curr] = rec;
            while ((curr != 0) && (heap[curr].compareTo(heap[parent(
                curr)]) < 0)) {
                swap(heap, curr, parent(curr));
                curr = parent(curr);
            }
            return true;
        }
    }



    /**
     * checks if a position is a leaf
     * 
     * @param p
     *            position being checked
     * @return true if p is a leaf
     */
    public boolean isLeaf(int p) {
        return (p >= (size / 2)) && (p < size);
    }


    /**
     * sifts the heap down
     * 
     * @param p
     *            position is sift
     */
    private void siftDown(int p) {
        if ((p < 0) || (p >= size)) {
            return;
        }
        while (!isLeaf(p)) {
            int j = getLeft(p);
            if ((j < (size - 1)) && (heap[j].compareTo(heap[j + 1]) > 0)) {
                j++;
            }
            if (heap[p].compareTo(heap[j]) <= 0) {
                return;
            }
            swap(heap, p, j);
            p = j;
        }
    }


    /**
     * re-"heaps" the heap
     */
    public void reheap() {
        for (int i = size - 1; i >= 0; i--) {
            siftDown(i);
        }
    }


    /**
     * removes the minimum value from the heap
     * 
     * @return the record that was removed
     */
    public Record removeMin() {
        if (size == 0) {
            return null;
        }
        swap(heap, 0, --size);
        if (size != 0) {
            siftDown(0);
        }
        Record rec = heap[size];
        // last = rec;
        heap[size] = null;
        return rec;
    }


    /**
     * removes a record from the heap
     * 
     * @param r
     *            record to be removed
     * @return true if the record is removed false otherwise
     */
    public boolean remove(Record r) {
        boolean found = false;
        int i = 0;
        while (!found && i < size) {
            if (heap[i].equals(r)) {
                found = true;
                swap(heap, i, --size);
            }
        }
        reheap();
        return found;
    }

}
