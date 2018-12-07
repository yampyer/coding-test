package com.jeanpigomez.codingtest.ui.language

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.jeanpigomez.codingtest.R
import com.jeanpigomez.codingtest.databinding.ItemLanguageBinding
import com.jeanpigomez.codingtest.model.Language

class LanguageListAdapter : RecyclerView.Adapter<LanguageListAdapter.ViewHolder>() {
    private lateinit var languageList: List<Language>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageListAdapter.ViewHolder {
        val binding: ItemLanguageBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_language, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LanguageListAdapter.ViewHolder, position: Int) {
        holder.bind(languageList[position])
    }

    override fun getItemCount(): Int {
        return if (::languageList.isInitialized) languageList.size else 0
    }

    fun updateLanguageList(languageList: List<Language>) {
        this.languageList = languageList
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: ItemLanguageBinding) : RecyclerView.ViewHolder(binding.root) {
        private val viewModel = LanguageViewModel()

        fun bind(language: Language) {
            viewModel.bind(language)
            binding.viewModel = viewModel
        }

    }
}
