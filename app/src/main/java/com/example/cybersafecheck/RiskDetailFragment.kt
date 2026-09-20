package com.example.cybersafecheck

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.cybersafecheck.data.CyberSafeDatabase
import com.example.cybersafecheck.data.RiskRepository
import com.example.cybersafecheck.model.RiskLab
import kotlinx.coroutines.launch

class RiskDetailFragment : Fragment(R.layout.fragment_risk_detail) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = requireArguments().getString(ARG_ITEM_ID)!!
        val repository = RiskRepository(CyberSafeDatabase.getInstance(requireContext()).riskDao())

        viewLifecycleOwner.lifecycleScope.launch {
            // Question, category and your answer come from Room
            val answer = repository.getById(id) ?: return@launch
            // The long explanation text comes from RiskLab
            val explanation = RiskLab.getItem(id)?.explanation.orEmpty()

            view.findViewById<TextView>(R.id.detail_category).text =
                answer.category.replace("_", " ")
            view.findViewById<TextView>(R.id.detail_question).text = answer.question
            view.findViewById<TextView>(R.id.detail_explanation).text = explanation
            view.findViewById<TextView>(R.id.detail_status).text =
                if (answer.isFlagged) "Your answer: Yes (flagged as a risk)"
                else "Your answer: No"
        }
    }

    companion object {
        private const val ARG_ITEM_ID = "item_id"

        fun newInstance(itemId: String) = RiskDetailFragment().apply {
            arguments = Bundle().apply { putString(ARG_ITEM_ID, itemId) }
        }
    }
}