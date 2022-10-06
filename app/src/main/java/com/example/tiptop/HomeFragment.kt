package com.example.tiptop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.tiptop.databinding.FragmentHomeBinding
import java.util.*


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: GameViewModel by activityViewModels()
    // Create a ViewModel the first time the fragment is created.
    // If the fragment is re-created, it receives the same GameViewModel instance created by the
    // first fragment

    // Variable for changing the display language
    lateinit var locale: Locale


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        //Set display language to Russian
        //setLocale("ru")

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.startNewGame.setOnClickListener {
            //findNavController().navigate(R.id.action_homeFragment_to_playersFragment)
            findNavController().navigate(R.id.action_homeFragment_to_taskFragment)
        }

        binding.btnGoToTaskFragment.setOnClickListener {
            viewModel.shuffleTaskList()
            findNavController().navigate(R.id.action_homeFragment_to_allTasksFragment)
        }

        binding.btnGoToInformationFragment.setOnClickListener {

            //findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
            Toast.makeText(activity, getString(R.string.function_unavailable), Toast.LENGTH_SHORT).show()
            //val toast = Toast.makeText(this, "ЭТА ФУНКЦИЯ ВРЕМЕННО НЕДОСТУПНА :P", Toast.LENGTH_SHORT)
            //val v = toast.view.findViewById<View>(android.R.id.message) as TextView
            //v.gravity = Gravity.CENTER
            //toast.show()

        }

        viewModel.reinitializeData()


        //Change the language
        binding.ruFlag.setOnClickListener{
            setLocale("ru")
        }
        binding.enFlag.setOnClickListener{
            setLocale("en")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    //private fun checkLocale(local)
    //Function for changing the language
    private fun setLocale(localeName: String) {

        locale = Locale(localeName)
        val res = resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.setLocale(locale)
        res.updateConfiguration(conf, dm)

        viewModel.changeLanguage(localeName)

        activity?.recreate();
        //setLocale("ru")

        //getFragmentManager()?.beginTransaction()?.detach(this)?.attach(this)?.commit();

        //fragmentManager!!.beginTransaction().detach(this).attach(this).commit()

       /* val refresh = Intent(
            this,
            MainActivity::class.java
        )
        startActivity(refresh);
        overridePendingTransition( 0, 0);
        refresh.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish(); */
    }



}