package com.aspose.android.asposewordsviewer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.aspose.words.SaveFormat;

public class ViewerActivity extends ActionBarActivity {

    final PageCache cache = new PageCache(10485760); // 10*1024*1024 = 10 MB
    int totalDocumentPages;
    Uri inputFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);
        ViewPager pager = (ViewPager) findViewById(R.id.pager);

        if (savedInstanceState != null) {
            this.inputFile = savedInstanceState.getParcelable("INPUT_FILE");
            this.totalDocumentPages = savedInstanceState.getInt("TOTAL_PAGES");
            Bitmap[] cachedBitmaps = (Bitmap[]) savedInstanceState.getParcelableArray("CACHED_BITMAPS");
            for (int i = 0; i < cachedBitmaps.length; i++) {
                if (cachedBitmaps[i] != null) {
                    synchronized (this.cache) {
                        this.cache.put(i, cachedBitmaps[i]);
                    }
                }
            }
        } else {
            if (Intent.ACTION_VIEW.equals(getIntent().getAction()) && getIntent().getData() != null) {
                this.inputFile = getIntent().getData();
            } else {
                Toast.makeText(this, R.string.no_input_file, Toast.LENGTH_SHORT).show();
            }
            this.totalDocumentPages = 1;
        }

        final DocumentPager adapter = new DocumentPager(inputFile, this.cache);
        adapter.setCount(this.totalDocumentPages);
        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                ViewerActivity.this.totalDocumentPages = adapter.getCount();
            }
        });
        pager.setAdapter(adapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("TOTAL_PAGES", this.totalDocumentPages);
        outState.putParcelable("INPUT_FILE", this.inputFile);
        Bitmap[] bitmaps = new Bitmap[((ViewPager) findViewById(R.id.pager)).getAdapter().getCount()];
        for (int i = 0; i < bitmaps.length; i++) {
            synchronized (this.cache) {
                bitmaps[i] = this.cache.get(i);
            }
        }
        outState.putParcelableArray("CACHED_BITMAPS", bitmaps);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_previous || id == R.id.action_next) {
            ViewPager pager = (ViewPager) findViewById(R.id.pager);
            switch (id) {
                case R.id.action_previous:
                    pager.setCurrentItem(pager.getCurrentItem() - 1);
                    return true;
                case R.id.action_next:
                    pager.setCurrentItem(pager.getCurrentItem() + 1);
                    return true;
            }
        }

        switch (id) {
            case R.id.action_convert_pdf:
                startDocumentConversion(SaveFormat.PDF, "application/pdf", "pdf");
                break;
            case R.id.action_convert_docx:
                startDocumentConversion(SaveFormat.DOCX, "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "docx");
                break;
            case R.id.action_convert_doc:
                startDocumentConversion(SaveFormat.DOC, "application/msword", "doc");
                break;
            case R.id.action_convert_odt:
                startDocumentConversion(SaveFormat.ODT, "application/vnd.oasis.opendocument.text", "odt");
                break;
            case R.id.action_convert_rtf:
                startDocumentConversion(SaveFormat.RTF, "application/rtf", "rtf");
                break;
            case R.id.action_convert_html:
                startDocumentConversion(SaveFormat.HTML, "text/html", "html");
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void toggleFullscreen(View v) {
        if ((getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) == 0) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            if (getSupportActionBar() != null) {
                getSupportActionBar().hide();
            }
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            if (getSupportActionBar() != null) {
                getSupportActionBar().show();
            }
        }
    }

    void startDocumentConversion(int format, String outputMime, String outputFileExtension) {
        Intent i = new Intent(this, ConversionService.class)
                .putExtra(ConversionService.EXTRA_SAVE_FORMAT, format)
                .putExtra(ConversionService.EXTRA_OUTPUT_MIME, outputMime)
                .putExtra(ConversionService.EXTRA_OUTPUT_FILE_EXTENSION, outputFileExtension)
                .setData(this.inputFile);
        startService(i);

        new AlertDialog.Builder(this)
                .setMessage(getString(R.string.msg_document_conversion_started))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }
}
