package org.dkeeney.redmart;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

public class SkiMapTest {
    @Test
    public void testConstructor() throws IOException {
        SkiMap sm = new SkiMap("/testmap.txt");

        assertEquals(4, sm.getHeight());
        assertEquals(4, sm.getWidth());
        assertEquals(3, sm.getHeight(3, 0));

        sm = new SkiMap("/map.txt");

        assertEquals(1_000, sm.getHeight());
        assertEquals(1_000, sm.getWidth());
        assertEquals(201, sm.getHeight(1, 0));
        assertEquals(1038, sm.getHeight(0, 1));
    }
}
