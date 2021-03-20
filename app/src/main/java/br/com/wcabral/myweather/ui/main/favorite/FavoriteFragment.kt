package br.com.wcabral.myweather.ui.main.favorite

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.wcabral.myweather.data.local.DatabaseApp
import br.com.wcabral.myweather.data.local.model.Favorite
import br.com.wcabral.myweather.databinding.FragmentFavoriteBinding
import br.com.wcabral.myweather.extension.toPx
import br.com.wcabral.myweather.util.MarginItemDecoration

class FavoriteFragment : Fragment(), FavoriteAdapter.OnClick {

    private lateinit var binding: FragmentFavoriteBinding
    private val favoriteAdapter by lazy { FavoriteAdapter() }
    private val dao by lazy { DatabaseApp.getInstance(requireContext()).getFavoriteDao() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        createAndSubmitList()
    }

    override fun onClick(favorite: Favorite?) {
        favorite?.let { fav -> dao.delete(fav) }.also {
            refreshList()
        }
    }

    private fun createAndSubmitList() {
        refreshList()
        favoriteAdapter.setOnClickListener(this)
    }

    private fun refreshList() {
        favoriteAdapter.submitList(dao.getAll().toMutableList())
    }

    private fun initUi() {
        applyAdapter()
        btnFavList()
        swipe()
    }

    private fun applyAdapter() {
        binding.rvFavorites.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = favoriteAdapter
            addItemDecoration(MarginItemDecoration(16.toPx()))
        }
    }

    private fun btnFavList() {
        binding.btnFavFilter.setOnClickListener {
            if (binding.edtFav.text.isEmpty()) {
                Toast.makeText(requireActivity(), "Por favor digite um valor.", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            search()
        }
    }

    private fun search() {
        val listFilter = dao.getByName(binding.edtFav.text.toString())
        if (listFilter.isEmpty()) {
            Toast.makeText(activity, "City not founded", Toast.LENGTH_SHORT).show()
            return
        }
        submitList(listFilter)
    }

    private fun submitList(listFilter: List<Favorite>) {
        favoriteAdapter.submitList(listFilter.toMutableList())
        Toast.makeText(activity, "Swipe to reload", Toast.LENGTH_SHORT).show()
    }

    private fun swipe() {
        binding.swipe.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed({
                refreshList()
                binding.swipe.isRefreshing = false
            }, 4000)
        }
    }

}