package com.crypto.ticker.ui.projectProfile

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.crypto.ticker.R
import com.crypto.ticker.core.common.BaseActivity
import com.crypto.ticker.data.local.database.CoinsListEntity
import com.crypto.ticker.databinding.ActivityProjectProfileBinding
import com.crypto.ticker.util.ChartHelper
import com.crypto.ticker.util.Constants
import com.crypto.ticker.util.ImageLoader
import com.crypto.ticker.util.UIHelper
import com.crypto.ticker.util.extensions.doOnChange
import com.crypto.ticker.util.extensions.dollarString
import com.crypto.ticker.util.extensions.emptyIfNull
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProjectProfileActivity : BaseActivity() {

    private val viewModel: ProjectProfileViewModel by viewModels()
    private lateinit var binding: ActivityProjectProfileBinding

    private var symbol: String? = null
    private var symbolId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_project_profile)
        binding.apply {
            lifecycleOwner = this@ProjectProfileActivity
            viewModel = this@ProjectProfileActivity.viewModel
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        if (intent?.hasExtra(Constants.EXTRA_SYMBOL) == true) {
            symbol = intent?.getStringExtra(Constants.EXTRA_SYMBOL)
        }

        if (intent?.hasExtra(Constants.EXTRA_SYMBOL_ID) == true) {
            symbolId = intent?.getStringExtra(Constants.EXTRA_SYMBOL_ID)
        }

        supportActionBar?.title = symbol ?: ""
        observeViewModel()

        viewModel.historicalData(symbolId)
    }

    private fun observeViewModel() {
        symbol?.let {
            viewModel.projectBySymbol(it).doOnChange(this) { project ->
                populateViews(project)
            }

            viewModel.historicalData.doOnChange(this) { historicalPriceList ->
                binding.lineChartTitle.text = getString(R.string.line_chart_title).format(30)
                ChartHelper.displayHistoricalLineChart(binding.lineChart, it, historicalPriceList)
            }

            viewModel.dataError.doOnChange(this) { error ->
                if (error) showToast(getString(R.string.historical_data_error))
            }
        }
    }

    private fun populateViews(project: CoinsListEntity) {
        binding.coinItemSymbolTextView.text = project.symbol
        binding.coinItemNameTextView.text = project.name.emptyIfNull()
        binding.coinItemPriceTextView.text = project.price.dollarString()
        UIHelper.showChangePercent(binding.coinItemChangeTextView, project.changePercent)
        ImageLoader.loadImage(binding.coinItemImageView, project.image ?: "")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }
}