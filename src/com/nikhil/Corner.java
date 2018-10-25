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

    public Direction getDirection() {
        return direction;
    }

    /**
     * Finds all intersections at the current target in the area and stores them in a singleIntersections
     * @param doubleIntersectionHandler callback for handling a double intersection
     * @return true if the current target retrieved a double intersection and it was accepted by the handler.
     */
    public boolean findPossibleIntersections(DoubleIntersectionFound doubleIntersectionHandler){

        // search for index at that point and retrieve all intersections Options on that index
        int projectedIndexOnSource = intersectionOption.source.projectingIndex(target);
        LinkedList<IntersectionOption> sourceIntersections = intersectionOption.source.getAvailableIntersectionOptionsAt(projectedIndexOnSource);

        int projectedIndexOnCrossing = intersectionOption.crossing.projectingIndex(target);
        LinkedList<IntersectionOption> crossingIntersections = intersectionOption.crossing.getAvailableIntersectionOptionsAt(projectedIndexOnCrossing);


        //check to see if any of the source intersections are intersecting with a crossing intersection
        for(IntersectionOption sourceIntersection : sourceIntersections){
            for(IntersectionOption crossingIntersection : crossingIntersections){

                // double intersection check
                if(sourceIntersection.intersectsWith(crossingIntersection)!=null){

                    // if double intersection is accepted, return true to end any further checks
                    if(doubleIntersectionHandler.onDoubleIntersection(this,sourceIntersection,crossingIntersection)){
                        return true;
                    }

                }

            }
        }

        // by this point we know that the double intersections were either not found, or were rejected
        // we will just add the single intersections to the list

        for(IntersectionOption sourceIntersection: sourceIntersections){
            addToSingleIntersectionsIfQualifies(sourceIntersection);
        }

        for(IntersectionOption crossingIntersection: crossingIntersections){
            addToSingleIntersectionsIfQualifies(crossingIntersection);
        }

        return false;
    }

    /**
     * Adds an intersection option to the list of single intersections provided the crossing word of which is
     * overlapping the current target.
     * @param intersectionOption the intersection which has no double intersection or was rejected from
     *                           double intersection
     * @return true if the list added the intersection, false if the list already contained the intersection
     * or the crossing word of the intersection option was not overlapping with the current target
     */
    private boolean addToSingleIntersectionsIfQualifies(IntersectionOption intersectionOption){

        // check to see if the crossing word of this intersection option touches the current target
        // here we will assume that the crossing word is unplaced
        Location crossingLocation = intersectionOption.projectedLocationOfCrossingWord();
        int row = crossingLocation.row;
        int col = crossingLocation.col;
        int length = intersectionOption.crossing.name.length();
        boolean vertical = !intersectionOption.source.vertical;

        boolean overlapsTarget = false;
        if(vertical){
            overlapsTarget = (target.col==col && // same column
                    target.row>=row && target.row< (row+length)); // within span
        }else{
            overlapsTarget = (target.row==row && // same row
                    target.col>=col && target.col< (col+length)); // within span

        }

        // if this intersection overlaps target, and single intersections don't contain this, then add
        if(overlapsTarget && !singleIntersections.contains(intersectionOption)){
            singleIntersections.add(intersectionOption);
            return true;
        }else{
            return false;
        }
    }

    /** Functional interface for handling a double intersection */
    interface DoubleIntersectionFound{
        /**
         * Callback on finding a double intersection. Source word of both argument intersection options is placed
         * @param fromSourceWord Stores the intersection occurring from the source word of a double intersection
         * @param fromCrossingWord Stores the intersection occurring from the crossing word of a double intersection
         * @return true if the caller has to break for a successful placement of this double intersection,
         * if false is returned, caller will resume and look for another double intersection.
         */
        boolean onDoubleIntersection(Corner corner,IntersectionOption fromSourceWord,IntersectionOption fromCrossingWord );
    }
}
