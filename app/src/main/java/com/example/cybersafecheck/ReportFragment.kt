package com.example.cybersafecheck

//Author: Mangesana E
//Student number: 2030630053
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cybersafecheck.data.CyberSafeDatabase
import com.example.cybersafecheck.data.ReportEntity
import com.example.cybersafecheck.data.ReportRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ReportFragment : Fragment(R.layout.fragment_report) {

    private lateinit var repository: ReportRepository
    private lateinit var adapter: ReportAdapter
    private val reports = mutableListOf<ReportEntity>()

    private lateinit var descriptionInput: EditText
    private lateinit var photoPreview: ImageView

    // The photo file the camera is writing to (or has written to) for the report being typed
    private var pendingPhotoFile: File? = null

    private val takePicture =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                showPreview()
            } else {
                // Camera was cancelled: remove the empty file we created
                discardPendingPhoto()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Survive rotation / process death while the camera app is open
        savedInstanceState?.getString(STATE_PHOTO_PATH)?.let { pendingPhotoFile = File(it) }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        pendingPhotoFile?.let { outState.putString(STATE_PHOTO_PATH, it.absolutePath) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = ReportRepository(CyberSafeDatabase.getInstance(requireContext()).reportDao())

        descriptionInput = view.findViewById(R.id.report_description)
        photoPreview = view.findViewById(R.id.report_photo_preview)

        adapter = ReportAdapter()
        val recyclerView = view.findViewById<RecyclerView>(R.id.report_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        view.findViewById<View>(R.id.btn_take_photo).setOnClickListener { launchCamera() }
        view.findViewById<View>(R.id.btn_save_report).setOnClickListener { saveReport() }

        if (pendingPhotoFile?.exists() == true && pendingPhotoFile!!.length() > 0) showPreview()

        loadReports()
    }

    private fun photoUriFor(file: File): Uri =
        FileProvider.getUriForFile(requireContext(), "${requireContext().packageName}.fileprovider", file)

    private fun launchCamera() {
        discardPendingPhoto() // replacing an earlier, unsaved photo

        val photoDir = File(requireContext().filesDir, "report_photos").apply { mkdirs() }
        val photoFile = File(photoDir, "report_${System.currentTimeMillis()}.jpg")
        pendingPhotoFile = photoFile

        try {
            takePicture.launch(photoUriFor(photoFile))
        } catch (e: Exception) {
            discardPendingPhoto()
            Toast.makeText(requireContext(), "No camera app available", Toast.LENGTH_SHORT).show()
        }
    }

    private fun discardPendingPhoto() {
        pendingPhotoFile?.delete()
        pendingPhotoFile = null
        if (::photoPreview.isInitialized) {
            photoPreview.setImageDrawable(null)
            photoPreview.visibility = View.GONE
        }
    }

    private fun showPreview() {
        val file = pendingPhotoFile ?: return
        val resolver = requireContext().contentResolver
        val uri = photoUriFor(file)
        viewLifecycleOwner.lifecycleScope.launch {
            val bitmap = withContext(Dispatchers.IO) { ImageUtil.decodeScaled(resolver, uri, 600) }
            if (bitmap != null) {
                photoPreview.setImageBitmap(bitmap)
                photoPreview.visibility = View.VISIBLE
            }
        }
    }

    private fun saveReport() {
        val description = descriptionInput.text.toString().trim()
        if (description.isEmpty()) {
            descriptionInput.error = "Describe what happened"
            return
        }

        val photoUri = pendingPhotoFile
            ?.takeIf { it.exists() && it.length() > 0 }
            ?.let { photoUriFor(it).toString() }

        viewLifecycleOwner.lifecycleScope.launch {
            repository.saveReport(description, photoUri)

            // The file now belongs to the saved report: don't delete it
            pendingPhotoFile = null
            descriptionInput.text.clear()
            photoPreview.setImageDrawable(null)
            photoPreview.visibility = View.GONE

            Toast.makeText(requireContext(), "Report saved", Toast.LENGTH_SHORT).show()
            loadReports()
        }
    }

    private fun loadReports() {
        viewLifecycleOwner.lifecycleScope.launch {
            reports.clear()
            reports.addAll(repository.getAll())
            adapter.notifyDataSetChanged()
        }
    }

    private inner class ReportHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val thumbnail: ImageView = view.findViewById(R.id.report_thumbnail)
        private val dateText: TextView = view.findViewById(R.id.report_date)
        private val descriptionText: TextView = view.findViewById(R.id.report_text)
        private val formatter = SimpleDateFormat("d MMM yyyy, HH:mm", Locale.getDefault())

        fun bind(item: ReportEntity) {
            dateText.text = formatter.format(Date(item.timestamp))
            descriptionText.text = item.description

            thumbnail.setImageDrawable(null)
            thumbnail.tag = item.id
            val uriString = item.photoUri
            if (uriString == null) {
                thumbnail.visibility = View.GONE
                return
            }
            thumbnail.visibility = View.VISIBLE

            val resolver = requireContext().contentResolver
            viewLifecycleOwner.lifecycleScope.launch {
                val bitmap = withContext(Dispatchers.IO) {
                    ImageUtil.decodeScaled(resolver, Uri.parse(uriString), 160)
                }
                // Only set it if this row hasn't been recycled for a different report
                if (thumbnail.tag == item.id) thumbnail.setImageBitmap(bitmap)
            }
        }
    }

    private inner class ReportAdapter : RecyclerView.Adapter<ReportHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_report, parent, false)
            return ReportHolder(view)
        }

        override fun onBindViewHolder(holder: ReportHolder, position: Int) {
            holder.bind(reports[position])
        }

        override fun getItemCount() = reports.size
    }

    companion object {
        private const val STATE_PHOTO_PATH = "state_photo_path"
    }
}
