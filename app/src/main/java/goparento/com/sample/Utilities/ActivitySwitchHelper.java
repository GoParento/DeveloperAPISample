package goparento.com.sample.Utilities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

import java.util.UUID;

import goparento.com.sample.activities.BrowserActivity;


/**
 * Created by manuja on 30/11/17.
 */

public class ActivitySwitchHelper {


    public static int STATUS_SUCCESS = 0;
    public static int STATUS_FAIL = -1;
    private static String GUEST_ID = null;
    public static String GUEST_TOKEN = "guest_token";
    public static String APP_ID = "guest_id";


    public static String getOrUpdateAPPID(Context context) {
        if (GUEST_ID != null) return GUEST_ID;
        SharedPreferences preferences = context.getSharedPreferences(Utils.SHARED_PREF, 0);
        GUEST_ID = preferences.getString(APP_ID, null);
        if (TextUtils.isEmpty(GUEST_ID)) {
            GUEST_ID = UUID.randomUUID().toString();
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(APP_ID, GUEST_ID);
            editor.apply();
        }
        return GUEST_ID;
    }

    public static boolean isAppInstalled(String packageName, Context context) {
        Intent mIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (mIntent != null) {
            return true;
        } else {
            return false;
        }
    }


    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void openInAppBrowser(String url, String name, Context context) {
        Intent intent = new Intent(context, BrowserActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("name", name);
        context.startActivity(intent);
    }


    public static void openInBrowser(Context context, String url) {
        Uri webpage = Uri.parse(url);

        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            webpage = Uri.parse("http://" + url);
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }

    }

}
