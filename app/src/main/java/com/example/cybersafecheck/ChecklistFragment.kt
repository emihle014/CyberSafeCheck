package com.example.cybersafecheck

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cybersafecheck.model.RiskItem
import com.example.cybersafecheck.model.RiskLab

class ChecklistFragment : Fragment(R.layout.fragment_checklist) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.risk_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = RiskAdapter(RiskLab.items)
    }

    private inner class RiskHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val questionText: TextView = view.findViewById(R.id.risk_question)
        private val riskSwitch: SwitchCompat = view.findViewById(R.id.risk_switch)

        fun bind(item: RiskItem) {
            questionText.text = item.question

            // Remove the listener BEFORE setting the state, otherwise recycled
            // rows fire the old listener and record wrong answers.
            riskSwitch.setOnCheckedChangeListener(null)
            riskSwitch.isChecked = item.isFlagged
            riskSwitch.setOnCheckedChangeListener { _, isChecked ->
                item.isFlagged = isChecked
            }

            // Only the text opens the detail screen, not the switch
            questionText.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, RiskDetailFragment.newInstance(item.id))
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    private inner class RiskAdapter(private val items: List<RiskItem>) :
        RecyclerView.Adapter<RiskHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RiskHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_risk, parent, false)
            return RiskHolder(view)
        }

        override fun onBindViewHolder(holder: RiskHolder, position: Int) {
            holder.bind(items[position])
        }

        override fun getItemCount() = items.size
    }
}