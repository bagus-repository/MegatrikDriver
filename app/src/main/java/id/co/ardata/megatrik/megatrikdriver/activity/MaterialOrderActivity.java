package id.co.ardata.megatrik.megatrikdriver.activity;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.ardata.megatrik.megatrikdriver.R;
import id.co.ardata.megatrik.megatrikdriver.adapter.OrderMaterialAdapter;
import id.co.ardata.megatrik.megatrikdriver.model.ErrorApiMsg;
import id.co.ardata.megatrik.megatrikdriver.model.MaterialCategory;
import id.co.ardata.megatrik.megatrikdriver.model.MateriallistsItem;
import id.co.ardata.megatrik.megatrikdriver.model.MaterialsItem;
import id.co.ardata.megatrik.megatrikdriver.model.OrderMaterial;
import id.co.ardata.megatrik.megatrikdriver.utils.ApiClient;
import id.co.ardata.megatrik.megatrikdriver.utils.ApiInterface;
import id.co.ardata.megatrik.megatrikdriver.utils.Tools;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MaterialOrderActivity extends AppCompatActivity {

    int order_id;

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    Context mContext;
    ApiInterface apiInterface;
    KProgressHUD progressHUD;
    Gson gson;
    OrderMaterialAdapter mAdapter;
    List<MaterialCategory> materialCategories;
    List<MateriallistsItem> materialsItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_order);
        ButterKnife.bind(this);

        mContext = this;
        gson = new Gson();
        order_id = getIntent().getIntExtra("order_id", 0);
        if (order_id == 0){
            Tools.Tshort(mContext, "Gagal mendapatkan order Id");
            finish();
        }

        toolbar.setTitle("List Material");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setHasFixedSize(true);

        apiInterface = ApiClient.getApiClient(mContext, true);

        generate_list_material();
        generate_material_category();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                generate_list_material();
            }
        });
    }

    private void generate_material_category() {
        Call<List<MaterialCategory>> call = apiInterface.getMaterialCategories();
        call.enqueue(new Callback<List<MaterialCategory>>() {
            @Override
            public void onResponse(Call<List<MaterialCategory>> call, Response<List<MaterialCategory>> response) {
                if (response.isSuccessful()){
                    materialCategories = response.body();
                }
            }

            @Override
            public void onFailure(Call<List<MaterialCategory>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.material_order_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add){
            show_add_dialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void show_add_dialog() {
        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.dialog_event);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        AppCompatSpinner spnMaterialCategory = (AppCompatSpinner) dialog.findViewById(R.id.spnMaterialCategory);
        final AppCompatSpinner spnMaterial = (AppCompatSpinner) dialog.findViewById(R.id.spnMaterial);
        final EditText etQty = (EditText) dialog.findViewById(R.id.etQty);

        if (materialCategories.size() > 0){
            String categories[] = new String[materialCategories.size()];
            for (int i = 0; i < materialCategories.size(); i++) {
                categories[i] = materialCategories.get(i).getName();
            }
            if (categories.length > 0){
                ArrayAdapter<String> categories_adapter = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item, categories);
                categories_adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                spnMaterialCategory.setAdapter(categories_adapter);
                spnMaterialCategory.setSelection(-1);
            }
        }

        spnMaterialCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int id = materialCategories.get(i).getId();
                Call<MaterialCategory> call = apiInterface.getMaterialCategoryList(String.valueOf(id));
                call.enqueue(new Callback<MaterialCategory>() {
                    @Override
                    public void onResponse(Call<MaterialCategory> call, Response<MaterialCategory> response) {
                        if (response.isSuccessful()){
                            materialsItems = response.body().getMateriallists();
                            if (materialsItems.size() > 0){
                                String categories[] = new String[materialsItems.size()];
                                for (int i = 0; i < materialsItems.size(); i++) {
                                    categories[i] = materialsItems.get(i).getName()+" (Rp. "+materialsItems.get(i).getPrice()+")";
                                }
                                if (categories.length > 0){
                                    ArrayAdapter<String> categories_adapter = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item, categories);
                                    categories_adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                                    spnMaterial.setAdapter(categories_adapter);
                                    spnMaterial.setSelection(-1);
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MaterialCategory> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ((ImageButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        ((Button) dialog.findViewById(R.id.bt_save)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String material_list_id;
                String quantity = etQty.getText().toString();
                boolean valid = true;

                try {
                    material_list_id = String.valueOf(materialsItems.get(spnMaterial.getSelectedItemPosition()).getId());
                }catch (Exception e){
                    material_list_id = null;
                }

                if (order_id == 0){
                    Tools.Tshort(mContext, "Order id kosong");
                    valid = false;
                }

                if (material_list_id == null || material_list_id.isEmpty()){
                    Tools.Tshort(mContext, "Material belum dipilih");
                    valid = false;
                }

                if (quantity.isEmpty()){
                    Tools.Tshort(mContext, "Quantity kosong");
                    valid = false;
                }

                if (valid){
                    progressHUD = KProgressHUD.create(mContext)
                            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                            .setLabel("Memuat")
                            .setCancellable(false)
                            .setAnimationSpeed(2)
                            .setDimAmount(0.5f)
                            .show();
                    HashMap<String, String> params = new HashMap<>();
                    params.put("order_id", String.valueOf(order_id));
                    params.put("material_list_id", String.valueOf(materialsItems.get(spnMaterial.getSelectedItemPosition()).getId()));
                    params.put("quantity", etQty.getText().toString());

                    Call<MaterialsItem> call = apiInterface.storeMaterial(params);
                    call.enqueue(new Callback<MaterialsItem>() {
                        @Override
                        public void onResponse(Call<MaterialsItem> call, Response<MaterialsItem> response) {
                            progressHUD.dismiss();
                            if (response.isSuccessful()){
                                dialog.dismiss();
                                Tools.Tshort(mContext, "Berhasil menambah material !");
                                generate_list_material();
                            }
                        }

                        @Override
                        public void onFailure(Call<MaterialsItem> call, Throwable t) {
                            progressHUD.dismiss();
                            t.printStackTrace();
                            Tools.Tshort(mContext, getString(R.string.error_connect_server));
                        }
                    });
                }
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void generate_list_material() {
        progressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Memuat")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        Call<OrderMaterial> call = apiInterface.getOrderMaterial(String.valueOf(order_id));
        call.enqueue(new Callback<OrderMaterial>() {
            @Override
            public void onResponse(Call<OrderMaterial> call, Response<OrderMaterial> response) {
                progressHUD.dismiss();
                swipeRefreshLayout.setRefreshing(false);
                if (response.isSuccessful()){
                    List<MaterialsItem> materialsItems = response.body().getMaterials();
                    mAdapter = new OrderMaterialAdapter(mContext, materialsItems);
                    mAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(mAdapter);
                }else {
                    ErrorApiMsg errorApiMsg = gson.fromJson(response.errorBody().charStream(), ErrorApiMsg.class);
                    Tools.Tshort(mContext, errorApiMsg.getMessage());
                }
            }

            @Override
            public void onFailure(Call<OrderMaterial> call, Throwable t) {
                progressHUD.dismiss();
                swipeRefreshLayout.setRefreshing(false);
                t.printStackTrace();
                Tools.Tshort(mContext, getString(R.string.error_connect_server));
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
