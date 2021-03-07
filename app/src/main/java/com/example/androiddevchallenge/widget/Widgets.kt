package com.example.androiddevchallenge.widget

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

class Widgets {
    @Composable
    fun NumberPicker(
        state: MutableState<Int>,
        modifier: Modifier = Modifier,
        range: IntRange? = null,
        textStyle: TextStyle = LocalTextStyle.current,
    ) {
        val coroutineScope = rememberCoroutineScope()
        val numbersColumnHeight = 36.dp
        val halvedNumbersColumnHeight = numbersColumnHeight / 2
        val halvedNumbersColumnHeightPx = with(LocalDensity.current) { halvedNumbersColumnHeight.toPx() }

        fun animatedStateValue(offset: Float): Int = state.value - (offset / halvedNumbersColumnHeightPx).toInt()

        val animatedOffset = remember { Animatable(0f) }.apply {
            if (range != null) {
                val offsetRange = remember(state.value, range) {
                    val value = state.value
                    val first = -(range.last - value) * halvedNumbersColumnHeightPx
                    val last = -(range.first - value) * halvedNumbersColumnHeightPx
                    first..last
                }
                updateBounds(offsetRange.start, offsetRange.endInclusive)
            }
        }
        val coercedAnimatedOffset = animatedOffset.value % halvedNumbersColumnHeightPx
        val animatedStateValue = animatedStateValue(animatedOffset.value)

        Column(
            modifier = modifier
                .wrapContentSize()
                .draggable(
                    orientation = Orientation.Vertical,
                    onDragStarted = {
                        coroutineScope.launch {
                            animatedOffset.snapTo(animatedOffset.value )
                        }
                    },

                    state= DraggableState {

                    },
                    onDragStopped = { velocity ->
                        coroutineScope.launch {
//                            val flingConfig =
//                                ScrollableDefaults.flingBehavior
//
//
//                            FlingBehavior(
//
//                                decayAnimation = FloatExponentialDecaySpec(
//                                    frictionMultiplier = 20f
//                                ),
//                                adjustTarget = { target ->
//                                    val coercedTarget = target % halvedNumbersColumnHeightPx
//                                    val coercedAnchors = listOf(
//                                        -halvedNumbersColumnHeightPx,
//                                        0f,
//                                        halvedNumbersColumnHeightPx
//                                    )
//                                    val coercedPoint =
//                                        coercedAnchors.minByOrNull { abs(it - coercedTarget) }!!
//                                    val base =
//                                        halvedNumbersColumnHeightPx * (target / halvedNumbersColumnHeightPx).toInt()
//                                    val adjusted = coercedPoint + base
//                                    TargetAnimation(adjusted, SpringSpec())
//                                }
//                            )
                            val endValue = animatedOffset.animateDecay(
                                initialVelocity = velocity,
                                animationSpec = exponentialDecay(
                                    frictionMultiplier = 20f,
                                    absVelocityThreshold =20f

                                )
                            ).endState.value

                            state.value = animatedStateValue(endValue)
                            animatedOffset.snapTo(0f)
                        }
                    }
                )
        ) {
            val spacing = 4.dp

            val arrowColor = MaterialTheme.colors.onSecondary.copy(alpha = ContentAlpha.disabled)

//            Arrow(direction = DrawerArrowDrawable.ArrowDirection.UP, tint = arrowColor)

            Spacer(modifier = Modifier.height(spacing))

            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .offset(y =  coercedAnimatedOffset.roundToInt().dp)
            ) {
                val baseLabelModifier = Modifier.align(Alignment.Center)
                ProvideTextStyle(textStyle) {
                    Label(
                        text = (animatedStateValue - 1).toString(),
                        modifier = baseLabelModifier
                            .offset(y = -halvedNumbersColumnHeight)
                            .alpha(coercedAnimatedOffset / halvedNumbersColumnHeightPx)
                    )
                    Label(
                        text = animatedStateValue.toString(),
                        modifier = baseLabelModifier
                            .alpha(1 - abs(coercedAnimatedOffset) / halvedNumbersColumnHeightPx)
                    )
                    Label(
                        text = (animatedStateValue + 1).toString(),
                        modifier = baseLabelModifier
                            .offset(y = halvedNumbersColumnHeight)
                            .alpha(-coercedAnimatedOffset / halvedNumbersColumnHeightPx)
                    )
                }
            }

            Spacer(modifier = Modifier.height(spacing))

//            Arrow(direction = DrawerArrowDrawable.ArrowDirection.DOWN, tint = arrowColor)
        }
    }

    @Composable
    private fun Label(text: String, modifier: Modifier) {
        Text(
            text = text,
            modifier = modifier.pointerInput(Unit) {
                detectTapGestures(onLongPress = {
                    // FIXME: Empty to disable text selection
                })
            },
            style = MaterialTheme.typography.h6.copy(color = Color.Black)
        )
    }
}