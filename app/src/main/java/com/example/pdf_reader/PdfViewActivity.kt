import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle
import com.github.barteksc.pdfviewer.util.FitPolicy
import java.io.File

class PdfViewerActivity : AppCompatActivity(), OnPageChangeListener, OnLoadCompleteListener {

    private lateinit var pdfView: PDFView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_viewer)

        pdfView = findViewById(R.id.pdfView)

        val pdfFilePath = intent.getStringExtra("pdfFilePath")

        if (pdfFilePath != null) {
            displayPdf(pdfFilePath)
        } else {
            // Handle the case where the PDF file path is not provided or is invalid
            // You can display an error message or take appropriate action here
        }
    }

    private fun displayPdf(pdfFilePath: String) {
        pdfView.fromFile(File(pdfFilePath))
            .defaultPage(0)
            .onPageChange(this)
            .onLoad(this)
            .scrollHandle(DefaultScrollHandle(this))
            .spacing(0) // spacing between pages in dp
            .pageFitPolicy(FitPolicy.BOTH)
            .load()
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        // Empty implementation (no action needed)
    }

    override fun onPageChanged(page: Int, pageCount: Int) {
        // Handle page change events here if needed
    }

    override fun loadComplete(nbPages: Int) {
        // Handle load complete event here if needed
    }
}
