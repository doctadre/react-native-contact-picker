package com.doctadre.contactpicker;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.WritableArray;

import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.CommonDataKinds.Email;

import java.util.*;

import static android.app.Activity.RESULT_OK;


public class ContactPickerModule extends ReactContextBaseJavaModule implements ActivityEventListener {
    private static final int CONTACT_PICKER_RESULT = 1001;
    private static final String DEBUG_TAG = "ContactPicker";

    private Promise mContactPickerPromise;
    private final ReactApplicationContext _reactContext;


    @Override
    public String getName() {
        return "ContactPicker";
    }

    public ContactPickerModule(ReactApplicationContext reactContext) {
        super(reactContext);
        _reactContext = reactContext;
        _reactContext.addActivityEventListener(this);
    }


    @ReactMethod
    public void pickContact(final Promise promise) {
        Activity currentActivity = getCurrentActivity();
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                Contacts.CONTENT_URI);
        mContactPickerPromise = promise;
        currentActivity.startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);

    }

    @Override
    public void  onActivityResult(int requestCode, int resultCode, Intent data) {
        WritableArray emails = Arguments.createArray();        try {
            if (resultCode == RESULT_OK) {
                switch (requestCode) {
                    case CONTACT_PICKER_RESULT:
                        Uri result = data.getData();
                        String id = result.getLastPathSegment();
                        Cursor cursor = _reactContext.getContentResolver().query(
                                Email.CONTENT_URI, null,
                                Email.CONTACT_ID + "=?",
                                new String[]{id}, null);
                        try {
                            while (cursor.moveToNext()) {
                                int emailIdx = cursor.getColumnIndex(Email.DATA);
                                String email = cursor.getString(emailIdx);
                                emails.pushString(email);
                            }
                        } finally {
                            cursor.close();
                        }
                        mContactPickerPromise.resolve(emails);
                        break;
                }

            } else {
                // gracefully handle failure
                mContactPickerPromise.reject("The user cancelled or there was no contact");
            }
        } catch (Exception e) {
            mContactPickerPromise.reject(e.getMessage());
        }
    }
}
