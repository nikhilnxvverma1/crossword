package com.nikhil;

/**
 * Corners are used to look for a target point within a defined region. They initiate at an intersection point
 * and move(scan) in their own {@link Direction}
 */
public class Corner {

    /** Initial point is the point of intersection between two words */
    private Location initialPoint;
    private Direction direction;
    private int limitingRow;
    private int limitingColumn;
    private Location target;

    public Corner(Location initialPoint, Direction direction, int limitingRow, int limitingColumn) {
        this.initialPoint = initialPoint;
        this.direction = direction;
        this.limitingRow = limitingRow;
        this.limitingColumn = limitingColumn;
        this.target = this.initialPoint.clone();
    }

    public boolean isTooSmall(){
        int across = Math.abs(limitingColumn - initialPoint.col);
        int down = Math.abs(limitingRow - initialPoint.row);
        return across==2 && down==2;
    }

}
