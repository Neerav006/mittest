package com.codefuelindia.mudraitsolution.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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


public class ForgotPwd22Activity extends AppCompatActivity {
    private final long DOUBLE_TAP = 1500;
    private TextInputLayout tiMobile;
    private TextInputEditText edtOTP;
    private Button btnSubmit;
    private Button btnResend;
    private Toolbar toolbar;
    private long lastclick = 0;
    private String id;
    private String TYPE;
    private ProgressDialog progressDialog;
    private SendOTP sendOTP;
    private ResendOTP resendOTP;


    SendOTP getAPIService(String baseUrl) {

        return RetrofitClient.getClient(BASE_URL).create(SendOTP.class);
    }

    ResendOTP getAPIService22(String baseUrl) {

        return RetrofitClient.getClient(BASE_URL).create(ResendOTP.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pwd22);

        sendOTP = getAPIService(BASE_URL);
        resendOTP = getAPIService22(BASE_URL);

        if (getIntent() != null) {

            id = getIntent().getStringExtra(Constants.ID);

            TYPE = getIntent().getStringExtra(Constants.TYPE);


        }

        tiMobile = (TextInputLayout) findViewById(R.id.tiMobile);
        edtOTP = (TextInputEditText) findViewById(R.id.edtOTP);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnResend = (Button) findViewById(R.id.btnResend);




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


                if (id != null && TYPE.equalsIgnoreCase("reg")) {
                    if (edtOTP.getText().toString().trim().isEmpty()) {

                        Toast.makeText(ForgotPwd22Activity.this, "Please enter OTP", Toast.LENGTH_SHORT).show();
                    } else {

                        showProgressDialog();

                        sendOTP.senotp(id, edtOTP.getText().toString().trim()).enqueue(new Callback<MyRes>() {
                            @Override
                            public void onResponse(Call<MyRes> call, Response<MyRes> response) {

                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }

                                if (response.isSuccessful()) {

                                    if (response.body().getMsg().equalsIgnoreCase("true")) {

                                        SharedPreferences.Editor editor = Utils.writeToPreference(Constants.MY_PREF, ForgotPwd22Activity.this);
                                        editor.putString(Constants.REG_IN_PROGRESS, "");
                                        editor.putString(Constants.USER_NAME,"");
                                        editor.apply();

                                        Toast.makeText(ForgotPwd22Activity.this, "Successfully registered", Toast.LENGTH_SHORT).show();
                                        finishAffinity();

                                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                        startActivity(intent);

                                    } else {
                                        Toast.makeText(ForgotPwd22Activity.this, "Not Successfully registered", Toast.LENGTH_SHORT).show();

                                    }


                                } else {
                                    Toast.makeText(ForgotPwd22Activity.this, "Not Successfully registered", Toast.LENGTH_SHORT).show();

                                }


                            }

                            @Override
                            public void onFailure(Call<MyRes> call, Throwable t) {
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }

                                Toast.makeText(ForgotPwd22Activity.this, "Not Successfully registered", Toast.LENGTH_SHORT).show();

                            }
                        });

                    }
                } else {
                    //forgot pwd


                    if (edtOTP.getText().toString().trim().isEmpty()) {
                        Toast.makeText(ForgotPwd22Activity.this, "Please enter OTP", Toast.LENGTH_SHORT).show();

                    } else {
                        showProgressDialog();

                        sendOTP.senotp(id, edtOTP.getText().toString().trim()).enqueue(new Callback<MyRes>() {
                            @Override
                            public void onResponse(Call<MyRes> call, Response<MyRes> response) {

                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }

                                if (response.isSuccessful()) {

                                    if (response.body().getMsg().equalsIgnoreCase("true")) {

                                        Intent intent = new Intent(ForgotPwd22Activity.this, ResetActivity.class);
                                        intent.putExtra(Constants.ID, id);
                                        startActivity(intent);


                                    } else {
                                        Toast.makeText(ForgotPwd22Activity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                                    }


                                } else {
                                    Toast.makeText(ForgotPwd22Activity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                                }


                            }

                            @Override
                            public void onFailure(Call<MyRes> call, Throwable t) {
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }

                                Toast.makeText(ForgotPwd22Activity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }


                }


            }
        });


        btnResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - lastclick < DOUBLE_TAP) {
                    return;
                }
                lastclick = SystemClock.elapsedRealtime();
                //resend

                if (id != null && TYPE.equalsIgnoreCase("reg")) {


                    showProgressDialog();
                    resendOTP.resend(id).enqueue(new Callback<MyRes>() {
                        @Override
                        public void onResponse(Call<MyRes> call, Response<MyRes> response) {

                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }

                            if (response.isSuccessful()) {

                            } else {
                                Toast.makeText(ForgotPwd22Activity.this, "Try again", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<MyRes> call, Throwable t) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }

                            Toast.makeText(ForgotPwd22Activity.this, "Try again", Toast.LENGTH_SHORT).show();

                        }
                    });


                } else {
                    //forgot
                    showProgressDialog();
                    resendOTP.resend(id).enqueue(new Callback<MyRes>() {
                        @Override
                        public void onResponse(Call<MyRes> call, Response<MyRes> response) {

                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }

                            if (response.isSuccessful()) {

                            } else {
                                Toast.makeText(ForgotPwd22Activity.this, "Try again", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<MyRes> call, Throwable t) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }

                            Toast.makeText(ForgotPwd22Activity.this, "Try again", Toast.LENGTH_SHORT).show();

                        }
                    });


                }

            }
        });
    }

    public void showProgressDialog() {
        progressDialog = new ProgressDialog(ForgotPwd22Activity.this);
        progressDialog.setTitle("Loading..");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }


    interface SendOTP {

        @POST("user/verifycodeapi/")
        @FormUrlEncoded
        Call<MyRes> senotp(@Field("id") String id,
                           @Field("code") String code
        );
    }


    interface ResendOTP {

        @POST("user/resetcodeapi/")
        @FormUrlEncoded
        Call<MyRes> resend(@Field("id") String id

        );


    }

    @Override
    protected void onResume() {
        super.onResume();

        Utils.hideSoftKeyBoard(this.getCurrentFocus(),ForgotPwd22Activity.this);
    }
}
