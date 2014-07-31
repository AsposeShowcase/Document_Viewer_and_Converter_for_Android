package com.aspose.android.asposewordsviewer;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class PageCache
        extends LruCache<Integer, Bitmap> {

    public PageCache(int maxSize) {
        super(maxSize);
    }

    @Override
    protected int sizeOf(Integer key, Bitmap value) {
        return value.getRowBytes() * value.getHeight();
    }
}
