package com.nikhil;

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
            return null;
        }

        // return false, if source words of the two intersection options is parallel
        if( this.source.vertical == perpendicular.source.vertical){
            return null;
        }

        // we need to find the coordinate of the both the crossing words based on the placement of their source words
        Location thisCrossingLocation = this.projectedLocationOfCrossingWord();
        Location otherCrossingLocation = perpendicular.projectedLocationOfCrossingWord();


        // TODO we don't know if they (crossing words) are definitively horizontal or not

        // this crossing word is horizontal and the perpendicular is vertical
        if(thisCrossingLocation.containsInSpan(otherCrossingLocation,this.crossing.name.length(),!this.source.vertical) &&
                otherCrossingLocation.containsInSpan(thisCrossingLocation,perpendicular.crossing.name.length(),!perpendicular.source.vertical)
        ){
            return new Location(thisCrossingLocation.row,otherCrossingLocation.col);

        }
        // perpendicular word is horizontal and this crossing word is vertical
        else if(otherCrossingLocation.containsInSpan(thisCrossingLocation,perpendicular.crossing.name.length(),!perpendicular.source.vertical) &&
                thisCrossingLocation.containsInSpan(otherCrossingLocation,this.crossing.name.length(),!this.source.vertical)
        ){
            return new Location(otherCrossingLocation.row,thisCrossingLocation.col);
        }
        // no intersection
        else{
            return null;
        }
    }

    /** Computes the location of the crossing word based on the location of the source word */
    public Location projectedLocationOfCrossingWord(){
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
}
