package com.example.myhashapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import com.example.hashapp.HomeViewModel
import com.example.myhashapplication.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {


    private val homeViewModel: HomeViewModel by viewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val aesKey = "1234567890987654" // Predefined AES key
    private val desKey = "myDesKey" // Predefined DES key

    override fun onResume() {
        super.onResume()
        val hashAlgorithms = resources.getStringArray(R.array.hash_algorithms)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_menu, hashAlgorithms)
        binding.autoCompleteTextView.setAdapter(arrayAdapter)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)
        binding.generateButton.setOnClickListener {
            onGenerateClicked()
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.clear_menu -> {
                binding.plainText.text.clear()
                showSnackBar("Cleared")
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun onGenerateClicked() {
        if (binding.plainText.text.isEmpty()) {
            showSnackBar("Field is Empty")
        } else {
            lifecycle.coroutineScope.launch {
                applyAnimations()
                val algorithm = binding.autoCompleteTextView.text.toString()
                if (algorithm == "AES" || algorithm == "DES") {
                    performEncryption(algorithm)
                } else {
                    navigateToSuccess(getHashData())
                }
            }
        }
    }

    private fun getHashData(): String {
        val algorithm = binding.autoCompleteTextView.text.toString()
        val plaintext = binding.plainText.text.toString()
        return homeViewModel.getHash(plaintext, algorithm)
    }

    private suspend fun applyAnimations() {
        binding.generateButton.isClickable = false
        binding.titleTextView.animate().alpha(0f).duration = 400L
        binding.generateButton.animate().alpha(0f).duration = 400L
        binding.textInputLayout.animate().alpha(0f).translationXBy(1200f).duration = 400L
        binding.plainText.animate().alpha(0f).translationXBy(-1200f).duration = 400L

        delay(300)

        binding.successBackground.animate().alpha(1f).duration = 600L
        binding.successBackground.animate().rotationBy(720f).duration = 600L
        binding.successBackground.animate().scaleXBy(900f).duration = 800L
        binding.successBackground.animate().scaleYBy(900f).duration = 800L

        delay(700)

        binding.successImageView.animate().alpha(1f).duration = 1000L

        delay(1500L)
    }

    private fun performEncryption(algorithm: String) {
        val plaintext = binding.plainText.text.toString()
        val encryptedText = when (algorithm) {
            "AES" -> AESUtil.encrypt(plaintext, aesKey)
            "DES" -> DESUtil.encrypt(plaintext, desKey)
            else -> "" // Handle unsupported algorithm
        }
        navigateToSuccess(encryptedText)
    }

    private fun showSnackBar(message: String) {
        val snackBar = Snackbar.make(
            binding.rootLayout,
            message,
            Snackbar.LENGTH_SHORT
        )
        snackBar.setAction("Okay"){}
        snackBar.setActionTextColor(ContextCompat.getColor(requireContext(), R.color.blue))
        snackBar.show()
    }

    private fun navigateToSuccess(hash: String) {
        val directions = HomeFragmentDirections.actionHomeFragmentToSuccesFragment(hash)
        findNavController().navigate(directions)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
