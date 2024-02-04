package com.github.llmaximll.test_em.features.profile

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.lifecycleScope
import com.github.llmaximll.test_em.core.common.components.LoadingState
import com.github.llmaximll.test_em.core.common.components.NotFoundState
import com.github.llmaximll.test_em.core.common.ext.asFormattedPhoneNumber
import com.github.llmaximll.test_em.core.common.log
import com.github.llmaximll.test_em.core.common.models.User
import com.github.llmaximll.test_em.core.common.theme.AppColors
import com.github.llmaximll.test_em.core.common.theme.CustomTypography
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.github.llmaximll.test_em.core.common.R as ResCommon

const val routeProfileScreen = "profile"

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onFavoriteNavigate: () -> Unit,
    onExit: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val currentUserState by viewModel.currentUser.collectAsState()
    val favoriteItemsCount by viewModel.favoriteItemsCount.collectAsState()

    LaunchedEffect(currentUserState) {
        log("CurrentUserState: $currentUserState")
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    viewModel.getFavoriteItemsCount()
                }

                else -> Unit
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val coroutineScope = rememberCoroutineScope()
    ProfileState(
        modifier = modifier,
        currentUserState = currentUserState,
        favoriteItemsCount = favoriteItemsCount,
        onFavoriteNavigate = onFavoriteNavigate,
        onExit = {
            coroutineScope.launch {
                viewModel.clearDatabase()
                delay(500L)
                onExit()
            }
        }
    )
}

@Composable
private fun ProfileState(
    currentUserState: CurrentUserState,
    favoriteItemsCount: Int,
    onFavoriteNavigate: () -> Unit,
    onExit: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedContent(
        modifier = modifier,
        targetState = currentUserState,
        label = "CurrentUserState"
    ) { state ->
        when (state) {
            CurrentUserState.Init -> LoadingState()
            CurrentUserState.Loading -> LoadingState()
            CurrentUserState.NotFound -> NotFoundState(
                textRes = R.string.features_profile_not_found
            )

            is CurrentUserState.Success -> ProfileContent(
                user = state.user,
                favoriteItemsCount = favoriteItemsCount,
                onFavoriteNavigate = onFavoriteNavigate,
                onExit = onExit
            )
        }
    }
}

@Composable
private fun ProfileContent(
    user: User,
    favoriteItemsCount: Int,
    onFavoriteNavigate: () -> Unit,
    onExit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Fio(
            user = user
        )

        Spacer(modifier = Modifier.height(24.dp))

        ButtonsList(
            favoriteItemsCount = favoriteItemsCount,
            onFavoriteNavigate = onFavoriteNavigate,
            onExit = onExit
        )
    }
}

@Composable
private fun Fio(
    user: User,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = AppColors.BackgroundLightGrey,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = ResCommon.drawable.ic_account),
                contentDescription = null,
                tint = AppColors.ElementDarkGrey
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "${user.name} ${user.lastName}",
                    color = AppColors.TextBlack,
                    style = CustomTypography.title2
                )

                Text(
                    text = user.phoneNumber.asFormattedPhoneNumber(),
                    color = AppColors.TextGrey,
                    style = CustomTypography.caption1
                )
            }
        }

        IconButton(
            onClick = { /*TODO*/ }
        ) {
            Icon(
                modifier = Modifier.size(32.dp),
                painter = painterResource(id = ResCommon.drawable.ic_logout),
                contentDescription = null,
                tint = AppColors.ElementDarkGrey
            )
        }
    }
}

@Composable
private fun ButtonsList(
    favoriteItemsCount: Int,
    onFavoriteNavigate: () -> Unit,
    onExit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ButtonsItem(
            iconRes = ResCommon.drawable.ic_heart,
            iconTint = AppColors.ElementPink,
            title = stringResource(id = R.string.features_profile_buttons_item_favorite),
            subtitle = if (favoriteItemsCount > 0) stringResource(
                id = R.string.features_profile_favorite_plural,
                favoriteItemsCount,
                LocalContext.current.resources.getQuantityString(
                    R.plurals.favorite,
                    favoriteItemsCount
                )
            ) else "",
            onClick = onFavoriteNavigate
        )

        ButtonsItem(
            iconRes = ResCommon.drawable.ic_shop,
            iconTint = AppColors.ElementPink,
            title = stringResource(id = R.string.features_profile_buttons_item_shop)
        )

        ButtonsItem(
            iconRes = ResCommon.drawable.ic_feedback,
            iconTint = AppColors.ElementOrange,
            title = stringResource(id = R.string.features_profile_buttons_item_feedback)
        )

        ButtonsItem(
            iconRes = ResCommon.drawable.ic_offer,
            iconTint = AppColors.TextGrey,
            title = stringResource(id = R.string.features_profile_buttons_item_offer)
        )

        ButtonsItem(
            iconRes = ResCommon.drawable.refund,
            iconTint = AppColors.TextGrey,
            title = stringResource(id = R.string.features_profile_buttons_item_return_product)
        )

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onExit,
            colors = ButtonDefaults.buttonColors(
                containerColor = AppColors.BackgroundLightGrey
            ),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.features_profile_button_exit),
                color = AppColors.TextBlack,
                style = CustomTypography.button2
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ButtonsItem(
    @DrawableRes iconRes: Int,
    title: String,
    modifier: Modifier = Modifier,
    iconTint: Color = Color.Unspecified,
    subtitle: String = "",
    onClick: () -> Unit = { }
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = AppColors.BackgroundLightGrey
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    tint = iconTint
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = title,
                        color = AppColors.TextBlack,
                        style = CustomTypography.title2
                    )

                    AnimatedVisibility(
                        visible = subtitle.isNotEmpty()
                    ) {
                        Text(
                            text = subtitle,
                            color = AppColors.TextGrey,
                            style = CustomTypography.caption1
                        )
                    }
                }
            }

            Icon(
                modifier = Modifier.size(32.dp),
                painter = painterResource(id = ResCommon.drawable.ic_right_arrow),
                contentDescription = null,
                tint = AppColors.ElementDarkGrey
            )
        }
    }
}