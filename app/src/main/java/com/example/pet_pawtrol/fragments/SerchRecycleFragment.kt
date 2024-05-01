package com.example.pet_pawtrol.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pet_pawtrol.Entity.Veterinars
import com.example.pet_pawtrol.MAIN
import com.example.pet_pawtrol.MainDb
import com.example.pet_pawtrol.adapters.SearchAdapter
import com.example.pet_pawtrol.adapters.SearchModel
import com.example.pet_pawtrol.databinding.FragmentSerchRecycleBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import java.io.IOException

class SerchRecycleFragment : Fragment() {

    private lateinit var binding: FragmentSerchRecycleBinding
    private lateinit var adapter: SearchAdapter
    var list = arrayListOf<SearchModel>()
    var count = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSerchRecycleBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenStarted {
            /*Proverca().observe(viewLifecycleOwner) { vlist ->
                if (vlist.isEmpty()) {
                    count=1
                }
            }*/
            if (count == 1) {
                setData()
            }
            init()
        }
    }

    suspend fun init() = coroutineScope {
            initRcView()
    }

    private fun Proverca(): LiveData<List<SearchModel>> {
        val database = MainDb.getDb(MAIN)
        val listVet = MutableLiveData<List<SearchModel>>()
        val query = database.getDao().getAllVeterinar()
        query.asLiveData().observe(viewLifecycleOwner){vetlist->
            val vetirList = ArrayList<SearchModel>()
            vetlist.forEach{ veterinars ->
                val vet = SearchModel(
                    veterinars.name,
                    veterinars.phNumber,
                    veterinars.comment,
                    veterinars.specialization,
                    veterinars.price,
                    veterinars.urlProfile
                )
                vetirList.add(vet)
            }
            listVet.value = vetirList
        }
        return listVet
    }
    private fun getData(): LiveData<List<SearchModel>>{
        val database = MainDb.getDb(MAIN)
        val listVet = MutableLiveData<List<SearchModel>>()
        val query = database.getDao().getAllVeterinar()
        query.asLiveData().observe(viewLifecycleOwner){vetlist->
            val vetirList = ArrayList<SearchModel>()
            vetlist.forEach{ veterinars ->
                val vet = SearchModel(
                    veterinars.name,
                    veterinars.phNumber,
                    veterinars.comment,
                    veterinars.specialization,
                    veterinars.price,
                    veterinars.urlProfile
                )
                vetirList.add(vet)
            }
            listVet.value = vetirList
        }
        return listVet
    }

    private suspend fun setData() = withContext(Dispatchers.IO) {
        val database = MainDb.getDb(MAIN)
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
                val comment = "Отзывы: ${container.select("div[class=specialist]").select("div[class=specialist-photo-container]").select("div[class=specialist-mark]").select("div[class=specialist-mark]").select("span[class=stars-rating-text strong]").text()}"
                val specialization = container.select("div[class=specialist]").select("div[class=specialist-top-info]").select("div[class=prof-spec-list specialist-spec-list]").select("a").text()
                val price = "Цена: 100p"
                val urlProfile = container.select("div[class=specialist]").select("div[class=specialist-top-info]").select("a[class=prof-name specialist-name js-specialist-card-link js-item-url js-link]").attr("href")

                val vet = Veterinars(
                    null,
                    name,
                    phNumber,
                    comment,
                    specialization,
                    price,
                    urlProfile
                )

                database.getDao().insertVeterinar(vet)
            }
        }
    }

    private fun initRcView() = with(binding){
        rcSerch.layoutManager = LinearLayoutManager(activity)
        adapter = SearchAdapter()
        getData().observe(viewLifecycleOwner){ vlist ->
            adapter.submitList(vlist)
            rcSerch.adapter = adapter
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = SerchRecycleFragment()
    }
}