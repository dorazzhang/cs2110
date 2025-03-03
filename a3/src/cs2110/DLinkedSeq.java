package cs2110;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A sequence of elements of type `T` implemented as a doubly-linked list.  Null elements are not
 * allowed.
 */
public class DLinkedSeq<T> implements Seq<T> {

    /**
     * Number of elements in the sequence.  Equal to the number of linked nodes reachable from
     * `head` (including `head` itself) using `next` arrows.
     */
    private int size;

    /**
     * First node of the doubly-linked sequence (null for empty sequence). `head.prev` must be null
     */
    private DNode head;

    /**
     * Last node of the doubly-linked sequence (null for empty sequence). `tail.next` must be null.
     */
    private DNode tail;


    /**
     * Assert that this object satisfies its class invariants.
     */
    private void assertInv() {
        assert size >= 0;
        if (size == 0) {
            assert head == null;
            assert tail == null;
        } else {
            assert head != null;
            assert tail != null;
            // TODO 8: By traversing the sequence from head to tail, check that
            //  (1) none of the nodes store null elements
            //  (2) the number of linked nodes is equal to the sequence's size
            //  (3) the last linked node is the same object as `tail`
            //  (4) the linking is consistent, i.e. for a non-tail node n, n.next.prev is n

            DNode temp = head;
            int count = 0;
            while (temp != null) {
                assert temp.data != null;
                temp = temp.next;
                count++;
            }
            assert count == size;
            temp = tail;
            while (temp.next != null) {
                temp = temp.next;
            }
            assert temp == tail;

            DNode forward = head;
            while (forward.next != null) {
                assert forward == forward.next.prev;
                forward = forward.next;
            }
            DNode reverse = tail;
            while (reverse.prev != null) {
                assert reverse == reverse.prev.next;
                reverse = reverse.prev;
            }
        }
    }

