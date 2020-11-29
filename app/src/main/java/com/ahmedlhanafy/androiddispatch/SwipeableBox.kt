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
import androidx.compose.ui.focus.isFocused
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.gesture.DragObserver
import androidx.compose.ui.gesture.dragGestureFilter
import androidx.compose.ui.gesture.scrollorientationlocking.Orientation
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.DensityAmbient
import androidx.compose.ui.unit.dp


enum class SwipeableState {
    END_COMPLETED, END_VISIBLE, HIDDEN, START_VISIBLE
}


@ExperimentalFocus
@ExperimentalMaterialApi
@Composable
fun SwipeableBox(
    modifier: Modifier = Modifier,
    color: Color = Color.Red,
    leftOnAction: () -> Unit,
    leftIcon: @Composable () -> Unit = {
        Icon(
            asset = Icons.Default.Delete,
            tint = Color.White,
            modifier = Modifier.fillMaxSize()
        )
    },
    content: @Composable () -> Unit
) {
    with(DensityAmbient.current) {
        WithConstraints {
            val width = constraints.maxWidth.toDp()
            val squareSize = 70.dp

            val swipeableState = rememberSwipeableState(SwipeableState.HIDDEN)
            val widthPx = width.toPx()
            val squareSizePx = squareSize.toPx()
            val anchors =
                mapOf(
                    0f to SwipeableState.END_COMPLETED,
                    widthPx - squareSizePx to SwipeableState.END_VISIBLE,
                    widthPx to SwipeableState.HIDDEN,
                    widthPx + squareSizePx to SwipeableState.START_VISIBLE
                )
            val offset =
                if (!swipeableState.offset.value.isNaN()) (width - swipeableState.offset.value.toDp()) * -1 else 0.dp
            val inputList = listOf(Float.MAX_VALUE, -1 * squareSize.toPx(), 0f)
            val opacity = interpolate(offset.toPx(), inputList, listOf(1f, 1f, 0f))
            val scale = interpolate(offset.toPx(), inputList, listOf(1f, 1f, 0.6f))

            onCommit(swipeableState.value) {
                if (swipeableState.value == SwipeableState.END_COMPLETED) {
                    leftOnAction()
                }
            }

            val focusRequester = remember { FocusRequester() }
            var isFocused by remember { mutableStateOf(false) }

            onCommit(isFocused) {
                if (!isFocused && swipeableState.value != SwipeableState.HIDDEN) {
                    swipeableState.animateTo(SwipeableState.HIDDEN)
                }
            }

            Box(
                modifier = modifier
                    .focusRequester(focusRequester)
                    .focusObserver { isFocused = it.isFocused }
                    .focus()
                    .preferredWidth(width)
                    .swipeable(
                        state = swipeableState,
                        anchors = anchors,
                        thresholds = { _, _ -> FractionalThreshold(0.5f) },
                        orientation = Orientation.Horizontal,
                    )
                    .dragGestureFilter(dragObserver = object : DragObserver {
                        override fun onStart(downPosition: Offset) {
                            super.onStart(downPosition)
                            focusRequester.requestFocus()
                        }

                        override fun onStop(velocity: Offset) {
                            super.onStop(velocity)
                            focusRequester.freeFocus()
                        }

                        override fun onCancel() {
                            super.onCancel()
                            focusRequester.freeFocus()
                        }
                    }).wrapContentHeight()
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
                            onClick = { swipeableState.animateTo(SwipeableState.END_COMPLETED) }
                        )
                    ) {
                        Box(Modifier.drawLayer(scaleX = scale, scaleY = scale, alpha = opacity)) {
                            leftIcon()
                        }
                    }
                }

            }
        }
    }
}

