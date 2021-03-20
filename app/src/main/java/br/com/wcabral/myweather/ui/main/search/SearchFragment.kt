package br.com.wcabral.myweather.ui.main.search

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKeys
import br.com.wcabral.myweather.data.local.DatabaseApp
import br.com.wcabral.myweather.data.local.model.Favorite
import br.com.wcabral.myweather.data.remote.RetrofitManager
import br.com.wcabral.myweather.data.remote.model.City
import br.com.wcabral.myweather.data.remote.model.FindResult
import br.com.wcabral.myweather.databinding.FragmentSearchBinding
import br.com.wcabral.myweather.extension.isInternetAvailable
import br.com.wcabral.myweather.extension.toPx
import br.com.wcabral.myweather.ui.main.ForecastActivity
import br.com.wcabral.myweather.util.MarginItemDecoration
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class SearchFragment : Fragment(), SearchAdapter.OnClick {

    private lateinit var binding: FragmentSearchBinding
    private val searchAdapter by lazy { SearchAdapter() }
    private val dao by lazy { DatabaseApp.getInstance(requireContext()).getFavoriteDao() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    override fun onClick(city: City?) {
        val intent = Intent(this.activity, ForecastActivity::class.java)
        intent.putExtra(CITY, city)
        startActivityForResult(intent, LAUNCH_ACTIVITY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when {
            requestCode == LAUNCH_ACTIVITY && resultCode == Activity.RESULT_OK -> {
                getResult(result = data?.getSerializableExtra(CITY) as City)
            }
            resultCode == Activity.RESULT_CANCELED -> {
                Log.i(TAG, "cancel")
            }
        }
    }

    private fun getResult(result: City?) {
        saveResult(result)
    }

    private fun saveResult(
        result: City?
    ) {
        result?.let { city ->
            val favorite = Favorite(city.id, city.name)
            dao.insert(favorite)
        }
    }

    private fun findCity() {
        if (requireContext().isInternetAvailable()) {
            searchAdapter.setOnClickListener(this)
            searchAdapter.setContext(requireContext())

            val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
            val masterKeyAlias =
                MasterKeys.getOrCreate(keyGenParameterSpec)
            val file = File(context?.filesDir, "myfile.txt")
            if (file.exists()) {
                file.delete()
            }

            val encryptedFile = EncryptedFile.Builder(
                file,
                requireActivity(),
                masterKeyAlias,
                EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
            ).build()
            encryptedFile.openFileOutput().use { writer ->
                writer.write("5fde54966e3e1c8a80e436245bdf9672".toByteArray())
            }

            var result = ""
            encryptedFile.openFileInput().use { inputStream ->
                result = inputStream.readBytes().decodeToString()
            }

            val call = RetrofitManager.getOpenWeatherService().findCity(
                binding.edtSearch.text.toString(),
                "metric",
                "pt",
                result
            )

            call.enqueue(object : Callback<FindResult> {
                override fun onResponse(call: Call<FindResult>, response: Response<FindResult>) {
                    if (response.isSuccessful) {
                        searchAdapter.submitList(response.body()?.cities)
                    } else {
                        Log.w(TAG, "onResponse: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<FindResult>, t: Throwable) {
                    Log.e(TAG, "onFailure", t)
                }
            })
        } else {
            Toast.makeText(requireContext(), "No network access", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initUi() {
        binding.rvCities.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = searchAdapter
            addItemDecoration(MarginItemDecoration(16.toPx()))
        }

        binding.btnSearch.setOnClickListener {
            findCity()
        }
    }

    companion object {
        private const val TAG = "SearchFragment"
        const val CITY = "CITY_CONST"
        const val LAUNCH_ACTIVITY = 1
    }
}