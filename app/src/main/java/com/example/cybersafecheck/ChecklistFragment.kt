package com.example.cybersafecheck

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cybersafecheck.data.AssessmentEntity
import com.example.cybersafecheck.data.CyberSafeDatabase
import com.example.cybersafecheck.data.RiskAnswerEntity
import com.example.cybersafecheck.data.RiskRepository
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class ChecklistFragment : Fragment(R.layout.fragment_checklist) {

    private lateinit var repository: RiskRepository
    private lateinit var adapter: RiskAdapter

    // Local copy of the rows so a recycled row shows the latest answer
    private val answers = mutableListOf<RiskAnswerEntity>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = RiskRepository(CyberSafeDatabase.getInstance(requireContext()).riskDao())
        adapter = RiskAdapter()

        val recyclerView = view.findViewById<RecyclerView>(R.id.risk_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            repository.seedIfEmpty()
            answers.clear()
            answers.addAll(repository.getAll())
            adapter.notifyDataSetChanged()
        }

        // Listens for "Save to History" from ScoreDialogFragment
        parentFragmentManager.setFragmentResultListener(
            ScoreDialogFragment.RESULT_KEY, viewLifecycleOwner
        ) { _, bundle ->
            val flagged = bundle.getInt(ScoreDialogFragment.RESULT_FLAGGED)
            val total = bundle.getInt(ScoreDialogFragment.RESULT_TOTAL)
            saveAssessment(flagged, total)
        }

        view.findViewById<View>(R.id.btn_calculate_score).setOnClickListener {
            showScoreDialog()
        }

        view.findViewById<View>(R.id.btn_view_history).setOnClickListener {
            findNavController().navigate(R.id.action_checklist_to_history)
        }

        view.findViewById<View>(R.id.btn_reset).setOnClickListener {
            showResetConfirmation()
        }
    }

    private fun showScoreDialog() {
        val flagged = answers.count { it.isFlagged }
        val total = answers.size

        val breakdown = answers
            .groupBy { it.category }
            .entries
            .joinToString("\n") { (category, items) ->
                val flaggedInCategory = items.count { it.isFlagged }
                val label =
                    category.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() }
                "$label: $flaggedInCategory/${items.size} flagged"
            }

        ScoreDialogFragment.newInstance(flagged, total, breakdown)
            .show(parentFragmentManager, "score_dialog")
    }

    private fun saveAssessment(flagged: Int, total: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            val dao = CyberSafeDatabase.getInstance(requireContext()).assessmentDao()
            dao.insert(
                AssessmentEntity(
                    timestamp = System.currentTimeMillis(),
                    flaggedCount = flagged,
                    totalCount = total
                )
            )
        }
    }

    private fun showResetConfirmation() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Reset Checklist")
            .setMessage("This clears every answer back to OFF. This can't be undone.")
            .setPositiveButton("Reset") { _, _ ->
                viewLifecycleOwner.lifecycleScope.launch {
                    repository.resetAll()
                    answers.clear()
                    answers.addAll(repository.getAll())
                    adapter.notifyDataSetChanged()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private inner class RiskHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val categoryText: TextView = view.findViewById(R.id.risk_category)
        private val questionText: TextView = view.findViewById(R.id.risk_question)
        private val riskSwitch: SwitchCompat = view.findViewById(R.id.risk_switch)

        fun bind(item: RiskAnswerEntity) {
            categoryText.text = item.category.replace("_", " ")
            questionText.text = item.question

            riskSwitch.setOnCheckedChangeListener(null)
            riskSwitch.isChecked = item.isFlagged
            riskSwitch.setOnCheckedChangeListener { _, isChecked ->
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    answers[position] = answers[position].copy(isFlagged = isChecked)
                    val id = answers[position].itemId
                    viewLifecycleOwner.lifecycleScope.launch {
                        repository.setFlagged(id, isChecked)
                    }
                }
            }

            questionText.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val action = ChecklistFragmentDirections
                        .actionChecklistToDetail(answers[position].itemId)
                    findNavController().navigate(action)
                }
            }
        }
    }

    private inner class RiskAdapter : RecyclerView.Adapter<RiskHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RiskHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_risk, parent, false)
            return RiskHolder(view)
        }

        override fun onBindViewHolder(holder: RiskHolder, position: Int) {
            holder.bind(answers[position])
        }

        override fun getItemCount() = answers.size
    }
}

//Author: Mangesana E
//Student number: 2030630053