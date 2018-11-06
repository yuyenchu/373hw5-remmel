package datastructures.sorting;

import misc.BaseTest;
import misc.Searcher;

import org.junit.Test;

import datastructures.concrete.ArrayHeap;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;


/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestSortingStress extends BaseTest {
    int cap = 5000000;
    
    @Test(timeout=15*SECOND)
    public void testArrayHeap() {
        IPriorityQueue<Integer> heap = new ArrayHeap<Integer>();
        for (int i = cap; i > 0; i--) {
            heap.insert(i);
            assertEquals(cap + 1 - i, heap.size());
        }
        for (int i = 0; i < heap.size(); i++) {
            assertEquals(i + 1, heap.removeMin());
            assertEquals(cap -1 - i, heap.size());
        }
    }
    
    @Test(timeout=15*SECOND)
    public void testKTopSort() {
        IList<Integer> list = new DoubleLinkedList<Integer>();
        for (int i = 0; i > cap; i--) {
            list.add(i);
            assertEquals(i + 1, list.size());
        }
        IList<Integer> top = Searcher.topKSort(cap - 1, list);
        for (int i = 0; i < top.size(); i++) {
            assertEquals(i + 1, top.delete(0));
            assertEquals(cap - 2 - i, top.size());
        }
    }
}
