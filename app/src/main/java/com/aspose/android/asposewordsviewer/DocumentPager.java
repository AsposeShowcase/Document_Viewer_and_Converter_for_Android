package com.aspose.android.asposewordsviewer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.aspose.words.Document;
import com.aspose.words.FileCorruptedException;
import com.aspose.words.UnsupportedFileFormatException;

import java.io.IOException;

public class DocumentPager extends PagerAdapter {
    Uri uri;
    Document doc;
    PageCache cache;
    int pages = 1;

    public DocumentPager(Uri uri, PageCache cache) {
        this.uri = uri;
        this.cache = cache;
    }

    @Override
    public int getCount() {
        return this.pages;
    }

    public void setCount(int count) {
        this.pages = count;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final View layout = ((LayoutInflater) container.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.document_page, null);

        Bitmap cachedBmp = this.cache.get(position);
        if (cachedBmp != null) {
            ((ImageView) layout.findViewById(R.id.image)).setImageBitmap(cachedBmp);
        } else {
            if (this.doc == null) {
                new LoadTask(container.getContext()) {
                    @Override
                    protected void onPostExecute(Document doc) {
                        super.onPostExecute(doc);

                        if (this.error != null) {
                            if (this.error instanceof FileCorruptedException) {
                                displayAlertAndFinish(container.getContext(), R.string.msg_input_file_is_corrupt);
                            } else if (this.error instanceof UnsupportedFileFormatException) {
                                displayAlertAndFinish(container.getContext(), R.string.msg_input_file_unsupported);
                            } else if (this.error instanceof IOException) {
                                displayAlertAndFinish(container.getContext(), R.string.msg_input_file_io_exception);
                            } else {
                                displayAlertAndFinish(container.getContext(), R.string.msg_input_file_unknown_error);
                            }
                            return;
                        }

                        DocumentPager.this.doc = doc;

                        new BitmapTask(container.getContext(), DocumentPager.this.doc) {
                            @Override
                            protected void onPostExecute(Bitmap bitmap) {
                                super.onPostExecute(bitmap);
                                ((ImageView) layout.findViewById(R.id.image)).setImageBitmap(bitmap);
                                layout.findViewById(R.id.loading_indicator).setVisibility(View.GONE);
                                DocumentPager.this.cache.put(position, bitmap);
                            }
                        }.execute(position);

                        new PageCountTask() {
                            @Override
                            protected void onPostExecute(Integer pages) {
                                super.onPostExecute(pages);
                                DocumentPager.this.pages = pages;
                                DocumentPager.this.notifyDataSetChanged();
                            }
                        }.execute(DocumentPager.this.doc);

                    }
                }.execute(this.uri);
            } else {
                new BitmapTask(container.getContext(), DocumentPager.this.doc) {
                    @Override
                    protected void onPostExecute(Bitmap bitmap) {
                        super.onPostExecute(bitmap);
                        ((ImageView) layout.findViewById(R.id.image)).setImageBitmap(bitmap);
                        layout.findViewById(R.id.loading_indicator).setVisibility(View.GONE);
                        DocumentPager.this.cache.put(position, bitmap);
                    }
                }.execute(position);
            }
        }

        ((ViewPager) container).addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        try {
            container.removeView((ImageView) object);
        } catch (Exception x) {
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    void displayAlertAndFinish(final Context context, int resId) {
        new AlertDialog.Builder(context)
                .setMessage(context.getString(resId))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        ((Activity) context).finish();
                    }
                })
                .show();
    }
}
