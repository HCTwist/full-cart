package uk.henrytwist.fullcart.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import uk.henrytwist.fullcart.R;

/**
 * Utility class to send feedback
 */
public class FeedbackUtil {

    private static final String EMAIL = "henrytwistprojects@gmail.com";

    private FeedbackUtil() {

    }

    public static boolean canSendFeedback(Context context) {

        return buildIntent(context).resolveActivity(context.getPackageManager()) != null;
    }

    public static void startFeedback(Context context) {

        Intent emailIntent = buildIntent(context);
        context.startActivity(Intent.createChooser(emailIntent, context.getString(R.string.setting_email)));
    }

    private static Intent buildIntent(Context context) {

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{EMAIL});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, context.getString(R.string.setting_email_subject));

        return emailIntent;
    }
}
