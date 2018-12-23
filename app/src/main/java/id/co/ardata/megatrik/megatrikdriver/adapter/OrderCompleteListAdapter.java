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
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.ardata.megatrik.megatrikdriver.R;
import id.co.ardata.megatrik.megatrikdriver.model.Order;
import id.co.ardata.megatrik.megatrikdriver.model.TechnicianOrdersItem;
import id.co.ardata.megatrik.megatrikdriver.utils.ApiClient;
import id.co.ardata.megatrik.megatrikdriver.utils.ApiInterface;
import id.co.ardata.megatrik.megatrikdriver.utils.Tools;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderCompleteListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<TechnicianOrdersItem> ordersItems;
    Context mContext;

    public OrderCompleteListAdapter(Context mContext, List<TechnicianOrdersItem> technicianOrdersItems) {
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
        if (holder instanceof MyViewHolder) {
            MyViewHolder v = (MyViewHolder) holder;

            v.tvOrderId.setText("Order ID "+item.getId());
            v.tvOrderDescription.setText(item.getDescription());
            v.tvOrderCreatedAt.setText("Tanggal : "+item.getCreatedAt());
            v.tvOrderTechnician.setText("Pelanggan : "+item.getCustomer().getName());
            v.tvOrderAddress.setText("Alamat : "+item.getAddress());
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

        public MyViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

            llKerjakan.setVisibility(View.GONE);
            llNotKerjakan.setVisibility(View.GONE);
        }
    }
}
