package com.example.tiptop

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.tiptop.databinding.FragmentTaskBinding

class TaskFragment : Fragment() {

    private var _binding: FragmentTaskBinding? = null
    private val binding get() = _binding!!

    // Access the shared ViewModel from the activity
    private val viewModel: GameViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the task text using HTML content from the ViewModel
        binding.tvTask.text = Html.fromHtml(viewModel.currentTask)

        // Set the number of players and seek bar state from the ViewModel
        binding.tvNumberOfPlayers.text = viewModel.numberOfPlayers.toString()
        binding.swMatchCards.isChecked = viewModel.areNumbersMatchingPlayers
        binding.seekBar.progress = viewModel.numberOfPlayers

        // SeekBar change listener to update the number of players
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                viewModel.updateNumberOfPlayers(progress)
                binding.tvNumberOfPlayers.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        // Button click listeners
        binding.btnChangeTask.setOnClickListener {
            if (viewModel.taskCounter < viewModel.taskList.size - 1) {
                viewModel.getNextTask()
                setTextSize()
                binding.tvTask.text = Html.fromHtml(viewModel.currentTask)
            } else {
                Toast.makeText(activity, getString(R.string.no_more_tasks), Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnStartRound.setOnClickListener {
            findNavController().navigate(R.id.action_taskFragment_to_gameFragment)
        }

        binding.btnNewGame.setOnClickListener {
            findNavController().navigate(R.id.action_taskFragment_to_homeFragment)
        }

        // Switch listener to update card matching preference
        binding.swMatchCards.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateSelection(isChecked)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Function to adjust text size based on task length
    private fun setTextSize() {
        if (viewModel.currentTask.length < 150) binding.tvTask.textSize = 16F
        else if (viewModel.currentTask.length < 250) binding.tvTask.textSize = 14F
        else binding.tvTask.textSize = 12F
    }
}
