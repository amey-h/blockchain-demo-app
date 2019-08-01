package com.example.blockchaindemoapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtils {

    private static PreferenceUtils appPref;
    private static SharedPreferences mPrefs;
    private static SharedPreferences.Editor editor;

    public static PreferenceUtils getInstance(Context context) {
        if (appPref == null) {

            mPrefs = context.getSharedPreferences("blockchain_prefs", Context.MODE_PRIVATE);
            appPref = new PreferenceUtils();
            editor = mPrefs.edit();
        }
        return appPref;
    }

    public void saveWalletAddress(String walletAddress) {
        editor.putString("wallet_address", walletAddress);
        editor.commit();
    }

    public String getWalletAddress() {
        String address = mPrefs.getString("wallet_address", "");
        return address;
    }

    public void saveWalletFileName(String walletFileName) {
        editor.putString("wallet_filename", walletFileName);
        editor.commit();
    }

    public String getWalletFileName() {
        String walletFileName = mPrefs.getString("wallet_filename", "");
        return walletFileName;
    }
}
