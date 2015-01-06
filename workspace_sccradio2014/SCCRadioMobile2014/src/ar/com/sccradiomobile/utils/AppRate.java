package ar.com.sccradiomobile.utils;

/**
 * Created by Mariano Salvetti
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.*;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;
import ar.com.sccradiomobile.storage.util.Constants;


/**
 * Created by Mariano Salvetti
 */
public class AppRate implements android.content.DialogInterface.OnClickListener, DialogInterface.OnCancelListener {

    private static final String TAG = "AppRater";

    private Activity hostActivity;
    private Context context;
    private DialogInterface.OnClickListener clickListener;
    private SharedPreferences preferences;
    private AlertDialog.Builder dialogBuilder = null;

    private int numberOfVisitPropertyDetails = 0;
    private boolean firstSaveAction = false;
    private boolean dontShowPopUpAgain = false;

    public AppRate(Activity hostActivity) {
        this.hostActivity = hostActivity;
        preferences = hostActivity.getSharedPreferences(PreferencesConstants.SHARED_PREFS_NAME, 0);
        firstSaveAction = preferences.getBoolean(PreferencesConstants.PREF_USER_HAS_SAVED_FAV, false);
        numberOfVisitPropertyDetails = preferences.getInt(PreferencesConstants.PREF_USER_HAS_VISIT_TO_PROPERTY_DETAILS, 0);

        if (preferences.getBoolean(PreferencesConstants.PREF_DONT_SHOW_AGAIN, false)) {
            dontShowPopUpAgain = true;
        }

        if (dontShowPopUpAgain){return;}

        SharedPreferences.Editor editor = preferences.edit();

        //Add to launch Counter
        long launch_count = preferences.getLong(PreferencesConstants.LAUNCH_COUNT, 0) +1;
        editor.putLong(PreferencesConstants.LAUNCH_COUNT, launch_count);

        //Get Date of first launch
        Long date_firstLaunch = preferences.getLong(PreferencesConstants.date_first_launch,0);
        if (date_firstLaunch == 0){
              date_firstLaunch = System.currentTimeMillis();
              editor.putLong(PreferencesConstants.date_first_launch, date_firstLaunch);
        }

          //Wait at least X days to launch
          if (launch_count >= PreferencesConstants.LAUNCH_UNTIL_PROMPT) {
              if (System.currentTimeMillis() >= date_firstLaunch + (PreferencesConstants.DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)){
                  showDialog();
              }
          }

          editor.commit();


        if (Constants.DEBUG) Log.d(TAG, "Ready the instance of AppRate with preferences.");
    }


    /**
     * @param initialVisits The minimum number of visit before showing the rate dialog.<br/>
     *                      Default value is 0 visits.
     * @return This {@link AppRate} object to allow chaining.
     */
    public AppRate setNumberOfVisitPropertyDetails(int initialVisits) {
        this.numberOfVisitPropertyDetails = initialVisits;
        return this;
    }


    /**
     * Use this method if you want to customize the style and content of the rate dialog.<br/>
     * When using the {@link AlertDialog.Builder} you should use:
     * <ul>
     * <li>{@link AlertDialog.Builder#setPositiveButton} for the <b>rate</b> button.</li>
     * <li>{@link AlertDialog.Builder#setNeutralButton} for the <b>rate later</b> button.</li>
     * <li>{@link AlertDialog.Builder#setNegativeButton} for the <b>never rate</b> button.</li>
     * </ul>
     *
     * @param customBuilder The custom dialog you want to use as the rate dialog.
     * @return This {@link AppRate} object to allow chaining.
     */
    public AppRate setCustomDialog(AlertDialog.Builder customBuilder) {
        dialogBuilder = customBuilder;
        return this;
    }

    /**
     * Reset all the data collected about number of launches and days until first launch.
     *
     * @param hostActivity A context.
     */
    public static void reset(Activity hostActivity) {
        hostActivity.getSharedPreferences(PreferencesConstants.SHARED_PREFS_NAME, 0).edit().clear().commit();
        if (Constants.DEBUG) Log.d(TAG, "Cleared AppRate shared preferences.");
    }


    /**
     * Shows the default rate dialog.
     *
     * @return
     */
    private void showDefaultDialog() {
        String title = "Calificar " + getApplicationName(hostActivity.getApplicationContext());
        String message = "Si te gusta usar "+ getApplicationName(hostActivity.getApplicationContext())+ ", por favor dedicanos un momento para calificarnos. Gracias por su apoyo!";
        String rate = "Si";
        String later = "Recordar más tarde";
        String dismiss = "No Gracias";

        new AlertDialog.Builder(hostActivity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(rate, this)
                .setNegativeButton(dismiss, this)
                .setNeutralButton(later, this)
                .setOnCancelListener(this)
                .create().show();
    }

    /**
     * Show the custom rate dialog.
     *
     * @return
     */
    private void showDialog(AlertDialog.Builder builder) {
        AlertDialog dialog = builder.create();
        dialog.show();

        String rate = (String) dialog.getButton(AlertDialog.BUTTON_POSITIVE).getText();
        String remindLater = (String) dialog.getButton(AlertDialog.BUTTON_NEUTRAL).getText();
        String dismiss = (String) dialog.getButton(AlertDialog.BUTTON_NEGATIVE).getText();

        dialog.setButton(AlertDialog.BUTTON_POSITIVE, rate, this);
        dialog.setButton(AlertDialog.BUTTON_NEUTRAL, remindLater, this);
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, dismiss, this);

        dialog.setOnCancelListener(this);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(PreferencesConstants.PREF_USER_HAS_RATE_APP, false);
        editor.commit();
    }

