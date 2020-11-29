package com.ahmedlhanafy.androiddispatch

import android.util.Log
import androidx.compose.foundation.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.focus.ExperimentalFocus
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.gesture.scrollorientationlocking.Orientation
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.DensityAmbient
import androidx.compose.ui.unit.dp


enum class SwipeableState {
    DELETED, VISIBLE, HIDDEN
}

val SwipeableAmbient = ambientOf<CallbackContainer> { error("") }


@ExperimentalFocus
@ExperimentalMaterialApi
@Composable
fun SwipeableBox(
    modifier: Modifier = Modifier,
    color: Color = Color.Red,
    onAction: () -> Unit,
    icon: @Composable () -> Unit = {
        Icon(
            asset = Icons.Default.Delete,
            tint = Color.White,
            modifier = Modifier.fillMaxSize()
        )
    },
    content: @Composable () -> Unit
) {
    val callbackState = SwipeableAmbient.current

    with(DensityAmbient.current) {
        WithConstraints {
            val width = constraints.maxWidth.toDp()
            val squareSize = 50.dp

            val swipeableState = rememberSwipeableState(SwipeableState.HIDDEN)
            val sizePx = (width - squareSize).toPx()
            val widthPx = width.toPx()
            val anchors =
                mapOf(
                    sizePx - widthPx + squareSize.toPx() to SwipeableState.DELETED,
                    sizePx to SwipeableState.VISIBLE,
                    widthPx to SwipeableState.HIDDEN,
                )
            val offset =
                if (!swipeableState.offset.value.isNaN()) (width - swipeableState.offset.value.toDp()) * -1 else 0.dp
            val inputList = listOf(Float.MAX_VALUE, -1 * squareSize.toPx(), 0f)
            val opacity = interpolate(offset.toPx(), inputList, listOf(1f, 1f, 0f))
            val scale = interpolate(offset.toPx(), inputList, listOf(1f, 1f, 0.6f))

            onCommit(swipeableState.value) {
                if (swipeableState.value == SwipeableState.DELETED) {
                    onAction()
                }
            }


            onCommit(swipeableState.value, swipeableState.targetValue) {
                if (swipeableState.value == SwipeableState.HIDDEN && swipeableState.targetValue == SwipeableState.VISIBLE) {
                    callbackState.current()
                    callbackState.current = { swipeableState.animateTo(SwipeableState.HIDDEN) }
                }
            }

            Box(
                modifier = modifier
                    .focusRequester(remember { FocusRequester() })
                    .focusObserver { it ->
                        Log.d("TAG", "SwipeableBox: $it")
                    }
                    .preferredWidth(width)
                    .swipeable(
                        state = swipeableState,
                        anchors = anchors,
                        thresholds =
                        { _, _ ->
                            FractionalThreshold(0.5f)
                        },
                        orientation = Orientation.Horizontal,
                    ).wrapContentHeight()
            ) {
                Box(
                    Modifier.offset(x = offset)
                        .align(Alignment.TopStart)
                ) {
                    content()
                }
                Box(
                    Modifier
                        .offsetPx(x = swipeableState.offset)
                        .preferredWidth(width)
                        .matchParentSize()
                        .background(color),
                    alignment = Alignment.Center
                ) {
                    Box(
                        Modifier.preferredWidth(squareSize).align(Alignment.TopStart).clickable(
                            onClick = { swipeableState.animateTo(SwipeableState.DELETED) }
                        )
                    ) {
                        Box(Modifier.drawLayer(scaleX = scale, scaleY = scale, alpha = opacity)) {
                            icon()
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun SwipeableProvider(children: @Composable () -> Unit) {
    val callback = remember { CallbackContainer({}) }

    Providers(SwipeableAmbient provides callback) {
        children()
    }
}