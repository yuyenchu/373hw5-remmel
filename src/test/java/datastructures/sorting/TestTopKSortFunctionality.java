package datastructures.sorting;

import misc.BaseTest;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import misc.Searcher;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestTopKSortFunctionality extends BaseTest {
    private void checkInOrder(IList<Integer> top) {
        int min = Integer.MIN_VALUE;
        for (int i = 0; i < top.size(); i++) {
            assertTrue(top.get(i) > min);
            min = top.get(i);
        }
    }
    
    private IList<Integer> makeList(int[] input){
        IList<Integer> list = new DoubleLinkedList<Integer>();
        for (int i = 0; i < input.length; i++) {
            list.add(input[i]);
        }
        return list;
    }
    
    private IList<Character> makeList(char[] input){
        IList<Character> list = new DoubleLinkedList<Character>();
        for (int i = 0; i < input.length; i++) {
            list.add(input[i]);
        }
        return list;
    }
    
    @Test(timeout=SECOND)
    public void testSimpleUsage() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }

        IList<Integer> top = Searcher.topKSort(5, list);
        assertEquals(5, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(15 + i, top.get(i));
        }
    }
    
    @Test(timeout=SECOND)
    public void testCase1() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i%2==0?i/2:i);
        }
        IList<Integer> top = Searcher.topKSort(7, list);
        assertEquals(7, top.size());
        checkInOrder(top);
    }
    
    @Test(timeout=SECOND)
    public void testCase2() {
        IList<Integer> list = makeList(new int[]{1, 3, 5, 6, 4, 2, 0, -1, -3, -4, -2});
        IList<Integer> top = Searcher.topKSort(9, list);
        assertEquals(9, top.size());
        checkInOrder(top);
    }
    
    @Test(timeout=SECOND)
    public void testCase3() {
        IList<Character> list = makeList("XYZabcdefgxyzABCEDFG".toCharArray());
        IList<Character> top = Searcher.topKSort(10, list);
        assertEquals(10, top.size());
        char[] arr = "xyzABCEDFG".toCharArray();
        Arrays.sort(arr);
        for (int i = 0; i < 10; i++) {
            assertEquals(arr[i], top.get(i));
        }
    }
    
    @Test(timeout=SECOND)
    public void testCase4() {
        IList<Character> list = makeList("<>,./?;':\"{}[]\\_+-=~!@#$%^&*()".toCharArray());
        IList<Character> top = Searcher.topKSort(15, list);
        assertEquals(15, top.size());
        char[] arr = "_+-=~!@#$%^&*()".toCharArray();
        Arrays.sort(arr);
        for (int i = 0; i < 15; i++) {
            assertEquals(arr[i], top.get(i));
        }
    }
}
