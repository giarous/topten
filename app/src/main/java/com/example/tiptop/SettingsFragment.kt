package com.example.tiptop

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.tiptop.databinding.FragmentSettingsBinding
import java.util.*


class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: GameViewModel by activityViewModels()

    // Variable for changing the display language
    lateinit var locale: Locale

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


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

    //Function for changing the language
    private fun setLocale(localeName: String) {

        locale = Locale(localeName)
        val res = resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.setLocale(locale)
        res.updateConfiguration(conf, dm)

        findNavController().navigate(R.id.action_settingsFragment_to_homeFragment)
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