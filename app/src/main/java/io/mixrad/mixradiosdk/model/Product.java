package io.mixrad.mixradiosdk.model;

import java.util.List;

/**
 * Created by RichardW on 04/03/15.
 */
public class Product {

        /** The ID of the product */
        public String id;
        /** The display name of the product. */
        public String name;
        /** The product’s category - i.e., whether it is an album, single, or track. */
        public Category category;
        /** The product’s genres. */
        public List<Genre> genres;
        /** The product’s performers. */
        public String performers;
        /** The product’s price. */
        public Price price;
        /** The owning Album or Single if appropriate. */
        public Product takenFrom;
        /** The track count for Album or Single products. */
        public int trackCount;

        /** Gets a URI to a 50px x 50px thumbnail (when available). */
        public String thumb50Uri;

        /** Gets a URI to a 100px x 100px thumbnail (when available). */
        public String thumb100Uri;

        /** Gets a URI to a 200px x 200px thumbnail (when available). */
        public String thumb200Uri;

        /** Gets a URI to a 320px x 320px thumbnail (when available). */
        public String thumb320Uri;

    /** Launches MixRadio to show details for the product. */
        public void show() {
            return;
        }

}
