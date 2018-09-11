package com.codefuelindia.mudraitsolution.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.codefuelindia.mudraitsolution.R
import com.codefuelindia.mudraitsolution.Utils.GlideApp
import com.codefuelindia.mudraitsolution.common.RetrofitClient
import com.codefuelindia.mudraitsolution.cons.Constants
import com.codefuelindia.mudraitsolution.model.Datum
import com.codefuelindia.mudraitsolution.model.SearchHistory

import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.content_search.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.POST

class SearchActivity : AppCompatActivity() {


    lateinit var getTodaysInvoice: GetTodaysInvoice

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setSupportActionBar(toolbar)
        getTodaysInvoice = getService(Constants.BASE_URL)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        toolbar.title="Today's Delivery JobCard"

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        rvList.layoutManager = LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)

        val dialog = showLoader()

        getTodaysInvoice.getTodayInvoice().enqueue(object : Callback<SearchHistory> {
            override fun onFailure(call: Call<SearchHistory>?, t: Throwable?) {

                dialog.dismiss()
                Toast.makeText(this@SearchActivity, "Connection error", Toast.LENGTH_LONG).show()


            }

            override fun onResponse(call: Call<SearchHistory>?, response: Response<SearchHistory>?) {

                dialog.dismiss()

                if (response!!.isSuccessful) {

                    val todayInvoiceList = response!!.body()
                    if (todayInvoiceList!!.data!=null && todayInvoiceList!!.data.size > 0) {

                        val customAdapter = CustomAdapter(todayInvoiceList.data as ArrayList<Datum>)
                        rvList.adapter = customAdapter

                    } else {
                        Toast.makeText(this@SearchActivity, "No data found", Toast.LENGTH_LONG).show()
                        val customAdapter = CustomAdapter(ArrayList<Datum>())
                        rvList.adapter = customAdapter
                    }


                }


            }


        })


    }


    interface GetTodaysInvoice {
        @POST("user/view_transaction_api/")
        fun getTodayInvoice(): Call<SearchHistory>
    }

    private fun getService(baseUrl: String): GetTodaysInvoice {
        return RetrofitClient.getClient(baseUrl).create(GetTodaysInvoice::class.java)
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



            init {
                // Define click listener for the ViewHolder's View.
                v.setOnClickListener {

                    val intent = Intent(this@SearchActivity, TransactionDetailActivity::class.java)
                    intent.putExtra("data", dataSet[adapterPosition])
                    startActivity(intent)


                }
                tvName = v.findViewById(R.id.tvName)
                tvMobile = v.findViewById(R.id.tvMobile)
                tvJobCardNo = v.findViewById(R.id.tvJobCardNo)
                productList = v.findViewById(R.id.tvProductList)

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
            viewHolder.tvJobCardNo.text = dataSet[position].customermaster.jobNumber

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

    fun showLoader(): AlertDialog {

        val dialogBuilder = AlertDialog.Builder(this@SearchActivity as Context)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.alert_label_editor, null)
        dialogBuilder.setView(dialogView)

        GlideApp.with(this@SearchActivity)
                .load(this@SearchActivity.getDrawable(R.drawable.loader)).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into(dialogView.findViewById<ImageView>(R.id.ivGif))
        dialogBuilder.setCancelable(false)
        val alertDialog = dialogBuilder.create()
        alertDialog.window.setBackgroundDrawable(this@SearchActivity.getDrawable(android.R.color.transparent))
        alertDialog.show()

        return alertDialog


    }


}
