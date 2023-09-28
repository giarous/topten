package com.example.tiptop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.tiptop.databinding.FragmentHomeBinding
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GameViewModel by activityViewModels()

    // Variable for changing the display language
    private lateinit var locale: Locale

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Handle "Start New Game" button click
        binding.startNewGame.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_taskFragment)
        }

        // Handle "Go to Tasks" button click
        binding.btnGoToTaskFragment.setOnClickListener {
            viewModel.shuffleTaskList()
            findNavController().navigate(R.id.action_homeFragment_to_allTasksFragment)
        }

        // Handle "Go to Information" button click
        binding.btnGoToInformationFragment.setOnClickListener {
            Toast.makeText(activity, getString(R.string.function_unavailable), Toast.LENGTH_SHORT).show()
        }

        // Reinitialize data when the fragment is created
        viewModel.reinitializeData()

        // Change the language to Russian
        binding.ruFlag.setOnClickListener {
            setLocale("ru")
        }

        // Change the language to English
        binding.enFlag.setOnClickListener {
            setLocale("en")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Function for changing the display language
    private fun setLocale(localeName: String) {
        locale = Locale(localeName)
        val res = resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.setLocale(locale)
        res.updateConfiguration(conf, dm)

        // Notify the ViewModel about the language change
        viewModel.changeLanguage(localeName)

        // Recreate the activity to apply language changes
        activity?.recreate()
    }
}
