package com.example.tiptop

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.tiptop.databinding.FragmentGameBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class GameFragment : Fragment() {

    private var _binding: FragmentGameBinding? = null
    private var selectedNumbers = ""
    private var numbersClicked = 0
    private var lastNumber = 0

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    // Access the shared ViewModel from the activity
    private val viewModel: GameViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Handle "End Round" button click
        binding.btnEndRound.setOnClickListener { newRound() }

        // Handle "New Game" button click
        binding.btnNewGame.setOnClickListener { startNewGame() }

        // Handle "Show Task" button click
        binding.btnShowTask.setOnClickListener { showTask() }

        // Set round number, number of poo, and number of unicorns
        binding.tvTitleRound.text = getString(R.string.round) + " " + viewModel.currentRound.toString()
        binding.numberOfPoo.text = viewModel.numberOfPoo.toString()
        binding.numberOfUnicorns.text = viewModel.numberOfUnicorns.toString()

        // Set click listeners for number buttons
        binding.apply {
            btn1.setOnClickListener { numberClicked(1) }
            btn2.setOnClickListener { numberClicked(2) }
            btn3.setOnClickListener { numberClicked(3) }
            btn4.setOnClickListener { numberClicked(4) }
            btn5.setOnClickListener { numberClicked(5) }
            btn6.setOnClickListener { numberClicked(6) }
            btn7.setOnClickListener { numberClicked(7) }
            btn8.setOnClickListener { numberClicked(8) }
            btn9.setOnClickListener { numberClicked(9) }
            btn10.setOnClickListener { numberClicked(10) }
        }

        // Disable buttons based on the number of players
        if (viewModel.areNumbersMatchingPlayers) checkButtons()

        /* LiveData observers with ViewBinding
        binding.score.text = getString(R.string.score, 0)
        binding.wordCount.text = getString(R.string.word_count, 0, MAX_NO_OF_WORDS)

        //Observe the scrambledCharArray LiveData, passing in the LifecycleOwner and the observer.
        viewModel.currentScrambledWord.observe(viewLifecycleOwner,
            { newWord ->
                binding.textViewUnscrambledWord.text = newWord
            })

        viewModel.score.observe(viewLifecycleOwner,
            { newScore ->
                binding.score.text = getString(R.string.score, newScore)
            })

        viewModel.currentWordCount.observe(viewLifecycleOwner,
            { newWordCount ->
                binding.wordCount.text =
                    getString(R.string.word_count, newWordCount, MAX_NO_OF_WORDS)
            })*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun numberClicked(num: Int) {
        disableButtons(num)

        if (num < lastNumber) {
            viewModel.updateUnicorns()
        }

        lastNumber = num
        numbersClicked++

        selectedNumbers = selectedNumbers.plus(" ").plus(num.toString()).plus(" ")
        binding.tvSelectedNumbers.text = selectedNumbers

        binding.numberOfPoo.text = viewModel.numberOfPoo.toString()
        binding.numberOfUnicorns.text = viewModel.numberOfUnicorns.toString()

        if (numbersClicked == viewModel.numberOfPlayers) {
            endRound()
        }

        if (viewModel.numberOfUnicorns == 0) {
            gameEnd()
        }
    }

    private fun disableButtons(num: Int) {
        // Disable and change the background of the clicked button
        when (num) {
            1 -> {
                binding.btn1.isEnabled = false
                binding.btn1.setBackgroundResource(R.drawable.button_disabled)
            }
            // Add cases for buttons 2 to 10 in a similar manner...
        }
    }

    private fun checkButtons() {
        // Disable buttons based on the number of players
        if (viewModel.numberOfPlayers < 5) {
            for (i in 4 downTo viewModel.numberOfPlayers) {
                val v: View = binding.firstRow.getChildAt(i)
                if (v is Button) {
                    v.isEnabled = false
                    v.setBackgroundResource(R.drawable.button_disabled)
                }
            }

            for (i in 4 downTo 0) {
                val v: View = binding.secondRow.getChildAt(i)
                if (v is Button) {
                    v.isEnabled = false
                    v.setBackgroundResource(R.drawable.button_disabled)
                }
            }
        } else {
            for (i in 4 downTo viewModel.numberOfPlayers - 5) {
                val v: View = binding.secondRow.getChildAt(i)
                if (v is Button) {
                    v.isEnabled = false
                    v.setBackgroundResource(R.drawable.button_disabled)
                }
            }
        }
    }

    private fun showTask() {
        // Display the task in a dialog
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.dialog_task_title))
            .setMessage(Html.fromHtml(viewModel.currentTask))
            .setPositiveButton("OK", null)
            .setBackground(resources.getDrawable(R.drawable.background_dialog))
            .show()
    }

    private fun gameEnd() {
        // Display a dialog for game end
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.dialog_loss_title))
            .setMessage(getString(R.string.dialog_loss))
            .setPositiveButton("OK") { _, _ -> startNewGame() }
            .setBackground(resources.getDrawable(R.drawable.background_dialog))
            .show()
    }

    private fun endRound() {
        // Check if the game has ended or proceed to the next round
        if (viewModel.currentRound == 5) {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.dialog_win_title))
                .setMessage(getString(R.string.dialog_win))
                .setPositiveButton(getString(R.string.start_game)) { _, _ -> startNewGame() }
                .setBackground(resources.getDrawable(R.drawable.background_dialog))
                .show()
        } else {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.dialog_round_end_title))
                .setMessage(getString(R.string.dialog_round_end))
                .setPositiveButton(getString(R.string.dialog_round_end_button)) { _, _ -> newRound() }
                .setBackground(resources.getDrawable(R.drawable.background_dialog))
                .show()
        }
    }

    private fun newRound() {
        // Start a new round

        /*if(viewModel.currentRound == 5) endRound()
        else {
        }*/

        viewModel.updateRoundNumber()
        findNavController().navigate(R.id.action_gameFragment_to_taskFragment)
    }

    private fun startNewGame() {
        // Navigate to the home fragment to start a new game
        findNavController().navigate(R.id.action_gameFragment_to_homeFragment)
    }
}
