package id.co.ardata.megatrik.megatrikdriver.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.crowdfire.cfalertdialog.CFAlertDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.ardata.megatrik.megatrikdriver.R;
import id.co.ardata.megatrik.megatrikdriver.model.MaterialsItem;
import id.co.ardata.megatrik.megatrikdriver.utils.ApiClient;
import id.co.ardata.megatrik.megatrikdriver.utils.ApiInterface;
import id.co.ardata.megatrik.megatrikdriver.utils.Tools;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderMaterialAdapter extends RecyclerView.Adapter {
    List<MaterialsItem> materialsItems;
    Context mContext;

    public OrderMaterialAdapter(Context mContext, List<MaterialsItem> materialsItems) {
        this.mContext = mContext;
        this.materialsItems = materialsItems;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_order_material, viewGroup, false);
        vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int pos) {
        MaterialsItem item = materialsItems.get(pos);
        if (holder instanceof MyViewHolder){
            MyViewHolder v = (MyViewHolder) holder;

            v.tvMaterialId.setText("ID Material\n"+item.getId());
            v.tvMaterialName.setText(item.getMateriallistsItem().getName());
            v.tvMaterialDesc.setText(item.getMateriallistsItem().getDescription());
        }
    }

    @Override
    public int getItemCount() {
        return materialsItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvMaterialId)
        TextView tvMaterialId;
        @BindView(R.id.tvMaterialName)
        TextView tvMaterialName;
        @BindView(R.id.tvMaterialDesc)
        TextView tvMaterialDesc;
        @BindView(R.id.ibDeleteMaterial)
        ImageButton ibDeleteMaterial;

        ApiInterface apiInterface;

        public MyViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

            ibDeleteMaterial.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MaterialsItem item = materialsItems.get(getAdapterPosition());
                    CFAlertDialog.Builder builder = new CFAlertDialog.Builder(mContext)
                        .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                        .setTitle("Konfirmasi")
                        .setMessage("Yakin hapus "+item.getMateriallistsItem().getName()+" ?")
                        .addButton("Ya", mContext.getResources().getColor(R.color.white),
                                mContext.getResources().getColor(R.color.red_A700), CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.END, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                MaterialsItem item = materialsItems.get(getAdapterPosition());
                                Log.d("Material", item.toString());
                                delete_material(item.getId());
                                dialogInterface.dismiss();
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

        private void delete_material(int id) {
            apiInterface = ApiClient.getApiClient(mContext, true);

            Call<ResponseBody> call = apiInterface.deleteMaterial(String.valueOf(id));
            Log.d("Material", call.request().url().toString());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.d("Material", response.body().toString());
                    if (response.isSuccessful()){
                        CFAlertDialog.Builder builder = new CFAlertDialog.Builder(mContext)
                                .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                                .setTitle("Info")
                                .setMessage("Berhasil dihapus")
                                .setCancelable(false)
                                .addButton("Ya", mContext.getResources().getColor(R.color.white),
                                        mContext.getResources().getColor(R.color.red_A700), CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.END, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                                ((Activity) mContext).recreate();
                                            }
                                        });
                        builder.show();
                    }else {
                        Tools.Tshort(mContext, "Hapus gagal");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                    Tools.Tshort(mContext, mContext.getString(R.string.error_connect_server));
                }
            });
        }
    }
}
