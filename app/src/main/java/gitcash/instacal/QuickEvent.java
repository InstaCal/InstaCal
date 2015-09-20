package gitcash.instacal;

import android.os.AsyncTask;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.util.DateTime;

import com.google.api.services.calendar.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * An asynchronous task that handles the Google Calendar API call.
 * Placing the API calls in their own task ensures the UI stays responsive.
 */
public class QuickEvent extends AsyncTask<Void, Void, Void> {
    private PhotoPreviewActivity mActivity;
    private String ocrString;
    /**
     * Constructor.
     * @param activity CameraActivity that spawned this task.
     */
    QuickEvent(PhotoPreviewActivity activity, String tess) {
        this.mActivity = activity;
        this.ocrString = tess;
    }

    /**
     * Background task to call Google Calendar API.
     * @param params no parameters needed for this task.
     */
    @Override
    protected Void doInBackground(Void... params) {
        try {
            mActivity.clearEvent();
            mActivity.updateEvent(getDataFromApi());

        } catch (final GooglePlayServicesAvailabilityIOException availabilityException) {
            mActivity.showGooglePlayServicesAvailabilityErrorDialog(
                    availabilityException.getConnectionStatusCode());

        } catch (UserRecoverableAuthIOException userRecoverableException) {
            mActivity.startActivityForResult(
                    userRecoverableException.getIntent(),
                    PhotoPreviewActivity.REQUEST_AUTHORIZATION);

        } catch (Exception e) {
//            mActivity.updateStatus("The following error occurred:\n" +
//                    e.getMessage());
        }
        /*
        if (mActivity.mProgress.isShowing()) {
            mActivity.mProgress.dismiss();
        }
        */
        return null;
    }

    /**
     * Fetch a list of the next 10 events from the primary calendar.
     * @return List of Strings describing returned events.
     * @throws IOException
     */
    private Event getDataFromApi() throws IOException {
        Event createdEvent = mActivity.mService.events().quickAdd("primary", ocrString).execute();
        return createdEvent;
    }

}
