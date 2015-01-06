package ar.com.sccradiomobile.storage;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import ar.com.sccradiomobile.BaseActivity;
import ar.com.sccradiomobile.SCCRadioMobileApp;
import ar.com.sccradiomobile.R;
import ar.com.sccradiomobile.utils.Utils;
import com.android.volley.Request;
import com.android.volley.Response;
 

/**
 * Activity that executes a request.
 * 
 * @author Mariano Salvetti
 * @param <T>
 */
public abstract class RequestActivity<T>  extends BaseActivity  implements
		Response.Listener<T>, Response.ErrorListener {

	public RequestActivity(int titleRes) {
		super(R.string.title_section2);
	}

	private static final String LOG_TAG = "lnb_mobile";

	// Variable needed to control a poll update just in the time the app is closing.
	public boolean isRunning = true;

	/**
	 * Loading progress dialog.
	 */
	protected ProgressDialog mProgressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		onCreateImpl(savedInstanceState);
	}

	/**
	 * Creates the child activity view.
	 * 
	 * @param savedInstanceState
	 */
	protected abstract void onCreateImpl(Bundle savedInstanceState);

	/**
	 * Creates a new request.
	 * 
	 * @return the request
	 */
	protected abstract Request<T> createRequest();

    protected void manageNoConnection() {

    }

	public void performRequest() {
		if (Utils.isConnected(this) && isRunning) {
			mProgressDialog = ProgressDialog.show(this,
					getString(R.string.progress_dialog_title),
					getString(R.string.progress_dialog_message));

			Request<T> request = createRequest();

			SCCRadioMobileApp.REQUEST_MANAGER.addToRequestQueue(request, SCCRadioMobileApp.TAG);
		} else {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
              builder.setIcon(android.R.drawable.ic_dialog_alert);
              builder.setMessage(getString(R.string.progress_dialog_error_message));
              builder.setCancelable(false);
             // set neutral button: Exit the app message

           builder.setNeutralButton(R.string.progress_dialog_ok,new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog,int id) {
                      manageNoConnection();
              }
            });

              builder.setTitle(getString(R.string.progress_dialog_title));
              builder.create().show();

        }
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		SCCRadioMobileApp.REQUEST_MANAGER.cancelPendingRequests(SCCRadioMobileApp.TAG);
		isRunning = false;
	}
}
