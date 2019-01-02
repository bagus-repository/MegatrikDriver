package id.co.ardata.megatrik.megatrikdriver.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.ardata.megatrik.megatrikdriver.R;
import id.co.ardata.megatrik.megatrikdriver.activity.MaterialOrderActivity;
import id.co.ardata.megatrik.megatrikdriver.model.ErrorApiMsg;
import id.co.ardata.megatrik.megatrikdriver.model.Order;
import id.co.ardata.megatrik.megatrikdriver.model.TechnicianOrdersItem;
import id.co.ardata.megatrik.megatrikdriver.utils.ApiClient;
import id.co.ardata.megatrik.megatrikdriver.utils.ApiInterface;
import id.co.ardata.megatrik.megatrikdriver.utils.Tools;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<TechnicianOrdersItem> ordersItems;
    Context mContext;

    public OrderListAdapter(Context mContext, List<TechnicianOrdersItem> technicianOrdersItems) {
        this.mContext = mContext;
        this.ordersItems = technicianOrdersItems;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_on_process, viewGroup, false);
        vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int pos) {
        TechnicianOrdersItem item = ordersItems.get(pos);
            MyViewHolder v = (MyViewHolder) holder;

            v.tvOrderId.setText("Order ID "+item.getId());
            v.tvOrderDescription.setText(item.getDescription());
            v.tvOrderCreatedAt.setText("Tanggal : "+item.getCreatedAt());
            v.tvOrderTechnician.setText("Pelanggan : "+item.getCustomer().getName());
            v.tvOrderAddress.setText("Alamat : "+item.getAddress());

            if (item.getOrderStart() != null){
                v.llNotKerjakan.setVisibility(View.GONE);
            }
    }

    @Override
    public int getItemCount() {
        return ordersItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvOrderId)
        TextView tvOrderId;
        @BindView(R.id.tvOrderDescription)
        TextView tvOrderDescription;
        @BindView(R.id.tvOrderCreatedAt)
        TextView tvOrderCreatedAt;
        @BindView(R.id.tvOrderTechnician)
        TextView tvOrderTechnician;
        @BindView(R.id.tvOrderAddress)
        TextView tvOrderAddress;
        @BindView(R.id.btNavigasi)
        Button btNavigasi;
        @BindView(R.id.btKerjakan)
        Button btKerjakan;
        @BindView(R.id.ibMaterial)
        ImageButton ibMaterial;
        @BindView(R.id.ibNavigasi)
        ImageButton ibNavigasi;
        @BindView(R.id.ibSelesai)
        ImageButton ibSelesai;
        @BindView(R.id.llKerjakan)
        LinearLayout llKerjakan;
        @BindView(R.id.llNotKerjakan)
        LinearLayout llNotKerjakan;

        KProgressHUD progressHUD;
        ApiInterface apiInterface;
        Gson gson;

        public MyViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            gson = new Gson();

            btNavigasi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TechnicianOrdersItem order = ordersItems.get(getAdapterPosition());
                    String url = "https://www.google.com/maps/dir/?api=1&destination="+order.getLatitude()+","+order.getLongitude();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    mContext.startActivity(i);
                }
            });
            btKerjakan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TechnicianOrdersItem order = ordersItems.get(getAdapterPosition());
                    order_kerjakan(order.getId());
                }
            });
            ibMaterial.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TechnicianOrdersItem order = ordersItems.get(getAdapterPosition());
                    Intent i = new Intent(mContext, MaterialOrderActivity.class);
                    i.putExtra("order_id", order.getId());
                    mContext.startActivity(i);
                }
            });
            ibNavigasi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TechnicianOrdersItem order = ordersItems.get(getAdapterPosition());
                    String url = "https://www.google.com/maps/dir/?api=1&destination="+order.getLatitude()+","+order.getLongitude();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    mContext.startActivity(i);
                }
            });
            ibSelesai.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CFAlertDialog.Builder builder = new CFAlertDialog.Builder(mContext)
                        .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                        .setTitle("Konfirmasi")
                        .setMessage("Anda yakin ?")
                        .addButton("Ya", mContext.getResources().getColor(R.color.white),
                                mContext.getResources().getColor(R.color.red_A700), CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.END, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        TechnicianOrdersItem order = ordersItems.get(getAdapterPosition());
                                        order_selesai(order.getId());
                                    }
                                })
                        .addButton("Tidak", mContext.getResources().getColor(R.color.white),
                                mContext.getResources().getColor(R.color.cfdialog_button_black_text_color), CFAlertDialog.CFAlertActionStyle.NEGATIVE, CFAlertDialog.CFAlertActionAlignment.END, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });
                    builder.show();
                }
            });

        }

        private void order_selesai(int id) {
            progressHUD = KProgressHUD.create(mContext)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Memuat")
                    .setCancellable(false)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f)
                    .show();
            apiInterface = ApiClient.getApiClient(mContext, true);

            Call<ResponseBody> call = apiInterface.updateOrderSelesai(String.valueOf(id), new HashMap<String, String>());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    progressHUD.dismiss();
                    if (response.isSuccessful()){
                        CFAlertDialog.Builder builder = new CFAlertDialog.Builder(mContext)
                                .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                                .setTitle("Info")
                                .setMessage("Terima Kasih, order selesai !!")
                                .addButton("OK", mContext.getResources().getColor(R.color.white), mContext.getResources().getColor(R.color.red_A700), CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.END, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });
                        builder.show();
                        llKerjakan.setVisibility(View.GONE);
                        llNotKerjakan.setVisibility(View.GONE);
                    }else {
                        ErrorApiMsg errorApiMsg = gson.fromJson(response.errorBody().charStream(), ErrorApiMsg.class);
                        Tools.Tshort(mContext, errorApiMsg.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    progressHUD.dismiss();
                    t.printStackTrace();
                    Tools.Tshort(mContext, mContext.getString(R.string.error_connect_server));
                }
            });
        }

        private void order_kerjakan(int id) {
            progressHUD = KProgressHUD.create(mContext)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Memuat")
                    .setCancellable(false)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f)
                    .show();
            apiInterface = ApiClient.getApiClient(mContext, true);

            Call<ResponseBody> call = apiInterface.updateOrderKerjakan(String.valueOf(id), new HashMap<String, String>());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    progressHUD.dismiss();
                    if (response.isSuccessful()){
                        CFAlertDialog.Builder builder = new CFAlertDialog.Builder(mContext)
                            .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                            .setTitle("Info")
                            .setMessage("Selamat mengerjakan !!")
                            .addButton("OK", mContext.getResources().getColor(R.color.white), mContext.getResources().getColor(R.color.red_A700), CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.END, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                        builder.show();
                        llNotKerjakan.setVisibility(View.GONE);
                        llKerjakan.setVisibility(View.VISIBLE);
                    }else {
                        Tools.Tshort(mContext, "Gagal coba lagi");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    progressHUD.dismiss();
                    t.printStackTrace();
                    Tools.Tshort(mContext, mContext.getString(R.string.error_connect_server));
                }
            });
        }
    }
}
