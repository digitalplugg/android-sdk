package io.mixrad.mixradiosdk.model;

/**
 * Created by RichardW on 04/03/15.
 */
public enum SortOrder {

    /** Items are ordered ascending (default). */
    ASCEND ("ascend"),
    /** Items are ordered descending. */
    DESCEND ("descend");

    private final String name;

    SortOrder(String s) {
        name = s;
    }

    public boolean equalsName(String otherName){
        return (otherName != null) && name.equals(otherName);
    }

    public String toString(){
        return name;
    }

}
