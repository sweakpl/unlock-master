package com.sweak.unlockmaster.presentation.common.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIos
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.sweak.unlockmaster.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationBar(
    title: String,
    onNavigationButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            navigationIconContentColor = MaterialTheme.colorScheme.onBackground
        ),
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.displayMedium
            )
        },
        navigationIcon = {
            IconButton(onClick = onNavigationButtonClick) {
                Icon(
                    imageVector = Icons.Outlined.ArrowBackIos,
                    contentDescription = stringResource(R.string.content_description_back_icon)
                )
            }
        },
        scrollBehavior = scrollBehavior,
        modifier = modifier
    )
}