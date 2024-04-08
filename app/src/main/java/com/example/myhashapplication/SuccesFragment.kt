package com.example.myhashapplication

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.myhashapplication.databinding.FragmentSuccesBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SuccesFragment : Fragment() {

    private val args: SuccesFragmentArgs by navArgs( )

    private var _binding: FragmentSuccesBinding? =null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =FragmentSuccesBinding.inflate(inflater,container,false)

        binding.hashTextView.text = args.hash

        // Inflate the layout for this fragment
        binding.copyButton.setOnClickListener {
            onCopyCLicked()
        }

        return  binding.root
    }
    private fun onCopyCLicked() {
        lifecycleScope.launch {
            copyToClipboard(args.hash)
        }

    }
    private fun copyToClipboard(hash: String) {
        val clipboardManager = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("Encrypted Text",hash)
        clipboardManager.setPrimaryClip(clipData)
    }
    private suspend fun applyAnimation()
    {
        binding.include.msgBackground.animate().translationY(80f).duration = 200L
        binding.include.msgTextView.animate().translationY(80f).duration = 200L

        delay(2000L)

        binding.include.msgBackground.animate().translationY(-80f).duration = 500L
        binding.include.msgTextView.animate().translationY(-80f).duration = 500L
    }
}