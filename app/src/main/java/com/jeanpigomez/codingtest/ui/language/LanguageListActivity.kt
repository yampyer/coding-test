package com.jeanpigomez.codingtest.ui.language

import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.jeanpigomez.codingtest.R
import com.jeanpigomez.codingtest.databinding.ActivityLanguageListBinding
import com.jeanpigomez.codingtest.di.ViewModelFactory
import kotlinx.android.synthetic.main.activity_language_list.*

class LanguageListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLanguageListBinding
    private lateinit var viewModel: LanguageListViewModel
    private var errorSnackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_language_list)
        binding.languageList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        setSupportActionBar(mainToolbar)

        viewModel = ViewModelProviders.of(this, ViewModelFactory(this)).get(LanguageListViewModel::class.java)
        viewModel.errorMessage.observe(this, Observer { errorMessage ->
            if (errorMessage != null) showError(errorMessage) else hideError()
        })
        binding.viewModel = viewModel
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_language_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_sort -> {
            val dialog = AlertDialog.Builder(this).setTitle(R.string.pick_sort_option).setItems(R.array.sort_options
            ) { _, which ->
                viewModel.onSortAction(which)
            }
            dialog.show()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun showError(@StringRes errorMessage: Int) {
        errorSnackbar = Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_INDEFINITE)
        errorSnackbar?.setAction(R.string.retry, viewModel.errorClickListener)
        errorSnackbar?.show()
    }

    private fun hideError() {
        errorSnackbar?.dismiss()
    }
}
