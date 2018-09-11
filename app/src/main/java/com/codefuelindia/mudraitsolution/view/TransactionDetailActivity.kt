package com.codefuelindia.mudraitsolution.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.SystemClock
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.codefuelindia.mudraitsolution.BuildConfig
import com.codefuelindia.mudraitsolution.R
import com.codefuelindia.mudraitsolution.Utils.GlideApp
import com.codefuelindia.mudraitsolution.common.RetrofitClient
import com.codefuelindia.mudraitsolution.cons.Constants
import com.codefuelindia.mudraitsolution.model.Datum
import com.codefuelindia.mudraitsolution.model.MyRes
import com.cunoraz.gifview.library.GifView

import kotlinx.android.synthetic.main.activity_transaction_detail.*
import kotlinx.android.synthetic.main.content_transaction_detail.*
import kotlinx.android.synthetic.main.content_view_transaction.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Path
import java.io.*
import java.security.spec.ECField

class TransactionDetailActivity : AppCompatActivity() {

    lateinit var datum: Datum
    private var lastclick = 0L
    private lateinit var payInvoice: PayInvoice
    private val PERMISSSION_WRITE_EXTERNAL = 200
    val stringBuilder = StringBuilder()
    val statusOnArrivalInfo = StringBuilder()
    val innerStringBuilder = StringBuilder()
    private lateinit var getPdfInvoice: GetPdfInvoice
    private lateinit var getPdfInvocie2: GetPdfInvocie2
    private var invoiceUrl = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_detail)
        setSupportActionBar(toolbar)
        payInvoice = getService(Constants.BASE_URL)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        getPdfInvoice = RetrofitClient.getClient(Constants.BASE_URL).create(GetPdfInvoice::class.java)
        getPdfInvocie2 = RetrofitClient.getClient(Constants.BASE_URL).create(GetPdfInvocie2::class.java)

        toolbar.title = "Transaction Detail"

        if (intent != null) {
            datum = intent.getParcelableExtra("data")
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }


        if (datum != null) {

            txtCustomerName.text = datum.customermaster.customerName
            txtAddress.text = datum.customermaster.address
            textView5.text = datum.customermaster.mobile
            txtJobCardNumber.text = datum.customermaster.jobNumber
            tvDateTime.text = datum.customermaster.date
            tvBrandName.text = datum.customermaster.brandName
            textView4.text = datum.customermaster.advancePay //todo
            tvIssue.text = datum.customermaster.issue

            textView5.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:".plus(datum.customermaster.mobile))
                startActivity(intent)
            }


            val remainingAmount: Double = if (datum.customermaster.advancePay.isEmpty()) {
                datum.customermaster.remaining_amount.toDouble()
            } else {
                datum.customermaster.remaining_amount.toDouble() - datum.customermaster.advancePay.toDouble()

            }

            txtCustomerRemainingPayment.text = remainingAmount.toString()// todo

            if (datum.customermaster.total_amount.isNotEmpty()) {

                textView4.text = datum.customermaster.advancePay + " " + "( Received Amt:- " + (datum.customermaster.total_amount.toDouble() -
                        datum.customermaster.remaining_amount.toDouble()) + " )"


            }


            if (datum.customermaster.total_amount_status == "0") {
                edtTotalAmount.isEnabled = true
                edtTotalAmount.isClickable = true
                edtTotalAmount.isFocusable = true

                if (datum.customermaster.total_amount.isEmpty()) {

                    if (datum.customermaster.estCharge.isNotEmpty()) {
                        edtTotalAmount.setText(datum.customermaster.estCharge.trim())

                    }


                } else {
                    edtTotalAmount.setText(datum.customermaster.total_amount)

                }


            } else {
                edtReceivedAmount.setText(remainingAmount.toString())

                edtTotalAmount.isEnabled = false
                edtTotalAmount.isClickable = false
                edtTotalAmount.isFocusable = false

                edtTotalAmount.setText(datum.customermaster.total_amount)


            }


            when {
                datum.customermaster.status == "0" -> { //pending
                    txtLabelTotalPayment.visibility = View.GONE
                    txtTotalPayment.visibility = View.GONE
                    txtStatus.text = "Pending"// todo
                    txtStatus.setTextColor(Color.RED)
                    txtInTotalAmount.visibility = View.VISIBLE
                    txtInReceivedAmount.visibility = View.VISIBLE
                    btnPay.visibility = View.VISIBLE


                    btnPay.setOnClickListener {

                        if (SystemClock.elapsedRealtime() - lastclick < 2000) {
                            return@setOnClickListener
                        }

                        lastclick = SystemClock.elapsedRealtime()

                        if ((datum.customermaster.total_amount_status == "0" && edtTotalAmount.text.toString().trim().isEmpty())
                                || (datum.customermaster.total_amount_status == "0" && edtTotalAmount.text.toString().trim().toDouble() <= 0.0)
                        ) {
                            edtTotalAmount.error = "Total charge required.."
                        } else {

                            if (edtReceivedAmount.text.toString().trim().isEmpty() || edtReceivedAmount.text.toString().trim().toDouble() <= 0.0) {
                                edtReceivedAmount.error = "Received amount required"
                            } else {
                                // all ok

                                //val dialog = showLoader()

                                btnPay.isEnabled = false
                                addPaymentForService()

                            }

                        }


                    }


                }
                datum.customermaster.status == "1" -> { //confirmed
                    txtLabelTotalPayment.visibility = View.VISIBLE
                    txtTotalPayment.visibility = View.VISIBLE
                    txtStatus.text = "Confirmed"// todo
                    txtStatus.setTextColor(Color.GREEN)
                    txtTotalPayment.text = datum.customermaster.total_amount
                    txtInReceivedAmount.visibility = View.GONE
                    txtInTotalAmount.visibility = View.GONE
                    txtLabelCustomerRemainingPayment.visibility = View.GONE
                    txtCustomerRemainingPayment.visibility = View.GONE
                    btnPay.visibility = View.GONE


                }
                datum.customermaster.status == "2" -> { //canceled
                    txtLabelTotalPayment.visibility = View.VISIBLE
                    txtTotalPayment.visibility = View.VISIBLE
                    txtInTotalAmount.visibility = View.GONE
                    txtInReceivedAmount.visibility = View.GONE
                    btnPay.visibility = View.GONE
                    txtStatus.text = "Canceled"// todo
                    txtStatus.setTextColor(Color.RED)

                }
            }




            if (datum.customermaster.pc != "0") {
                stringBuilder.append(datum.customermaster.pc + " ")
            }

            if (datum.customermaster.laptop != "0") {
                stringBuilder.append(datum.customermaster.laptop + " ")
            }

            if (datum.customermaster.miniNotebook != "0") {
                stringBuilder.append(datum.customermaster.miniNotebook + " ")
            }


            if (datum.customermaster.printerAiopsc != "0") {
                stringBuilder.append(datum.customermaster.printerAiopsc + " ")
            }

            if (datum.customermaster.allInPcTablate != "0") {
                stringBuilder.append(datum.customermaster.allInPcTablate + " ")
            }

            txtProduct.text = stringBuilder.toString()


            /**
             *  Innner product
             */


            if (datum.customermaster.adapter != "0") {
                innerStringBuilder.append(datum.customermaster.adapter + ", ")
            }

            if (datum.customermaster.powerCord != "0") {
                innerStringBuilder.append(datum.customermaster.powerCord + ", ")
            }

            if (datum.customermaster.battery != "0") {
                innerStringBuilder.append(datum.customermaster.battery + ", ")
            }


            if (datum.customermaster.drive != "0") {
                innerStringBuilder.append(datum.customermaster.drive + ", ")
            }

            if (datum.customermaster.bag != "0") {
                innerStringBuilder.append(datum.customermaster.bag + ", ")
            }

            if (datum.customermaster.osDisk != "0") {
                innerStringBuilder.append(datum.customermaster.osDisk + ", ")
            }

            if (datum.customermaster.driveDisk != "0") {
                innerStringBuilder.append(datum.customermaster.driveDisk + ", ")
            }


            if (datum.customermaster.printerDriveDisk != "0") {
                innerStringBuilder.append(datum.customermaster.printerDriveDisk + ", ")
            }

            if (datum.customermaster.printerAdapter != "0") {
                innerStringBuilder.append(datum.customermaster.printerAdapter + " ")
            }

            tvInnerProducts.text = innerStringBuilder.toString()


            /**
             *   Status on arrival
             *
             */


            if (datum.customermaster.deadNoPower != "0") {
                statusOnArrivalInfo.append(datum.customermaster.deadNoPower + ", ")
            }

            if (datum.customermaster.dimDisplay != "0") {
                statusOnArrivalInfo.append(datum.customermaster.dimDisplay + ", ")
            }

            if (datum.customermaster.bodyDamage != "0") {
                statusOnArrivalInfo.append(datum.customermaster.bodyDamage + ", ")
            }


            if (datum.customermaster.noBootup != "0") {
                statusOnArrivalInfo.append(datum.customermaster.noBootup + ", ")
            }

            if (datum.customermaster.os_reload != "0") {
                statusOnArrivalInfo.append(datum.customermaster.os_reload + ", ")
            }

            if (datum.customermaster.servicing != "0") {
                statusOnArrivalInfo.append(datum.customermaster.servicing + ", ")
            }

            if (datum.customermaster.display_fliker != "0") {
                statusOnArrivalInfo.append(datum.customermaster.display_fliker + ", ")
            }


            if (datum.customermaster.body_repair != "0") {
                statusOnArrivalInfo.append(datum.customermaster.body_repair + " ")
            }

            tvStatusArrival.text = statusOnArrivalInfo.toString()


        }


    }

    interface PayInvoice {

        @POST("user/addtransaction_api/")
        @FormUrlEncoded
        fun payInvocieApi(@Field("job_number") job_number: String
                          , @Field("parent_id") parent_id: String,
                          @Field("total_amount") total_amount: String,
                          @Field("received_amount") received_amount: String,
                          @Field("remain_amount") remain_amount: String,
                          @Field("advance_pay") advance_pay: String): Call<MyRes>

    }

    internal fun getService(baseUrl: String): PayInvoice {
        return RetrofitClient.getClient(baseUrl).create(PayInvoice::class.java)
    }


    fun showLoader(): AlertDialog {

        val dialogBuilder = AlertDialog.Builder(this@TransactionDetailActivity as Context)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.row_alert_dialog22, null)
        dialogBuilder.setView(dialogView)
        val gifView = dialogView.findViewById<GifView>(R.id.ivGif)

        gifView.gifResource = R.drawable.loader22

        gifView.play()


