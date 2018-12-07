package com.jeanpigomez.codingtest.ui.language

import android.arch.lifecycle.MutableLiveData
import android.view.View
import com.jeanpigomez.codingtest.R
import com.jeanpigomez.codingtest.base.BaseViewModel
import com.jeanpigomez.codingtest.model.Language
import com.jeanpigomez.codingtest.model.LanguageDao
import com.jeanpigomez.codingtest.network.LanguageApi
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.Arrays.sort
import javax.inject.Inject

class LanguageListViewModel(private val languageDao: LanguageDao) : BaseViewModel() {
    @Inject
    lateinit var languageApi: LanguageApi
    val languageListAdapter: LanguageListAdapter = LanguageListAdapter()

    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    val errorMessage: MutableLiveData<Int> = MutableLiveData()
    val sortOption: MutableLiveData<Int> = MutableLiveData()
    val errorClickListener = View.OnClickListener { loadLanguages() }

    private lateinit var subscription: Disposable

    init {
        loadLanguages()
    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }

    fun loadLanguages() {
        subscription = Observable.fromCallable { languageDao.all }
                .concatMap { dbLanguageList ->
                    if (dbLanguageList.isEmpty())
                        languageApi.getLanguages().concatMap { apiLanguageList ->
                            languageDao.insertAll(*apiLanguageList.toTypedArray())
                            Observable.just(apiLanguageList)
                        }
                    else
                        Observable.just(dbLanguageList)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { onRetrieveLanguageListStart() }
                .doOnTerminate { onRetrieveLanguageListFinish() }
                .subscribe(
                        { result -> onRetrieveLanguageListSuccess(result) },
                        { onRetrieveLanguageListError() }
                )
    }

    private fun onRetrieveLanguageListStart() {
        loadingVisibility.value = View.VISIBLE
        errorMessage.value = null
    }

    private fun onRetrieveLanguageListFinish() {
        loadingVisibility.value = View.GONE
    }

    private fun onRetrieveLanguageListSuccess(languageList: List<Language>) {
        var sortedList = languageList.sortedWith(compareBy { it.name })

        if (sortOption.value != null) {
            when (sortOption.value) {
                0 -> sortedList = languageList.sortedWith(compareBy { it.name })
                1 -> sortedList = languageList.sortedWith(compareBy { it.score })
                2 -> sortedList = languageList.sortedWith(compareBy { it.endDate })
            }
        }
        languageListAdapter.updateLanguageList(sortedList)
    }

    private fun onRetrieveLanguageListError() {
        errorMessage.value = R.string.language_error
    }

    fun onSortAction(option: Int) {
        sortOption.value = option
        sortItems()
    }

    private fun sortItems() {
        loadLanguages()
    }
}
