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


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class GameFragment : Fragment() {

    private var _binding: FragmentGameBinding? = null

    private var selectedNumbers = ""
    private var numbersClicked = 0
    private var lastNumber = 0


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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

        binding.btnEndRound.setOnClickListener { newRound() }

        binding.btnNewGame.setOnClickListener { startNewGame() }
        binding.btnShowTask.setOnClickListener { showTask() }
        binding.tvTitleRound.text = getString(R.string.round) + " " + viewModel.currentRound.toString()
        binding.numberOfPoo.text = viewModel.numberOfPoo.toString()
        binding.numberOfUnicorns.text = viewModel.numberOfUnicorns.toString()

        //change to automatic selection of buttons
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

        if(viewModel.areNumbersMatchingPlayers) checkButtons()


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

    private fun numberClicked(num: Int){

        disableButtons(num)

        if(num<lastNumber){
            viewModel.updateUnicorns()
        }

        lastNumber = num
        numbersClicked++

        selectedNumbers = selectedNumbers.plus(" ").plus(num.toString()).plus(" ")
        binding.tvSelectedNumbers.text = selectedNumbers

        binding.numberOfPoo.text = viewModel.numberOfPoo.toString()
        binding.numberOfUnicorns.text = viewModel.numberOfUnicorns.toString()


        if(numbersClicked==viewModel.numberOfPlayers){
            endRound()
        }

        if(viewModel.numberOfUnicorns == 0){
            gameEnd()
        }

    }

    private fun disableButtons(num: Int){
        when(num){
            1 -> {
                binding.btn1.isEnabled = false
                binding.btn1.setBackgroundResource(R.drawable.button_disabled)}
            2 -> {
                binding.btn2.isEnabled = false
                binding.btn2.setBackgroundResource(R.drawable.button_disabled)}
            3 -> {
                binding.btn3.isEnabled = false
                binding.btn3.setBackgroundResource(R.drawable.button_disabled)}
            4 -> {
                binding.btn4.isEnabled = false
                binding.btn4.setBackgroundResource(R.drawable.button_disabled)}
            5 -> {
                binding.btn5.isEnabled = false
                binding.btn5.setBackgroundResource(R.drawable.button_disabled)}
            6 -> {
                binding.btn6.isEnabled = false
                binding.btn6.setBackgroundResource(R.drawable.button_disabled)}
            7 -> {
                binding.btn7.isEnabled = false
                binding.btn7.setBackgroundResource(R.drawable.button_disabled)}
            8 -> {
                binding.btn8.isEnabled = false
                binding.btn8.setBackgroundResource(R.drawable.button_disabled)}
            9 -> {
                binding.btn9.isEnabled = false
                binding.btn9.setBackgroundResource(R.drawable.button_disabled)}
            else ->{
                binding.btn10.isEnabled = false
                binding.btn10.setBackgroundResource(R.drawable.button_disabled)}
        }
    }

    //Loop through all the buttons
    private fun checkButtons()
    {
        if(viewModel.numberOfPlayers<5){
            for (i in 4 downTo viewModel.numberOfPlayers) {
                val v: View =  binding.firstRow.getChildAt(i)
                if (v is Button) {
                    v.isEnabled = false
                    v.setBackgroundResource(R.drawable.button_disabled)
                }
            }

            for (i in 4 downTo 0) {
                val v: View =  binding.secondRow.getChildAt(i)
                if (v is Button) {
                    v.isEnabled = false
                    v.setBackgroundResource(R.drawable.button_disabled)
                }
            }
        }
        else{
            for (i in 4 downTo viewModel.numberOfPlayers-5) {
                val v: View =  binding.secondRow.getChildAt(i)
                if (v is Button) {
                    v.isEnabled = false
                    v.setBackgroundResource(R.drawable.button_disabled)
                }
            }
        }



    }


    // Creates and shows an AlertDialog with the task.
    private fun showTask(){

        //Need to check the theme
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.dialog_task_title))
            .setMessage(Html.fromHtml(viewModel.currentTask))
            //.setCancelable(false)
            //.setNegativeButton(getString(R.string.exit)) { _, _ -> exitGame() }
            .setPositiveButton("OK", null)
            .setBackground(resources.getDrawable(R.drawable.background_dialog))
            .show()
    }


    private fun gameEnd()
    {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.dialog_loss_title))
            .setMessage(getString(R.string.dialog_loss))
            .setPositiveButton("OK") { _, _ -> startNewGame()}
            .setBackground(resources.getDrawable(R.drawable.background_dialog))
            .show()
    }

    private fun endRound(){

        if(viewModel.currentRound == 5){
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.dialog_win_title))
                .setMessage(getString(R.string.dialog_win))
                .setPositiveButton(getString(R.string.start_game)) { _, _ -> startNewGame()}
                .setBackground(resources.getDrawable(R.drawable.background_dialog))
                .show()
        }
        else{
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.dialog_round_end_title))
                .setMessage(getString(R.string.dialog_round_end))
                //.setCancelable(false)
                //.setNegativeButton(getString(R.string.exit)) { _, _ -> exitGame() }
                .setPositiveButton(getString(R.string.dialog_round_end_button)) { _, _ -> newRound()}
                .setBackground(resources.getDrawable(R.drawable.background_dialog))
                .show()
        }

    }

    private fun newRound (){
        /*if(viewModel.currentRound == 5) endRound()
        else {
        }*/
        viewModel.updateRoundNumber()
        findNavController().navigate(R.id.action_gameFragment_to_taskFragment)
    }

    /*
     * Exits the game.
     */
    private fun exitGame() {
        activity?.finish()
    }

    private fun startNewGame(){
        findNavController().navigate(R.id.action_gameFragment_to_homeFragment)
    }




}