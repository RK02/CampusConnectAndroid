
package com.campusconnect.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * <h1>NetworkAvailability</h1>
 *This purpose of this class to check state of network.
 * @author  Canopus InfoSystems Pvt. Ltd.
 * @version 1.1
 * @since   2015-02-20
 */
public class NetworkAvailablity {
	/**
	 * This method is used to just check the status of internet connection
	 * @param context
	 * @return {@link Boolean} true if state in connected available otherwise
	 *         false
	 */
	public static boolean chkStatus(Context context) {
		final ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connMgr.getActiveNetworkInfo() != null
				&& connMgr.getActiveNetworkInfo().isAvailable()
				&& connMgr.getActiveNetworkInfo().isConnected()) {

			return true;
		} else {
			return false;
		}
	}

	/**
	 * This method is used to just check the connection of internet connection
	 *
	 * @param context
	 * @return {@link Boolean} true if connection available otherwise false
	 */
	public static boolean hasInternetConnection(Context context) {


	    ConnectivityManager connectivity = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;

	}

}