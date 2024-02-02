package com.github.llmaximll.sign_up

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.llmaximll.test_em.core.common.log
import com.github.llmaximll.test_em.core.common.theme.AppColors
import com.github.llmaximll.test_em.core.common.theme.CustomTypography
import com.github.llmaximll.test_em.core.common.R as ResCommon

const val routeSignUpScreen = "sign_up"

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    onSignUp: () -> Unit,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val signUpState by viewModel.signUpState.collectAsState()

    val name by viewModel.name.collectAsState()
    val lastName by viewModel.lastName.collectAsState()
    val phoneNumber by viewModel.phoneNumber.collectAsState()

    LaunchedEffect(signUpState) {
        log("SignUpState: $signUpState")

        if (signUpState is SignUpState.Success)
            onSignUp()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier)

        Fields(
            name = name,
            lastName = lastName,
            phoneNumber = phoneNumber,
            signUpButtonEnabled = signUpState !in listOf(SignUpState.Loading, SignUpState.Success),
            onNameChange = { viewModel.changeName(it) },
            onLastNameChange = { viewModel.changeLastName(it) },
            onPhoneNumberChange = { viewModel.changePhoneNumber(it) },
            onSignUp = viewModel::signUp
        )

        LoyaltyProgramText()
    }
}

@Composable
private fun Fields(
    modifier: Modifier = Modifier,
    name: InputState,
    lastName: InputState,
    phoneNumber: InputState,
    signUpButtonEnabled: Boolean,
    onNameChange: (InputState) -> Unit,
    onLastNameChange: (InputState) -> Unit,
    onPhoneNumberChange: (InputState) -> Unit,
    onSignUp: () -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Field(
            placeholderRes = R.string.name,
            value = name.value,
            onValueChange = { onNameChange(name.copy(value = it)) },
            errorMessageRes = name.errorMessageRes
        )

        Field(
            placeholderRes = R.string.lastname,
            value = lastName.value,
            onValueChange = { onLastNameChange(lastName.copy(value = it)) },
            errorMessageRes = lastName.errorMessageRes
        )

        EmailField(
            placeholderRes = R.string.phone_number,
            value = phoneNumber.value,
            onValueChange = { onPhoneNumberChange(phoneNumber.copy(value = it)) },
            errorMessageRes = phoneNumber.errorMessageRes
        )

        SignUpButton(
            modifier = Modifier.padding(top = 16.dp),
            isEnabled = signUpButtonEnabled && name.isSuccess && lastName.isSuccess && phoneNumber.isSuccess,
            onSignUp = onSignUp
        )
    }
}

@Composable
private fun Field(
    modifier: Modifier = Modifier,
    @StringRes placeholderRes: Int,
    value: String,
    onValueChange: (String) -> Unit,
    @StringRes errorMessageRes: Int?
) {
    TextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
            focusedContainerColor = AppColors.BackgroundLightGrey,
            unfocusedContainerColor = AppColors.BackgroundLightGrey,
            disabledContainerColor = AppColors.BackgroundLightGrey,
            errorContainerColor = AppColors.ContainerError,
            errorPlaceholderColor = AppColors.OnContainerError,
            unfocusedPlaceholderColor = AppColors.TextGrey,
            disabledPlaceholderColor = AppColors.TextGrey,
            focusedPlaceholderColor = AppColors.TextGrey,
            focusedTextColor = AppColors.TextBlack,
            disabledTextColor = AppColors.TextBlack,
            unfocusedTextColor = AppColors.TextBlack,
            errorSupportingTextColor = AppColors.OnContainerError
        ),
        shape = RoundedCornerShape(8.dp),
        placeholder = {
            Text(text = stringResource(id = placeholderRes))
        },
        textStyle = CustomTypography.placeholder,
        trailingIcon = {
            AnimatedVisibility(
                visible = value.isNotEmpty(),
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                IconButton(
                    onClick = {
                        onValueChange("")
                    }
                ) {
                    Icon(
                        painter = painterResource(id = ResCommon.drawable.ic_big_close),
                        contentDescription = null,
                        tint = AppColors.ElementDarkGrey
                    )
                }
            }
        },
        isError = errorMessageRes != null,
        supportingText = {
            if (errorMessageRes != null) {
                Text(text = stringResource(id = errorMessageRes))
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
    )
}

@Composable
private fun EmailField(
    modifier: Modifier = Modifier,
    @StringRes placeholderRes: Int,
    value: String,
    onValueChange: (String) -> Unit,
    @StringRes errorMessageRes: Int?,
) {
    TextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        onValueChange = {
            if (it.length <= 10 && !(it.length == 1 && it[0] == '7')) {
                onValueChange(it)
            }
        },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
            focusedContainerColor = AppColors.BackgroundLightGrey,
            unfocusedContainerColor = AppColors.BackgroundLightGrey,
            disabledContainerColor = AppColors.BackgroundLightGrey,
            errorContainerColor = AppColors.ContainerError,
            errorPlaceholderColor = AppColors.OnContainerError,
            unfocusedPlaceholderColor = AppColors.TextGrey,
            disabledPlaceholderColor = AppColors.TextGrey,
            focusedPlaceholderColor = AppColors.TextGrey,
            focusedTextColor = AppColors.TextBlack,
            disabledTextColor = AppColors.TextBlack,
            unfocusedTextColor = AppColors.TextBlack,
            errorSupportingTextColor = AppColors.OnContainerError
        ),
        shape = RoundedCornerShape(8.dp),
        placeholder = {
            Text(text = stringResource(id = placeholderRes))
        },
        textStyle = CustomTypography.placeholder,
        trailingIcon = {
            AnimatedVisibility(
                visible = value.isNotEmpty(),
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                IconButton(
                    onClick = {
                        onValueChange("")
                    }
                ) {
                    Icon(
                        painter = painterResource(id = ResCommon.drawable.ic_big_close),
                        contentDescription = null,
                        tint = AppColors.ElementDarkGrey
                    )
                }
            }
        },
        isError = errorMessageRes != null,
        supportingText = {
            if (errorMessageRes != null) {
                Text(text = stringResource(id = errorMessageRes))
            }
        },
        visualTransformation = PhoneVisualTransformation(
            mask = "+7 000 000-00-00",
            maskNumber = '0'
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

@Composable
private fun SignUpButton(
    modifier: Modifier = Modifier,
    isEnabled: Boolean,
    onSignUp: () -> Unit,
) {
    Button(
        modifier = modifier.fillMaxWidth(),
        onClick = { onSignUp() },
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        enabled = isEnabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = AppColors.BackgroundPink,
            contentColor = AppColors.TextWhite,
            disabledContainerColor = AppColors.TextLightPink
        )
    ) {
        Text(
            text = stringResource(id = R.string.sign_up_button),
            style = CustomTypography.button2,
            color = AppColors.TextWhite
        )
    }
}

@Composable
private fun LoyaltyProgramText(
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier
            .padding(vertical = 12.dp, horizontal = 48.dp),
        text = buildAnnotatedString {
            pushStyle(SpanStyle(color = AppColors.TextGrey))

            append(stringResource(id = R.string.loyalty_program_1))

            append(" ")

            withStyle(
                style = SpanStyle(
                    textDecoration = TextDecoration.Underline,
                ),
            ) {
                append(stringResource(id = R.string.loyalty_program_2))
            }
        },
        textAlign = TextAlign.Center,
        style = CustomTypography.link
    )
}