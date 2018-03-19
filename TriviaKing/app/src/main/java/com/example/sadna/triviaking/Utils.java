package com.example.sadna.triviaking;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import org.json.JSONObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utils class with static function to use in other activities
 */

public class Utils {

    /**
     * check if a mail is in the right pattern
     * @param target the mail to check
     * @return true if the mail is valid, false otherwise
     */
    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    /**
     * gets the http address
     * @return the http address
     */
    public static String getHttpAddress() {
        return ("http://192.168.1.17:8080/");
    }

    /**
     * gets jsonObject from specific field in a jsonObject
     * @param json json object
     * @param field the field name
     * @return jsonObject
     */
    public static JSONObject jsonFieldToJSONObj(JSONObject json, String field) {
        try {
            return json.getJSONObject(field);
        } catch (Exception e) {
            Log.println(Log.DEBUG, "debug", "exception=" + e.toString());
            return new JSONObject();
        }
    }

    /**
     * gets boolean value from a specific field in a jsonObject
     * @param json json object
     * @param field the field name
     * @return boolean value
     */
    public static boolean jsonFieldToBoolean(JSONObject json, String field) {
        try {
            return (json.get(field).equals("true"));
        } catch (Exception e) {
            Log.println(Log.DEBUG, "debug", "exception=" + e.toString());
            return false;
        }
    }

    /**
     * gets int value from a specific field in a jsonObject
     * @param json json object
     * @param field the field name
     * @return int value
     */
    public static int jsonFieldToInt(JSONObject json, String field) {
        try {
            return (json.getInt(field));
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * gets string from a specific field in a jsonObject
     * @param json json object
     * @param field the field name
     * @return string
     */
    public static String jsonFieldToString(JSONObject json, String field) {
        try {
            return (json.getString(field));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * save string in the shared preferences
     * @param context
     * @param key the name of the key
     * @param value the string to save
     */
    public static void savePreferences(Context context, String key, String value) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * save int value in the shared preferences
     * @param context
     * @param key the name of the key
     * @param value the int to save
     */
    public static void saveIntPreferences(Context context, String key, int value) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * hsahing a string with Sha-256
     * @param s the string to hash
     * @return the hashed string
     */
    public static String hash(String s)
    {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        md.update(s.getBytes());

        byte byteData[] = md.digest();

        //convert the byte to hex format
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }

        System.out.println("Hex format:" + sb.toString());
        return sb.toString();

    }
}