    /**
     * Create an empty sequence.
     */
    public DLinkedSeq() {
        size = 0;
        head = null;
        tail = null;
        assertInv();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void prepend(T elem) {
        assert elem != null;

        DNode newHead = new DNode(elem, null, head);
        if (size == 0) {
            tail = newHead; // if sequence was empty, assign tail as well
        } else {
            head.prev = newHead; // sequence had a different head, must point back to new head
        }
        head = newHead;
        size += 1;
        assertInv();
    }

    @Override
    public void append(T elem) {
        // TODO 9: Implement this method according to its specification
        // Implementation constraint: efficiency must not depend on the size of the sequence.
        assert elem != null;

        DNode newTail = new DNode(elem, tail, null);
        if (size == 0) {
            head = newTail;
        } else {
            tail.next = newTail;
        }
        tail = newTail;
        size++;
        assertInv();
    }

    @Override
    public T get(int index) {
        // TODO 10: Implement this method according to its specification.
        if (index < 1 || index > size) {
            throw new IndexOutOfBoundsException();
        }
        int count = 1;
        DNode temp = head;
        while (count < index) {
            temp = temp.next;
            count++;
        }
        return temp.data;
    }

    /**
     * Return the first node `n` such that `n.data.equals(elem)`, or null if this sequence does not
     * contain `elem`.
     */
    private DNode firstNodeWith(T elem) {
        assert elem != null;

        // TODO 11: Implement this private helper method according to its specification
        if (size == 0) {
            return null;
        }
        DNode temp = head;
        while (temp != null) {
            if (temp.data.equals(elem)) {
                return temp;
            }
            temp = temp.next;
        }
        return null;
    }

    @Override
    public boolean contains(T elem) {
        // TODO 12: Implement this method according to its specification.
        // Implementation constraint: use `firstNodeWith()` to avoid code duplication.
        return firstNodeWith(elem) != null;
    }

    @Override
    public void insertBefore(T elem, T successor) {
        // TODO 13: Implement this method according to its specification.
        // Implementation constraint: use `firstNodeWith()` to avoid code duplication.
        if (firstNodeWith(successor) == null) {
            throw new NoSuchElementException();
        }

        DNode newNode = new DNode(elem, firstNodeWith(successor).prev, );

        if (firstNodeWith(successor).prev != null) {
            firstNodeWith(successor).prev.next = firstNodeWith(successor).next;
        } else {
            head = newNode;
        }
        firstNodeWith(successor).prev = new
        size++;
        assertInv();
    }

    @Override
    public boolean remove(T elem) {
        // TODO 14: Implement this method according to its specification.
        // Implementation constraint: use `firstNodeWith()` to avoid code duplication.
        if (firstNodeWith(elem) == null) {
            return false;
        }
        if (firstNodeWith(elem).prev == head && firstNodeWith(elem).next == tail) {
            head = null;
            tail = null;
            size--;
            return true;
        } else if (firstNodeWith(elem) == head) {
            head = firstNodeWith(elem).next;
            head.prev = null;
        } else if (firstNodeWith(elem) == tail) {
            tail = firstNodeWith(elem).prev;
            tail.next = null;
        } else {
            firstNodeWith(elem).prev.next = firstNodeWith(elem).next;
            firstNodeWith(elem).next.prev = firstNodeWith(elem).prev;
        }
        size--;
        return true;
    }

    /**
     * Return whether this and `other` are `DLinkedSeq`s containing the same elements in the same
     * order.  Two elements `e1` and `e2` are "the same" if `e1.equals(e2)`.  Note that `DLinkedSeq`
     * is mutable, so equivalence between two objects may change over time.  See `Object.equals()`
     * for additional guarantees.
     */
    @Override
    public boolean equals(Object other) {
        /* Note: In the `instanceof` check, we write `DLinkedSeq` instead of `DLinkedSeq<T>` because
         * of a limitation inherent in Java generics: it is not possible to check at run-time what
         * what the specific type `T` is.  So instead we check a weaker property: that `other` is
         * some (unknown) instantiation of `DLinkedSeq`.  As a result, the static type of
         * `currNodeOther.data` is `Object`.
         */
        if ((other == null) || (getClass() != other.getClass())) {
            return false;
        }

        @SuppressWarnings("unchecked")
        DLinkedSeq<Object> otherSeq = (DLinkedSeq) other;
        DNode currNodeThis = head;

        for (Object o : otherSeq) {
            if (currNodeThis == null || !currNodeThis.data.equals(o)) {
                return false;
            }
            currNodeThis = currNodeThis.next;
        }
        return currNodeThis == null;
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

    /**
     * Return a text representation of this sequence with the following format: the string starts
     * with '[' and ends with ']'.  In between are the string representations of each element, in
     * sequence order, separated by ", ".
     * <p>
     * Example: a sequence containing 4 7 8 in that order would be represented by "[4, 7, 8]".
     * <p>
     * Example: a sequence containing two empty strings would be represented by "[, ]".
     * <p>
     * The string representations of elements may contain the characters '[', ',', and ']'; these
     * are not treated specially.
     */
    @Override
    public String toString() {
        // Note: We don't take advantage of being `Iterable` here (as in ArraySeq) so that you can
        //  use strings for testing before you've implemented DLinkedSeqIterator.

        StringBuilder str = new StringBuilder("[");
        DNode node = head;
        while (node != null) {
            str.append(node.data);
            if (node.next != null) {
                str.append(", ");
            }
            node = node.next;
        }
        str.append("]");
        return str.toString();
    }

    /**
     * Return an iterator over the elements of this sequence (in order).  By implementing
     * `Iterable`, clients can use Java's "enhanced for-loops" to iterate over the elements of the
     * sequence.  Requires that the sequence not be mutated while the iterator is in use (except via
     * a single active Iterator's `remove()` method).
     */
    @Override
    public Iterator<T> iterator() {
        return new DLinkedSeqIterator();
    }

    /**
     * A forward iterator over a doubly-linked sequence.  Generally requires that the sequence is
     * not mutated except via this iterator's own `remove()` method.  Optional behavior: methods may
     * throw ConcurrentModificatoinException if sequence is mutated by any means other than this
     * iterator's own `remove()` method.
     */
    protected class DLinkedSeqIterator implements Iterator<T> {

        // TODO 15: Determine, declare, initialize, and document the fields of this iterator class

        /**
         * Return whether the iteration has more elements.
         */
        @Override
        public boolean hasNext() {
            // TODO 16a: Implement this method according to its specification.
            // Implementation constraint: efficiency must not depend on the size of the sequence.
            throw new UnsupportedOperationException();
        }

        /**
         * Return the next element in the iteration, advancing this iterator. Throws
         * NoSuchElementException if the iteration has no more elements.
         */
        @Override
        public T next() {
            // TODO 16b: Implement this method according to its specification.
            // Implementation constraint: efficiency must not depend on the size of the sequence.
            throw new UnsupportedOperationException();
        }

        /**
         * Removes from the underlying collection the last element returned by this iterator.  Does
         * not affect the next element that will be returned from the iteration.  Throws
         * IllegalStateException if `next()` has not yet been called or if `remove()` has already
         * been called since the last call to `next()`.
         */
        @Override
        public void remove() {
            // TODO (challenge extension): Implement this method according to its specification.
            // Implementation constraint: efficiency must not depend on the size of the sequence.
            throw new UnsupportedOperationException();
        }
    }

    /**
     * A node of a doubly-linked sequence whose elements have type `T`.
     */
    private class DNode {

        /**
         * The element in this node.
         */
        final T data;

        /**
         * Next node in the sequence (null if this is the last node).
         */
        DNode next;

        /**
         * Previous node in the sequence (null if this is the first node).
         */
        DNode prev;

        /**
         * Create a Node containing element `elem`, pointing backward to node 'prev' (may be null),
         * and pointing forward to node `next` (may be null).
         */
        DNode(T elem, DNode prev, DNode next) {
            data = elem;
            this.prev = prev;
            this.next = next;
        }
    }
}
