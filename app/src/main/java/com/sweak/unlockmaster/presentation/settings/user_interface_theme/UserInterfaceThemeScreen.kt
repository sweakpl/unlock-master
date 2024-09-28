package com.sweak.unlockmaster.presentation.settings.user_interface_theme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.sweak.unlockmaster.R
import com.sweak.unlockmaster.domain.model.UiThemeMode
import com.sweak.unlockmaster.presentation.common.components.NavigationBar
import com.sweak.unlockmaster.presentation.common.theme.space
import com.sweak.unlockmaster.presentation.common.util.popBackStackThrottled

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInterfaceThemeScreen(
    userInterfaceThemeViewModel: UserInterfaceThemeViewModel = hiltViewModel(),
    navController: NavController
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val userInterfaceThemeScreenState = userInterfaceThemeViewModel.state
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(connection = scrollBehavior.nestedScrollConnection),
        topBar = {
            NavigationBar(
                title = stringResource(R.string.user_interface_theme),
                onNavigationButtonClick = { navController.popBackStackThrottled(lifecycleOwner) },
                scrollBehavior = scrollBehavior
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Column(modifier = Modifier.padding(paddingValues = paddingValues)) {
                Text(
                    text = stringResource(R.string.user_interface_theme),
                    style = MaterialTheme.typography.displayLarge,
                    modifier = Modifier
                        .padding(
                            start = MaterialTheme.space.medium,
                            top = MaterialTheme.space.medium,
                            end = MaterialTheme.space.medium,
                            bottom = MaterialTheme.space.small
                        )
                )

                Text(
                    text = stringResource(R.string.user_interface_theme_description),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(
                            start = MaterialTheme.space.medium,
                            end = MaterialTheme.space.medium,
                            bottom = MaterialTheme.space.smallMedium
                        )
                )

                Column(
                    modifier = Modifier
                        .padding(
                            start = MaterialTheme.space.medium,
                            end = MaterialTheme.space.medium,
                            bottom = MaterialTheme.space.medium
                        )
                        .selectableGroup()
                ) {
                    userInterfaceThemeScreenState.availableUiThemeModes.forEach {
                        val isSelected = (it == userInterfaceThemeScreenState.selectedUiThemeMode)

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(height = MaterialTheme.space.xLarge)
                                .selectable(
                                    selected = isSelected,
                                    onClick = {
                                        userInterfaceThemeViewModel.onEvent(
                                            UserInterfaceThemeScreenEvent.SelectUiThemeMode(it)
                                        )
                                    },
                                    role = Role.RadioButton
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = null,
                                modifier = Modifier
                                    .padding(
                                        start = MaterialTheme.space.small,
                                        end = MaterialTheme.space.medium
                                    )
                            )

                            val text = stringResource(
                                when (it) {
                                    UiThemeMode.LIGHT -> R.string.light_theme
                                    UiThemeMode.DARK -> R.string.dark_theme
                                    UiThemeMode.SYSTEM -> R.string.system_theme
                                }
                            )

                            Text(
                                text = text,
                                style = MaterialTheme.typography.displayMedium
                            )
                        }
                    }
                }
            }
        }
    }
}