package goparento.com.sample.Utilities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.view.MenuItem;


public class Utils {
    public static final String SHARED_PREF = "goparento_sdk";




    private static String getRootDomainUrl(String url) {
        String[] domainKeys = url.split("/")[2].split("\\.");
        int length = domainKeys.length;
        int dummy = domainKeys[0].equals("www") ? 1 : 0;
        if (length - dummy == 2)
            return domainKeys[length - 2] + "." + domainKeys[length - 1];
        else {
            if (domainKeys[length - 1].length() == 2) {
                return domainKeys[length - 3] + "." + domainKeys[length - 2] + "." + domainKeys[length - 1];
            } else {
                return domainKeys[length - 2] + "." + domainKeys[length - 1];
            }
        }
    }

    public static void tintMenuIcon(Context context, MenuItem item, int color) {
        Drawable drawable = item.getIcon();
        if (drawable != null) {
            // If we don't mutate the drawable, then all drawable's with this id will have a color
            // filter applied to it.
            drawable.mutate();
            drawable.setColorFilter(ContextCompat.getColor(context, color), PorterDuff.Mode.SRC_ATOP);
        }
    }

    public static void openGoParentoApp(Context context) {
        if (isAppInstalled("com.baby.android", context)) {
            Intent i;
            PackageManager manager = context.getPackageManager();
            try {
                i = manager.getLaunchIntentForPackage("com.baby.android");
                if (i == null)
                    throw new PackageManager.NameNotFoundException();
                i.addCategory(Intent.CATEGORY_LAUNCHER);
                context.startActivity(i);
            } catch (PackageManager.NameNotFoundException e) {

            }

        } else {
            Intent rateintent = new Intent(Intent.ACTION_VIEW);
            rateintent.setData(Uri.parse("market://details?id=com.baby.android"));
            context.startActivity(rateintent);
        }
    }

    public static boolean isAppInstalled(String packageName, Context context) {
        Intent mIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (mIntent != null) {
            return true;
        } else {
            return false;
        }
    }

    public static void shareWebPage(Context context,String url) {
        String string = "Click here - " + url + "\n" + "Download Best Parenting App " + "http://bit.ly/goparentonani";

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_TEXT, string);
        context.startActivity(Intent.createChooser(i, "Share via "));
        ActivitySwitchHelper.showToast(context,"Share In Groups and Chats");
    }
}
