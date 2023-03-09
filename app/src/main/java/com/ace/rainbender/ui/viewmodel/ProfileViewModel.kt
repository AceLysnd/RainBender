package com.ace.rainbender.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ace.rainbender.data.local.user.AccountEntity
import com.ace.rainbender.di.LocalRepository
import com.ace.whatmovie.wrapper.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class ProfileViewModel @Inject constructor(private val repository: LocalRepository) : ViewModel() {

    val detailDataResult = MutableLiveData<AccountEntity>()
    val updateResult = MutableLiveData<Resource<Number>>()

    fun getAccountById(id: Long) {
        viewModelScope.launch {
            detailDataResult.postValue(repository.getAccountById(id))
        }
    }

    fun updateUser(account: AccountEntity) {
        viewModelScope.launch {
            repository.updateAccount(account)
        }
    }


    fun getAccountId(): LiveData<Long>{
        return repository.getAccountId()
    }

    fun setLoginStatus(loginStatus: Boolean){
        viewModelScope.launch {
            repository.setLoginStatus(loginStatus)
        }
    }
}