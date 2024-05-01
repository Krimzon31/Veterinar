package com.example.pet_pawtrol.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pet_pawtrol.adapters.SearchAdapter
import com.example.pet_pawtrol.adapters.SearchModel
import com.example.pet_pawtrol.databinding.FragmentSerchRecycleBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import java.io.IOException

class SerchRecycleFragment : Fragment() {

    private lateinit var binding: FragmentSerchRecycleBinding
    private lateinit var adapter: SearchAdapter
    var list = arrayListOf<SearchModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSerchRecycleBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenStarted {
            getData()
            initRcView()
        }
    }

    private suspend fun getData() = withContext(Dispatchers.IO) {
        val url = "https://zoon.ru/penza/p-veterinar/"
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val document = Jsoup.parse(response.body?.string())

            val containers = document.select("div[class=js-results-item]")
            for (container in containers) {
                val name = container.select("div[class=specialist]").select("div[class=specialist-top-info]").select("a[class=prof-name specialist-name js-specialist-card-link js-item-url js-link]").text()
                val phNumber = container.select("div[class=specialist]").select("div[class=z-flex z-gap--12]").select("div[class=z-flex z-flex--column z-gap--4 z-mt--12 js-link]").select("div[class=specialist-phone]").select("div[class=js-phone js-phone-box  phoneView phone-hidden]").select("a").text()
                val otz = "Отзывы: ${container.select("div[class=specialist]").select("div[class=specialist-photo-container]").select("div[class=specialist-mark]").select("div[class=specialist-mark]").select("span[class=stars-rating-text strong]").text()}"
                val specialization = container.select("div[class=specialist]").select("div[class=specialist-top-info]").select("div[class=prof-spec-list specialist-spec-list]").select("a").text()
                val price = "Цена: 100p"
                val urlProfile = container.select("div[class=specialist]").select("div[class=specialist-top-info]").select("a[class=prof-name specialist-name js-specialist-card-link js-item-url js-link]").attr("href")
                list.add(SearchModel(name, phNumber, otz, specialization, price, urlProfile))
            }

            println(list)
        }
    }

    private fun initRcView() = with(binding){
        rcSerch.layoutManager = LinearLayoutManager(activity)
        adapter = SearchAdapter()
        rcSerch.adapter = adapter
        adapter.submitList(list)
    }

    companion object {
        @JvmStatic
        fun newInstance() = SerchRecycleFragment()
    }
}