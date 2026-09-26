package com.example.cybersafecheck
//Author: Mangesana E
//Student number: 2030630053
import android.app.Dialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ScoreDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val flagged = requireArguments().getInt(ARG_FLAGGED)
        val total = requireArguments().getInt(ARG_TOTAL)
        val breakdown = requireArguments().getString(ARG_BREAKDOWN).orEmpty()

        val message = "Flagged: $flagged / $total\n\n$breakdown"

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle("Your Risk Score")
            .setMessage(message)
            .setPositiveButton("Save to History") { _, _ ->
                parentFragmentManager.setFragmentResult(
                    RESULT_KEY,
                    bundleOf(RESULT_FLAGGED to flagged, RESULT_TOTAL to total)
                )
            }
            .setNegativeButton("Close", null)
            .create()
    }

    companion object {
        private const val ARG_FLAGGED = "arg_flagged"
        private const val ARG_TOTAL = "arg_total"
        private const val ARG_BREAKDOWN = "arg_breakdown"

        const val RESULT_KEY = "score_dialog_result"
        const val RESULT_FLAGGED = "result_flagged"
        const val RESULT_TOTAL = "result_total"

        fun newInstance(flagged: Int, total: Int, breakdown: String): ScoreDialogFragment {
            return ScoreDialogFragment().apply {
                arguments = bundleOf(
                    ARG_FLAGGED to flagged,
                    ARG_TOTAL to total,
                    ARG_BREAKDOWN to breakdown
                )
            }
        }
    }
}