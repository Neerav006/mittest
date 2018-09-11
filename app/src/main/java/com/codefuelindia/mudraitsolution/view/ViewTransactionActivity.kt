package com.codefuelindia.mudraitsolution.view

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.core.text.italic
import androidx.core.text.underline
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

import com.codefuelindia.mudraitsolution.R
import com.codefuelindia.mudraitsolution.R.id.constraint_view_transactionTop
import com.codefuelindia.mudraitsolution.R.id.italic
import com.codefuelindia.mudraitsolution.Utils.GlideApp
import com.codefuelindia.mudraitsolution.Utils.Utils
import com.codefuelindia.mudraitsolution.common.RetrofitClient
import com.codefuelindia.mudraitsolution.cons.Constants
import com.codefuelindia.mudraitsolution.model.Datum
import com.codefuelindia.mudraitsolution.model.MyRes
import com.codefuelindia.mudraitsolution.model.SearchHistory
import kotlinx.android.synthetic.main.content_view_transaction.*
import kotlinx.android.synthetic.main.row_view_history.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.text.SimpleDateFormat
import java.util.*

class ViewTransactionActivity : AppCompatActivity() {

    var arr = intArrayOf(12,9,1,0,4,2)

    lateinit var searchHistory: SearchHistory
    lateinit var latestSearch: LatestSearch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_transaction)

       

        searchHistory = getService(Constants.BASE_URL)
        latestSearch = RetrofitClient.getClient(Constants.BASE_URL).create(LatestSearch::class.java)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayShowTitleEnabled(false)

        toolbar.title = "View JobCard"

        val fab = findViewById<View>(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            finish()
        }


        edtJobCardNo.setOnEditorActionListener { v, actionId, event ->

            if (actionId == EditorInfo.IME_ACTION_DONE) {

                if (edtJobCardNo.text.toString().trim().isEmpty() && edtMobile.text.toString().trim().isEmpty()
                        && edtName.text.toString().trim().isEmpty()) {

                    Toast.makeText(this@ViewTransactionActivity, "Enter search criteria", Toast.LENGTH_LONG).show()


                } else {
                    // ok lets search

                    val dialog = showLoader()

                    searchHistory.viewHistory(edtJobCardNo.text.toString().trim()
                            , edtMobile.text.toString().trim(), edtName.text.toString().trim()).enqueue(
                            object : Callback<com.codefuelindia.mudraitsolution.model.SearchHistory> {
                                override fun onFailure(call: Call<com.codefuelindia.mudraitsolution.model.SearchHistory>?, t: Throwable?) {

                                    Utils.hideSoftKeyBoard(constraint_view_transactionTop, this@ViewTransactionActivity)


                                    Toast.makeText(this@ViewTransactionActivity, "Connection error", Toast.LENGTH_LONG).show()
                                    dialog.dismiss()

                                }

                                override fun onResponse(call: Call<com.codefuelindia.mudraitsolution.model.SearchHistory>?, response: Response<com.codefuelindia.mudraitsolution.model.SearchHistory>?) {
                                    Utils.hideSoftKeyBoard(constraint_view_transactionTop, this@ViewTransactionActivity)

                                    dialog.dismiss()
                                    if (response!!.isSuccessful) {

                                        val customerlist = response.body()!!.data

                                        if (customerlist != null && customerlist.size > 0) {


                                            customerlist.sortByDescending { SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(it.customermaster.date) }

                                            rvList.adapter = CustomAdapter(customerlist as ArrayList<Datum>)

                                        } else {
                                            Toast.makeText(this@ViewTransactionActivity, "No result found", Toast.LENGTH_LONG).show()
                                            rvList.adapter = CustomAdapter(ArrayList<Datum>())
                                        }


                                    } else {
                                        Toast.makeText(this@ViewTransactionActivity, "Connection error", Toast.LENGTH_LONG).show()
                                    }


                                }


                            }
                    )


                }
                return@setOnEditorActionListener true
            }

            false
        }


        rvList.layoutManager = LinearLayoutManager(this@ViewTransactionActivity, LinearLayoutManager.VERTICAL, false)
        val dialog = showLoader()


        latestSearch.latestJobCard().enqueue(object : Callback<com.codefuelindia.mudraitsolution.model.SearchHistory> {
            override fun onFailure(call: Call<com.codefuelindia.mudraitsolution.model.SearchHistory>?, t: Throwable?) {
                Utils.hideSoftKeyBoard(constraint_view_transactionTop, this@ViewTransactionActivity)

                if (dialog.isShowing) {
                    dialog.dismiss()
                }


            }

            override fun onResponse(call: Call<com.codefuelindia.mudraitsolution.model.SearchHistory>?, response: Response<com.codefuelindia.mudraitsolution.model.SearchHistory>?) {
                Utils.hideSoftKeyBoard(constraint_view_transactionTop, this@ViewTransactionActivity)

                if (dialog.isShowing) {
                    dialog.dismiss()
                }

                if (response!!.isSuccessful) {

                    val customerlist = response.body()!!.data

                    if (customerlist != null && customerlist.size > 0) {


                        customerlist.sortByDescending { SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(it.customermaster.date) }

                        rvList.adapter = CustomAdapter(customerlist as ArrayList<Datum>)

                    } else {
                        Toast.makeText(this@ViewTransactionActivity, "No result found", Toast.LENGTH_LONG).show()
                        rvList.adapter = CustomAdapter(ArrayList<Datum>())
                    }


                } else {
                    Toast.makeText(this@ViewTransactionActivity, "Connection error", Toast.LENGTH_LONG).show()
                }


            }


        })





        btnSearch.setOnClickListener {

            if (edtJobCardNo.text.toString().trim().isEmpty() && edtMobile.text.toString().trim().isEmpty()
                    && edtName.text.toString().trim().isEmpty()) {

                Toast.makeText(this@ViewTransactionActivity, "Enter search criteria", Toast.LENGTH_LONG).show()


            } else {
                // ok lets search

                val dialog = showLoader()

                searchHistory.viewHistory(edtJobCardNo.text.toString().trim()
                        , edtMobile.text.toString().trim(), edtName.text.toString().trim()).enqueue(
                        object : Callback<com.codefuelindia.mudraitsolution.model.SearchHistory> {
                            override fun onFailure(call: Call<com.codefuelindia.mudraitsolution.model.SearchHistory>?, t: Throwable?) {

                                Utils.hideSoftKeyBoard(constraint_view_transactionTop, this@ViewTransactionActivity)


                                Toast.makeText(this@ViewTransactionActivity, "Connection error", Toast.LENGTH_LONG).show()
                                dialog.dismiss()

                            }

                            override fun onResponse(call: Call<com.codefuelindia.mudraitsolution.model.SearchHistory>?, response: Response<com.codefuelindia.mudraitsolution.model.SearchHistory>?) {
                                Utils.hideSoftKeyBoard(constraint_view_transactionTop, this@ViewTransactionActivity)

                                dialog.dismiss()
                                if (response!!.isSuccessful) {

                                    val customerlist = response.body()!!.data

                                    if (customerlist != null && customerlist.size > 0) {


                                        customerlist.sortByDescending { SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(it.customermaster.date) }

                                        rvList.adapter = CustomAdapter(customerlist as ArrayList<Datum>)

                                    } else {
                                        Toast.makeText(this@ViewTransactionActivity, "No result found", Toast.LENGTH_LONG).show()
                                        rvList.adapter = CustomAdapter(ArrayList<Datum>())
                                    }


                                } else {
                                    Toast.makeText(this@ViewTransactionActivity, "Connection error", Toast.LENGTH_LONG).show()
                                }


                            }


                        }
                )


            }


        }


    }

    /**
     * Provide views to RecyclerView with data from dataSet.
     *
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
     */
    private inner class CustomAdapter(private val dataSet: ArrayList<Datum>) :
            RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

        val TAG = "Recycler"

        /**
         * Provide a reference to the type of views that you are using (custom ViewHolder)
         */
        inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            val tvName: TextView
            val tvMobile: TextView
            val tvJobCardNo: TextView
            val productList: TextView
            val tvDateTime: TextView
            val tvBrandName: TextView
            val tvStatus: TextView


            init {
                // Define click listener for the ViewHolder's View.
                v.setOnClickListener {

                    val intent = Intent(this@ViewTransactionActivity, TransactionDetailActivity::class.java)
                    intent.putExtra("data", dataSet[adapterPosition])
                    startActivity(intent)


                }
                tvName = v.findViewById(R.id.tvName)
                tvMobile = v.findViewById(R.id.tvMobile)
                tvJobCardNo = v.findViewById(R.id.tvJobCardNo)
                productList = v.findViewById(R.id.tvProductList)
                tvDateTime = v.findViewById(R.id.tvDateTime)
                tvBrandName = v.findViewById(R.id.tvBrandName)
                tvStatus = v.findViewById(R.id.tvStatus)


                tvMobile.setOnClickListener {
                    val intent = Intent(Intent.ACTION_DIAL)
                    intent.data = Uri.parse("tel:".plus(dataSet[adapterPosition].customermaster.mobile))
                    startActivity(intent)
                }
            }
        }

        // Create new views (invoked by the layout manager)
        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            // Create a new view.
            val v = LayoutInflater.from(viewGroup.context)
                    .inflate(R.layout.row_view_history, viewGroup, false)

            return ViewHolder(v)
        }

        // Replace the contents of a view (invoked by the layout manager)
        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
            Log.d(TAG, "Element $position set.")

            // Get element from your dataset at this position and replace the contents of the view
            // with that element
            viewHolder.tvName.text = dataSet[position].customermaster.customerName
            viewHolder.tvMobile.text = dataSet[position].customermaster.mobile
            viewHolder.tvJobCardNo.text = "JobCard: " + dataSet[position].customermaster.jobNumber
            viewHolder.tvDateTime.text = "Date: ".plus(getYMDDate(dataSet[position].customermaster.date))
            //   viewHolder.tvBrandName.text = "Brand: ".plus(dataSet[position].customermaster.brandName)


            when (dataSet[position].customermaster.ready_status) {

                "0" -> {
                    if(dataSet[position].customermaster.status == "1"){
                        viewHolder.tvStatus.text = "Delivered."
                        viewHolder.tvStatus.setTextColor(Color.GREEN)

                    }
                    else{
                        viewHolder.tvStatus.text = " "
                    }
                }

                "1" -> {

                    if(dataSet[position].customermaster.status == "1"){
                        viewHolder.tvStatus.text = "Delivered."
                        viewHolder.tvStatus.setTextColor(Color.GREEN)

                    }
                    else{
                        viewHolder.tvStatus.text = "Ready for Dispatch"
                        viewHolder.tvStatus.setTextColor(Color.GREEN)
                    }


                }

            }


            val builder = SpannableStringBuilder("Brand: ")
                    .bold { append(dataSet[position].customermaster.brandName) }

            viewHolder.tvBrandName.text = builder
            val stringBuilder = StringBuilder()

            if (dataSet[position].customermaster.pc != "0") {
                stringBuilder.append(dataSet[position].customermaster.pc + " ")
            }

            if (dataSet[position].customermaster.laptop != "0") {
                stringBuilder.append(dataSet[position].customermaster.laptop + " ")
            }

            if (dataSet[position].customermaster.miniNotebook != "0") {
                stringBuilder.append(dataSet[position].customermaster.miniNotebook + " ")
            }


            if (dataSet[position].customermaster.printerAiopsc != "0") {
                stringBuilder.append(dataSet[position].customermaster.printerAiopsc + " ")
            }

            if (dataSet[position].customermaster.allInPcTablate != "0") {
                stringBuilder.append(dataSet[position].customermaster.allInPcTablate + " ")
            }


            viewHolder.productList.text = stringBuilder.toString()

        }

        // Return the size of your dataset (invoked by the layout manager)
        override fun getItemCount() = dataSet.size


    }

    interface SearchHistory {

        @POST("user/getcustomer_api/")
        @FormUrlEncoded
        fun viewHistory(@Field("job") job: String, @Field("mobile") mobile: String
                        , @Field("name") name: String): Call<com.codefuelindia.mudraitsolution.model.SearchHistory>

    }


    interface LatestSearch {
        @POST("user/latest_jobcardlistapi")
        fun latestJobCard(): Call<com.codefuelindia.mudraitsolution.model.SearchHistory>

    }


    internal fun getService(baseUrl: String): SearchHistory {
        return RetrofitClient.getClient(baseUrl).create(SearchHistory::class.java)
    }

    fun showLoader(): AlertDialog {

        val dialogBuilder = AlertDialog.Builder(this@ViewTransactionActivity as Context)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.alert_label_editor, null)
        dialogBuilder.setView(dialogView)

        GlideApp.with(this@ViewTransactionActivity)
                .load(this@ViewTransactionActivity.getDrawable(R.drawable.loader)).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into(dialogView.findViewById<ImageView>(R.id.ivGif))
        dialogBuilder.setCancelable(false)
        val alertDialog = dialogBuilder.create()
        alertDialog.window.setBackgroundDrawable(this@ViewTransactionActivity.getDrawable(android.R.color.transparent))
        alertDialog.show()

        return alertDialog


    }

    fun getYMDDate(dateString: String): String {


//        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
//        val date = format.parse(dateString)

        val dateTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(dateString)
        val newstring = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(dateTime)

        Log.e("formated date", newstring)

        return newstring


    }


}
