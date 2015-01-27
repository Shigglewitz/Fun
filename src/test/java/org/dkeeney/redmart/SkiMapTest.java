package org.dkeeney.redmart;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SkiMapTest {
    private static final String TEST_MAP = "/testmap.txt";
    private static final String REAL_MAP = "/map.txt";

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

        sm.init();

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
    }
}
