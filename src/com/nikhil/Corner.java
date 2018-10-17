package com.nikhil;

import java.util.LinkedList;

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
    private IntersectionOption intersectionOption;
    private Location target;

    /** Simple data holder for storing single intersections by scanning the area covered by this {@link Corner} */
    private LinkedList<IntersectionOption> singleIntersections = new LinkedList<>();

    /** Stores the intersection occurring from the vertical word of a double intersection */
    private IntersectionOption fromVerticalWord = null;

    /** Stores the intersection occurring from the horizontal word of a double intersection */
    private IntersectionOption fromHorizontalWord = null;


    /**
     * Creates a corner from two placed words
     * @param initialPoint the point of intersection in grid
     * @param direction the {@link Direction} which this corner is facing
     * @param limitingRow the vertical extent of this corner's area
     * @param limitingColumn the horizontal extent of this corner's area
     * @param intersectionOption the intersection causing this corner to happen. Both the words of this intersection
     *                           must be placed
     */
    public Corner(Location initialPoint, Direction direction, int limitingRow, int limitingColumn, IntersectionOption intersectionOption) {
        if(!intersectionOption.source.placed || !intersectionOption.crossing.placed){
            throw new RuntimeException("One of the words of the intersection is not placed while creating a corner");
        }
        this.initialPoint = initialPoint;
        this.direction = direction;
        this.limitingRow = limitingRow;
        this.limitingColumn = limitingColumn;
        this.intersectionOption = intersectionOption;
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

    public LinkedList<IntersectionOption> getSingleIntersections() {
        return singleIntersections;
    }

    public IntersectionOption getFromVerticalWord() {
        return fromVerticalWord;
    }

    public IntersectionOption getFromHorizontalWord() {
        return fromHorizontalWord;
    }

    /**
     * Finds all intersections at the current target in the area and stores them in a singleIntersections
     * @return true if the current target retrieved a double intersection. Places those intersections in
     * fromVerticalWord and fromHorizontalWord
     */
    public boolean findPossibleIntersections(){

        // TODO introduce callbacks on finding double intersections.

        // TODO search for index at that point and retrieve all intersections Options on that index
        if(intersectionOption.source.vertical){

        }else{

        }
        return false;
    }
}
