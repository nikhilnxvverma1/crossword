package com.nikhil;

import java.util.LinkedList;
import java.util.List;

/**
 * Storing intersection point data which is also responsible for computing projected intersection points.
 * Here the source word is always considered to be placed
 * Created by nikhilverma on 7/9/18.
 */
public class IntersectionOption {
    /** The placed word */
    Word source;
    /** Index of the source word at which the intersection is taking place */
    int sourceIndex;
    /** The unplaced word that is intersecting with the source word */
    Word crossing;
    /** Index of the crossing word at which the intersection is taking place */
    int crossingIndex;

    public IntersectionOption(Word source, int sourceIndex, Word crossing, int crossingIndex) {
        this.source = source;
        this.sourceIndex = sourceIndex;
        this.crossing = crossing;
        this.crossingIndex = crossingIndex;
    }

    @Override
    public String toString() {
        return "IntersectionOption{" +
                "source=" + source.name +
                ", sourceIndex=" + sourceIndex +
                ", crossing=" + crossing.name +
                ", crossingIndex=" + crossingIndex +
                '}';
    }

    /**
     * Checks if the crossing word of the two intersection options, feasibly intersect
     * with each other at right angles or not. This does not check if the intersecting
     * crossing words are colliding with other words in the grid or not. The source word
     * of both the intersection option's is assumed to be placed but is not required to
     * be intersecting one another.
     * @param perpendicular the other intersection option whose source word is perpendicular
     *                      to this intersection option's source word, whose crossing word will be
     *                      checked against this intersection option's crossing word
     * @return location of the intersection point if the crossing of two intersection options crosses, null otherwise.
     */
    public Location intersectsWith(IntersectionOption perpendicular){

        // if either source words is not placed, preemptively return false
        if( !this.source.placed || !perpendicular.source.placed){
            throw new RuntimeException("One or more source words is not placed while finding intersection " +
                    "with another intersection option");
        }

        // return false, if source words of the two intersection options is parallel
        if( this.source.vertical == perpendicular.source.vertical){
            return null;
        }

        // we need to find the coordinate of the both the crossing words based on the placement of their source words
        Location thisCrossingLocation = this.projectedLocationOfCrossingWord();
        Location otherCrossingLocation = perpendicular.projectedLocationOfCrossingWord();

        // find out the common intersection point, letter at that intersection for both the crossing words
        Location intersectionPoint;
        char thisCrossingWordsLetter='0';
        char otherCrossingWordsLetter='0';

        // check if these crossing words mutually lie in the span of each other
        // note : For alignment of crossing word, we always check the alignment of the source word because
        // crossing word is most likely unplaced
        if(thisCrossingLocation.containsInSpan(otherCrossingLocation,this.crossing.name.length(),!this.source.vertical) &&
                otherCrossingLocation.containsInSpan(thisCrossingLocation,perpendicular.crossing.name.length(),!perpendicular.source.vertical)
        ){
            // this crossing word is vertical and the perpendicular is horizontal
            if(!this.source.vertical){
                intersectionPoint = new Location(otherCrossingLocation.row,thisCrossingLocation.col);
                thisCrossingWordsLetter = this.crossing.name.charAt(Math.abs(otherCrossingLocation.row - thisCrossingLocation.row));
                otherCrossingWordsLetter = perpendicular.crossing.name.charAt(Math.abs(thisCrossingLocation.col - otherCrossingLocation.col));
            }
            // perpendicular word is vertical and this crossing word is horizontal
            else{
                intersectionPoint = new Location(thisCrossingLocation.row,otherCrossingLocation.col);
                thisCrossingWordsLetter = this.crossing.name.charAt(Math.abs(otherCrossingLocation.col - thisCrossingLocation.col));
                otherCrossingWordsLetter = perpendicular.crossing.name.charAt(Math.abs(thisCrossingLocation.row - otherCrossingLocation.row));
            }
        }
        // no intersection
        else{
            intersectionPoint =null;
        }

        if(intersectionPoint!=null && thisCrossingWordsLetter == otherCrossingWordsLetter &&
                !intersectionPoint.touches(this.source) && !intersectionPoint.touches(perpendicular.source)){
            return intersectionPoint;
        }else{
            return null;
        }
    }

    /** Computes the location of the crossing word based on the location of the source word */
    public Location projectedLocationOfCrossingWord(){

        if(!source.placed){
            throw new RuntimeException("Source word is not placed in finding projected location of crossing word");
        }

        Location location = new Location();

        // projected row
        if(this.source.vertical){
            location.row = this.source.row + this.sourceIndex;
        }else{
            location.row = this.source.row - this.crossingIndex;
        }

        // projected column
        if(this.source.vertical){
            location.col = this.source.col - this.crossingIndex;
        }else{
            location.col = this.source.col + this.sourceIndex;
        }

        return location;
    }