//        GlideApp.with(this@TransactionDetailActivity)
//                .load(this@TransactionDetailActivity.getDrawable(R.drawable.loader))
//                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
//                .into(gifView)


        dialogBuilder.setCancelable(false)
        val alertDialog = dialogBuilder.create()
        alertDialog.window.setLayout(600, 400)
        alertDialog.window.setBackgroundDrawable(this@TransactionDetailActivity.getDrawable(android.R.color.transparent))
        alertDialog.show()
        return alertDialog


    }


    private fun addPaymentForService() {

        val builder = AlertDialog.Builder(this@TransactionDetailActivity)
        // Set the alert dialog title
        builder.setTitle("Add Payment")

        // Display a message on alert dialog
        builder.setMessage("Are you sure to continue?")

        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton("YES") { dialog, which ->
            // Do something when user press the positive button

            val dialog = showLoader()

            payInvoice.payInvocieApi(
                    datum.customermaster.jobNumber,
                    datum.customermaster.id,
                    edtTotalAmount.text.toString().trim(),
                    edtReceivedAmount.text.toString().trim(),
                    datum.customermaster.remaining_amount,
                    datum.customermaster.advancePay
            ).enqueue(object : Callback<MyRes> {
                override fun onFailure(call: Call<MyRes>?, t: Throwable?) {

                    dialog.dismiss()
                    btnPay.isEnabled = true
                    Toast.makeText(this@TransactionDetailActivity, "Transaction failed..", Toast.LENGTH_LONG).show()
                    finish()

                }

                override fun onResponse(call: Call<MyRes>?, response: Response<MyRes>?) {

                    dialog.dismiss()
                    if (response!!.isSuccessful) {
                        if (response.body()?.msg.equals("true", true)) {
                            Toast.makeText(this@TransactionDetailActivity,
                                    "Transaction success", Toast.LENGTH_LONG).show()
                            val intent = Intent(this@TransactionDetailActivity, HomeActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                            startActivity(intent)


                        }
                    } else {
                        dialog.dismiss()
                        btnPay.isEnabled = true
                        Toast.makeText(this@TransactionDetailActivity, "Transaction failed..", Toast.LENGTH_LONG).show()
                        finish()
                    }

                }

            })

        }


        // Display a negative button on alert dialog
        builder.setNegativeButton("No") { dialog, which ->
            btnPay.isEnabled = true
            dialog.dismiss()
        }

        val alertDialog: AlertDialog = builder.create()

        // Display the alert dialog on app interface
        alertDialog.show()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_pdf_view, menu)



        return super.onCreateOptionsMenu(menu)


    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if (item!!.itemId == R.id.action_pdf_view) {

            if (ContextCompat.checkSelfPermission(this@TransactionDetailActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this@TransactionDetailActivity,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                    ActivityCompat.requestPermissions(this@TransactionDetailActivity,
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            PERMISSSION_WRITE_EXTERNAL)

                    Toast.makeText(this@TransactionDetailActivity, "Write storage permission needed for pdf", Toast.LENGTH_LONG).show()

                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(this@TransactionDetailActivity,
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            PERMISSSION_WRITE_EXTERNAL)

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
                // permission is granted for


                //  createPdf()


//                val intent =Intent(this@TransactionDetailActivity,PdfViewActivity::class.java)
//                intent.putExtra("pdf","http://go2hitech.com/jms/user/download_invoice/${datum.customermaster.id}.pdf")
//                startActivity(intent)
                val dialog = showLoader()
                dialog.show()


                if (datum.customermaster.status == "1") {
                    getPdfInvocie2.pdfInvoice("1", datum.customermaster.id).enqueue(object : Callback<ResponseBody> {
                        override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {


                            dialog.dismiss()
                        }

                        override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {

                            dialog.dismiss()
                            if (response!!.isSuccessful) {

                                Log.e("response", response.body().toString())


                                Log.e("is ok", writeResponseBodyToDisk(response!!.body()).toString())


                            }


                        }

                    })

                } else {
                    getPdfInvoice.pdfInvoice("1", datum.customermaster.id).enqueue(object : Callback<ResponseBody> {
                        override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {


                            dialog.dismiss()
                        }

                        override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {

                            dialog.dismiss()
                            if (response!!.isSuccessful) {

                                Log.e("response", response.body().toString())


                                Log.e("is ok", writeResponseBodyToDisk(response!!.body()).toString())


                            }


                        }

                    })

                }


            }




            return true
        }


        return super.onOptionsItemSelected(item)
    }


    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSSION_WRITE_EXTERNAL -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    createPdf()

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

    fun createPdf() {


        // B5 document size 500*710


        val remainingAmount: Double = if (datum.customermaster.advancePay.isEmpty()) {
            datum.customermaster.remaining_amount.toDouble()
        } else {
            datum.customermaster.remaining_amount.toDouble() - datum.customermaster.advancePay.toDouble()

        }


        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val pdfPage: PdfDocument.Page = pdfDocument.startPage(pageInfo)
        val canvas: Canvas = pdfPage.canvas

        val paint = Paint()
        paint.style = Paint.Style.STROKE
        paint.color = Color.BLACK

        canvas.drawRect(RectF(20f, 20f, 575f, 420f), paint)


        val paintHeader1 = Paint(Paint.ANTI_ALIAS_FLAG)
        paintHeader1.style = Paint.Style.FILL
        paintHeader1.color = Color.BLACK
        paintHeader1.textSize = 15f
        paintHeader1.textAlign = Paint.Align.CENTER


        val paintHeader2 = Paint(Paint.ANTI_ALIAS_FLAG)
        paintHeader2.style = Paint.Style.FILL
        paintHeader2.color = Color.parseColor("#4d4d4d")
        paintHeader2.textSize = 10f
        paintHeader2.textAlign = Paint.Align.CENTER


        val paintHeader3 = Paint(Paint.ANTI_ALIAS_FLAG)
        paintHeader3.style = Paint.Style.FILL
        paintHeader3.color = Color.BLACK
        paintHeader3.textSize = 15f
        paintHeader3.textAlign = Paint.Align.CENTER


//        canvas.drawText("Hi Tech", (pageInfo.pageWidth / 2f), 60f, paintHeader1)
//        canvas.drawText("Computer Services", (pageInfo.pageWidth / 2f), 72.5f, paintHeader2)
//         canvas.drawText("Multi brand IT Store", (pageInfo.pageWidth / 2f), 95f, paintHeader3)
//
//
//        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.hightechprintlogo)
//        val logoConatainer= Rect((pageInfo.pageWidth / 2f-75).toInt(),30,(pageInfo.pageWidth / 2f+75).toInt(),80)
//
//
//        canvas.drawBitmap(bitmap,null,logoConatainer,null)


        paintHeader2.textAlign = Paint.Align.LEFT
        paintHeader2.textSize = 12f
        paintHeader1.textSize = 12f


        /**   170f --  Customer name
         *    195f --  Job Card No
         *    220f --  Mobile No
         *    245f --  Address
         *    280f --  Product issue
         *    800f --  Date
         *
         *
         *    350f -- product name
         *    380f -- brand name
         *    410 --  serial no
         *    (45f,700f) --- Advance pay
         *    (200f,700f) -- total Amount
         *    (390,700f) -- Remaining amount
         *
         *    440f --- inner product
         *    470f --- status on arrival
         *
         *    510f -- estimated charge
         *    550f -- estimated time
         *
         *
         *    New Half size
         *
         *
         *
         *
         *
         *
         */


        canvas.drawText("Customer Name:", 40f, 40f, paintHeader2)
        canvas.drawText("Job card No:", 40f, 65f, paintHeader2)
        canvas.drawText("Mobile No:", 40f, 90f, paintHeader2)
        canvas.drawText("Address:", 40f, 115f, paintHeader2)
        canvas.drawText("Product Issue", 40f, 140f, paintHeader2)
        // canvas.drawText("Date: ", 40f, 800f, paintHeader2)

        paintHeader1.textAlign = Paint.Align.LEFT

        val tempRect = Rect()
        paintHeader2.getTextBounds("Customer Name:", 0, "Customer Name:".length, tempRect)
        canvas.drawText(datum.customermaster.customerName, tempRect.width() + 50f, 40f, paintHeader1)
        canvas.drawText(datum.customermaster.jobNumber, tempRect.width() + 50f, 65f, paintHeader1)
        canvas.drawText(datum.customermaster.mobile, tempRect.width() + 50f, 90f, paintHeader1)
        canvas.drawText(datum.customermaster.address, tempRect.width() + 50f, 115f, paintHeader1)
        canvas.drawText(datum.customermaster.issue, tempRect.width() + 50f, 140f, paintHeader1)
        // canvas.drawText(datum.customermaster.date, tempRect.width() + 50f, 800f, paintHeader1)

        paintHeader1.textAlign = Paint.Align.CENTER
        canvas.drawText("Product Detail", pageInfo.pageWidth / 2f, 160f, paintHeader1)

        paintHeader1.style = Paint.Style.STROKE
        canvas.drawRect(40f, 180f, 555f, 210f, paintHeader1)
        canvas.drawRect(40f, 210f, 555f, 240f, paintHeader1)
        canvas.drawRect(40f, 240f, 555f, 270f, paintHeader1)
        canvas.drawRect(40f, 270f, 555f, 300f, paintHeader1)
        canvas.drawRect(40f, 300f, 555f, 330f, paintHeader1)



        canvas.drawRect(40f, 340f, 555f, 370f, paintHeader1)
        canvas.drawRect(40f, 370f, 555f, 400f, paintHeader1)

        canvas.drawLine(190f, 340f, 190f, 400f, paintHeader1)
        canvas.drawLine(380f, 340f, 380f, 400f, paintHeader1)


        canvas.drawText("Product:", 45f, 200f, paintHeader2)
        canvas.drawText("Brand Name:", 45f, 230f, paintHeader2)
        canvas.drawText("Serial No:", 45f, 260f, paintHeader2)

        canvas.drawText("Accessories:", 45f, 290f, paintHeader2)
        canvas.drawText("Status: ", 45f, 320f, paintHeader2)

        canvas.drawText("Estimated Charge:- ", (pageInfo.pageWidth / 2f), 65f, paintHeader2)
        canvas.drawText("Estimated time:- ", (pageInfo.pageWidth / 2f), 90f, paintHeader2)

        canvas.drawText("Advance Pay:", 45f, 360f, paintHeader2)
        canvas.drawText("Total Amount:", 200f, 360f, paintHeader2)
        canvas.drawText("Remaining Amount:", 390f, 360f, paintHeader2)


        paintHeader1.style = Paint.Style.FILL
        paintHeader1.textAlign = Paint.Align.LEFT

        canvas.drawText(stringBuilder.toString(), tempRect.width() + 50f, 200f, paintHeader1)
        canvas.drawText(datum.customermaster.brandName, tempRect.width() + 50f, 230f, paintHeader1)
        canvas.drawText(datum.customermaster.srNo, tempRect.width() + 50f, 260f, paintHeader1)





        canvas.drawText(datum.customermaster.advancePay, 45f, 390f, paintHeader1)
        canvas.drawText(datum.customermaster.total_amount, 200f, 390f, paintHeader1)
        canvas.drawText(remainingAmount.toString(), 390f, 390f, paintHeader1)
        canvas.drawText(datum.customermaster.estCharge, (pageInfo.pageWidth / 2f) + 120f, 65f, paintHeader1)
        canvas.drawText(datum.customermaster.estTime, (pageInfo.pageWidth / 2f) + 120f, 90f, paintHeader1)


        paintHeader1.textSize = 10f

        canvas.drawText(innerStringBuilder.toString(), tempRect.width() + 50f, 290f, paintHeader1)
        canvas.drawText(statusOnArrivalInfo.toString(), tempRect.width() + 50f, 320f, paintHeader1)


        pdfDocument.finishPage(pdfPage)

        val filePath = File(getPublicAlbumStorageDir("MudraIt"), "jobcard.pdf")
        try {
            pdfDocument.writeTo(FileOutputStream(filePath))

            val intent = Intent(this@TransactionDetailActivity, PdfShareActivity::class.java)
            intent.putExtra("pdf", filePath.absolutePath)
            startActivity(intent)


        } catch (e: Exception) {

        }
        pdfDocument.close()

        //open pdf document


    }

    fun getPublicAlbumStorageDir(albumName: String): File? {
        // Get the directory for the user's public pictures directory.
        val file = File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), albumName)
        if (!file?.mkdirs()) {

        }
        return file
    }


    interface GetPdfInvoice {

        @POST("user/download_invoice/{pdfid}.pdf")
        @FormUrlEncoded
        fun pdfInvoice(@Field("id") id: String, @Path("pdfid") pdfid: String): Call<ResponseBody>


    }

    interface GetPdfInvocie2 {
        @POST("user/download_invoice2/{pdfid}.pdf")
        @FormUrlEncoded
        fun pdfInvoice(@Field("id") id: String, @Path("pdfid") pdfid: String): Call<ResponseBody>


    }


    private fun writeResponseBodyToDisk(body: ResponseBody?): Boolean {
        try {
            // todo change the file location/name according to your needs
            val futureStudioIconFile = File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOCUMENTS), "jobcard.pdf")

            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null

            try {
                val fileReader = ByteArray(4096)

                val fileSize = body!!.contentLength()
                var fileSizeDownloaded: Long = 0

                inputStream = body.byteStream()
                outputStream = FileOutputStream(futureStudioIconFile)

                while (true) {
                    val read = inputStream!!.read(fileReader)

                    if (read == -1) {
                        break
                    }

                    outputStream!!.write(fileReader, 0, read)

                    fileSizeDownloaded += read.toLong()

                    //Log.d(FragmentActivity.TAG, "file download: $fileSizeDownloaded of $fileSize")
                }

                outputStream!!.flush()


                val intent = Intent(this@TransactionDetailActivity, PdfShareActivity::class.java)
                intent.putExtra("pdf", futureStudioIconFile.absolutePath)
                startActivity(intent)

                return true
            } catch (e: IOException) {
                return false
            } finally {
                if (inputStream != null) {
                    inputStream!!.close()
                }

                if (outputStream != null) {
                    outputStream!!.close()
                }
            }
        } catch (e: IOException) {
            return false
        }

    }

}
