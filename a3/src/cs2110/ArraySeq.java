package cs2110;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A sequence of non-null elements of type `T` implemented using a dynamically resized array. The
 * array begins with some initial capacity and doubles in size whenever adding an element would
 * cause the sequence to exceed its capacity.
 */
public class ArraySeq<T> implements Seq<T> {

    /**
     * The initial capacity of the backing storage for new instances of `ArraySeq`.
     */
    private static final int INITIAL_CAPACITY = 10;

    /**
     * Backing storage of this sequence. Indices 0..size-1 are non-null while indices
     * size..contents.length are null.  Length must be at least 1.
     */
    private T[] contents;

    /**
     * Number of elements in the sequence. Must be non-negative and at most `contents.length`
     */
    private int size;

    public void assertInv() {
        assert(size >= 0 && size <= contents.length);
        for (int i = 0; i < size; i++) {
            assert(contents[i] != null);
        }
        for (int i = size; i < contents.length; i++) {
            assert(contents[i] == null);
        }
    }

    /**
     * Create an empty list.
     */
    @SuppressWarnings("unchecked")
    public ArraySeq() {
        contents = (T[]) new Object[INITIAL_CAPACITY];
        size = 0;
        assertInv();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public T get(int index) {
        return contents[index];
    }

    /**
     * If our backing storage is full, replace it with an array that is twice as large while
     * preserving its contents. Otherwise, do nothing.
     */
    private void resizeIfNeeded() {
        if (size == contents.length) {
            contents = Arrays.copyOf(contents, 2 * size);
        }
        assertInv();
    }

    @Override
    public void prepend(T elem) {
        resizeIfNeeded();

        // Shift all contents one element to the right
        System.arraycopy(contents, 0, contents, 1, size);

        contents[0] = elem;
        size += 1;
        assertInv();
    }

    @Override
    public void append(T elem) {
        resizeIfNeeded();
        contents[size] = elem;
        size += 1;
        assertInv();
    }

    /**
     * Return the smallest index i such that `contents[i].equals(elem)`, or -1 if this sequence does
     * not contain `elem`.
     */
    private int firstIndexOf(T elem) {
        assert elem != null;

        for (int i = 0; i < size; i += 1) {
            if (contents[i].equals(elem)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean contains(T elem) {
        return firstIndexOf(elem) != -1;
    }

    @Override
    public void insertBefore(T elem, T successor) {
        int i = firstIndexOf(successor);
        assert i != -1 : "Precondition: `successor` must be contained in this sequence";
        resizeIfNeeded();
        // Shift `contents[i..]` one element to the right
        System.arraycopy(contents, i, contents, i + 1, size - i);

        contents[i] = elem;
        size += 1;
        assertInv();
    }

    @Override
    public boolean remove(T elem) {
        int i = firstIndexOf(elem);

        // Return immediately if `elem` is not contained in this sequence
        if (i == -1) {
            return false;
        }

        // Shift all contents after removed value one element to the left
        System.arraycopy(contents, i + 1, contents, i, size - i - 1);

        size--;
        contents[size] = null;
        assertInv();

        return true;
    }

    /**
     * Return whether this and `other` are `ArraySeq`s containing the same elements in the same
     * order.  Two elements `e1` and `e2` are "the same" if `e1.equals(e2)`.  Note that `ArraySeq`
     * is mutable, so equivalence between two objects may change over time.  See `Object.equals()`
     * for additional guarantees.
     */
    @Override
    public boolean equals(Object other) {
        if ((other == null) || (getClass() != other.getClass())) {
            return false;
        }

        // Java doesn't provide a good way to check the type of other's generic parameter T at
        // runtime, so cast to a "raw" ArraySeq. Later in our elementwise comparison, we will
        // delegate to the equals() method of T to check that other is actually storing Ts
        @SuppressWarnings("unchecked")
        ArraySeq otherSeq = (ArraySeq) other;

        /* In this next condition, there is something subtle happening that we haven't seen before.
         We are using otherSeq.size (notice no parentheses... we are accessing the *field* of
         otherSeq, not the return value of the size() accessor method). But, size is a private
         field !? Remember, private means accessible only "within the class", but it doesn't
         restrict to only "within that instance of the class". The upshot is that within a class,
         an object doesn't only have access to its own private fields, but also to the private
         fields of any other instance of the class. */
        if (size != otherSeq.size) {
            return false;
        }

        for (int i = 0; i < size; i += 1) {
            if (!contents[i].equals(otherSeq.contents[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns a hash code value for the object.  See `Object.hashCode()` for additional
     * guarantees.
     */
    @Override
    public int hashCode() {
        // Whenever overriding `equals()`, must also override `hashCode()` to be consistent.
        // This hash recipe is recommended in _Effective Java_ (Joshua Bloch, 2008).
        int hash = 1;
        for (T e : this) {
            hash = 31 * hash + e.hashCode();
        }
        return hash;
    }

    @Override
    public String toString() {
        String str = "";
        Iterator<T> it = this.iterator();
        while (it.hasNext()) {
            str += it.next() + (it.hasNext() ? ", " : "");
        }
        return "[" + str + "]";
    }

    /**
     * Return an iterator over the elements of this list (in sequence order).  By implementing
     * `Iterable`, clients can use Java's "enhanced for-loops" to iterate over the elements of the
     * list.
     */
    @Override
    public Iterator<T> iterator() {
        return new ArraySeqIterator();
    }

    /**
     * A private inner class that acts as an iterator over an ArraySeq.
     */
    private class ArraySeqIterator implements Iterator<T> {

        /**
         * The next index to be visited by the iterator
         */
        private int index = 0;

        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return contents[index++];
        }

    }
}
