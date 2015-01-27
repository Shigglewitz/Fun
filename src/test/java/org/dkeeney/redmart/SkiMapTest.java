package org.dkeeney.redmart;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Queue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatchman;
import org.junit.runners.model.FrameworkMethod;

public class SkiMapTest {
    private static final String TEST_MAP = "/testmap.txt";
    private static final String REAL_MAP = "/map.txt";

    @Rule public TestWatchman watchman = new TestWatchman() {
        @Override
        public void starting(FrameworkMethod method) {
            System.out.println("***");
            System.out.println("Starting: " + method.getName());
            System.out.println("***\n");
        }

        @Override
        public void failed(Throwable e, FrameworkMethod method) {
            System.out.println("***");
            System.out.println(method.getName() + " failed!");
            System.out.println("***\n");
        }

        @Override
        public void succeeded(FrameworkMethod method) {
            System.out.println("***");
            System.out.println(method.getName() + " success!");
            System.out.println("***\n");
        }
    };

    @Test
    public void testConstructor() {
        SkiMap sm = new SkiMap(TEST_MAP);

        assertEquals(4, sm.getHeight());
        assertEquals(4, sm.getWidth());
        assertEquals(3, sm.getHeight(3, 0));

        sm = new SkiMap(REAL_MAP);

        assertEquals(1_000, sm.getHeight());
        assertEquals(1_000, sm.getWidth());
        assertEquals(201, sm.getHeight(1, 0));
        assertEquals(1038, sm.getHeight(0, 1));
    }

    @Test
    public void testInit() {
        SkiMap sm = new SkiMap(TEST_MAP);

        Queue<SkiSlope> queue = sm.init();

        for (int i = 0; i < sm.getHeight(); i++) {
            assertFalse("Element " + 0 + ", " + i + " can left", sm.getSlope(0,
                    i).canLeft());
            assertFalse("Element " + (sm.getWidth() - 1) + ", " + i
                    + " can right", sm.getSlope(sm.getWidth() - 1, i)
                    .canRight());
        }

        for (int i = 0; i < sm.getWidth(); i++) {
            assertFalse("Element " + i + ", " + 0 + " can up", sm
                    .getSlope(i, 0).canUp());
            assertFalse("Element " + i + ", " + (sm.getHeight() - 1)
                    + " can down", sm.getSlope(i, sm.getHeight() - 1).canDown());
        }

        int[] solvedX = {
                2, 3, 0, 3, 0
        };
        int[] solvedY = {
                3, 0, 1, 1, 3
        };

        for (int i = 0; i < solvedX.length; i++) {
            assertTrue("Element " + solvedX[i] + ", " + solvedY[i]
                    + " is not solved!", sm.getSlope(solvedX[i], solvedY[i])
                    .isSolved());
        }

        assertEquals(solvedX.length, queue.size());
    }

    @Test
    public void testProcessNeighbor() {
        SkiMap sm = new SkiMap(TEST_MAP);

        sm.init();

        SkiSlope slope = sm.getSlope(2, 2);
        assertEquals("Wrong slope!", 2, slope.getHeight());
        sm.processNeighbor(slope);
        assertTrue(slope.isSolved());
    }

    @Test
    public void testSolve() {
        SkiMap sm = new SkiMap(TEST_MAP);

        SkiSlope answer = sm.solve();

        assertEquals(2, answer.getX());
        assertEquals(1, answer.getY());
        assertEquals(8, answer.getGreatestDrop());
        assertEquals(5, answer.getGreatestLength());

        SkiSlope bestSource = sm.getSlope(2, 1);
        assertEquals(8, bestSource.getGreatestDrop());
        assertEquals(5, bestSource.getGreatestLength());

        System.out.println("*************");
        System.out.println("ANSWER");
        System.out.println("*************");
        System.out.println("DISTANCE: " + answer.getGreatestLength());
        System.out.println("DESCENT:  " + answer.getGreatestDrop());
    }

    @Test
    public void testRealSolve() {
        SkiMap sm = new SkiMap(REAL_MAP);
        SkiSlope answer = sm.solve();

        System.out.println("*************");
        System.out.println("ANSWER");
        System.out.println("*************");
        System.out.println("DISTANCE: " + answer.getGreatestLength());
        System.out.println("DESCENT:  " + answer.getGreatestDrop());
    }
}
