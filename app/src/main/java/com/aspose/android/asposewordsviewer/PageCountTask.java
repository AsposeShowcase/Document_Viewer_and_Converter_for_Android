package com.aspose.android.asposewordsviewer;

import android.os.AsyncTask;

import com.aspose.words.Document;

public class PageCountTask
        extends AsyncTask<Document, Void, Integer> {

    @Override
    protected Integer doInBackground(Document... documents) {
        try {
            return documents[0].getPageCount();
        } catch (Exception x) {
            throw new RuntimeException(x);
        }
    }
}
