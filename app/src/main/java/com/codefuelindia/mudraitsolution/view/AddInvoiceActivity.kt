package com.codefuelindia.mudraitsolution.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.SystemClock
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.util.SparseBooleanArray
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

import com.codefuelindia.mudraitsolution.R
import com.codefuelindia.mudraitsolution.Utils.GlideApp
import com.codefuelindia.mudraitsolution.Utils.Utils
import com.codefuelindia.mudraitsolution.common.RetrofitClient
import com.codefuelindia.mudraitsolution.cons.Constants
import com.codefuelindia.mudraitsolution.model.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.content_add_invoice.*
import kotlinx.android.synthetic.main.content_home.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Path
import java.io.*

class AddInvoiceActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener {
    var invoiceMaster = InvoiceMaster()
    val innerStringBuilder = StringBuilder()
    val statusOnArrivalInfo = StringBuilder()
    val productsInfo = StringBuilder()
    private lateinit var postInvoice: PostInvoice
    private lateinit var getPdfInvoice: GetPdfInvoice
    private var lastclick = 0L
    private var empid = ""
    private var sparseBooleanArray: SparseBooleanArray = SparseBooleanArray()
    private var sparseInnerProduct: SparseBooleanArray = SparseBooleanArray()
    private var sparseStatusArrival: SparseBooleanArray = SparseBooleanArray()
    private var productlist: ArrayList<com.codefuelindia.mudraitsolution.model.Product> = ArrayList()
    private var innerProductlist: ArrayList<InnerProduct> = ArrayList()
    private var statusArrivallist: ArrayList<StatusArrival> = ArrayList()
    private var productArray = arrayOf(R.id.chkPc, R.id.chkLaptop, R.id.chkAllInOnePc, R.id.chkMiniLaptop, R.id.chkPrinter)
    private var deliveryType = 1

    private val PERMISSSION_WRITE_EXTERNAL = 420

    private var innerProductArray = arrayOf(

            R.id.chkAdapter,
            R.id.chkPowerCord,
            R.id.chkDrive,
            R.id.chkDriversDisk,
            R.id.chkOSDisk,
            R.id.chkPrinterAdapter,
            R.id.chkPrinterDriverDisk,
            R.id.chkBag,
            R.id.chkBattery


    )


    private var statusArrivalArray = arrayOf(
            R.id.chkDead, R.id.chkDimDisplay, R.id.chkBodyDamage, R.id.chkCondition,
            R.id.chkOSreload, R.id.chkBodyRepair, R.id.chkDisplayFlicker, R.id.chkServicing
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_invoice)
        postInvoice = getAPIService(Constants.BASE_URL)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        edtOtherIssue.setOnTouchListener { v, event ->
            if (edtOtherIssue.hasFocus()) {
                v!!.parent.requestDisallowInterceptTouchEvent(true)

                when (event!!.action and MotionEvent.ACTION_MASK) {

                    MotionEvent.ACTION_UP -> {
                        v.parent.requestDisallowInterceptTouchEvent(false)
                    }

                }

            }
            false
        }


        edtAddress.setOnTouchListener { v, event ->
            if (edtAddress.hasFocus()) {
                v!!.parent.requestDisallowInterceptTouchEvent(true)

                when (event!!.action and MotionEvent.ACTION_MASK) {

                    MotionEvent.ACTION_UP -> {
                        v.parent.requestDisallowInterceptTouchEvent(false)
                    }

                }

            }
            false
        }