    /**
     * @param onClickListener A listener to be called back on.
     * @return This {@link AppRate} object to allow chaining.
     */
    public AppRate setOnClickListener(DialogInterface.OnClickListener onClickListener) {
        clickListener = onClickListener;
        return this;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

        SharedPreferences.Editor editor = preferences.edit();

        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                try {
                    hostActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + hostActivity.getPackageName())));
                    editor.putBoolean(PreferencesConstants.PREF_USER_HAS_RATE_APP, true);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(hostActivity, "No tiene el Play Store instalado en su dispositivo", Toast.LENGTH_SHORT).show();
                }
                editor.putBoolean(PreferencesConstants.PREF_DONT_SHOW_AGAIN, true);
                break;

            case DialogInterface.BUTTON_NEGATIVE:
                editor.putBoolean(PreferencesConstants.PREF_USER_HAS_RATE_APP, false);
                //	editor.putBoolean(PreferencesConstants.PREF_DONT_SHOW_AGAIN, true);
                break;

            case DialogInterface.BUTTON_NEUTRAL:
                //	editor.putLong(PreferencesConstants.PREF_DATE_FIRST_LAUNCH, System.currentTimeMillis());
                //	editor.putLong(PreferencesConstants.PREF_LAUNCH_COUNT, 0);
                break;

            default:
                break;
        }

        editor.commit();
        dialog.dismiss();

        if (clickListener != null) {
            clickListener.onClick(dialog, which);
        }
    }

    /**
     * @param context A context of the current application.
     * @return The application name of the current application.
     */
    private static final String getApplicationName(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
        } catch (final PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        return (String) (applicationInfo != null ? packageManager.getApplicationLabel(applicationInfo) : "(unknown)");
    }

    /**
     * We use this method to chekc if is the "PREF_MAX_VISIT_TO_PROPERTY_DETAILS" visit to Property Details Page.
     * For requeriments, we must show the Dialog after 5 tap throughs to Property Details pages.
     */
    public void addVisitInPropertyDetails(Activity hostActivity) {
        this.hostActivity = hostActivity;
        this.numberOfVisitPropertyDetails = preferences.getInt(PreferencesConstants.PREF_USER_HAS_VISIT_TO_PROPERTY_DETAILS, 0);
        if (this.numberOfVisitPropertyDetails >= PreferencesConstants.PREF_MAX_VISIT_TO_PROPERTY_DETAILS) {
            this.numberOfVisitPropertyDetails = 0;
            showDialog();
        } else {
            this.numberOfVisitPropertyDetails++;
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(PreferencesConstants.PREF_USER_HAS_VISIT_TO_PROPERTY_DETAILS, this.numberOfVisitPropertyDetails);
        editor.commit();
    }

    /**
    * We use this method to check if is the user saved to favorites.
    * For requeriments, we must show the Dialog in the first time that we Save to favorites.
    */
    public void saveToFavorites(Activity hostActivity) {
        this.hostActivity = hostActivity;
        if (!preferences.getBoolean(PreferencesConstants.PREF_USER_HAS_SAVED_FAV, false)) {
            firstSaveAction = true;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(PreferencesConstants.PREF_USER_HAS_SAVED_FAV, firstSaveAction);
            editor.commit();
            showDialog();
        } else {
            if (Constants.DEBUG) Log.d(TAG, "====> firstSaveAction= " + firstSaveAction);
        }
    }

    public void showDialog() {
        dontShowPopUpAgain = preferences.getBoolean(PreferencesConstants.PREF_USER_HAS_RATE_APP, false);
        if (!dontShowPopUpAgain) {
            showDefaultDialog();
        }
    }

    /**
     * We use this method to show the Rate My App Dialog from specific screens, like after Share's o Write an email.
     * @param hostActivity
     */
    public void showDialog(Activity hostActivity) {
        this.hostActivity = hostActivity;
        dontShowPopUpAgain = preferences.getBoolean(PreferencesConstants.PREF_USER_HAS_RATE_APP, false);
        if (!dontShowPopUpAgain) {
            showDefaultDialog();
        }
    }

    /**
        * We use this method to show the Rate My App Dialog from specific screens, like after Share's o Write an email.
        * @param hostActivity
        */
       public void showDialogForced(Activity hostActivity) {
           this.hostActivity = hostActivity;
            showDefaultDialog();
       }
}