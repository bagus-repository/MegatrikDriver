package id.co.ardata.megatrik.megatrikdriver.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.ardata.megatrik.megatrikdriver.R;
import id.co.ardata.megatrik.megatrikdriver.model.User;
import id.co.ardata.megatrik.megatrikdriver.utils.ApiClient;
import id.co.ardata.megatrik.megatrikdriver.utils.ApiInterface;
import id.co.ardata.megatrik.megatrikdriver.utils.SessionManager;
import id.co.ardata.megatrik.megatrikdriver.utils.Tools;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.etRePassword)
    EditText etRePassword;
    @BindView(R.id.btUbah)
    Button btUbah;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private static final String TAG = ChangePasswordActivity.class.getSimpleName();

    SessionManager sessionManager;
    AwesomeValidation awesomeValidation;
    KProgressHUD progressHUD;
    ApiInterface apiInterface;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);

        toolbar.setTitle("Ubah Password");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mContext = this;
        sessionManager = new SessionManager(mContext);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        btUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ubah_password();
            }
        });
    }

    private void ubah_password() {
        awesomeValidation.clear();

        awesomeValidation.addValidation(etPassword, RegexTemplate.NOT_EMPTY, "Password tidak valid");
        awesomeValidation.addValidation(etRePassword, etPassword, "Password konfirmasi tidak valid");

        progressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Memuat")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
        if (awesomeValidation.validate()){
            apiInterface = ApiClient.getApiClient(this, true);

            HashMap<String, String> params = new HashMap<>();
            params.put("password", etPassword.getText().toString());

            Call<User> call = apiInterface.updateUser(sessionManager.getUserId(), params);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()){
                        CFAlertDialog.Builder builder = new CFAlertDialog.Builder(mContext)
                            .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                            .setTitle("Konfirmasi")
                            .setMessage("Anda yakin keluar ?")
                            .addButton("Ya", getResources().getColor(R.color.white), getResources().getColor(R.color.red_A700), CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.END, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .addButton("Tidak", getResources().getColor(R.color.white), getResources().getColor(R.color.cfdialog_button_black_text_color), CFAlertDialog.CFAlertActionStyle.NEGATIVE, CFAlertDialog.CFAlertActionAlignment.END, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                        builder.show();
                    }else{
                        Log.d(TAG, response.errorBody().toString());
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    progressHUD.dismiss();
                    t.printStackTrace();
                    Tools.Tshort(mContext, getString(R.string.error_connect_server));
                }
            });
        }else {
            progressHUD.dismiss();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }
}
