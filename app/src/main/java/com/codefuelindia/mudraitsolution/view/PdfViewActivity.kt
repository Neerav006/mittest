package com.codefuelindia.mudraitsolution.view

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient

import com.codefuelindia.mudraitsolution.R
import kotlinx.android.synthetic.main.content_pdf_view.*

class PdfViewActivity : AppCompatActivity() {

    var pdfUrl = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_view)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        toolbar?.title = "Pdf View"

        toolbar.setNavigationOnClickListener { finish() }


        if (intent != null) {
            pdfUrl = intent.getStringExtra("pdf")
        }


        pdfView.settings.javaScriptEnabled = true
        pdfView.settings.domStorageEnabled = true
        pdfView.settings.allowFileAccess = true
        pdfView.settings.displayZoomControls = true
        pdfView.webViewClient = MyWebClient()
        pdfView.loadUrl("http://docs.google.com/gview?embedded=true&url=$pdfUrl")

    }


    inner class MyWebClient : WebViewClient() {

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)


        }

        override fun onPageFinished(view: WebView?, url: String?) {

            progressBar.visibility = View.GONE

            super.onPageFinished(view, url)


        }

        override fun onLoadResource(view: WebView?, url: String?) {
            super.onLoadResource(view, url)
        }

        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            return false
        }


    }

}
