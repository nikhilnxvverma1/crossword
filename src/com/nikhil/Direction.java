package com.nikhil;

/**
 * Enumerates 8 clockwise directions
 */
public enum Direction {
    TOP,
    TOP_RIGHT,
    RIGHT,
    BOTTOM_RIGHT,
    BOTTOM,
    BOTTOM_LEFT,
    LEFT,
    TOP_LEFT;

    /**
     * Checks if this direction is adjacent to the given direction
     * @param direction one of the 8 directions
     * @return true if this direction is adjacent or equal to the argument direction
     */
    public boolean siblingOf(Direction direction){
        switch (direction){

            case TOP:
                return this==TOP || this==TOP_RIGHT || this==TOP_LEFT;
            case TOP_RIGHT:
                return this==TOP_RIGHT || this==TOP || this==RIGHT;
            case RIGHT:
                return this==RIGHT || this==TOP_RIGHT || this==BOTTOM_RIGHT;
            case BOTTOM_RIGHT:
                return this==BOTTOM_RIGHT || this==BOTTOM || this==RIGHT;
            case BOTTOM:
                return this==BOTTOM || this==BOTTOM_RIGHT || this==BOTTOM_LEFT;
            case BOTTOM_LEFT:
                return this==BOTTOM_LEFT || this==BOTTOM || this==LEFT;
            case LEFT:
                return this==LEFT || this==TOP_LEFT || this==BOTTOM_LEFT;
            case TOP_LEFT:
                return this==TOP_LEFT || this==TOP || this==LEFT;
        }
        return false;
    }

    /** @return Opposite direction to this direction */
    public Direction getOppositeDirection(){
        switch(this){

            case TOP:
                return BOTTOM;
            case TOP_RIGHT:
                return BOTTOM_LEFT;
            case RIGHT:
                return LEFT;
            case BOTTOM_RIGHT:
                return TOP_LEFT;
            case BOTTOM:
                return TOP;
            case BOTTOM_LEFT:
                return TOP_RIGHT;
            case LEFT:
                return RIGHT;
            case TOP_LEFT:
                return BOTTOM_RIGHT;
        }
        return null;
    }
}
