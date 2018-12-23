package id.co.ardata.megatrik.megatrikdriver.utils;

import android.app.Activity;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import id.co.ardata.megatrik.megatrikdriver.activity.BidOrderActivity;

public class NotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {

    Context mContext;

    public NotificationOpenedHandler(Context context){
        this.mContext = context;
    }

    @Override
    public void notificationOpened(OSNotificationOpenResult result) {
//        OSNotificationAction.ActionType actionType = result.action.type;
//        if (actionType == OSNotificationAction.ActionType.ActionTaken){
//
//        }
        JSONObject data = result.notification.payload.additionalData;

        if (data != null){
            try {
                if (data.getString("type").equals("bid")){
                    Intent toBid = new Intent(mContext, BidOrderActivity.class);
                    toBid.putExtra("order_id", data.getInt("order_id"));
                    mContext.startActivity(toBid);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
