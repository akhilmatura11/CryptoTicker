package com.crypto.ticker.ui.home.coinsList

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
import com.crypto.ticker.databinding.FragmentListBinding
import com.crypto.ticker.core.common.MainNavigationFragment
import com.crypto.ticker.ui.projectProfile.ProjectProfileActivity
import com.crypto.ticker.util.Constants
import com.crypto.ticker.util.extensions.doOnChange
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CoinListFragment : MainNavigationFragment(), OnItemClickCallback {

    private val viewModel: CoinListViewModel by viewModels()
    private lateinit var binding: FragmentListBinding
    private var coinsListAdapter = CoinsListAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                viewModel = this@CoinListFragment.viewModel
            }
        observeViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
        viewModel.loadCoinsFromApi()
    }

    override fun initializeViews() {
        binding.coinsListRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = coinsListAdapter
        }
    }

    override fun observeViewModel() {
        viewModel.isLoading.doOnChange(this) {
            binding.coinsListLoading.visibility =
                if (viewModel.isListEmpty() && it) View.VISIBLE else View.GONE

            if (it) {
                binding.coinsListErrorView.visibility = View.GONE
            }
        }

        viewModel.coinsListData.doOnChange(this) {
            coinsListAdapter.updateList(it)

            binding.coinsListErrorView.visibility =
                if (viewModel.isListEmpty()) View.VISIBLE else View.GONE
        }

        viewModel.favouriteStock.doOnChange(this) {
            it?.let {
                showToast(
                    getString(if (it.isFavourite) R.string.added_to_favourite else R.string.removed_to_favourite).format(
                        it.symbol
                    )
                )
            }
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