package datastructures.sorting;

import misc.BaseTest;
import misc.Searcher;

import org.junit.Test;

import datastructures.concrete.ArrayHeap;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;

import static org.junit.Assert.assertTrue;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestSortingStress extends BaseTest {
    @Test(timeout=15*SECOND)
    public void testArrayHeap() {
        IPriorityQueue<Integer> heap = new ArrayHeap<Integer>();
        for (int i = 200000; i > 0; i--) {
            heap.insert(i);
            assertEquals(200001 - i, heap.size());
        }
        int min = -1;
        for (int i = 0; i < heap.size(); i++) {
            assertTrue(heap.peekMin() > min);
            min = heap.removeMin();
            assertEquals(199999 - i, heap.size());
        }
    }
    
    @Test(timeout=15*SECOND)
    public void testKTopSort() {
        IList<Integer> list = new DoubleLinkedList<Integer>();
        for (int i = 200000; i > 0; i--) {
            list.add(i);
            assertEquals(200001 - i, list.size());
        }
        IList<Integer> top = Searcher.topKSort(199999, list);
        int min = -1;
        for (int i = 0; i < top.size(); i++) {
            assertTrue(top.get(0) > min);
            min = top.delete(0);
            assertEquals(199998 - i, top.size());
        }
    }
}
