package com.xinlan.imageeditlibrary.picchooser;

class BucketItem extends GridItem {

    final int id;
    int images = 1;

    /**
     * Creates a new BucketItem
     *
     * @param n the name of the bucket
     * @param p the absolute path to the bucket
     * @param i the bucket ID
     */
    public BucketItem(final String n, final String p,final String taken, int i) {
        super(n, p,taken,0);
        id = i;
    }

}
