package id.co.ardata.megatrik.megatrikdriver.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.ardata.megatrik.megatrikdriver.R;
import id.co.ardata.megatrik.megatrikdriver.model.ErrorApiMsg;
import id.co.ardata.megatrik.megatrikdriver.model.User;
import id.co.ardata.megatrik.megatrikdriver.utils.ApiClient;
import id.co.ardata.megatrik.megatrikdriver.utils.ApiInterface;
import id.co.ardata.megatrik.megatrikdriver.utils.Tools;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.tietFullname)
    TextInputEditText tietFullname;
    @BindView(R.id.tietEmail)
    TextInputEditText tietEmail;
    @BindView(R.id.tietPassword)
    TextInputEditText tietPassword;
    @BindView(R.id.tietRePassword)
    TextInputEditText tietRePassword;
    @BindView(R.id.btSignUp)
    Button btSIgnUp;
    @OnClick(R.id.tvSignIn)
    public void gotTo(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    Context mContext;
    AwesomeValidation awesomeValidation;
    KProgressHUD progressHUD;
    ApiInterface apiInterface;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        mContext = this;
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        gson = new Gson();

        btSIgnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                do_register();
            }
        });

    }

    private void do_register() {
        awesomeValidation.clear();

        progressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Memuat")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        awesomeValidation.addValidation(tietFullname, RegexTemplate.NOT_EMPTY, "Nama tidak valid");
        awesomeValidation.addValidation(tietPassword, RegexTemplate.NOT_EMPTY, "Password tidak valid");
        awesomeValidation.addValidation(tietRePassword, tietPassword, "Konfirmasi password tidak valid");

        if (awesomeValidation.validate()){
            apiInterface = ApiClient.getApiClient(mContext, false);

            HashMap<String, String> params = new HashMap<>();
            params.put("name", tietFullname.getText().toString());
            params.put("email", tietEmail.getText().toString());
            params.put("password", tietPassword.getText().toString());
            params.put("role_id", "3");

            Call<User> call = apiInterface.storeUser(params);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    progressHUD.dismiss();
                    if (response.isSuccessful()){
                        Tools.Tshort(mContext, "Pendaftaran berhasil, silahkan login !");

                        Intent intent = new Intent(mContext, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        ErrorApiMsg errorApiMsg = gson.fromJson(response.errorBody().charStream(), ErrorApiMsg.class);
                        Tools.Tshort(mContext, errorApiMsg.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    progressHUD.dismiss();
                    t.printStackTrace();
                    Tools.Tshort(mContext, getString(R.string.error_connect_server));
                }
            });
        }else{
            progressHUD.dismiss();
        }
    }
}
