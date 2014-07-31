package com.aspose.android.asposewordsviewer;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;

import com.aspose.words.Document;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConversionService extends IntentService {

    public final static String EXTRA_SAVE_FORMAT = ConversionService.class.getName() + "SAVE_FORMAT";
    public final static String EXTRA_OUTPUT_MIME = ConversionService.class.getName() + "OUTPUT_MIME";
    public final static String EXTRA_OUTPUT_FILE_EXTENSION = ConversionService.class.getName() + "OUTPUT_FILE_EXTENSION";
    File defaultLocation;

    public ConversionService() {
        super("ConversionService");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        this.defaultLocation = new File(Environment.getExternalStorageDirectory(), "Aspose");
        this.defaultLocation = new File(this.defaultLocation, "Converted Documents");
        this.defaultLocation.mkdirs();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        getNotificationManager().cancel(ConversionService.class.getName(), 1);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        File output = new File(this.defaultLocation, new SimpleDateFormat("'Document 'yyyy-MM-dd' 'HHmmss'.'").format(new Date()) + intent.getStringExtra(EXTRA_OUTPUT_FILE_EXTENSION));

        getNotificationManager().notify(ConversionService.class.getName(), 1, new NotificationCompat.Builder(this)
                .setContentTitle("Converting documents")
                .setContentText(defaultLocation.toString())
                .setSmallIcon(R.drawable.ic_launcher)
                .setOngoing(true).build());

        InputStream is = null;
        try {
            is = getContentResolver().openInputStream(intent.getData());
            Document doc = new Document(is);
            App.closeStream(is);
            doc.save(output.toString(), intent.getIntExtra(EXTRA_SAVE_FORMAT, -1));

            getNotificationManager().notify(ConversionService.class.getName(), 2, new NotificationCompat.Builder(this)
                    .setContentTitle("Document converted")
                    .setContentText(output.toString())
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentIntent(PendingIntent.getActivity(ConversionService.this, 0, new Intent(Intent.ACTION_VIEW)
                            .setDataAndType(Uri.fromFile(output), intent.getStringExtra(EXTRA_OUTPUT_MIME)), PendingIntent.FLAG_CANCEL_CURRENT))
                    .build());

        } catch (Exception x) {
            getNotificationManager().notify(ConversionService.class.getName(), 3, new NotificationCompat.Builder(this)
                    .setContentTitle("Document conversion failed")
                    .setContentText(x.getMessage())
                    .setSmallIcon(R.drawable.ic_launcher)
                    .build());
        } finally {
            App.closeStream(is);
        }

        getNotificationManager().cancel(ConversionService.class.getName(), 1);
    }

    NotificationManager getNotificationManager() {
        return (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }
}