        edtOtherIssue.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (edtOtherIssue.layout.lineCount >= 4) {
                    edtOtherIssue.imeOptions = EditorInfo.IME_ACTION_NEXT
                    edtOtherIssue.setRawInputType(InputType.TYPE_CLASS_TEXT)


                } else {
                    edtOtherIssue.imeOptions = EditorInfo.IME_ACTION_NONE
                    edtOtherIssue.setRawInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {



            }

        })



        if (ContextCompat.checkSelfPermission(this@AddInvoiceActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this@AddInvoiceActivity,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                ActivityCompat.requestPermissions(this@AddInvoiceActivity,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        PERMISSSION_WRITE_EXTERNAL)

                Toast.makeText(this@AddInvoiceActivity, "Write storage permission needed for pdf", Toast.LENGTH_LONG).show()

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this@AddInvoiceActivity,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        PERMISSSION_WRITE_EXTERNAL)

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // permission is granted for


        }


        getPdfInvoice = RetrofitClient.getClient(Constants.BASE_URL).create(GetPdfInvoice::class.java)


        rbNormal.isChecked = true
        edtTime.hint = "Estimated time in day.."

        val sharedPreferences = Utils.getSharedPreference(Constants.MY_PREF, applicationContext)
        empid = sharedPreferences.getString(Constants.ID, "")

        val fab = findViewById<View>(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.title = "Add JobCard"

        toolbar.setNavigationOnClickListener {
            finish()
        }

        chkPc.setOnCheckedChangeListener(this)
        chkMiniLaptop.setOnCheckedChangeListener(this)
        chkAllInOnePc.setOnCheckedChangeListener(this)
        chkLaptop.setOnCheckedChangeListener(this)
        chkPrinter.setOnCheckedChangeListener(this)
        chkAdapter.setOnCheckedChangeListener(this)
        chkBattery.setOnCheckedChangeListener(this)
        chkDrive.setOnCheckedChangeListener(this)
        chkBag.setOnCheckedChangeListener(this)
        chkOSDisk.setOnCheckedChangeListener(this)
        chkDriversDisk.setOnCheckedChangeListener(this)
        chkPrinterDriverDisk.setOnCheckedChangeListener(this)
        chkPrinterAdapter.setOnCheckedChangeListener(this)
        chkPowerCord.setOnCheckedChangeListener(this)
        chkDead.setOnCheckedChangeListener(this)
        chkBodyDamage.setOnCheckedChangeListener(this)
        chkDimDisplay.setOnCheckedChangeListener(this)
        chkCondition.setOnCheckedChangeListener(this)
        chkOSreload.setOnCheckedChangeListener(this)
        chkServicing.setOnCheckedChangeListener(this)
        chkDisplayFlicker.setOnCheckedChangeListener(this)
        chkBodyRepair.setOnCheckedChangeListener(this)


        rgDeliveryType.setOnCheckedChangeListener { group, checkedId ->

            if (group.checkedRadioButtonId == R.id.rbNormal) {   // in days...1


                val filterArray = arrayOfNulls<InputFilter>(1)
                filterArray[0] = InputFilter.LengthFilter(4)

                edtTime.hint = "Estimated time in days "
                edtTime.filters = filterArray
                deliveryType = 1

            } else if (group.checkedRadioButtonId == R.id.rbSameDayDelivery) {  // in hours same day--2

                val filterArray = arrayOfNulls<InputFilter>(1)
                filterArray[0] = InputFilter.LengthFilter(2)

                edtTime.hint = "Estimated time in Hour "
                edtTime.filters = filterArray
                deliveryType = 2
            }


        }




        btnSubmit.setOnClickListener {

            var isProductSelected: Boolean = false
            if (SystemClock.elapsedRealtime() - lastclick < 2000) {
                return@setOnClickListener
            }

            lastclick = SystemClock.elapsedRealtime()

            productlist.clear()
            innerProductlist.clear()
            statusArrivallist.clear()

            edtCustomerName.error = null
            edtAddress.error = null
            edtCustomerNumber.error = null
            edtBrand.error = null
            edtSrNo.error = null
            edtTime.error = null
            edtEstimation.error = null

            when {
                edtCustomerName.text.toString().trim().isEmpty() -> {
                    edtCustomerName.error = "Name required"

                }
                edtCustomerNumber.text.toString().trim().length != 10 -> {
                    edtCustomerNumber.error = "Mobile required"

                }

                edtCustomerEmail.text.toString().trim().isNotEmpty() && !Utils.isValidEmail(edtCustomerEmail.text.toString().trim()) -> {
                    edtCustomerEmail.error = "Email is not valid"
                }

                edtBrand.text.toString().trim().isEmpty() -> {
                    edtBrand.error = "Brand name required"
                }
                edtSrNo.text.toString().trim().isEmpty() -> {
                    edtSrNo.error = "Sr no required"
                }
                edtEstimation.text.toString().trim().isEmpty() -> {
                    edtEstimation.error = "Approx charge required"
                }

                edtTime.text.toString().trim().isEmpty() -> {
                    edtTime.error = "Estimated time required in days"
                }
                else -> {

                    for (item in productArray) {
                        if (sparseBooleanArray[item]) {
                            isProductSelected = true
                            break
                        }
                    }

                    if (isProductSelected) {
                        // all ok

                        invoiceMaster = InvoiceMaster()

                        invoiceMaster.customerName = edtCustomerName.text.toString().trim()
                        invoiceMaster.mobile = edtCustomerNumber.text.toString().trim()
                        invoiceMaster.email = edtCustomerEmail.text.toString().trim()
                        invoiceMaster.addr = edtAddress.text.toString().trim()
                        invoiceMaster.empId = empid
                        invoiceMaster.advancePay = edtAdvancePay.text.toString().trim()
                        invoiceMaster.brandName = edtBrand.text.toString().trim()
                        invoiceMaster.srNo = edtSrNo.text.toString().trim()
                        invoiceMaster.estCharge = edtEstimation.text.toString().trim()
                        invoiceMaster.estTime = edtTime.text.toString().trim()
                        invoiceMaster.issue = edtOtherIssue.text.toString().trim()
                        invoiceMaster.del_status = deliveryType

                        for (item in productArray) {
                            if (sparseBooleanArray[item]) {


                                when (item) {
                                    R.id.chkPc -> {
                                        val product = com.codefuelindia.mudraitsolution.model.Product()
                                        product.id = Product.PC.id
                                        product.name = chkPc.text.toString()
                                        productlist.add(product)


                                    }
                                    R.id.chkLaptop -> {

                                        val product = com.codefuelindia.mudraitsolution.model.Product()
                                        product.id = Product.LAPTOP.id
                                        product.name = chkLaptop.text.toString()
                                        productlist.add(product)

                                    }
                                    R.id.chkMiniLaptop -> {

                                        val product = com.codefuelindia.mudraitsolution.model.Product()
                                        product.id = Product.MINI_NETBOOK.id
                                        product.name = chkMiniLaptop.text.toString()
                                        productlist.add(product)

                                    }
                                    R.id.chkAllInOnePc -> {
                                        val product = com.codefuelindia.mudraitsolution.model.Product()
                                        product.id = Product.ALL_IN_ONE_PC.id
                                        product.name = chkAllInOnePc.text.toString()
                                        productlist.add(product)

                                    }
                                    R.id.chkPrinter -> {
                                        val product = com.codefuelindia.mudraitsolution.model.Product()
                                        product.id = Product.PRINTER.id
                                        product.name = chkPrinter.text.toString()
                                        productlist.add(product)

                                    }
                                }


                            }
                        }


                        // inner product

                        for (item in innerProductArray) {
                            if (sparseInnerProduct[item]) {

                                when (item) {
                                    R.id.chkPowerCord -> {

                                        val innerProduct = InnerProduct()
                                        innerProduct.id = Product.POWER_CARD.id
                                        innerProduct.name = chkPowerCord.text.toString()
                                        innerProductlist.add(innerProduct)


                                    }
                                    R.id.chkDrive -> {

                                        val innerProduct = InnerProduct()
                                        innerProduct.id = Product.DRIVE.id
                                        innerProduct.name = chkDrive.text.toString()
                                        innerProductlist.add(innerProduct)
                                    }
                                    R.id.chkDriversDisk -> {

                                        val innerProduct = InnerProduct()
                                        innerProduct.id = Product.DRIVER_DISK.id
                                        innerProduct.name = chkDriversDisk.text.toString()
                                        innerProductlist.add(innerProduct)
                                    }
                                    R.id.chkBag -> {

                                        val innerProduct = InnerProduct()
                                        innerProduct.id = Product.BAG.id
                                        innerProduct.name = chkBag.text.toString()
                                        innerProductlist.add(innerProduct)
                                    }
                                    R.id.chkAdapter -> {

                                        val innerProduct = InnerProduct()
                                        innerProduct.id = Product.ADAPTER.id
                                        innerProduct.name = chkAdapter.text.toString()
                                        innerProductlist.add(innerProduct)
                                    }
                                    R.id.chkPrinterAdapter -> {

                                        val innerProduct = InnerProduct()
                                        innerProduct.id = Product.PRINTER_ADAPTER.id
                                        innerProduct.name = chkPrinterAdapter.text.toString()
                                        innerProductlist.add(innerProduct)
                                    }
                                    R.id.chkBattery -> {

                                        val innerProduct = InnerProduct()
                                        innerProduct.id = Product.BATERY.id
                                        innerProduct.name = chkBattery.text.toString()
                                        innerProductlist.add(innerProduct)
                                    }
                                    R.id.chkOSDisk -> {

                                        val innerProduct = InnerProduct()
                                        innerProduct.id = Product.OS_DISK.id
                                        innerProduct.name = chkOSDisk.text.toString()
                                        innerProductlist.add(innerProduct)
                                    }
                                    R.id.chkPrinterDriverDisk -> {

                                        val innerProduct = InnerProduct()
                                        innerProduct.id = Product.PRINTER_DRIVER_DISK.id
                                        innerProduct.name = chkPrinterDriverDisk.text.toString()
                                        innerProductlist.add(innerProduct)
                                    }
                                }


                            }
                        }


                        // status on arrival

                        for (item in statusArrivalArray) {
                            if (sparseStatusArrival[item]) {

                                when (item) {
                                    R.id.chkDead -> {
                                        val statusArrival = StatusArrival()
                                        statusArrival.id = Product.DEAD_NO_POWER.id
                                        statusArrival.name = chkDead.text.toString()
                                        statusArrivallist.add(statusArrival)
                                    }
                                    R.id.chkBodyDamage -> {
                                        val statusArrival = StatusArrival()
                                        statusArrival.id = Product.BODY_DAMAGE.id
                                        statusArrival.name = chkBodyDamage.text.toString()
                                        statusArrivallist.add(statusArrival)
                                    }
                                    R.id.chkDimDisplay -> {
                                        val statusArrival = StatusArrival()
                                        statusArrival.id = Product.DIM_DISPLAY.id
                                        statusArrival.name = chkDimDisplay.text.toString()
                                        statusArrivallist.add(statusArrival)
                                    }
                                    R.id.chkCondition -> {
                                        val statusArrival = StatusArrival()
                                        statusArrival.id = Product.POWER_ON_NO_BOOT.id
                                        statusArrival.name = chkCondition.text.toString()
                                        statusArrivallist.add(statusArrival)
                                    }

                                    R.id.chkServicing -> {
                                        val statusArrival = StatusArrival()
                                        statusArrival.id = Product.SERVICING.id
                                        statusArrival.name = chkServicing.text.toString()
                                        statusArrivallist.add(statusArrival)
                                    }
                                    R.id.chkBodyRepair -> {
                                        val statusArrival = StatusArrival()
                                        statusArrival.id = Product.BODY_REPAIR.id
                                        statusArrival.name = chkBodyRepair.text.toString()
                                        statusArrivallist.add(statusArrival)
                                    }
                                    R.id.chkDisplayFlicker -> {
                                        val statusArrival = StatusArrival()
                                        statusArrival.id = Product.DISPLAY_FLICKER.id
                                        statusArrival.name = chkDisplayFlicker.text.toString()
                                        statusArrivallist.add(statusArrival)
                                    }
                                    R.id.chkOSreload -> {
                                        val statusArrival = StatusArrival()
                                        statusArrival.id = Product.OS_RELOAD.id
                                        statusArrival.name = chkOSreload.text.toString()
                                        statusArrivallist.add(statusArrival)
                                    }

                                }


                            }
                        }

                        invoiceMaster.product = productlist
                        invoiceMaster.innerProduct = innerProductlist
                        invoiceMaster.statusArrival = statusArrivallist


                        for (products in productlist) {
                            productsInfo.append(products.name.plus(" ,"))
                        }

                        for (innerProducts in innerProductlist) {
                            innerStringBuilder.append(innerProducts.name.plus(" ,"))
                        }

                        for (statusArrival in statusArrivallist) {
                            statusOnArrivalInfo.append(statusArrival.name.plus(" ,"))

                        }


                        Log.e("products json", Gson().toJson(invoiceMaster))


                        addInvoiceOnConfirmation(invoiceMaster)


                    } else {
                        Toast.makeText(this@AddInvoiceActivity, "Select product", Toast.LENGTH_LONG).show()
                    }


                }
            }


        }


    }


    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {

        when {

            buttonView!!.id == R.id.chkPc -> {
                sparseBooleanArray.put(buttonView!!.id, isChecked)


            }
            buttonView!!.id == R.id.chkLaptop -> {
                sparseBooleanArray.put(buttonView!!.id, isChecked)

            }
            buttonView.id == R.id.chkMiniLaptop -> {
                sparseBooleanArray.put(buttonView!!.id, isChecked)

            }
            buttonView.id == R.id.chkAllInOnePc -> {
                sparseBooleanArray.put(buttonView!!.id, isChecked)

            }
            buttonView.id == R.id.chkPrinter -> {
                sparseBooleanArray.put(buttonView!!.id, isChecked)

            }

            // inner product

            buttonView.id == R.id.chkPowerCord -> {
                sparseInnerProduct.put(buttonView!!.id, isChecked)

            }

            buttonView.id == R.id.chkDrive -> {
                sparseInnerProduct.put(buttonView!!.id, isChecked)

            }

            buttonView.id == R.id.chkDriversDisk -> {
                sparseInnerProduct.put(buttonView!!.id, isChecked)

            }

            buttonView.id == R.id.chkBag -> {
                sparseInnerProduct.put(buttonView!!.id, isChecked)

            }

            buttonView.id == R.id.chkAdapter -> {
                sparseInnerProduct.put(buttonView!!.id, isChecked)

            }

            buttonView.id == R.id.chkPrinterAdapter -> {
                sparseInnerProduct.put(buttonView!!.id, isChecked)

            }

            buttonView.id == R.id.chkBattery -> {
                sparseInnerProduct.put(buttonView!!.id, isChecked)

            }

            buttonView.id == R.id.chkOSDisk -> {
                sparseInnerProduct.put(buttonView!!.id, isChecked)

            }

            buttonView.id == R.id.chkPrinterDriverDisk -> {
                sparseInnerProduct.put(buttonView!!.id, isChecked)

            }


            // status on arrival

            buttonView.id == R.id.chkDead -> {
                sparseStatusArrival.put(buttonView!!.id, isChecked)

            }

            buttonView.id == R.id.chkDimDisplay -> {
                sparseStatusArrival.put(buttonView!!.id, isChecked)

            }

            buttonView.id == R.id.chkBodyDamage -> {
                sparseStatusArrival.put(buttonView!!.id, isChecked)

            }

            buttonView.id == R.id.chkCondition -> {
                sparseStatusArrival.put(buttonView!!.id, isChecked)

            }

            buttonView.id == R.id.chkDisplayFlicker -> {
                sparseStatusArrival.put(buttonView!!.id, isChecked)

            }

            buttonView.id == R.id.chkOSreload -> {
                sparseStatusArrival.put(buttonView!!.id, isChecked)

            }

            buttonView.id == R.id.chkServicing -> {
                sparseStatusArrival.put(buttonView!!.id, isChecked)

            }

            buttonView.id == R.id.chkBodyRepair -> {
                sparseStatusArrival.put(buttonView!!.id, isChecked)

            }


        }


    }

    private enum class Product(val id: String) {

        PRODUCT_CAT("1"),
        INNER_PRODUCT_CAT("2"),
        STATUS_ARRIVAL("3"),

        //product
        PC("1"),
        LAPTOP("2"),
        MINI_NETBOOK("3"),
        ALL_IN_ONE_PC("4"),
        PRINTER("5"),


        //inner product
        ADAPTER("1"),
        POWER_CARD("2"),
        BATERY("3"),
        DRIVE("4"),
        BAG("5"),
        OS_DISK("6"),
        DRIVER_DISK("7"),
        PRINTER_DRIVER_DISK("8"),
        PRINTER_ADAPTER("9"),

        //status on arrival

        DEAD_NO_POWER("1"),
        DIM_DISPLAY("2"),
        BODY_DAMAGE("3"),
        POWER_ON_NO_BOOT("4"),
        OS_RELOAD("5"),
        SERVICING("6"),
        DISPLAY_FLICKER("7"),
        BODY_REPAIR("8")


    }

    interface PostInvoice {

        @POST("user/addreport_api/")
        fun addInvoice(@Body invocie: InvoiceMaster): Call<MyRes>

    }

    internal fun getAPIService(baseUrl: String): PostInvoice {

        return RetrofitClient.getClient(baseUrl).create(PostInvoice::class.java)
    }


    private fun addInvoiceOnConfirmation(invocie: InvoiceMaster) {


        btnSubmit.isEnabled = false


        val builder = AlertDialog.Builder(this@AddInvoiceActivity)

        // Set the alert dialog title
        builder.setTitle("Add Invoice")
        builder.setCancelable(false)
        // Display a message on alert dialog
        builder.setMessage("Are you sure to continue?")

        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton("YES") { dialog, which ->
            // Do something when user press the positive button

            val dialog = showLoader()

            postInvoice.addInvoice(invocie).enqueue(object : Callback<MyRes> {
                override fun onFailure(call: Call<MyRes>?, t: Throwable?) {
                    Toast.makeText(this@AddInvoiceActivity, "Connection error", Toast.LENGTH_LONG).show()

                    btnSubmit.isEnabled = true
                    dialog.dismiss()
                }

                override fun onResponse(call: Call<MyRes>?, response: Response<MyRes>?) {
                    btnSubmit.isEnabled = true
                    dialog.dismiss()
                    if (response!!.isSuccessful) {

                        if (response.body()?.msg.equals("true", true)) {
                            Toast.makeText(this@AddInvoiceActivity, "Successfully added", Toast.LENGTH_LONG).show()

                            //createPdf(response!!.body()!!.jobcard)


//                            val intent =Intent(this@AddInvoiceActivity,PdfViewActivity::class.java)
//                            intent.putExtra("pdf","http://go2hitech.com/jms/user/download_invoice/${response!!.body()!!.pdf_id}")
//                            startActivity(intent)


                            val intent = Intent(this@AddInvoiceActivity, IntermediateLoader::class.java)
                            intent.putExtra("pdf_id", response!!.body()!!.pdf_id)
                            startActivity(intent)
                            finish()


                        } else {
                            Toast.makeText(this@AddInvoiceActivity, "Error occurred", Toast.LENGTH_LONG).show()

                        }


                    } else {
                        Toast.makeText(this@AddInvoiceActivity, "Error occurred", Toast.LENGTH_LONG).show()

                    }


                }
            })

        }


        // Display a negative button on alert dialog
        builder.setNegativeButton("No") { dialog, which ->
            btnSubmit.isEnabled = true
            dialog.dismiss()
        }

        val alertDialog: AlertDialog = builder.create()

        // Display the alert dialog on app interface
        alertDialog.show()
    }


    fun showLoader(): AlertDialog {

        val dialogBuilder = AlertDialog.Builder(this@AddInvoiceActivity as Context)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.alert_label_editor, null)
        dialogBuilder.setView(dialogView)

        GlideApp.with(this@AddInvoiceActivity)
                .load(this@AddInvoiceActivity.getDrawable(R.drawable.loader)).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into(dialogView.findViewById<ImageView>(R.id.ivGif))
        dialogBuilder.setCancelable(false)
        val alertDialog = dialogBuilder.create()
        alertDialog.window.setBackgroundDrawable(this@AddInvoiceActivity.getDrawable(android.R.color.transparent))
        alertDialog.show()

        return alertDialog


    }


    fun createPdf(jobCard: String) {

//        val pdfDocument = PdfDocument()
//        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
//        val pdfPage: PdfDocument.Page = pdfDocument.startPage(pageInfo)
//        val canvas: Canvas = pdfPage.canvas
//
//        val paint = Paint()
//        paint.style = Paint.Style.STROKE
//        paint.color = Color.BLACK
//
//        canvas.drawRect(RectF(20f, 20f, 575f, 822f), paint)
//
//
//        val paintHeader1 = Paint(Paint.ANTI_ALIAS_FLAG)
//        paintHeader1.style = Paint.Style.FILL
//        paintHeader1.color = Color.BLACK
//        paintHeader1.textSize = 20f
//        paintHeader1.textAlign = Paint.Align.CENTER
//
//
//        val paintHeader2 = Paint(Paint.ANTI_ALIAS_FLAG)
//        paintHeader2.style = Paint.Style.FILL
//        paintHeader2.color = Color.GRAY
//        paintHeader2.textSize = 20f
//        paintHeader2.textAlign = Paint.Align.CENTER
//
//
//        val paintHeader3 = Paint(Paint.ANTI_ALIAS_FLAG)
//        paintHeader3.style = Paint.Style.FILL
//        paintHeader3.color = Color.BLACK
//        paintHeader3.textSize = 20f
//        paintHeader3.textAlign = Paint.Align.CENTER
//
//
//
//        canvas.drawText("Hi Tech", (pageInfo.pageWidth / 2f), 60f, paintHeader1)
//        canvas.drawText("Computer Services", (pageInfo.pageWidth / 2f), 85f, paintHeader2)
//        canvas.drawText("Multi brand IT Store", (pageInfo.pageWidth / 2f), 110f, paintHeader3)
//
//
//        paintHeader2.textAlign = Paint.Align.LEFT
//        paintHeader2.textSize = 15f
//        paintHeader1.textSize = 15f
//
//
//        /**   170f --  Customer name
//         *    195f --  Job Card No
//         *    220f --  Mobile No
//         *    245f --  Address
//         *    280f --  Product issue
//         *    800f --  Date
//         *
//         *
//         *    350f -- product name
//         *    380f -- brand name
//         *    410 --  serial no
//         *    (45f,700f) --- Advance pay
//         *    (200f,700f) -- total Amount
//         *    (390,700f) -- Remaining amount
//         *
//         *    440f --- inner product
//         *    470f --- status on arrival
//         *
//         *    510f -- estimated charge
//         *    550f -- estimated time
//         *
//         *
//         *
//         *
//         *
//         *
//         */
//
//
//        canvas.drawText("Customer Name:", 40f, 170f, paintHeader2)
//        canvas.drawText("Job card No:", 40f, 195f, paintHeader2)
//        canvas.drawText("Mobile No:", 40f, 220f, paintHeader2)
//        canvas.drawText("Address:", 40f, 245f, paintHeader2)
//        canvas.drawText("Product Issue", 40f, 280f, paintHeader2)
//        // canvas.drawText("Date: ", 40f, 800f, paintHeader2)
//
//        paintHeader1.textAlign = Paint.Align.LEFT
//
//        val tempRect = Rect()
//        paintHeader2.getTextBounds("Customer Name:", 0, "Customer Name:".length, tempRect)
//        canvas.drawText(invoiceMaster.customerName, tempRect.width() + 50f, 170f, paintHeader1)
//        canvas.drawText(jobCard, tempRect.width() + 50f, 195f, paintHeader1)
//        canvas.drawText(invoiceMaster.mobile, tempRect.width() + 50f, 220f, paintHeader1)
//        canvas.drawText(invoiceMaster.addr, tempRect.width() + 50f, 245f, paintHeader1)
//        canvas.drawText(invoiceMaster.issue, tempRect.width() + 50f, 280f, paintHeader1)
//        // canvas.drawText(invoiceMaster.t, tempRect.width() + 50f, 800f, paintHeader1)
//
//        paintHeader1.textAlign = Paint.Align.CENTER
//        canvas.drawText("Product Detail", pageInfo.pageWidth / 2f, 320f, paintHeader1)
//
//        paintHeader1.style = Paint.Style.STROKE
//        canvas.drawRect(40f, 330f, 555f, 360f, paintHeader1)
//        canvas.drawRect(40f, 360f, 555f, 390f, paintHeader1)
//        canvas.drawRect(40f, 390f, 555f, 420f, paintHeader1)
//        canvas.drawRect(40f, 420f, 555f, 450f, paintHeader1)
//        canvas.drawRect(40f, 450f, 555f, 480f, paintHeader1)
//
//
//
//        canvas.drawRect(40f, 650f, 555f, 680f, paintHeader1)
//        canvas.drawRect(40f, 680f, 555f, 710f, paintHeader1)
//
//        canvas.drawLine(190f, 650f, 190f, 710f, paintHeader1)
//        canvas.drawLine(380f, 650f, 380f, 710f, paintHeader1)
//
//
//
//
//        canvas.drawText("Product:", 45f, 350f, paintHeader2)
//        canvas.drawText("Brand Name:", 45f, 380f, paintHeader2)
//        canvas.drawText("Serial No:", 45f, 410f, paintHeader2)
//
//        canvas.drawText("Accessories:", 45f, 440f, paintHeader2)
//        canvas.drawText("Status: ", 45f, 470f, paintHeader2)
//
//        canvas.drawText("Estimated Charge:- ", 45f, 510f, paintHeader2)
//        canvas.drawText("Estimated time:- ", 45f, 550f, paintHeader2)
//
//        canvas.drawText("Advance Pay:", 45f, 670f, paintHeader2)
//        canvas.drawText("Total Amount:", 200f, 670f, paintHeader2)
//        canvas.drawText("Remaining Amount:", 390f, 670f, paintHeader2)
//
//
//        paintHeader1.style = Paint.Style.FILL
//        paintHeader1.textAlign = Paint.Align.LEFT
//
//        canvas.drawText(productsInfo.toString(), tempRect.width() + 50f, 350f, paintHeader1)
//        canvas.drawText(invoiceMaster.brandName, tempRect.width() + 50f, 380f, paintHeader1)
//        canvas.drawText(invoiceMaster.srNo, tempRect.width() + 50f, 410f, paintHeader1)
//        canvas.drawText(invoiceMaster.advancePay, 45f, 700f, paintHeader1)
//        // canvas.drawText(invoiceMaster.total_amount, 200f, 700f, paintHeader1)
//        // canvas.drawText(invoiceMaster.re, 390f, 700f, paintHeader1)
//        canvas.drawText(invoiceMaster.estCharge, tempRect.width() + 100f, 510f, paintHeader1)
//        canvas.drawText(invoiceMaster.estTime, tempRect.width() + 50f, 550f, paintHeader1)
//
//
//        paintHeader1.textSize = 10f
//
//        canvas.drawText(innerStringBuilder.toString(), tempRect.width() + 50f, 440f, paintHeader1)
//        canvas.drawText(statusOnArrivalInfo.toString(), tempRect.width() + 50f, 470f, paintHeader1)
//
//
//        pdfDocument.finishPage(pdfPage)
//
//        val filePath = File(getPublicAlbumStorageDir("MudraIt"), "jobcard.pdf")
//        try {
//            pdfDocument.writeTo(FileOutputStream(filePath))
//
//            val intent = Intent(this@AddInvoiceActivity, PdfShareActivity::class.java)
//            intent.putExtra("pdf", filePath.absolutePath)
//            startActivity(intent)
//
//
//        } catch (e: Exception) {
//
//        }
//        pdfDocument.close()

        //open pdf document


        /**
         *   New Half size Start
         *
         */


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
        canvas.drawText(invoiceMaster.customerName, tempRect.width() + 50f, 40f, paintHeader1)
        canvas.drawText(jobCard, tempRect.width() + 50f, 65f, paintHeader1)
        canvas.drawText(invoiceMaster.mobile, tempRect.width() + 50f, 90f, paintHeader1)
        canvas.drawText(invoiceMaster.addr, tempRect.width() + 50f, 115f, paintHeader1)
        canvas.drawText(invoiceMaster.issue, tempRect.width() + 50f, 140f, paintHeader1)
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

        canvas.drawText(productsInfo.toString(), tempRect.width() + 50f, 200f, paintHeader1)
        canvas.drawText(invoiceMaster.brandName, tempRect.width() + 50f, 230f, paintHeader1)
        canvas.drawText(invoiceMaster.srNo, tempRect.width() + 50f, 260f, paintHeader1)



        canvas.drawText(invoiceMaster.advancePay, 45f, 390f, paintHeader1)
//        canvas.drawText(invoiceMaster., 200f, 390f, paintHeader1)
//        canvas.drawText(remainingAmount.toString(), 390f, 390f, paintHeader1)
        canvas.drawText(invoiceMaster.estCharge, (pageInfo.pageWidth / 2f) + 120f, 65f, paintHeader1)

        if (rgDeliveryType.checkedRadioButtonId == R.id.rbNormal) {
            canvas.drawText(invoiceMaster.estTime.plus(" Day"), (pageInfo.pageWidth / 2f) + 120f, 90f, paintHeader1)

        } else {
            canvas.drawText(invoiceMaster.estTime.plus(" Hour"), (pageInfo.pageWidth / 2f) + 120f, 90f, paintHeader1)

        }



        paintHeader1.textSize = 10f

        canvas.drawText(innerStringBuilder.toString(), tempRect.width() + 50f, 290f, paintHeader1)
        canvas.drawText(statusOnArrivalInfo.toString(), tempRect.width() + 50f, 320f, paintHeader1)


        pdfDocument.finishPage(pdfPage)

        val filePath = File(getPublicAlbumStorageDir("MudraIt"), "jobcard.pdf")
        try {
            pdfDocument.writeTo(FileOutputStream(filePath))

            val intent = Intent(this@AddInvoiceActivity, PdfShareActivity::class.java)
            intent.putExtra("pdf", filePath.absolutePath)
            startActivity(intent)


        } catch (e: Exception) {

        }
        pdfDocument.close()


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


                val intent = Intent(this@AddInvoiceActivity, PdfShareActivity::class.java)
                intent.putExtra("pdf", futureStudioIconFile.absolutePath)
                startActivity(intent)
                finish()

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


    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSSION_WRITE_EXTERNAL -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.


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


}



