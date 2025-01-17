package com.example.instant_message.data.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instant_message.domain.entity.LoginResult
import com.example.instant_message.data.reponsitory.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: UserRepository
) : ViewModel() {

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> get() = _loginResult

    private val _username = MutableLiveData<String?>()
    val username : LiveData<String?>get() = _username

    fun setUsername(username: String?) {
        _username.value = username
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            try{
                val result = repository.login(username, password)
                if (result.isSuccess) {
                    result.getOrNull()?.let { loginResponse ->
                        _loginResult.value = LoginResult(true, loginResponse.user)
                    }
                } else {
                    _loginResult.value = LoginResult(false, null,result.exceptionOrNull()?.message)
                }
            }catch (e: Exception){
                Log.e("UserViewModel", "Login failed with exception: ${e.message}", e)
                _loginResult.value = LoginResult(false, null,e.message)

            }
        }
    }
}





