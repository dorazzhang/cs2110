package cs2110;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public abstract class SeqTest {

    /**
     * Abstract method to construct an empty sequence. Subclasses are responsible for implementing
     * this method to construct the Seq subtype that they wish to test.
     */
    abstract <T> Seq<T> makeSeq();
    // **Important Note:** Within the tests in this class, you should always use `makeSeq()` to
    // construct any `Seq` object. This will allow these tests to be applied to both `ArraySeq`s and
    // `DLinkedSeq`s.

    // The following two methods provide convenient ways to create sequences for your test cases

    /**
     * Creates a sequence containing the same elements (in the same order) as array `elements` using
     * only `prepend` operations on an initially empty sequence.
     */
    <T> Seq<T> makeSeq(T[] elements) {
        Seq<T> seq = makeSeq();
        for (int i = elements.length; i > 0; i--) {
            seq.prepend(elements[i - 1]);
        }
        return seq;
    }

    /**
     * Creates a sequence containing the integers 0..n-1 (in sorted order) using only `prepend`
     * operations on an initially empty sequence.
     */
    Seq<Integer> makeSeqOfLength(int n) {
        Seq<Integer> seq = makeSeq();
        for (int i = n - 1; i >= 0; i--) {
            seq.prepend(i);
        }
        return seq;
    }

    @DisplayName("WHEN a Seq is first constructed, THEN it should be empty.")
    @Test
    void testConstructorSize() {
        Seq<String> list = makeSeq();
        assertEquals(0, list.size());
    }

    @DisplayName("GIVEN a Seq, WHEN an element is prepended, " +
            "THEN its size should increase by 1 each time.")
    @Test
    void testPrependSize() {
        Seq<Integer> seq = makeSeq();
        seq.prepend(1);
        assertEquals(1, seq.size());
        seq.prepend(2);
        assertEquals(2, seq.size());
        seq.prepend(3);
        assertEquals(3, seq.size());
    }

    @DisplayName("GIVEN a Seq containing a sequence of values, " +
            "THEN its string representation should include the string representations of its " +
            "values, in order, separated by a comma and space, all enclosed in square brackets.")
    @Test
    void testToString() {
        // WHEN sequence is empty
        Seq<String> seq0 = makeSeq();
        assertEquals("[]", seq0.toString());

        // WHEN head = tail
        Seq<String> seq1 = makeSeq(new String[]{"A"});
        assertEquals("[A]", seq1.toString());

        // WHEN only items are head and tail
        Seq<String> seq2 = makeSeq(new String[]{"A", "B"});
        assertEquals("[A, B]", seq2.toString());

        // WHEN there are at least 3 nodes
        Seq<String> seq3 = makeSeq(new String[]{"A", "B", "C"});
        assertEquals("[A, B, C]", seq3.toString());

        // WHEN values are not strings
        Seq<Integer> intSeq = makeSeqOfLength(5);
        assertEquals("[0, 1, 2, 3, 4]", intSeq.toString());
    }

    @DisplayName("GIVEN a Seq, WHEN an element is prepended, " +
            "THEN it is added to the beginning of the Seq.")
    @Test
    void testPrependToString() {
        Seq<Integer> seq = makeSeq();
        seq.prepend(1);
        assertEquals("[1]", seq.toString());
        seq.prepend(2);
        assertEquals("[2, 1]", seq.toString());
        seq.prepend(3);
        assertEquals("[3, 2, 1]", seq.toString());
    }

    // TODOs 1-5: Add new test cases here for the methods of `Seq`.  To save typing, you may combine
    //  multiple tests for the _same_ method in the same @Test procedure, but be sure that each test
    //  case is visibly distinct (comments are good for this, as demonstrated above). You are
    //  welcome to compare against an expected `toString()` output in order to check multiple
    //  aspects of the state at once (in general, later tests may make use of methods that have
    //  previously been tested).  Each test procedure must describe its scenario using @DisplayName.
    //
    // As you write each testcase, run it via `ArraySeqTest` to see whether our `ArraySeq` passes
    //  your test.  Note that there are TWO BUGS in the `ArraySeq` release code.  Ensure that your
    //  testcases catch these bugs, then fix them and respond to reflection question 1 in
    //  "reflection.txt".

    // TODO 1: Add tests for `append()`. Your tests should append to lists of at least three
    //  different sizes and use accessor methods to write assertions about properties of the
    //  resulting sequences.

    @DisplayName("WHEN an element is appended, THEN it is added to the end of the Seq.")
    @Test
    void testAppend() {
        Seq<Integer> seq = makeSeq();
        //when the seq is empty
        seq.append(1);
        assertEquals("[1]", seq.toString());
        //when the seq has 1 element
        seq.append(2);
        assertEquals("[1, 2]", seq.toString());
        //when the seq has multiple elements
        seq.append(3);
        seq.append(4);
        assertEquals("[1, 2, 3, 4]", seq.toString());
        //when the seq is very large
        for (int i = 5; i < 20; i++) {
            seq.append(i);
        }
        assertEquals(19, seq.size());
        assertEquals("[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19]", seq.toString());

        DLinkedSeq test = new DLinkedSeq();
        //when a doubly linked list is empty
        test.append(1);
        assertEquals("[1]", test.toString());
        //when a doubly linked list has one node
        test.append(2);
        assertEquals("[1, 2]", test.toString());
        //when a doubly linked list has multiple elements
        test.append(3);
        test.append(4);
        assertEquals("[1, 2, 3, 4]", test.toString());
        //when a doubly linked list is large;
        for(int i = 5; i <= 10; i++){
            test.append(i);
        }
        assertEquals("[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]", test.toString());
    }

    // TODO 2: Add tests for `get()`. Your tests should get elements from at at least three
    //  different indices. Think about which indices might cover corner cases in an implementation.

    @DisplayName("WHEN get is called for an index, THEN it should return the element at the index.")
    @Test
    void testGet() {
        Seq<Integer> seq = makeSeq();
        seq.append(1);
        seq.append(2);
        seq.append(3);
        seq.append(4);
        assertEquals(1, seq.get(0));
        assertEquals(4, seq.get(seq.size() - 1));
        seq.append(5);
        assertEquals(5, seq.get(4));
        assertEquals(3, seq.get(2));

        DLinkedSeq<Integer> test = new DLinkedSeq<>();
        test.append(2);
        test.append(4);
        test.append(6);
        //When get() is called for the first element
        assertEquals(2, test.get(1));
    }

    // TODO 3: Add tests for `contains()`. Your tests should cover the possibility that the list
    // does not contain `elem`, contains `elem` once, and contains `elem` multiple times.

    @DisplayName("WHEN contains() is called, THEN it should return whether elem is in the list")
    @Test
    void testContains() {
        Seq<Integer> seq = makeSeq();
        seq.append(1);
        seq.append(1);
        seq.append(1);
        seq.append(4);
        seq.append(5);
        //WHEN the list does not contain 'elem', THEN return false
        assertFalse(seq.contains(2));
        //WHEN the list does contain 'elem', THEN return true
        assertTrue(seq.contains(5));
        //WHEN the list contains multiple of 'elem', THEN return true
        assertTrue(seq.contains(1));

        DLinkedSeq<Integer> test = new DLinkedSeq<>();

        // When doubly linked list is empty, then return false
        assertFalse(test.contains(1));

        test.append(3);
        test.append(2);
        test.append(1);
        test.append(4);
        test.append(6);
        // When doubly linked list contains elem in the first index
        assertTrue(test.contains(3));
        // When doubly linked list contains elem in the last index
        assertTrue(test.contains(6));
        // When doubly linked list does not contain elem
        assertFalse(test.contains(9));

    }

    // TODO 4: Add tests for `insertBefore()`.  At this point, we've given a lot of hints about what
    //  a comprehensive set of tests might look like, so here we leave it up to you to determine.
    //  Consider using a loop to test dozens of different list sizes in one testcase.

    @DisplayName("WHEN insertBefore() is called, THEN it should modify the list by putting 'elem' "
            + "before its successor")
    @Test
    void testInsertBefore() {
        Seq<Integer> seq = makeSeq();
        seq.append(2);
        seq.append(8);
        seq.append(9);

        // WHEN inserting before an element that exists
        seq.insertBefore(1, 8);
        assertEquals("[2, 1, 8, 9]", seq.toString());

        // WHEN inserting before the first element
        seq.insertBefore(0, 2);
        assertEquals("[0, 2, 1, 8, 9]", seq.toString());

        // WHEN inserting before the last element
        seq.insertBefore(3, 9);
        assertEquals("[0, 2, 1, 8, 3, 9]", seq.toString());

        // WHEN successor appears multiple times, THEN insert before the first occurrence
        seq.append(8);
        seq.insertBefore(7, 8);
        assertEquals("[0, 2, 1, 7, 8, 3, 9, 8]", seq.toString());

        // WHEN inserting in a sequence that is full
        Seq<Integer> seq2 = makeSeq();
        seq2.append(1);
        seq2.append(2);
        seq2.append(3);
        seq2.append(4);
        seq2.append(5);
        seq2.append(6);
        seq2.append(7);
        seq2.append(8);
        seq2.append(9);
        seq2.append(11);
        seq2.insertBefore(10, 11);
        assertEquals("[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11]", seq2.toString());


    }

    // TODO 5: Add tests for `remove()`. Your tests should consider multiple distinct scenarios
    //  (which we again leave up to you to determine).

    @DisplayName("WHEN Remove() is called, THEN it should remove first occurrence of 'elem'"
            + "and return true or return false if not found")
    @Test
    void testRemove() {
        Seq<Integer> seq = makeSeq();
        seq.append(2);
        seq.append(8);
        seq.append(8);
        seq.append(9);
        seq.append(1);
        seq.append(3);
        seq.append(8);

        // WHEN removing an element that exists, THEN remove and return true
        assertTrue(seq.remove(2));
        assertEquals("[8, 8, 9, 1, 3, 8]", seq.toString());

        // WHEN seq contains multiple of 'elem', THEN remove first occurrence and return true
        assertTrue(seq.remove(8));
        assertEquals("[8, 9, 1, 3, 8]", seq.toString());

        // WHEN removing an element that doesn't exist, THEN do nothing and return false
        assertFalse(seq.remove(10));
        assertEquals("[8, 9, 1, 3, 8]", seq.toString());

        // WHEN seq is empty THEN, do nothing and return false
        Seq<Integer> seqEmpty = makeSeq();
        assertFalse(seqEmpty.remove(10));
        assertEquals("[]", seqEmpty.toString());
    }


    @Nested
    @DisplayName("GIVEN two distinct Seqs containing the same elements in the same order,"
            + "then, they should be compared equal...")
    class EqualsTrueTest {

        @Test
        @DisplayName("when both are empty")
        void testEqualsTrue0() {
            // In these tests, you might be wondering why we are using the assertTrue(a.equals(b))
            // pattern instead of assertEquals(a,b). It's true that both of these make the same
            // assertion (and likely IntelliJ will offer assertEquals as a simplification). However,
            // philosophically, asserting equality goes against what we are testing. We're trying to
            // verify the functionality of the equals() method, meaning we want to know whether it
            // is returning true/false in the correct scenarios. When we use assertEquals, it's
            // because we want to make sure that its second argument (the computed value) is equal
            // to the expected value, and that's not true in these tests.
            assertTrue(makeSeq().equals(makeSeq()));
        }

        @Test
        @DisplayName("when both sequences contain one element")
        void testEqualsTrue1() {
            Seq<Integer> seq1 = makeSeqOfLength(1);
            Seq<Integer> seq2 = makeSeq();
            seq2.append(0);
            assertTrue(seq1.equals(seq2));
            assertTrue(seq2.equals(seq1));
        }

        @Test
        @DisplayName("when both sequences contain two elements")
        void testEqualsTrue2() {
            Seq<Integer> seq1 = makeSeqOfLength(2);
            Seq<Integer> seq2 = makeSeq();
            seq2.append(0);
            seq2.append(1);
            assertTrue(seq1.equals(seq2));
            assertTrue(seq2.equals(seq1));
        }

        @Test
        @DisplayName("when both sequences contain three elements")
        void testEqualsTrue3() {
            Seq<Integer> seq1 = makeSeqOfLength(3);
            Seq<Integer> seq2 = makeSeq();
            seq2.append(0);
            seq2.append(1);
            seq2.append(2);
            assertTrue(seq1.equals(seq2));
            assertTrue(seq2.equals(seq1));
        }
    }

    @Nested
    @DisplayName("GIVEN two distinct Seqs, then, they should not be compared equal when...")
    class EqualsFalseTest {

        @Test
        @DisplayName("the second is null")
        void testEqualsFalseNull() {
            assertFalse(makeSeq().equals(null));
        }

        @Test
        @DisplayName("they have the same length but contain different elements")
        void testEqualsFalseDistinct() {
            assertFalse(makeSeq(new String[]{"A"}).equals(makeSeq(new String[]{"B"})));
            assertFalse(makeSeq(new String[]{"1"}).equals(makeSeqOfLength(1)));
            assertFalse(makeSeq(new String[]{"A", "B"}).equals(makeSeq(new String[]{"A", "C"})));
            assertFalse(makeSeq(new String[]{"A", "C"}).equals(makeSeq(new String[]{"B", "C"})));
        }

        @Test
        @DisplayName("the argument sequence contains the target sequence as a prefix")
        void testEqualsFalseLonger() {
            assertFalse(makeSeqOfLength(1).equals(makeSeqOfLength(2)));
            assertFalse(makeSeqOfLength(2).equals(makeSeqOfLength(3)));
        }

        @Test
        @DisplayName("the target sequence contains the argument sequence as a prefix")
        void testEqualsFalseShorter() {
            assertFalse(makeSeqOfLength(2).equals(makeSeqOfLength(1)));
            assertFalse(makeSeqOfLength(3).equals(makeSeqOfLength(2)));
        }
    }

    @DisplayName("GIVEN two distinct LinkedSeqs containing equivalent values in the same order, " +
            "THEN their hash codes should be the same.")
    @Test
    void testHashCode() {
        // WHEN empty
        assertEquals(makeSeq().hashCode(), makeSeq().hashCode());

        // WHEN head and tail are the same
        assertEquals(makeSeqOfLength(1).hashCode(), makeSeqOfLength(1).hashCode());

        // WHEN there are no nodes between head and tail
        assertEquals(makeSeqOfLength(2).hashCode(), makeSeqOfLength(2).hashCode());

        // WHEN there are at least 3 nodes
        assertEquals(makeSeqOfLength(3).hashCode(), makeSeqOfLength(3).hashCode());
    }

    // TODO 7: Add test cases here for the Seq iterator that verify its functionality on sequences
    //  of at least three different sizes. Your tests should make assertions about the return values
    //  of all implemented methods of the iterator.

}

class ArraySeqTest extends SeqTest {

    @Override
    <T> Seq<T> makeSeq() {
        return new ArraySeq<>();
    }
}

class DLinkedSeqTest extends SeqTest {

    @Override
    <T> Seq<T> makeSeq() {
        return new DLinkedSeq<>();
    }

    // TODO (challenge extension): Add tests for iterator's `remove()` method (by only adding them
    //  to DLinkedSeqTest, your ArraySeqTest suite will stay green).

    // TODO (challenge extension): Add tests for iterator's fail-fast behavior (by only adding them
    //  to DLinkedSeqTest, your ArraySeqTest suite will stay green).
}
