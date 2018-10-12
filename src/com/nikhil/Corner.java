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
        this.reset();
    }

    /**
     * If the area to be scanned is less than 2X2 it is considered small enough to give any useful intersections
     * @return true if any dimension is less than  or equal to 2
     */
    public boolean isTooSmall(){
        int across = Math.abs(limitingColumn - initialPoint.col);
        int down = Math.abs(limitingRow - initialPoint.row);
        return across<=2 && down<=2;
    }

    /**
     * Moves the target point one step ahead row-wise
     * @return true if the final row and column limit have been reached.
     */
    public boolean moveToNextIfPossible(){

        switch (this.direction){

            case TOP_RIGHT:
                if(++this.target.col>=limitingColumn){
                    this.target.col = this.initialPoint.col + 1;
                    if(--this.target.row<=limitingRow){
                        this.reset();
                        return true;
                    }
                }
                break;
            case BOTTOM_RIGHT:
                if(++this.target.col>=limitingColumn){
                    this.target.col = this.initialPoint.col + 1;
                    if(++this.target.row>=limitingRow){
                        this.reset();
                        return true;
                    }
                }
                break;
            case BOTTOM_LEFT:
                if(--this.target.col<=limitingColumn){
                    this.target.col = this.initialPoint.col - 1;
                    if(++this.target.row>=limitingRow){
                        this.reset();
                        return true;
                    }
                }
                break;
            case TOP_LEFT:
                if(--this.target.col<=limitingColumn){
                    this.target.col = this.initialPoint.col - 1;
                    if(--this.target.row>=limitingRow){
                        this.reset();
                        return true;
                    }
                }
                break;
        }
        return false;
    }

    /** Resets target position back to initial point */
    public void reset(){
        this.target = this.initialPoint.clone();
    }

    public Location getTarget() {
        return target;
    }
}
