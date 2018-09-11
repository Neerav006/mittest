package com.codefuelindia.mudraitsolution.view

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

import com.codefuelindia.mudraitsolution.R
import com.codefuelindia.mudraitsolution.Utils.GlideApp
import com.codefuelindia.mudraitsolution.common.RetrofitClient
import com.codefuelindia.mudraitsolution.cons.Constants
import com.cunoraz.gifview.library.GifView
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Path
import java.io.*

class IntermediateLoader : AppCompatActivity() {
    private lateinit var getPdfInvoice: GetPdfInvoice

    private var pdf_id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intermediate_loader)
        getPdfInvoice = RetrofitClient.getClient(Constants.BASE_URL).create(GetPdfInvoice::class.java)

        pdf_id = intent.getStringExtra("pdf_id")

        val loader = showLoader()
        loader.show()

        getPdfInvoice.pdfInvoice("1", pdf_id).enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {

                loader.dismiss()

            }

            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {

                loader.dismiss()

                if (response!!.isSuccessful) {


                    Log.e("response", response.body().toString())

                    Log.e("is ok", writeResponseBodyToDisk(response!!.body()).toString())


                }


            }

        })


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


                val intent = Intent(this@IntermediateLoader, PdfShareActivity::class.java)
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


    interface GetPdfInvoice {

        @POST("user/download_invoice/{pdfid}.pdf")
        @FormUrlEncoded
        fun pdfInvoice(@Field("id") id: String, @Path("pdfid") pdfid: String): Call<ResponseBody>


    }


    fun showLoader(): AlertDialog {

        val dialogBuilder = AlertDialog.Builder(this@IntermediateLoader as Context)
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
        alertDialog.window.setBackgroundDrawable(this@IntermediateLoader.getDrawable(android.R.color.transparent))
        alertDialog.show()



        return alertDialog


    }


}
