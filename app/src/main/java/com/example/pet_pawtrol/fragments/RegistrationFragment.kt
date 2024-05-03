package com.example.pet_pawtrol.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import com.example.pet_pawtrol.Entity.Users
import com.example.pet_pawtrol.MAIN
import com.example.pet_pawtrol.MainDb
import com.example.pet_pawtrol.R
import com.example.pet_pawtrol.databinding.FragmentRegistrationBinding

class RegistrationFragment : Fragment() {

    private lateinit var binding: FragmentRegistrationBinding
    private val db = MainDb.getDb(MAIN)
    var count = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imgButtonAut.setOnClickListener {
            MAIN.navController.navigate(R.id.action_registrationFragment_to_autorizationFragment)
        }

        binding.regButtton.setOnClickListener {
            registrationUser()
        }
    }
    private fun registrationUser() {
        val database = MainDb.getDb(MAIN)
        if (binding.pasEditText.text.toString().trim() == binding.dubPasEditText.text.toString().trim()) {
            database.getDao().getAllUser().asLiveData().observe(MAIN) { list ->
                if (list.isEmpty()) {
                    val user = Users(
                        null,
                        binding.firstNameEditText.text.toString().trim(),
                        binding.lastNameEditText.text.toString().trim(),
                        binding.phoneNumberEditText.text.toString().trim(),
                        binding.emailEditText.text.toString().trim(),
                        binding.loginEditText.text.toString().trim(),
                        binding.pasEditText.text.toString().trim()
                    )
                    Thread {
                        db.getDao().insertUser(user)
                    }.start()
                    MAIN.navController.navigate(R.id.action_registrationFragment_to_autorizationFragment)
                    Toast.makeText(
                        MAIN,
                        "Зарегистрирован пользователь ${binding.lastNameEditText.text.toString()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else{


                    list.forEach { userdata ->
                        if (userdata.email == binding.emailEditText.text.toString().trim()) {
                            count = 1
                            return@forEach
                        }
                    }
                    if (count == 1) {
                        Toast.makeText(MAIN, "Такой пользователь уже есть", Toast.LENGTH_SHORT)
                            .show()
                    }
                    if (count == 0) {
                        val user = Users(
                            null,
                            binding.firstNameEditText.text.toString(),
                            binding.lastNameEditText.text.toString(),
                            binding.phoneNumberEditText.text.toString(),
                            binding.emailEditText.text.toString(),
                            binding.loginEditText.text.toString(),
                            binding.pasEditText.text.toString()
                        )
                        Thread {
                            db.getDao().insertUser(user)
                        }.start()
                        MAIN.navController.navigate(R.id.action_registrationFragment_to_autorizationFragment)
                        Toast.makeText(
                            MAIN,
                            "Зарегистрирован пользователь ${binding.lastNameEditText.text.toString()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
        else {
            Toast.makeText(MAIN, "Пароли не совпадают", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        fun newInstance() = RegistrationFragment()
    }
}