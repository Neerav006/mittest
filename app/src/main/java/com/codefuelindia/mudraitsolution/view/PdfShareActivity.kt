package com.codefuelindia.mudraitsolution.view

import android.content.Intent
import android.graphics.pdf.PdfRenderer
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import com.codefuelindia.mudraitsolution.R

import kotlinx.android.synthetic.main.activity_pdf_share.*
import java.io.File
import android.opengl.ETC1.getHeight
import com.itextpdf.xmp.impl.ISO8601Converter.render
import android.graphics.Bitmap
import kotlinx.android.synthetic.main.content_pdf_share.*
import android.support.v4.content.FileProvider
import com.codefuelindia.mudraitsolution.BuildConfig


class PdfShareActivity : AppCompatActivity() {

    var path = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_share)
        setSupportActionBar(toolbar)

        if (intent != null) {

            path = intent.getStringExtra("pdf")


        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        if (path != null) {
            openPdf(path)
        }

        fab.setOnClickListener { view ->

            val uri = FileProvider.getUriForFile(this@PdfShareActivity, BuildConfig.APPLICATION_ID + ".provider", File(path))
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.putExtra(Intent.EXTRA_STREAM,uri)
            shareIntent.setDataAndType(uri, "application/pdf")
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivity(shareIntent)


        }
    }

    fun openPdf(path: String) {

        val file = File(path)
        val parceFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
        val pdfRenderer = PdfRenderer(parceFileDescriptor)

        val rendererPage = pdfRenderer.openPage(0)
        val rendererPageWidth = rendererPage.width
        val rendererPageHeight = rendererPage.height

        val bitmap = Bitmap.createBitmap(
                resources.displayMetrics.densityDpi/4*rendererPageWidth/72,
                resources.displayMetrics.densityDpi/4*rendererPageHeight/72,
                Bitmap.Config.ARGB_8888)
        rendererPage.render(bitmap, null, null,
                PdfRenderer.Page.RENDER_MODE_FOR_PRINT)


        ivPdfRender.setImageBitmap(bitmap)
        rendererPage.close()

        pdfRenderer.close()
        parceFileDescriptor.close()


    }

}
