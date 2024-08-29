package com.jvrcoding.weatherapp.presentation.util

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToDeleteContainer(
    modifier: Modifier = Modifier,
    onDelete: (Int) -> Unit,
    onEdit: () -> Unit,
    animationDuration: Int = 500,
    content: @Composable () -> Unit,
) {
    val swipeState = rememberSwipeToDismissBoxState()

    var isRemoved by remember {
        mutableStateOf(false)
    }

    lateinit var icon: ImageVector
    lateinit var alignment: Alignment
    val color: Color

    when (swipeState.dismissDirection) {
        SwipeToDismissBoxValue.EndToStart,
        SwipeToDismissBoxValue.Settled-> {
            icon = Icons.Outlined.Delete
            alignment = Alignment.CenterEnd
            color = MaterialTheme.colorScheme.errorContainer
        }
        SwipeToDismissBoxValue.StartToEnd -> {
            icon = Icons.Outlined.Edit
            alignment = Alignment.CenterEnd // replace to center stand if needed
            color = Color.Transparent
        }
    }

    AnimatedVisibility(
        visible = !isRemoved,
        exit = shrinkVertically(
            animationSpec = tween(durationMillis = animationDuration),
            shrinkTowards = Alignment.Top
        ) + fadeOut()
    ) {
        SwipeToDismissBox(
            modifier = modifier.animateContentSize(),
            state = swipeState,
            enableDismissFromStartToEnd = false,
            backgroundContent = {
                Box(
                    contentAlignment = alignment,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color)
                ) {
                    Icon(
                        modifier = Modifier.minimumInteractiveComponentSize(),
                        imageVector = icon, contentDescription = null
                    )
                }
            }
        ) {
            content()
        }

    }

    when (swipeState.currentValue) {
        SwipeToDismissBoxValue.EndToStart -> {
            onDelete(animationDuration)
            isRemoved = true
        }

        SwipeToDismissBoxValue.StartToEnd -> {
            LaunchedEffect(swipeState) {
                onEdit()
                swipeState.snapTo(SwipeToDismissBoxValue.Settled)
            }
        }

        SwipeToDismissBoxValue.Settled -> {}
    }
}