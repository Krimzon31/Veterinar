package com.example.pet_pawtrol.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.pet_pawtrol.MAIN
import com.example.pet_pawtrol.R
import com.example.pet_pawtrol.databinding.FragmentMoreInformationBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import java.io.IOException


class MoreInformationFragment : Fragment() {

    private lateinit var binding: FragmentMoreInformationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoreInformationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val urlProfile = arguments?.getString("urlProfile", "https://zoon.ru/penza/p-veterinar/ekaterina_dmitrievna_nikogosova/")
        lifecycleScope.launchWhenStarted {
            if (urlProfile != null) {
                setData(urlProfile)
            }
        }
        binding.BackImBut.setOnClickListener {
            MAIN.navController.navigate(R.id.action_moreInformationFragment_to_searchFragment)
        }
    }
    private suspend fun setData(urlProfile: String) = withContext(Dispatchers.IO) {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(urlProfile)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val document = Jsoup.parse(response.body?.string()).select("div[class=prof-page-wrapper]")

                val name = document.select("div[class=prof-page__header-wrapper]").select("div[class=prof-page__header]").select("div[class=prof-about prof-page__header-info]").select("div[class=z-flex z-flex--center-y z-gap--24]").select("h1").text()
                val stag = document.select("ul[class=prof-page__header-props fs-simple]").select("li").text()
                val phNumber = document.select("div[class=prof-page__header-wrapper]").select("div[class=prof-page__header]").select("a").text()
                val osenca = "Оценка: ${document.select("div[class=prof-page__header-rate]").select("div[class=prof-page__header-rating]").text()}"
                val information = document.select("div[class=service-page bg-gray js-phone-holder pd-m clearfix]").select("div[class=ss-container flexbox]").select("div[class=oh]").select("div[class=service-box-white service-block]").select("div[class=service-page-info]").select("div[class=service-description-box b0 vtop]").select("div[class=box-padding]").select("div[class=params-list params-list-default]").select("dl[class=fluid]").text()
                val price = "Цена: 100p"
                val urlImg = document.select("div[class=specialist]").select("div[class=specialist-top-info]").select("a[class=prof-name specialist-name js-specialist-card-link js-item-url js-link]").attr("href")

            withContext(Dispatchers.Main) {
                binding.tvName.text = name
                binding.tvStag.text = stag
                binding.tvOtz.text = osenca
                binding.tvPhoneNumber.text = phNumber
                binding.tvInformation.text = information
                binding.tvPrice.text = price
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = MoreInformationFragment()
    }
}