    /**
     * Finds the intersection point of the two words. Source word is assumed to be placed
     * @return The intersection point depending on the position, alignment and sourceIndex of the source word
     */
    public Location getIntersectionPoint(){
        if(!source.placed){
            throw new RuntimeException("Source word is not placed in the computation of intersection point");
        }

        Location intersectionPoint = new Location();
        //vertical source word
        if(this.source.vertical){
            intersectionPoint.row = this.source.row + this.sourceIndex;
            intersectionPoint.col = this.source.col;
        }
        // horizontal source word
        else{
            intersectionPoint.row = this.source.row;
            intersectionPoint.col = this.source.col + this.sourceIndex;
        }

        return intersectionPoint;
    }

    /**
     * Finds all corners based on the alignment and placement of the source word (which is assumed to be placed)
     * @return A list of corners made by this intersection option.
     */
    public LinkedList<Corner> computeCorners(){
        if(!source.placed){
            throw new RuntimeException("Source word is not placed while finding corners");
        }

        LinkedList<Corner> cornerList = new LinkedList<>();

        Location projectedCrossingLocation = projectedLocationOfCrossingWord();
        Location intersectionPoint = getIntersectionPoint();

        if(sourceIndex==0){ // beginning of source word
            if(crossingIndex==0){ // beginning of crossing word

                 if(source.vertical){
                     Corner bottomRight = new Corner(intersectionPoint,
                             Direction.BOTTOM_RIGHT,
                             source.row + source.name.length(),
                             projectedCrossingLocation.col + crossing.name.length());
                     cornerList.add(bottomRight);
                 }else{
                     Corner bottomRight = new Corner(intersectionPoint,
                             Direction.BOTTOM_RIGHT,
                             projectedCrossingLocation.row + crossing.name.length(),
                             source.col + source.name.length());
                     cornerList.add(bottomRight);
                 }

            }else if(crossingIndex== (crossing.name.length()-1)){ // end of crossing word

                if(source.vertical){
                    Corner bottomLeft = new Corner(intersectionPoint,
                            Direction.BOTTOM_LEFT,
                            source.row + source.name.length(),
                            projectedCrossingLocation.col);

                    cornerList.add(bottomLeft);
                }else{
                    Corner topRight = new Corner(intersectionPoint,
                            Direction.TOP_RIGHT,
                            projectedCrossingLocation.row,
                            source.col + source.name.length());
                    cornerList.add(topRight);
                }
            }else{ // in the middle of the crossing word
                if(source.vertical){
                    Corner bottomLeft = new Corner(intersectionPoint,
                            Direction.BOTTOM_LEFT,
                            source.row + source.name.length(),
                            projectedCrossingLocation.col);

                    cornerList.add(bottomLeft);

                    Corner bottomRight = new Corner(intersectionPoint,
                            Direction.BOTTOM_RIGHT,
                            source.row + source.name.length(),
                            projectedCrossingLocation.col + crossing.name.length());
                    cornerList.add(bottomRight);
                }else{
                    Corner topRight = new Corner(intersectionPoint,
                            Direction.TOP_RIGHT,
                            projectedCrossingLocation.row,
                            source.col + source.name.length());
                    cornerList.add(topRight);

                    Corner bottomRight = new Corner(intersectionPoint,
                            Direction.BOTTOM_RIGHT,
                            projectedCrossingLocation.row + crossing.name.length(),
                            source.col + source.name.length());
                    cornerList.add(bottomRight);
                }

            }
        }else if(sourceIndex== (source.name.length()-1)){ // end of source word
            if(crossingIndex==0){ // beginning of crossing word
                if(source.vertical){
                    Corner topRight = new Corner(intersectionPoint,
                            Direction.TOP_RIGHT,
                            source.row,
                            projectedCrossingLocation.col+crossing.name.length());
                    cornerList.add(topRight);
                }else{
                    Corner bottomLeft = new Corner(intersectionPoint,
                            Direction.BOTTOM_LEFT,
                            projectedCrossingLocation.row+crossing.name.length(),
                            source.col);
                    cornerList.add(bottomLeft);
                }

            }else if(crossingIndex== (crossing.name.length()-1)){ // end of crossing word
                if(source.vertical){
                    Corner topLeft = new Corner(intersectionPoint,
                            Direction.TOP_LEFT,
                            source.row,
                            projectedCrossingLocation.col);
                    cornerList.add(topLeft);
                }else{
                    Corner topLeft = new Corner(intersectionPoint,
                            Direction.TOP_LEFT,
                            projectedCrossingLocation.row,
                            source.col);
                    cornerList.add(topLeft);
                }
            }else{ // in the middle of the crossing word
                if(source.vertical){
                    Corner topLeft = new Corner(intersectionPoint,
                            Direction.TOP_LEFT,
                            source.row,
                            projectedCrossingLocation.col);
                    cornerList.add(topLeft);

                    Corner topRight = new Corner(intersectionPoint,
                            Direction.TOP_RIGHT,
                            source.row,
                            projectedCrossingLocation.col+crossing.name.length());
                    cornerList.add(topRight);
                }else {
                    Corner topLeft = new Corner(intersectionPoint,
                            Direction.TOP_LEFT,
                            projectedCrossingLocation.row,
                            source.col);
                    cornerList.add(topLeft);

                    Corner bottomLeft = new Corner(intersectionPoint,
                            Direction.BOTTOM_LEFT,
                            projectedCrossingLocation.row+crossing.name.length(),
                            source.col);
                    cornerList.add(bottomLeft);
                }
            }
        }else{ // in the middle of the source word
            if(crossingIndex==0){ // beginning of crossing word
                if(source.vertical){
                    Corner topRight = new Corner(intersectionPoint,
                            Direction.TOP_RIGHT,
                            source.row,
                            projectedCrossingLocation.col+crossing.name.length());
                    cornerList.add(topRight);

                    Corner bottomRight = new Corner(intersectionPoint,
                            Direction.BOTTOM_RIGHT,
                            source.row+source.name.length(),
                            projectedCrossingLocation.col+crossing.name.length());
                    cornerList.add(bottomRight);
                }else{
                    Corner bottomLeft = new Corner(intersectionPoint,
                            Direction.BOTTOM_LEFT,
                            projectedCrossingLocation.row+crossing.name.length(),
                            source.col);
                    cornerList.add(bottomLeft);

                    Corner bottomRight = new Corner(intersectionPoint,Direction.BOTTOM_RIGHT,
                            projectedCrossingLocation.col+crossing.name.length(),
                            source.col+source.name.length());
                    cornerList.add(bottomRight);
                }
            }else if(crossingIndex== (crossing.name.length()-1)){ // end of crossing word
                if(source.vertical){
                    Corner topLeft = new Corner(intersectionPoint, Direction.TOP_LEFT,
                           source.row,
                            projectedCrossingLocation.col);
                    cornerList.add(topLeft);

                    Corner bottomLeft = new Corner(intersectionPoint,Direction.BOTTOM_LEFT,
                            source.row+source.name.length(),
                            projectedCrossingLocation.col);
                    cornerList.add(bottomLeft);
                }else{
                    Corner topLeft = new Corner(intersectionPoint,Direction.TOP_LEFT,
                            projectedCrossingLocation.row,
                            source.col);
                    cornerList.add(topLeft);

                    Corner topRight = new Corner(intersectionPoint,Direction.TOP_RIGHT,
                            projectedCrossingLocation.row,
                            source.col+source.name.length());
                    cornerList.add(topRight);
                }
            }else{ // in the middle of the crossing word
                if(source.vertical){
                    Corner topRight = new Corner(intersectionPoint,
                            Direction.TOP_RIGHT,
                            source.row,
                            projectedCrossingLocation.col+crossing.name.length());
                    cornerList.add(topRight);

                    Corner bottomRight = new Corner(intersectionPoint,
                            Direction.BOTTOM_RIGHT,
                            source.row+source.name.length(),
                            projectedCrossingLocation.col+crossing.name.length());
                    cornerList.add(bottomRight);

                    Corner topLeft = new Corner(intersectionPoint, Direction.TOP_LEFT,
                            source.row,
                            projectedCrossingLocation.col);
                    cornerList.add(topLeft);

                    Corner bottomLeft = new Corner(intersectionPoint,Direction.BOTTOM_LEFT,
                            source.row+source.name.length(),
                            projectedCrossingLocation.col);
                    cornerList.add(bottomLeft);
                }else{
                    Corner topLeft = new Corner(intersectionPoint,Direction.TOP_LEFT,
                            projectedCrossingLocation.row,
                            source.col);
                    cornerList.add(topLeft);

                    Corner topRight = new Corner(intersectionPoint,Direction.TOP_RIGHT,
                            projectedCrossingLocation.row,
                            source.col+source.name.length());
                    cornerList.add(topRight);

                    Corner bottomRight = new Corner(intersectionPoint,Direction.BOTTOM_RIGHT,
                            projectedCrossingLocation.col+crossing.name.length(),
                            source.col+source.name.length());
                    cornerList.add(bottomRight);

                    Corner bottomLeft = new Corner(intersectionPoint,
                            Direction.BOTTOM_LEFT,
                            projectedCrossingLocation.row+crossing.name.length(),
                            source.col);
                    cornerList.add(bottomLeft);
                }
            }
        }

        return cornerList;
    }

    /**
     * Places the crossing word with respect to source word. Source word is assumed to be placed
     * @param wordList list of words in the grid
     * @return List of acute corners formed as a result of this placement
     */
    public List<Corner> placeCrossingWord(List<Word> wordList){

        if(!source.placed){
            throw new RuntimeException("Source word is not placed while placing crossing word");
        }

        // place crossing word
        Location crossingLocation = this.projectedLocationOfCrossingWord();
        this.crossing.placeAt(crossingLocation.row,crossingLocation.col,!this.source.vertical);

        // find and return all corners with the placed words in the list
        return this.crossing.findAllCorners(wordList);
    }
}
