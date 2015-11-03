package io.mixrad.mixradiosdk.model;

/**
 * Created by RichardW on 04/03/15.
 */
public enum OrderBy {

    /** Items are ordered by relevance (default). */
    RELEVANCE ("relevance"),
    /** Items are ordered by name. */
    NAME ("name"),
    /** Items are ordered by release date. */
    RELEASE_DATE ("releasedate");

    private final String name;

    OrderBy(String s) {
        name = s;
    }

    public boolean equalsName(String otherName){
        return (otherName != null) && name.equals(otherName);
    }

    public String toString(){
        return name;
    }

}


