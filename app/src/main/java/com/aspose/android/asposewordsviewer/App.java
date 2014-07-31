package com.aspose.android.asposewordsviewer;

import android.app.Application;
import android.util.Log;

import com.aspose.words.License;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

public class App extends Application {

    public static void closeStream(Closeable... list) {
        if (list != null) {
            for (Closeable c : list) {
                if (c != null) {
                    try {
                        c.close();
                    } catch (IOException x) {
                    }
                }
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        applyAsposeLicense();
    }

    void applyAsposeLicense() {
        License license = new License();
        InputStream i = null;
        try {
            i = getResources().getAssets().open(getString(R.string.aspose_license_asset_file));
            license.setLicense(i);
            closeStream(i);
        } catch (Exception x) {
            Log.e(App.class.getName(), Log.getStackTraceString(x));
        } finally {
            closeStream(i);
        }
    }
}
