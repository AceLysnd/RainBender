package com.ace.rainbender.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ace.rainbender.di.LocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(private val repository: LocalRepository) : ViewModel() {
    fun getLoginStatus(): LiveData<Boolean> {
        return repository.getLoginStatus()
    }
    fun setHideBotnav(hideBotnav: Boolean){
        viewModelScope.launch {
            repository.setHideBotnav(hideBotnav)
        }
    }
}