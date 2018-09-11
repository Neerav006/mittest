package com.codefuelindia.mudraitsolution.view

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock

import android.util.Log
import android.view.View

import android.widget.Toast
import com.bumptech.glide.Glide

import com.codefuelindia.mudraitsolution.R
import com.codefuelindia.mudraitsolution.Utils.Utils
import com.codefuelindia.mudraitsolution.common.RetrofitClient
import com.codefuelindia.mudraitsolution.cons.Constants
import com.codefuelindia.mudraitsolution.model.MyRes
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.dummy_progress_view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

class LoginActivity : AppCompatActivity() {


    private val DOUBLE_TAP: Long = 1500
    private val root = "BA326C6BC3AD52ED2861784D680D2348F2F8DFB9131DF2AAECBFBE20C1884FF7"
    private var lastclick: Long = 0

    private var loginAPI: LoginAPI? = null
    private var reg_in_progress: String? = ""
    private var user_name: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val sharedPreferences = Utils.getSharedPreference(Constants.MY_PREF, applicationContext)
        loginAPI = getAPIService(Constants.BASE_URL)


        if (sharedPreferences.getBoolean(Constants.IS_LOGIN, false)) {

            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
            finish()


        }


        btnLogin!!.setOnClickListener {
            if (SystemClock.elapsedRealtime() - lastclick < DOUBLE_TAP) {
                Log.e("double tap", "")
                return@setOnClickListener
            }
            lastclick = SystemClock.elapsedRealtime()

            when {
                edtUserName.getText().toString().trim { it <= ' ' }.isEmpty() -> {
                    edtUserName.requestFocus()
                    Toast.makeText(this@LoginActivity, "Enter username", Toast.LENGTH_SHORT).show()
                }
                edtPassword.getText().toString().trim { it <= ' ' }.isEmpty() -> {
                    edtPassword.requestFocus()
                    Toast.makeText(this@LoginActivity, "Enter password", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    constraintTop.visibility = View.GONE
                    includeDummy.visibility = View.VISIBLE

                    Glide.with(this@LoginActivity).asGif().load(R.drawable.progress).into(ivProgress!!)

                    loginAPI!!.login(edtUserName.text.toString().trim { it <= ' ' },
                            edtPassword.text.toString().trim { it <= ' ' }).enqueue(object : Callback<MyRes> {
                        override fun onResponse(call: Call<MyRes>, response: Response<MyRes>) {


                            if (response.isSuccessful) {

                                if (response.body() != null && response.body()!!.msg.equals("true", true)) {

                                    //save state in preference

                                    val editor = Utils.writeToPreference(Constants.MY_PREF, this@LoginActivity)
                                    editor.putString(Constants.ID, response.body()!!.id)
                                    editor.putString(Constants.USER_NAME, response.body()!!.user_name)
                                    editor.putString(Constants.NAME, response.body()!!.name)
                                    editor.putString(Constants.ROLE, response.body()!!.role)
                                    editor.putString(Constants.IS_REGISTERED, response.body()!!.isPremium)
                                    editor.putBoolean(Constants.IS_LOGIN, true)
                                    editor.putString(Constants.MEMBER_ID, response.body()!!.mem_id)
                                    editor.putString(Constants.MEMBER_STATUS, response.body()!!.status)
                                    editor.putString(Constants.GOR, response.body()!!.gor)
                                    editor.putString("family_count", response.body()!!.family_count)
                                    editor.putString("addr", response.body()!!.addr)
                                    editor.putString("gr_count", response.body()!!.group_count)
                                    editor.apply()

                                    val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    constraintTop.visibility = View.VISIBLE
                                    includeDummy.visibility = View.GONE

                                    Toast.makeText(this@LoginActivity, "Invalid credential", Toast.LENGTH_SHORT).show()

                                }

                            } else {

                                //dummy
                                constraintTop.visibility = View.VISIBLE
                                includeDummy.visibility = View.GONE

                                Toast.makeText(this@LoginActivity, "Connection error", Toast.LENGTH_SHORT).show()


                            }


                        }

                        override fun onFailure(call: Call<MyRes>, t: Throwable) {

                            constraintTop.visibility = View.VISIBLE
                            includeDummy.visibility = View.GONE
                            Toast.makeText(this@LoginActivity, "Connection error", Toast.LENGTH_SHORT).show()


                        }
                    })


                }
            }
        }



        tvForgot.setOnClickListener {
            if (SystemClock.elapsedRealtime() - lastclick < DOUBLE_TAP) {
                return@setOnClickListener
            }
            lastclick = SystemClock.elapsedRealtime()

            val intent = Intent(this@LoginActivity, ForgotPwdActivity::class.java)
            startActivity(intent)
        }


    }


    internal fun getAPIService(baseUrl: String): LoginAPI {

        return RetrofitClient.getClient(baseUrl).create(LoginAPI::class.java)
    }

    override fun onResume() {
        super.onResume()
    }

    internal interface LoginAPI {

        @POST("user/loginapi/")
        @FormUrlEncoded
        fun login(@Field("email") username: String,
                  @Field("password") pwd: String
        ): Call<MyRes>
    }
}
