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

    /**
     * Checks if this location lies in the touching/overlapping proximity to the given word
     * @param word a placed word, with defined coordinates and alignment
     * @return True if location is touching the word or word is unplaced.
     * False if there is a gap or this location is situated diagonally at the tip of the word.
     */
    public boolean touches(Word word){
        if(!word.placed){
            return false;
        }

        if(word.vertical){
            return (this.col>=(word.col-1) && this.col<=(word.col+1) && // side to side
                    this.row>=word.row && this.row< (word.row+word.name.length())) || // within span
                    (this.row == (word.row-1) && this.col == word.col)|| // above
                    (this.row == (word.row+word.name.length()) && this.col == word.col); // below
        }else{
            return (this.row>=(word.row-1) && this.row<=(word.row+1) && // above and below
                    this.col>=word.col && this.col< (word.col+word.name.length())) || // within span
                    (this.row == word.row && this.col == (word.col-1))|| // left
                    (this.row == word.row && this.col == (word.col + word.name.length())); // right
        }
    }

    @Override
    public String toString() {
        return "Location(" + row +", " + col + ")";
    }
}
