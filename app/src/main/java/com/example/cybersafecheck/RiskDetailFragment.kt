package com.example.cybersafecheck

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.cybersafecheck.model.RiskLab

class RiskDetailFragment : Fragment(R.layout.fragment_risk_detail) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = requireArguments().getString(ARG_ITEM_ID)!!
        val item = RiskLab.getItem(id) ?: return

        view.findViewById<TextView>(R.id.detail_category).text = item.category.name.replace("_", " ")
        view.findViewById<TextView>(R.id.detail_question).text = item.question
        view.findViewById<TextView>(R.id.detail_explanation).text = item.explanation
    }

    companion object {
        private const val ARG_ITEM_ID = "item_id"

        fun newInstance(itemId: String) = RiskDetailFragment().apply {
            arguments = Bundle().apply { putString(ARG_ITEM_ID, itemId) }
        }
    }
}