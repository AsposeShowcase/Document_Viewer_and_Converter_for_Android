package com.aspose.android.asposewordsviewer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.dropbox.chooser.android.DbxChooser;

public class ChooserActivity extends ActionBarActivity {

    static final int REQUEST_DROPBOX_CHOOSER = 1;
    static final int REQUEST_FILE_CHOOSER = 2;
    DbxChooser dropboxChooser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooser);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    public void chooseFromDropbox(View view) {
        if (dropboxChooser == null) {
            dropboxChooser = new DbxChooser(getString(R.string.dropbox_app_key)).forResultType(DbxChooser.ResultType.FILE_CONTENT);
        }
        dropboxChooser.launch(this, REQUEST_DROPBOX_CHOOSER);
    }

    public void chooseFromStorage(View view) {
        Intent i = new Intent();
        i.setAction(Intent.ACTION_GET_CONTENT);
        i.setType("*/*");
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setClassName(this, "com.ipaulpro.afilechooser.FileChooserActivity");
        startActivityForResult(i, REQUEST_FILE_CHOOSER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_DROPBOX_CHOOSER:
                if (resultCode == RESULT_OK) {
                    Intent i = new Intent(this, ViewerActivity.class);
                    i.setAction(Intent.ACTION_VIEW);
                    i.setData(new DbxChooser.Result(data).getLink());
                    startActivity(i);
                }
                break;
            case REQUEST_FILE_CHOOSER:
                if (resultCode == RESULT_OK) {
                    Intent i = new Intent(this, ViewerActivity.class);
                    i.setAction(Intent.ACTION_VIEW);
                    i.setData(data.getData());
                    startActivity(i);
                }
                break;
        }
    }
}
