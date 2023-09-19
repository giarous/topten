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
import androidx.navigation.fragment.findNavController
import com.example.tiptop.databinding.FragmentAllTasksBinding
import com.example.tiptop.databinding.FragmentTaskBinding


class AllTasksFragment : Fragment() {


    private var _binding: FragmentAllTasksBinding? = null

    private var taskCounter = 0

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: GameViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAllTasksBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.tvTask1.text = Html.fromHtml(viewModel.taskList[taskCounter])
        binding.tvTask2.text = Html.fromHtml(viewModel.taskList[taskCounter+1])


        binding.btnChangeTasks.setOnClickListener {
            if(taskCounter<viewModel.taskList.size-3){
                taskCounter+=2
                setTextSize()
                binding.tvTask1.text = Html.fromHtml(viewModel.taskList[taskCounter])
                binding.tvTask2.text = Html.fromHtml(viewModel.taskList[taskCounter+1])
            }
            else{
                Toast.makeText(activity, getString(R.string.no_more_tasks), Toast.LENGTH_SHORT).show()
            }
        }


        binding.btnNewGame.setOnClickListener {
            findNavController().navigate(R.id.action_allTasksFragment_to_homeFragment)
            //viewModel.reinitializeData()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setTextSize(){
        if(viewModel.taskList[taskCounter].length < 150) binding.tvTask1.textSize = 16F
        else if (viewModel.taskList[taskCounter].length<250) binding.tvTask1.textSize = 14F
        else binding.tvTask1.textSize = 12F

        if(viewModel.taskList[taskCounter+1].length < 150) binding.tvTask2.textSize = 16F
        else if (viewModel.taskList[taskCounter+1].length<250) binding.tvTask2.textSize = 14F
        else binding.tvTask2.textSize = 12F
    }

}