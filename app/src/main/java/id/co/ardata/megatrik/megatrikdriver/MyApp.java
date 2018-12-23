package id.co.ardata.megatrik.megatrikdriver;

import android.app.Application;

import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;

import id.co.ardata.megatrik.megatrikdriver.utils.NotificationOpenedHandler;
import id.co.ardata.megatrik.megatrikdriver.utils.SessionManager;

public class MyApp extends Application {

    SessionManager sessionManager;

    @Override
    public void onCreate() {
        super.onCreate();

        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .setNotificationOpenedHandler(new NotificationOpenedHandler(this))
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        sessionManager = new SessionManager(this);
        updateOsPlayerId();
    }

    private void updateOsPlayerId() {
        OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
        boolean isEnabled = status.getPermissionStatus().getEnabled();
        boolean isSubscribed = status.getSubscriptionStatus().getSubscribed();
        if (isEnabled && isSubscribed){
            String userID = status.getSubscriptionStatus().getUserId();
            sessionManager.setUserOsPlayerId(userID);
        }
    }
}
