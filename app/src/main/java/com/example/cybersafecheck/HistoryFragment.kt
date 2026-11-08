package com.example.cybersafecheck

//Author: Mangesana E
//Student number: 2030630053
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cybersafecheck.data.AssessmentEntity
import com.example.cybersafecheck.data.CyberSafeDatabase
import com.example.cybersafecheck.data.PreferencesRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryFragment : Fragment(R.layout.fragment_history) {

    // Every saved assessment, and the ones that match what the student typed
    private val allAssessments = mutableListOf<AssessmentEntity>()
    private val shownAssessments = mutableListOf<AssessmentEntity>()

    private val formatter = SimpleDateFormat("d MMM yyyy, HH:mm", Locale.getDefault())

    private lateinit var adapter: HistoryAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyText: TextView
    private lateinit var searchView: SearchView
    private lateinit var preferences: PreferencesRepository

    private var currentQuery = ""
    private var rememberLastSearch = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.history_recycler_view)
        emptyText = view.findViewById(R.id.history_empty_text)
        searchView = view.findViewById(R.id.history_search)
        val rememberSwitch = view.findViewById<SwitchCompat>(R.id.history_remember_search)

        adapter = HistoryAdapter()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        preferences = PreferencesRepository(requireContext())
        val assessmentDao = CyberSafeDatabase.getInstance(requireContext()).assessmentDao()

        viewLifecycleOwner.lifecycleScope.launch {
            // Load the saved assessments from Room
            allAssessments.addAll(assessmentDao.getAll())

            // Load the saved settings from DataStore
            rememberLastSearch = preferences.rememberLastSearch.first()
            rememberSwitch.isChecked = rememberLastSearch
            if (rememberLastSearch) {
                currentQuery = preferences.lastSearch.first()
                searchView.setQuery(currentQuery, false)
            }
            showMatches()

            // Listeners are added after loading, so loading doesn't trigger a save
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    searchView.clearFocus()
                    return true
                }

                // Runs every time the student types a letter
                override fun onQueryTextChange(newText: String?): Boolean {
                    currentQuery = newText ?: ""
                    showMatches()
                    if (rememberLastSearch) {
                        viewLifecycleOwner.lifecycleScope.launch {
                            preferences.setLastSearch(currentQuery)
                        }
                    }
                    return true
                }
            })

            rememberSwitch.setOnCheckedChangeListener { _, isChecked ->
                rememberLastSearch = isChecked
                viewLifecycleOwner.lifecycleScope.launch {
                    preferences.setRememberLastSearch(isChecked)
                    preferences.setLastSearch(if (isChecked) currentQuery else "")
                }
            }
        }
    }

    // Keeps only the assessments whose date or score contains the typed text
    private fun showMatches() {
        val query = currentQuery.trim()
        shownAssessments.clear()
        for (item in allAssessments) {
            val dateText = formatter.format(Date(item.timestamp))
            val scoreText = "Flagged: ${item.flaggedCount} / ${item.totalCount}"
            if (query.isEmpty() ||
                dateText.contains(query, ignoreCase = true) ||
                scoreText.contains(query, ignoreCase = true)
            ) {
                shownAssessments.add(item)
            }
        }
        adapter.notifyDataSetChanged()

        if (allAssessments.isEmpty()) {
            emptyText.text = "No assessments saved yet. Calculate your score and save it from the checklist screen."
        } else {
            emptyText.text = "No assessments match your search."
        }
        if (shownAssessments.isEmpty()) {
            emptyText.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            emptyText.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }

    private inner class HistoryHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val dateText: TextView = view.findViewById(R.id.history_date)
        private val scoreText: TextView = view.findViewById(R.id.history_score)

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
            holder.bind(shownAssessments[position])
        }

        override fun getItemCount() = shownAssessments.size
    }
}
