package com.example.cybersafecheck

//Author: Mangesana E
//Student number: 2030630053
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.cybersafecheck.network.BreachMatcher
import com.example.cybersafecheck.network.PwnedPasswordsClient
import com.example.cybersafecheck.network.Sha1Util
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.text.NumberFormat

class PasswordCheckFragment : Fragment(R.layout.fragment_password_check) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val passwordInput = view.findViewById<EditText>(R.id.password_input)
        val checkButton = view.findViewById<Button>(R.id.btn_check_exposure)
        val progressBar = view.findViewById<ProgressBar>(R.id.check_progress)
        val resultText = view.findViewById<TextView>(R.id.check_result)

        checkButton.setOnClickListener {
            val password = passwordInput.text.toString()
            if (password.isEmpty()) {
                passwordInput.error = "Enter a password to check"
                return@setOnClickListener
            }

            // Step 1: hash the password on the phone
            val hash = Sha1Util.sha1Hex(password)

            // Step 2: only the first 5 characters get sent; the other 35 stay here
            val prefix = hash.substring(0, 5)
            val suffix = hash.substring(5)

            // The password is never logged or shown, and the box is cleared
            passwordInput.text.clear()

            checkButton.isEnabled = false
            progressBar.visibility = View.VISIBLE
            resultText.text = ""

            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    // Step 3: the network call runs on Dispatchers.IO so the screen doesn't freeze
                    val responseBody = withContext(Dispatchers.IO) {
                        PwnedPasswordsClient.api.checkRange(prefix).string()
                    }

                    // Step 4: back on the main thread, look for our suffix in the list
                    val count = BreachMatcher.countFor(responseBody, suffix)
                    if (count == 0L) {
                        resultText.text = "Not found in known breaches"
                        resultText.setTextColor(Color.rgb(46, 158, 79))
                    } else {
                        val timesText = NumberFormat.getInstance().format(count)
                        resultText.text =
                            "Found $timesText times, consider changing this password"
                        resultText.setTextColor(Color.rgb(229, 57, 53))
                    }
                } catch (e: IOException) {
                    resultText.text = "No internet connection. Please try again."
                    resultText.setTextColor(Color.rgb(229, 57, 53))
                } catch (e: HttpException) {
                    resultText.text = "The breach service had a problem. Please try again later."
                    resultText.setTextColor(Color.rgb(229, 57, 53))
                } finally {
                    progressBar.visibility = View.GONE
                    checkButton.isEnabled = true
                }
            }
        }
    }
}
