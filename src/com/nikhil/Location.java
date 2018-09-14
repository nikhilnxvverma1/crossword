package com.nikhil;

/** Data holder for storing grid location */
public class Location {
    int row;
    int col;

    public Location(){}

    public Location(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Checks if another location lies within span of this location going in right or down direction
     * @param other the location that should be contained within a specified distance
     * @param span the distance within which the location should overlap
     * @param vertical weather to check vertically or horizontally for location overlap
     * @return true if other location is situated within span of this location, false otherwise
     */
    public boolean containsInSpan(Location other, int span, boolean vertical){

        if(vertical){ // going down
            return this.row<=other.row && other.row<=(this.row+span);
        }else{ // going right
            return this.col<=other.col && other.col<=(this.col+span);
        }
    }
}
