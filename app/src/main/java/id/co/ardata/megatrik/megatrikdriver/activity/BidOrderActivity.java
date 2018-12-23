package id.co.ardata.megatrik.megatrikdriver.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.ardata.megatrik.megatrikdriver.R;
import id.co.ardata.megatrik.megatrikdriver.model.ErrorApiMsg;
import id.co.ardata.megatrik.megatrikdriver.model.Order;
import id.co.ardata.megatrik.megatrikdriver.model.ServicesItem;
import id.co.ardata.megatrik.megatrikdriver.utils.ApiClient;
import id.co.ardata.megatrik.megatrikdriver.utils.ApiInterface;
import id.co.ardata.megatrik.megatrikdriver.utils.SessionManager;
import id.co.ardata.megatrik.megatrikdriver.utils.Tools;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BidOrderActivity extends AppCompatActivity {

    int order_id;

    @BindView(R.id.tvOrderId)
    TextView tvOrderId;
    @BindView(R.id.tvCustomerName)
    TextView tvCustomerName;
    @BindView(R.id.tvOrderAddress)
    TextView tvOrderAddress;
    @BindView(R.id.tvOrderServices)
    TextView tvOrderServices;
    @BindView(R.id.tvOrderStatus)
    TextView tvOrderStatus;
    @BindView(R.id.btTerima)
    Button btTerima;
    @BindView(R.id.btBatal)
    Button btBatal;

    KProgressHUD progressHUD;
    ApiInterface apiInterface;
    Context mContext;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid_order);
        ButterKnife.bind(this);

        mContext = this;
        sessionManager = new SessionManager(mContext);

        order_id = getIntent().getIntExtra("order_id", 0);
        check_order_status(order_id);

        btBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btTerima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accept_bid();
            }
        });
    }

    private void accept_bid() {
        progressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Memuat")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        apiInterface = ApiClient.getApiClient(mContext, true);

        HashMap<String, String> params = new HashMap<>();
        params.put("technician_id", sessionManager.getUserId());

        Call<Order> call = apiInterface.updateOrder(String.valueOf(order_id), params);
        Log.d("Bid", call.request().url().toString());
        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                progressHUD.dismiss();
                CFAlertDialog.Builder builder = new CFAlertDialog.Builder(mContext)
                        .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                        .setTitle("Bid");
                if (response.isSuccessful()){
                    builder.setMessage("Selamat! Bid anda menangkan, silahkan cek halaman order.");
                }else {
                    builder.setMessage("Maaf order sudah terbid.");
                }
                builder.setCancelable(true)
                    .addButton("OK", getResources().getColor(R.color.white), getResources().getColor(R.color.red_A700), CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.END, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            finish();
                        }
                    });
                builder.show();
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                progressHUD.dismiss();
                t.printStackTrace();
                Tools.Tshort(mContext, getString(R.string.error_connect_server));
            }
        });
    }

    private void check_order_status(int order_id) {
        progressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Memuat")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        apiInterface = ApiClient.getApiClient(mContext, true);

        Call<Order> call = apiInterface.getOrder(String.valueOf(order_id));
        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                progressHUD.dismiss();
                if (response.isSuccessful()){
                    Log.d("Bid", response.body().toString());
                    tvOrderId.setText(String.valueOf(response.body().getId()));
                    tvCustomerName.setText(response.body().getCustomer().getName());
                    tvOrderAddress.setText(response.body().getAddress());
                    List<ServicesItem> servicesItems = response.body().getServices();
                    String serviceText = "";
                    for (int i = 0; i < servicesItems.size(); i++) {
                        serviceText += "- "+servicesItems.get(i).getServicelist().getName()+"\n";
                    }
                    tvOrderServices.setText(serviceText);
                    int is_accepted = response.body().getOrderStatus().getIsAccepted();
                    if (is_accepted == 1){
                        tvOrderStatus.setText("Maaf, order sudah menemukan teknisi");
                        btTerima.setVisibility(View.GONE);
                    }else {
                        tvOrderStatus.setText("Bid masih tersedia");
                    }
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                progressHUD.dismiss();
                t.printStackTrace();
                Tools.Tshort(mContext, getString(R.string.error_connect_server));
            }
        });
    }
}
