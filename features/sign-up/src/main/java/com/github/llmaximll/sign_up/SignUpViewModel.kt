package com.github.llmaximll.sign_up

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.github.llmaximll.test_em.core.common.launchWithHandler
import com.github.llmaximll.test_em.core.common.models.User
import com.github.llmaximll.test_em.core.common.repositories_abstract.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _signUpState = MutableStateFlow<SignUpState>(SignUpState.Init)
    val signUpState: StateFlow<SignUpState> =
        _signUpState.asStateFlow()

    private val _name = MutableStateFlow(InputState("", false))
    val name: StateFlow<InputState> = _name.asStateFlow()

    fun changeName(newValue: InputState) {
        val regex = Regex("[А-Яа-я]+")

        var errorMessage: Int? = null
        when {
            newValue.value.isEmpty() -> errorMessage = R.string.input_state_empty
            !regex.matches(newValue.value) -> errorMessage = R.string.input_state_name_regex
        }

        _name.value = newValue.copy(
            isSuccess = errorMessage == null,
            errorMessageRes = errorMessage
        )
    }

    private val _lastName = MutableStateFlow(InputState("", false))
    val lastName: StateFlow<InputState> = _lastName.asStateFlow()

    fun changeLastName(newValue: InputState) {
        val regex = Regex("[А-Яа-я]+")

        var errorMessage: Int? = null
        when {
            newValue.value.isEmpty() -> errorMessage = R.string.input_state_empty
            !regex.matches(newValue.value) -> errorMessage = R.string.input_state_name_regex
        }

        _lastName.value = newValue.copy(
            isSuccess = errorMessage == null,
            errorMessageRes = errorMessage
        )
    }

    private val _phoneNumber = MutableStateFlow(InputState("", false))
    val phoneNumber: StateFlow<InputState> = _phoneNumber.asStateFlow()

    fun changePhoneNumber(newValue: InputState) {
        var errorMessage: Int? = null
        when {
            newValue.value.isEmpty() -> errorMessage = R.string.input_state_empty
            newValue.value.length != 10 || !newValue.value.all { it.isDigit() } -> errorMessage =
                R.string.input_state_phone_number_incorrect
        }

        _phoneNumber.value = newValue.copy(
            isSuccess = errorMessage == null,
            errorMessageRes = errorMessage
        )
    }

    fun signUp() = launchWithHandler(
        onException = {
            _signUpState.value = SignUpState.Error(it)
        }
    ) {
        if (signUpState.value is SignUpState.Loading || !name.value.isSuccess ||
            !lastName.value.isSuccess || !phoneNumber.value.isSuccess
        ) {
            return@launchWithHandler
        }

        _signUpState.value = SignUpState.Loading

        val result = userRepository.insertUser(
            User(
                name = name.value.value,
                lastName = lastName.value.value,
                phoneNumber = phoneNumber.value.value,
            )
        )

        if (result != null) {
            _signUpState.value = SignUpState.Success
        } else {
            _signUpState.value = SignUpState.Error(NoSuchElementException())
        }
    }
}

data class InputState(
    val value: String,
    val isSuccess: Boolean,
    @StringRes val errorMessageRes: Int? = null
)

sealed class SignUpState {

    data object Init : SignUpState()

    data object Loading : SignUpState()

    data class Error(val throwable: Throwable?) : SignUpState()

    data object Success : SignUpState()
}