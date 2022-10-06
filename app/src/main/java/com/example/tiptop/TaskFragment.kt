package com.example.tiptop

import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.tiptop.databinding.FragmentTaskBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class TaskFragment : Fragment() {

    private var _binding: FragmentTaskBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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


        binding.tvTask.text = Html.fromHtml(viewModel.currentTask)

        binding.tvNumberOfPlayers.text = viewModel.numberOfPlayers.toString()
        binding.swMatchCards.isChecked = viewModel.areNumbersMatchingPlayers

        binding.seekBar.progress = viewModel.numberOfPlayers

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                viewModel.updateNumberOfPlayers(progress)
                binding.tvNumberOfPlayers.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        binding.btnChangeTask.setOnClickListener {
            if(viewModel.taskCounter<viewModel.taskList.size-1){
                viewModel.getNextTask()
                setTextSize()
                binding.tvTask.text = Html.fromHtml(viewModel.currentTask)
            }
            else{
                Toast.makeText(activity, getString(R.string.no_more_tasks), Toast.LENGTH_SHORT).show()
            }

        }

        binding.btnStartRound.setOnClickListener {
            findNavController().navigate(R.id.action_taskFragment_to_gameFragment)
        }

        binding.btnNewGame.setOnClickListener {
            findNavController().navigate(R.id.action_taskFragment_to_homeFragment)
        }

        binding.swMatchCards.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateSelection(isChecked)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setTextSize(){
        if(viewModel.currentTask.length < 150) binding.tvTask.textSize = 16F
        else if (viewModel.currentTask.length<250) binding.tvTask.textSize = 14F
        else binding.tvTask.textSize = 12F
    }
}