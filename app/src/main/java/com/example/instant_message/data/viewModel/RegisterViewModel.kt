package com.example.instant_message.data.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instant_message.domain.entity.RegisterResult
import com.example.instant_message.data.reponsitory.UserRepository
import kotlinx.coroutines.launch

class RegisterViewModel(private val userRepository: UserRepository): ViewModel() {
    private val _registerResult = MutableLiveData<RegisterResult>()
    val registerResult: LiveData<RegisterResult> get() = _registerResult

    fun register(username: String, password: String){
        viewModelScope.launch {
            try {
                val result = userRepository.register(username, password)
                if (result.isSuccess){
                    result.getOrNull()?.let { registerResponse ->

                        _registerResult.value = RegisterResult(true,registerResponse.user,"Register Succeed")
                    }

                } else {
                    _registerResult.value = RegisterResult(false,null,result.exceptionOrNull()?.message)
                }
            }catch (e: Exception){
                Log.d("RegisterViewModel", "Register Failed: ${e.message}")
                _registerResult.value = RegisterResult(false,null,e.message)
            }
        }
    }
}