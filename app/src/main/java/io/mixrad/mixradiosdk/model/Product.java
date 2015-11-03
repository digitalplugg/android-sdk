package io.mixrad.mixradiosdk.model;

import java.util.List;

/**
 * Created by RichardW on 04/03/15.
 */
public class Product extends MusicItem {

    /** The product’s category - i.e., whether it is an album, single, or track. */
    public Category category;
    /** The product’s genres. */
    public List<Genre> genres;
    /** The product’s performers. */
    public List<Artist> performers;
    /** The product’s price. */
    public Price price;
    /** The owning Album or Single if appropriate. */
    public Product takenFrom;
    /** The track count for Album or Single products. */
    public int trackCount;

    /** Launches MixRadio to show details for the product. */
    public void show() {
        return;
    }

}
