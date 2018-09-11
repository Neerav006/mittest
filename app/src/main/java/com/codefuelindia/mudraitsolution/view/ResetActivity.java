package com.codefuelindia.mudraitsolution.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.codefuelindia.mudraitsolution.R;
import com.codefuelindia.mudraitsolution.Utils.Utils;
import com.codefuelindia.mudraitsolution.common.RetrofitClient;
import com.codefuelindia.mudraitsolution.cons.Constants;
import com.codefuelindia.mudraitsolution.model.MyRes;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import static com.codefuelindia.mudraitsolution.cons.Constants.BASE_URL;


public class ResetActivity extends AppCompatActivity {
    private final long DOUBLE_TAP = 1500;
    private TextInputLayout tiMobile;
    private TextInputEditText edtPwd;
    private TextInputLayout tiMobile22;
    private TextInputEditText edtPwd22;
    private Button btnSubmit;
    private Toolbar toolbar;
    private ResetApi resetApi;
    private long lastclick = 0;
    private String id;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);


        if (getIntent() != null) {
            id = getIntent().getStringExtra(Constants.ID);
        }

        resetApi = getAPIService(BASE_URL);

        tiMobile = (TextInputLayout) findViewById(R.id.tiMobile);
        edtPwd = (TextInputEditText) findViewById(R.id.edtPwd);
        tiMobile22 = (TextInputLayout) findViewById(R.id.tiMobile22);
        edtPwd22 = (TextInputEditText) findViewById(R.id.edtPwd22);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - lastclick < DOUBLE_TAP) {
                    return;
                }
                lastclick = SystemClock.elapsedRealtime();
                if (!edtPwd.getText().toString().trim().isEmpty()) {

                    if (edtPwd.getText().toString().trim().equals(edtPwd22.getText().toString().trim())) {

                        showProgressDialog();
                        resetApi.reset(id, edtPwd.getText().toString().trim()).enqueue(new Callback<MyRes>() {
                            @Override
                            public void onResponse(Call<MyRes> call, Response<MyRes> response) {

                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }

                                if (response.isSuccessful()) {

                                    if (response.body().getMsg().equalsIgnoreCase("true")) {

                                        Toast.makeText(getApplicationContext(), "Successfully changed", Toast.LENGTH_SHORT).show();

                                        finishAffinity();
                                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                        startActivity(intent);

                                    } else {
                                        Toast.makeText(ResetActivity.this, "Not successfully changed", Toast.LENGTH_SHORT).show();

                                    }

                                } else {
                                    Toast.makeText(ResetActivity.this, "Not successfully changed", Toast.LENGTH_SHORT).show();

                                }
                            }

                            @Override
                            public void onFailure(Call<MyRes> call, Throwable t) {

                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                                Toast.makeText(ResetActivity.this, "Not successfully changed", Toast.LENGTH_SHORT).show();

                            }
                        });

                    } else {

                        edtPwd22.requestFocus();
                        Toast.makeText(ResetActivity.this, "Enter correct password", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    edtPwd.requestFocus();
                    Toast.makeText(ResetActivity.this, "Enter password", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }


    ResetApi getAPIService(String baseUrl) {

        return RetrofitClient.getClient(baseUrl).create(ResetApi.class);
    }

    public void showProgressDialog() {
        progressDialog = new ProgressDialog(ResetActivity.this);
        progressDialog.setTitle("Loading..");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    interface ResetApi {

        @POST("user/resetpassword_api")
        @FormUrlEncoded
        Call<MyRes> reset(@Field("id") String id, @Field("new_pwd") String username
        );
    }

    @Override
    protected void onResume() {
        super.onResume();

        Utils.hideSoftKeyBoard(this.getCurrentFocus(),ResetActivity.this);
    }


}
