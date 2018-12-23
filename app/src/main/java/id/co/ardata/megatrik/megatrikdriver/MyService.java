package id.co.ardata.megatrik.megatrikdriver;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import id.co.ardata.megatrik.megatrikdriver.utils.ApiClient;
import id.co.ardata.megatrik.megatrikdriver.utils.ApiInterface;
import id.co.ardata.megatrik.megatrikdriver.utils.Tools;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    Handler handler;
    GoogleApiClient googleApiClient;
    Location location;
    double latitude, longitude;
    ApiInterface apiInterface;
    private String user_id;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        latitude = 0.0;
        longitude = 0.0;

        user_id = intent.getExtras().getString("id");

        buildGoogleApiClient();

        if (googleApiClient != null && !googleApiClient.isConnected()){
            googleApiClient.connect();
        }

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                findLatLngNow();
                update_location_user();
                handler.postDelayed(this, TimeUnit.MINUTES.toMillis(15));
            }
        }, 1000);
        return Service.START_STICKY;
    }

    private void update_location_user() {
        if (user_id != null && latitude != 0.0 && longitude != 0.0){
            apiInterface = ApiClient.getApiClient(getBaseContext(), true);

            HashMap<String, Double> params = new HashMap<>();
            params.put("latitude", latitude);
            params.put("longitude", longitude);

            Call<ResponseBody> call = apiInterface.updateLocation(user_id, params);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()){
                        Log.d(MyService.class.getSimpleName(), "Lokasi terupdate");
                    }else{
                        try {
                            Log.d(MyService.class.getSimpleName(), response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }else{
            Tools.Tshort(getBaseContext(), "Lokasi null");
        }
    }

    private void buildGoogleApiClient() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(getBaseContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        findLatLngNow();
    }

    private void findLatLngNow() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(MyService.class.getSimpleName(), "Permission not accepted");
        }else {
            location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (location != null){
                this.latitude = location.getLatitude();
                this.longitude = location.getLongitude();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


}
