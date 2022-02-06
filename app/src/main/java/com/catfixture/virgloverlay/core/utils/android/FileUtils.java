package com.catfixture.virgloverlay.core.utils.android;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import com.catfixture.virgloverlay.core.debug.Dbg;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import kotlin.NotImplementedError;

public class FileUtils {
    public static void RemoveAllFilesInDir(Context context, String path) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Uri contentUri = MediaStore.Files.getContentUri("external");

            String selection = MediaStore.MediaColumns.RELATIVE_PATH + "=?";

            String[] selectionArgs = new String[]{path + "/"};

            Cursor cursor = context.getContentResolver().query(contentUri, null, selection, selectionArgs, null);

            if (cursor.getCount() == 0) {
                Toast.makeText(context, "No file found in dir", Toast.LENGTH_LONG).show();
            } else {
                while (cursor.moveToNext()) {
                    String fileName = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME));
                    long id = cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
                    Uri uri = ContentUris.withAppendedId(contentUri, id);
                    context.getContentResolver().delete(uri, null, null);
                    Toast.makeText(context, "DEl " + fileName, Toast.LENGTH_LONG).show();
                }
            }
        } else {
            throw new NotImplementedError();
        }
    }

    public static boolean WriteFileToDownloads(Context context, String path, String name, byte[] data) {
        try {
            OutputStream fos = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContentResolver resolver = context.getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "application/octet-stream");
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, path);

                Uri imageUri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues);
                fos = resolver.openOutputStream(imageUri, "rwt");
            } else {
                File fileDir = Environment.getExternalStoragePublicDirectory(path);
                if ( !fileDir.exists()) {
                    if (!fileDir.mkdirs()) Dbg.Error("Could not create dirs!");
                }

                File image = new File(fileDir, name);
                Dbg.Error("PATH IS : " + image.getAbsolutePath());
                if ( !image.exists()) {
                    if ( image.createNewFile()) {

                    }
                }
                fos = new FileOutputStream(image);
            }
            fos.write(data);
            fos.flush();
            fos.close();
            Dbg.Msg("File " + name + " written");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public static File CopyRawToTemp (Context context, int rawId, String targetName) {
        InputStream rawIs = context.getResources().openRawResource(rawId);
        File filesDir = context.getFilesDir();
        File tempFile = new File(filesDir, targetName);
        try {
            if(tempFile.exists() && !tempFile.delete())
                throw new IOException("Cant delete file");

            if (tempFile.createNewFile()) {
                FileOutputStream fos = new FileOutputStream(tempFile);

                byte[] tempBuffer = new byte[1024];
                int readen = 0;
                while((readen = rawIs.read(tempBuffer)) > 0) {
                    fos.write(tempBuffer, 0, readen);
                }
                fos.flush();
                fos.close();
                Dbg.Msg("Tempfile " + tempFile.getAbsolutePath() + " created");
                return tempFile;
            } else throw new IOException("Cant create file");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            rawIs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean CheckFileExists(String s) {
        File fileDir = Environment.getExternalStoragePublicDirectory(s);
        return fileDir.exists();
    }
}
