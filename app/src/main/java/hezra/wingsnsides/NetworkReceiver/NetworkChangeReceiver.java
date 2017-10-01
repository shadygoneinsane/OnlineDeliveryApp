package hezra.wingsnsides.NetworkReceiver;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import hezra.wingsnsides.R;
import hezra.wingsnsides.Utils.Utils;

public class NetworkChangeReceiver extends BroadcastReceiver {
    private Dialog dialog;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!Utils.isNetworkAvailable(context)) {
            dialog = new Dialog(context);
            dialog.setTitle(context.getString(R.string.check_your_internet_connection));
            dialog.setCancelable(true);
        } else {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }
}
