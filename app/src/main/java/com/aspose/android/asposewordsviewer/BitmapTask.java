package com.aspose.android.asposewordsviewer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.aspose.words.Document;
import com.aspose.words.ImageSaveOptions;
import com.aspose.words.SaveFormat;

import java.io.ByteArrayOutputStream;

public class BitmapTask extends AsyncTask<Integer, Void, Bitmap> {

    Context context;
    Document doc;

    public BitmapTask(Context context, Document doc) {
        this.context = context;
        this.doc = doc;
    }

    @Override
    protected Bitmap doInBackground(Integer... pages) {
        try {
            ImageSaveOptions o = new ImageSaveOptions(SaveFormat.JPEG);
            o.setPageIndex(pages[0]);
            ByteArrayOutputStream outs = new ByteArrayOutputStream();
            this.doc.save(outs, o);
            Bitmap bmp = BitmapFactory.decodeByteArray(outs.toByteArray(), 0, outs.size());
            outs.close();
            return bmp;
        } catch (Exception x) {
            throw new RuntimeException(x);
        }
    }
}
