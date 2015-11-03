package io.mixrad.mixradiosdk.model;

/**
 * Created by RichardW on 04/03/15.
 */
public class ListResponse extends Response{

    /** Gets the start index the API call was asked for (when appropriate). */
    public int startIndex;
    /** Gets the items per page the API call was asked for (when appropriate). */
    public int itemsPerPage;
    /** Gets the total results available (when appropriate). */
    public int totalResults;

}
