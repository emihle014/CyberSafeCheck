package com.example.cybersafecheck

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cybersafecheck.data.CyberSafeDatabase
import com.example.cybersafecheck.data.RiskAnswerEntity
import com.example.cybersafecheck.data.RiskRepository
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

        // Load from Room (on a background thread inside the repository)
        viewLifecycleOwner.lifecycleScope.launch {
            repository.seedIfEmpty()
            answers.clear()
            answers.addAll(repository.getAll())
            adapter.notifyDataSetChanged()
        }
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
                    // Update the local copy, then write to Room
                    answers[position] = answers[position].copy(isFlagged = isChecked)
                    val id = answers[position].itemId
                    viewLifecycleOwner.lifecycleScope.launch {
                        repository.setFlagged(id, isChecked)
                    }
                }
            }

            // Only the question text opens the detail screen, not the switch
            questionText.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    parentFragmentManager.beginTransaction()
                        .replace(
                            R.id.fragment_container,
                            RiskDetailFragment.newInstance(answers[position].itemId)
                        )
                        .addToBackStack(null)
                        .commit()
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