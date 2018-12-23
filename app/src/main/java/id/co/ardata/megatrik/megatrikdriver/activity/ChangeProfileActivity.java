package id.co.ardata.megatrik.megatrikdriver.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
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

public class ChangeProfileActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.etFullname)
    EditText etFullname;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etTelepon)
    EditText etTelepon;
    @BindView(R.id.btUbah)
    Button btUbah;

    private static final String TAG = ChangeProfileActivity.class.getSimpleName();

    AwesomeValidation awesomeValidation;
    KProgressHUD progressHUD;
    SessionManager sessionManager;
    ApiInterface apiInterface;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile);
        ButterKnife.bind(this);

        mContext = this;

        toolbar.setTitle("Ubah Profil");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        sessionManager = new SessionManager(this);

        etFullname.setText(sessionManager.getUserName());
        etEmail.setText(sessionManager.getUserEmail());
        etTelepon.setText(sessionManager.getUserPhone());

        btUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_profile();
            }
        });
    }

    private void update_profile() {
        awesomeValidation.clear();

        awesomeValidation.addValidation(etFullname, RegexTemplate.NOT_EMPTY, "Nama tidak valid");
        awesomeValidation.addValidation(etTelepon, RegexTemplate.TELEPHONE, "Telepon tidak valid");

        progressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Memuat")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        if (awesomeValidation.validate()){
            apiInterface = ApiClient.getApiClient(mContext, true);

            HashMap<String, String> params = new HashMap<>();
            params.put("name", etFullname.getText().toString());
            params.put("no_hp", etTelepon.getText().toString());

            Call<User> call = apiInterface.updateUser(sessionManager.getUserId(), params);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    progressHUD.dismiss();
                    if (response.isSuccessful()){
                        sessionManager.setUserName(etFullname.getText().toString());
                        sessionManager.setUserPhone(etTelepon.getText().toString());

                        CFAlertDialog.Builder builder = new CFAlertDialog.Builder(mContext)
                            .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                            .setTitle("Konfirmasi")
                            .setMessage("Berhasil ubah profil !")
                            .addButton("Ya", getResources().getColor(R.color.white), getResources().getColor(R.color.red_A700), CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.END, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();

                                }
                            });
                        builder.show();
                    }else {
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
