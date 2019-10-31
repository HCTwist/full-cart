package com.twisthenry8gmail.fullcart;

import android.content.Context;
import android.content.Intent;

/**
 * Utility class to send feedback
 */
class FeedbackUtil {

    private static final String EMAIL = "twisthenry8@gmail.com";

    static final String MENU_FEEDBACK_SUBJECT = "FullCart Menu Suggestions";
    static final String PREMIUM_FEEDBACK_SUBJECT = "FullCart Premium Feature Request";
    static final String FEEDBACK_SUBJECT = "FullCart Feedback";

    private FeedbackUtil() {

    }

    /**
     * Starts composing an email to send to {@value EMAIL}
     *
     * @param context the context
     * @param subject the subject of the message
     */
    static void startFeedback(Context context, String subject) {

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{EMAIL});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);

        if (emailIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(Intent.createChooser(emailIntent, context.getString(R.string.email_intent_title)));
        }
    }
}
