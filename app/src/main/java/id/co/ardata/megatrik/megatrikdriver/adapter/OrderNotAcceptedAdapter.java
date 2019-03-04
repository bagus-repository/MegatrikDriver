package id.co.ardata.megatrik.megatrikdriver.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.ardata.megatrik.megatrikdriver.R;
import id.co.ardata.megatrik.megatrikdriver.activity.SearchOrderActivity;
import id.co.ardata.megatrik.megatrikdriver.model.Order;
import id.co.ardata.megatrik.megatrikdriver.utils.ApiClient;
import id.co.ardata.megatrik.megatrikdriver.utils.ApiInterface;
import id.co.ardata.megatrik.megatrikdriver.utils.SessionManager;
import id.co.ardata.megatrik.megatrikdriver.utils.Tools;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderNotAcceptedAdapter extends RecyclerView.Adapter {
    List<Order> orderList;
    Context ctx;

    public OrderNotAcceptedAdapter(Context mContext, List<Order> orderList) {
        this.ctx = mContext;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_order_not_accepted, viewGroup, false);
        vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int pos) {
        Order order = orderList.get(pos);
        if (holder instanceof MyViewHolder){
            MyViewHolder v = (MyViewHolder) holder;

            v.tvOrderId.setText("#"+order.getId());
            v.tvOrderCustomerName.setText("Pelanggan : "+order.getCustomer().getName());
            v.tvOrderDescription.setText("Keluhan :\n"+order.getDescription());
            v.tvOrderAddress.setText("Alamat :\n"+order.getAddress());
        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvOrderId)
        TextView tvOrderId;
        @BindView(R.id.tvOrderCustomerName)
        TextView tvOrderCustomerName;
        @BindView(R.id.tvOrderDescription)
        TextView tvOrderDescription;
        @BindView(R.id.tvOrderAddress)
        TextView tvOrderAddress;
        @BindView(R.id.btBid)
        Button btBid;

        KProgressHUD progressHUD;
        ApiInterface apiInterface;
        SessionManager sessionManager;

        public MyViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

            sessionManager = new SessionManager(ctx);

            btBid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Order order = orderList.get(getAdapterPosition());
                    accept_bid(order.getId());
                }
            });
        }

        private void accept_bid(int order_id) {
            progressHUD = KProgressHUD.create(ctx)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Memuat")
                    .setCancellable(false)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f)
                    .show();

            apiInterface = ApiClient.getApiClient(ctx, true);

            HashMap<String, String> params = new HashMap<>();
            params.put("technician_id", sessionManager.getUserId());

            Call<Order> call = apiInterface.updateOrder(String.valueOf(order_id), params);
            Log.d("Bid", call.request().url().toString());
            call.enqueue(new Callback<Order>() {
                @Override
                public void onResponse(Call<Order> call, Response<Order> response) {
                    progressHUD.dismiss();
                    CFAlertDialog.Builder builder = new CFAlertDialog.Builder(ctx)
                            .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                            .setTitle("Bid");
                    if (response.isSuccessful()){
                        builder.setMessage("Selamat! Bid anda menangkan, silahkan cek halaman order.");
                    }else {
                        builder.setMessage("Maaf order sudah terbid.");
                    }
                    builder.setCancelable(false)
                            .addButton("OK", ctx.getResources().getColor(R.color.white),
                                    ctx.getResources().getColor(R.color.red_A700), CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.END, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    ((SearchOrderActivity) ctx).generate_order_not_accepted();
                                }
                            });
                    builder.show();
                }

                @Override
                public void onFailure(Call<Order> call, Throwable t) {
                    progressHUD.dismiss();
                    t.printStackTrace();
                    Tools.Tshort(ctx, ctx.getString(R.string.error_connect_server));
                }
            });
        }
    }
}
