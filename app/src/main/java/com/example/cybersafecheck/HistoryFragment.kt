package com.example.cybersafecheck

//Author: Mangesana E
//Student number: 2030630053
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cybersafecheck.data.AssessmentEntity
import com.example.cybersafecheck.data.CyberSafeDatabase
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryFragment : Fragment(R.layout.fragment_history) {

    private val assessments = mutableListOf<AssessmentEntity>()
    private lateinit var adapter: HistoryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.history_recycler_view)
        val emptyText = view.findViewById<TextView>(R.id.history_empty_text)

        adapter = HistoryAdapter()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        val dao = CyberSafeDatabase.getInstance(requireContext()).assessmentDao()

        viewLifecycleOwner.lifecycleScope.launch {
            assessments.clear()
            assessments.addAll(dao.getAll())
            adapter.notifyDataSetChanged()

            emptyText.visibility = if (assessments.isEmpty()) View.VISIBLE else View.GONE
            recyclerView.visibility = if (assessments.isEmpty()) View.GONE else View.VISIBLE
        }
    }

    private class HistoryHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val dateText: TextView = view.findViewById(R.id.history_date)
        private val scoreText: TextView = view.findViewById(R.id.history_score)
        private val formatter = SimpleDateFormat("d MMM yyyy, HH:mm", Locale.getDefault())

        fun bind(item: AssessmentEntity) {
            dateText.text = formatter.format(Date(item.timestamp))
            scoreText.text = "Flagged: ${item.flaggedCount} / ${item.totalCount}"
        }
    }

    private inner class HistoryAdapter : RecyclerView.Adapter<HistoryHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_history, parent, false)
            return HistoryHolder(view)
        }

        override fun onBindViewHolder(holder: HistoryHolder, position: Int) {
            holder.bind(assessments[position])
        }

        override fun getItemCount() = assessments.size
    }
}