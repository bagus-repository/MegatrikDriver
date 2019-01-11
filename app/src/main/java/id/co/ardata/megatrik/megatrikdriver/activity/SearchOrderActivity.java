package id.co.ardata.megatrik.megatrikdriver.activity;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.ardata.megatrik.megatrikdriver.R;
import id.co.ardata.megatrik.megatrikdriver.adapter.OrderNotAcceptedAdapter;
import id.co.ardata.megatrik.megatrikdriver.model.ErrorApiMsg;
import id.co.ardata.megatrik.megatrikdriver.model.Order;
import id.co.ardata.megatrik.megatrikdriver.utils.ApiClient;
import id.co.ardata.megatrik.megatrikdriver.utils.ApiInterface;
import id.co.ardata.megatrik.megatrikdriver.utils.Tools;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchOrderActivity extends AppCompatActivity {

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    Context mContext;
    ApiInterface apiInterface;
    Gson gson;
    OrderNotAcceptedAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_order);
        ButterKnife.bind(this);

        mContext = this;
        apiInterface = ApiClient.getApiClient(mContext, true);
        toolbar.setTitle("List Material");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setHasFixedSize(true);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                generate_order_not_accepted();
            }
        });

        generate_order_not_accepted();
    }

    private void generate_order_not_accepted() {
        swipeRefreshLayout.setRefreshing(true);

        Call<List<Order>> call = apiInterface.getOrderNotAccpeted();
        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                swipeRefreshLayout.setRefreshing(false);
                if (response.isSuccessful()){
                    mAdapter = new OrderNotAcceptedAdapter(mContext, response.body());
                    mAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(mAdapter);
                }else {
                    ErrorApiMsg errorApiMsg = gson.fromJson(response.errorBody().charStream(), ErrorApiMsg.class);
                    Tools.Tshort(mContext, errorApiMsg.getMessage());
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
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
