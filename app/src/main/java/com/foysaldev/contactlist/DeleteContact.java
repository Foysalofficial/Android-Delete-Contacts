package com.foysaldev.contactlist;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;

import com.daasuu.cat.CountAnimationTextView;

public class DeleteContact extends AsyncTask<Void, String, Integer> {
    private final ContentResolver contentResolver;
    private final Cursor cursor;
    private final ProgressDialog progressDialog;
    private final ProgressStatusListener progressStatusListener;
    private final int maxcount;
    private Context ctx;
    private CountAnimationTextView textView;

    DeleteContact(Context ctx, ProgressStatusListener progressStatusListener, Cursor cursor, ContentResolver contentResolver, ProgressDialog progressDialog, CountAnimationTextView textview) {
        this.ctx = ctx;
        this.cursor = cursor;
        this.contentResolver = contentResolver;
        this.progressDialog = progressDialog;
        this.progressStatusListener = progressStatusListener;
        this.textView = textview;
        maxcount = cursor.getCount();


    }

    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.show();

    }

    @Override
    protected Integer doInBackground(Void... voids) {
        int num = 0;

        while (cursor.moveToNext()) {
            try {
                @SuppressLint("Range")
                String lookupKey = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);
                //System.out.println("The uri is " + uri.toString());
                contentResolver.delete(uri, null, null);

                @SuppressLint("Range")
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                //  System.out.println("Contact name : " + name);


                num++;

                String uProgress[] = new String[]{num + "", name};
                publishProgress(uProgress);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return num;
    }

    protected void onProgressUpdate(String... progress) {

        // super.onProgressUpdate(progress);

        progressStatusListener.onProgressUpdate(Integer.parseInt(progress[0]), progress[1]);


    }

    protected void onPostExecute(Integer result) {
        // super.onPostExecute(result);
        progressStatusListener.onProgressFinished(maxcount - result, textView);
        ctx = null;
        textView = null;


    }
}