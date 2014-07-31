## Document Viewer and Converter for Android

![](http://www.aspose.com/blogs/wp-content/uploads/2014/07/device-2014-07-31-20345hfjhf9.png)

Document Viewer and Converter for Android is a sample project that demonstrates how to use [Aspose.Words for
Android](http://www.aspose.com/android/word-component.aspx) in your Android application to view
and convert Word documents to different formats. User can either choose the document from
external storage or Dropbox.

Document Viewer and Converter for Android uses `ViewPager` to display each page of the document. The page is converted
first to JPEG image and cached in memory to retrieve quickly when user navigates between pages,
or rotate screen. All operations are performed using `AsyncTask` to keep UI thread working fine.

Conversion is done by an `IntentService` and user gets progress in Notification area.

## Setup

Download the project files and import into Android Studio. There are some dependency files which
you need to add to specific directories. Afterwith, your project will be ready to run.

## Dependencies

### Aspose.Words for Android

Go to [Aspose.Words for Android page](http://www.aspose.com/android/word-component.aspx) and
download it. From the archive, copy `Aspose.Words.1.9.0.jdk16.jar` (or any newer version) to
`app/libs` directory of your project.

### Aspose License

The project works without a license, with limitations. To remove limitations, you can buy or acquire
a [temporary license](http://www.aspose.com/corporate/purchase/temporary-license.aspx).

Copy the license file to `app/src/main/assets` directory in your project. If your license filename
if different than `Aspose.Total.Android.lic`, update it in `app/src/main/res/values/strings.xml`.

### aFileChooser

Go to [aFileChooser project](https://github.com/iPaulPro/aFileChooser) and download it. Copy the
following files from aFileChooser project to specified location in your project.

| aFileChooser | Your project |
| ------------ | ------------ |
| `aFileChooser/res/*` | `aFileChooser/src/main/res/` |
| `aFileChooser/src/*` | `aFileChooser/src/main/java/` |
| `aFileChooser/AndroidManifest.xml` | `aFileChooser/src/main/AndroidManifest.xml` |

### Dropbox Android Chooser SDK

Go to [Dropbox Android Chooser SDK](https://www.dropbox.com/developers/dropins/chooser/android) and
download it. Copy the following files from Dropbox Android Chooser SDK to specified
location in your project.

| Dropbox Android Chooser SDK | Your project |
| --------------------------- | ------------ |
| `res/*` | `dropbox-android-chooser-sdk/src/main/res/` |
| `src/*` | `dropbox-android-chooser-sdk/src/main/java/` |
| `AndroidManifest.xml` | `dropbox-android-chooser-sdk/src/main/AndroidManifest.xml` |
