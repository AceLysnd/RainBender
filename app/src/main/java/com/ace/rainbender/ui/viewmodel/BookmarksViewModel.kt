package com.ace.rainbender.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ace.rainbender.data.local.user.AccountEntity
import com.ace.rainbender.data.model.Prefs
import com.ace.rainbender.data.model.geocoding.Result
import com.ace.rainbender.di.LocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarksViewModel @Inject constructor(private val repository: LocalRepository) : ViewModel() {

    private var _getUserResult = MutableLiveData<AccountEntity>()
    val getUser: LiveData<AccountEntity> get() = _getUserResult

    fun getUser(username: String) {
        viewModelScope.launch {
            _getUserResult.postValue(repository.getAccount(username))
        }
    }

    fun getAccount(): LiveData<Prefs> {
        return repository.getAccountPrefs()
    }

    fun updateBookmark(accountId: Long, bookmark: List<Result>) {
        viewModelScope.launch {
            repository.updateBookmark(accountId, bookmark)
        }
    }

}