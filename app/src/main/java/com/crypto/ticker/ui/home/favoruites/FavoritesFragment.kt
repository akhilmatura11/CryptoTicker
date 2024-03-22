package com.crypto.ticker.ui.home.favoruites

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.crypto.ticker.R
import com.crypto.ticker.adapters.CoinsListAdapter
import com.crypto.ticker.adapters.OnItemClickCallback
import com.crypto.ticker.databinding.FragmentFavoritesBinding
import com.crypto.ticker.core.common.MainNavigationFragment
import com.crypto.ticker.ui.projectProfile.ProjectProfileActivity
import com.crypto.ticker.util.Constants
import com.crypto.ticker.util.extensions.doOnChange
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : MainNavigationFragment(), OnItemClickCallback {

    private val viewModel: FavoritesViewModel by viewModels()
    private lateinit var binding: FragmentFavoritesBinding
    private var favouritesAdapter = CoinsListAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                viewModel = this@FavoritesFragment.viewModel
            }
        observeViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
    }


    override fun initializeViews() {
        binding.favouritesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = favouritesAdapter
        }
    }

    override fun observeViewModel() {
        viewModel.favoriteCoinsList.doOnChange(this) {
            favouritesAdapter.updateList(it)

            if (it.isEmpty()) {
                viewModel.isFavouritesEmpty(true)
            }
        }

        viewModel.toastError.doOnChange(this) {
            showToast(it)
        }

        viewModel.favouriteStock.doOnChange(this) {
            showToast(
                getString(if (it.isFavourite) R.string.added_to_favourite else R.string.removed_to_favourite).format(
                    it.symbol
                )
            )
        }
    }

    override fun onItemClick(symbol: String, id: String) {
        requireActivity().run {
            startActivity(
                Intent(this, ProjectProfileActivity::class.java)
                    .apply {
                        putExtra(Constants.EXTRA_SYMBOL, symbol)
                        putExtra(Constants.EXTRA_SYMBOL_ID, id)
                    }
            )
        }

    }

    override fun onFavouriteClicked(symbol: String) {
        viewModel.updateFavouriteStatus(symbol)
    }
}