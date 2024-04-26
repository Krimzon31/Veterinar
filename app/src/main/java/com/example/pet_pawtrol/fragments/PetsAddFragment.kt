package com.example.pet_pawtrol.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pet_pawtrol.R
import com.example.pet_pawtrol.databinding.FragmentPetsAddBinding


class PetsAddFragment : Fragment() {

    private lateinit var binding: FragmentPetsAddBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPetsAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tvText = binding.tvText
        val text = arguments?.getString("MyArg")
        tvText.text = text
    }

    companion object {
        @JvmStatic
        fun newInstance() = PetsAddFragment()
    }
}