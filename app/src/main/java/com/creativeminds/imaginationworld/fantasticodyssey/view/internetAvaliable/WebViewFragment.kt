package com.creativeminds.imaginationworld.fantasticodyssey.view.internetAvaliable

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.creativeminds.imaginationworld.fantasticodyssey.R
import com.creativeminds.imaginationworld.fantasticodyssey.databinding.FragmentWebViewBinding
import com.creativeminds.imaginationworld.fantasticodyssey.viewModels.ViewModelResponse
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WebViewFragment : Fragment() {

    private val viewModel by lazy { ViewModelProvider(requireActivity())[ViewModelResponse::class.java] }
    private val binding by lazy { FragmentWebViewBinding.inflate(layoutInflater) }
    private var mFilePathCallback: ValueCallback<Array<Uri>>? = null
    private var pictureFromCameraFileName: String = ""
    private var pictureFromCameraFile: File = File("")

    private val getResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode != AppCompatActivity.RESULT_OK) return@registerForActivityResult
            mFilePathCallback!!.onReceiveValue(
                if (result.data?.dataString == null) {
                    arrayOf(getImageUri(pictureFromCameraFile))
                } else {
                    arrayOf(Uri.parse(result.data?.dataString))
                }
            )
            mFilePathCallback = null
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.topAppBar.setNavigationOnClickListener {
            if (binding.webView.canGoBack()) {
                binding.webView.goBack()
            } else {
                findNavController().navigateUp()
            }
        }
        if (ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        }

        binding.webView.webViewClient = object : WebViewClient() {
            override fun onReceivedError(
                view: WebView?,
                errorCode: Int,
                description: String?,
                failingUrl: String?
            ) {
                if (errorCode == ERROR_CONNECT || errorCode == ERROR_HOST_LOOKUP) {
                    showErrorConnection()
                } else {
                    super.onReceivedError(view, errorCode, description, failingUrl)
                }
            }
        }
        binding.webView.webChromeClient = ChromeClient()

        // show previous page if a user presses back button
        binding.webView.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP && binding.webView.canGoBack()) {
                binding.webView.goBack()
                return@setOnKeyListener true
            }
            false
        }

        val webSettings = binding.webView.settings
        supportWebView(webSettings)

        if (isInternetAvailable()) {
            viewModel.orderLink.observe(viewLifecycleOwner) { url ->
                binding.webView.loadUrl(url)
            }
        } else {
            showErrorConnection()
        }
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork
        val activeNetwork = connectivityManager.getNetworkCapabilities(networkCapabilities)
        return activeNetwork != null
    }

    private fun showErrorConnection() {
        binding.webView.visibility = View.GONE
        binding.invalidConnectionLayout.visibility = View.VISIBLE
        binding.button.setOnClickListener {
            if (isInternetAvailable() && binding.webView.canGoBack()) {
                binding.invalidConnectionLayout.visibility = View.GONE
                binding.webView.visibility = View.VISIBLE
                binding.webView.goBack()
            } else {
                findNavController().navigateUp()
            }


        }
    }


    @SuppressLint("SetJavaScriptEnabled")
    private fun supportWebView(webSettings: WebSettings) {
        val cookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)
        webSettings.apply {
            webSettings.javaScriptEnabled = true
            webSettings.javaScriptCanOpenWindowsAutomatically = true
            webSettings.loadWithOverviewMode = true
            webSettings.useWideViewPort = true
            webSettings.domStorageEnabled = true
            webSettings.databaseEnabled = true
            webSettings.setSupportZoom(false)
            webSettings.allowFileAccess = true
            webSettings.allowContentAccess = true
            webSettings.loadWithOverviewMode = true
            webSettings.useWideViewPort = true
        }

        binding.webView.settings.domStorageEnabled = true
        binding.webView.settings.javaScriptCanOpenWindowsAutomatically = true
    }

    private fun createCameraIntent(): Intent {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(Date())
        pictureFromCameraFileName = "FIN_TEST_$timeStamp.jpg"

        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        pictureFromCameraFile = File(path, pictureFromCameraFileName)
        val imageUri = getImageUri(pictureFromCameraFile)

        return Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            .putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            .addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
    }

    private fun getImageUri(file: File): Uri = FileProvider.getUriForFile(
        requireActivity(),
        requireContext().packageName + ".provider",
        file
    )

    inner class ChromeClient : WebChromeClient() {
        override fun onShowFileChooser(
            view: WebView, filePath: ValueCallback<Array<Uri>>, fileChooserParams: FileChooserParams
        ): Boolean {
            mFilePathCallback?.onReceiveValue(null)
            mFilePathCallback = filePath

            val takePictureIntent = createCameraIntent()

            val contentSelectionIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "image/*"
            }

            val chooserIntent =
                Intent(Intent.ACTION_CHOOSER).putExtra(Intent.EXTRA_TITLE, "Image Chooser")
                    .putExtra(Intent.EXTRA_INTENT, contentSelectionIntent)
                    .putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(takePictureIntent))

            getResult.launch(chooserIntent)
            return true
        }
    }

    inner class CustomWebViewClient : WebViewClient() {

    }


    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 100
    }
}