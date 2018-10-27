package com.nikhil;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DirectionTest {


    @Test
    void siblingOf() {

        // all positive test cases only

        assertTrue(Direction.TOP.siblingOf(Direction.TOP_LEFT));
        assertTrue(Direction.TOP.siblingOf(Direction.TOP_RIGHT));

        assertTrue(Direction.TOP_RIGHT.siblingOf(Direction.TOP));
        assertTrue(Direction.TOP_RIGHT.siblingOf(Direction.RIGHT));

        assertTrue(Direction.RIGHT.siblingOf(Direction.TOP_RIGHT));
        assertTrue(Direction.RIGHT.siblingOf(Direction.BOTTOM_RIGHT));

        assertTrue(Direction.BOTTOM_RIGHT.siblingOf(Direction.RIGHT));
        assertTrue(Direction.BOTTOM_RIGHT.siblingOf(Direction.BOTTOM));

        assertTrue(Direction.BOTTOM.siblingOf(Direction.BOTTOM_RIGHT));
        assertTrue(Direction.BOTTOM.siblingOf(Direction.BOTTOM_LEFT));

        assertTrue(Direction.BOTTOM_LEFT.siblingOf(Direction.BOTTOM));
        assertTrue(Direction.BOTTOM_LEFT.siblingOf(Direction.LEFT));

        assertTrue(Direction.LEFT.siblingOf(Direction.BOTTOM_LEFT));
        assertTrue(Direction.LEFT.siblingOf(Direction.TOP_LEFT));

        assertTrue(Direction.TOP_LEFT.siblingOf(Direction.LEFT));
        assertTrue(Direction.TOP_LEFT.siblingOf(Direction.TOP));

    }

    @Test
    void getOppositeDirection() {

        assertEquals(Direction.TOP.getOppositeDirection(),Direction.BOTTOM);
        assertEquals(Direction.TOP_LEFT.getOppositeDirection(),Direction.BOTTOM_RIGHT);
        assertEquals(Direction.TOP_RIGHT.getOppositeDirection(),Direction.BOTTOM_LEFT);
        assertEquals(Direction.BOTTOM.getOppositeDirection(),Direction.TOP);
        assertEquals(Direction.BOTTOM_LEFT.getOppositeDirection(),Direction.TOP_RIGHT);
        assertEquals(Direction.BOTTOM_RIGHT.getOppositeDirection(),Direction.TOP_LEFT);
        assertEquals(Direction.LEFT.getOppositeDirection(),Direction.RIGHT);
        assertEquals(Direction.RIGHT.getOppositeDirection(),Direction.LEFT);

    }
}