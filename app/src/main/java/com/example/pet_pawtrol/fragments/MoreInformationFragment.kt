package com.example.pet_pawtrol.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pet_pawtrol.R
import com.example.pet_pawtrol.databinding.FragmentMoreInformationBinding


class MoreInformationFragment : Fragment() {

    private lateinit var binding: FragmentMoreInformationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoreInformationBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance() = MoreInformationFragment()
    }
}