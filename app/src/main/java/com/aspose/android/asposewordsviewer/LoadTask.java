package com.aspose.android.asposewordsviewer;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import com.aspose.words.Document;

import java.io.InputStream;

public class LoadTask extends AsyncTask<Uri, Void, Document> {

    Context context;
    Exception error;

    public LoadTask(Context context) {
        this.context = context;
    }

    @Override
    protected Document doInBackground(Uri... uris) {
        InputStream i = null;
        try {
            i = this.context.getApplicationContext().getContentResolver().openInputStream(uris[0]);
            Document d = new Document(i);
            App.closeStream(i);
            return d;
        } catch (Exception x) {
            this.error = x;
        } finally {
            App.closeStream(i);
        }

        return null;
    }
}
