package com.example.cybersafecheck

//Author: Mangesana E
//Student number: 2030630053
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.cybersafecheck.data.CyberSafeDatabase
import com.example.cybersafecheck.data.RiskRepository
import com.example.cybersafecheck.model.RiskLab
import kotlinx.coroutines.launch

class RiskDetailFragment : Fragment(R.layout.fragment_risk_detail) {

    private val args: RiskDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = args.itemId
        val repository = RiskRepository(CyberSafeDatabase.getInstance(requireContext()).riskDao())

        viewLifecycleOwner.lifecycleScope.launch {
            val answer = repository.getById(id) ?: return@launch
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
}