package io.mixrad.mixradiosdk.model;

/**
 * Created by RichardW on 04/03/15.
 */
public enum Category {

        /** Denotes an Artist. */
        ARTIST ("artist"),
        /** Denotes an album product */
        ALBUM ("album"),
        /** Denotes a single product - typically 2 or 3 tracks bundled together. */
        SINGLE ("single"),
        /** Denotes a single track product. */
        TRACK ("track");


    private final String name;

    Category(String s) {
        name = s;
    }

    public boolean equalsName(String otherName){
        return (otherName != null) && name.equals(otherName);
    }

    public String toString(){
        return name;
    }

}
