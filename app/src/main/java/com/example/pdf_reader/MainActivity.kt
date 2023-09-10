package com.example.pdf_reader

import PdfViewerActivity
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class PdfListActivity : AppCompatActivity() {

    private val pdfList = ArrayList<String>()
    private lateinit var pdfListView: ListView

    companion object {
        private const val REQUEST_PERMISSION = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pdfListView = findViewById(R.id.pdfListView)

        // Request permissions if not granted
        if (!checkPermissions()) {
            requestPermissions()
        } else {
            // Permissions are already granted, so list PDFs
            listPdfFiles()
        }
    }

    private fun checkPermissions(): Boolean {
        val readStoragePermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        return readStoragePermission == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            REQUEST_PERMISSION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, list PDFs
                listPdfFiles()
            } else {
                // Permission denied, handle accordingly (e.g., show a message)
            }
        }
    }

    private fun listPdfFiles() {
        // List all PDF files in internal storage
        val pdfFiles = getPDFFiles()

        // Display the list of PDF files in a ListView
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, pdfFiles)
        pdfListView.adapter = adapter

        // Handle item click to view the selected PDF
        pdfListView.setOnItemClickListener { _, _, position, _ ->
            val selectedPdfPath = pdfFiles[position]
            val intent = Intent(this@PdfListActivity, PdfViewerActivity::class.java)
            intent.putExtra("pdfFilePath", selectedPdfPath)
            startActivity(intent)
        }

    }

    private fun getPDFFiles(): ArrayList<String> {
        val pdfFiles = ArrayList<String>()
        val externalStorageDirectory = Environment.getExternalStorageDirectory()
        val internalStorageDirectory = filesDir

        val storageDirectories = arrayOf(externalStorageDirectory, internalStorageDirectory)

        for (storageDirectory in storageDirectories) {
            if (storageDirectory.exists()) {
                val files = storageDirectory.listFiles()
                if (files != null) {
                    for (file in files) {
                        if (file.isFile && file.extension == "pdf") {
                            pdfFiles.add(file.absolutePath)
                        }
                    }
                }
            }
        }

        return pdfFiles
    }
}