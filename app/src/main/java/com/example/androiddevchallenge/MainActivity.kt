/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.example.androiddevchallenge.ui.theme.*
import com.example.androiddevchallenge.widget.Widgets

class MainActivity : AppCompatActivity() {

    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpViewModel()
        viewModel.initStopWatch()
        setContent {
            MyTheme {
                MyApp()
            }
        }
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }
}


lateinit var viewModel: MainViewModel

// Start building your app here!
@ExperimentalAnimationApi
@Composable
fun MyApp() {

    val minFocus by viewModel.minFocus.observeAsState(false)
    val hourFocus by viewModel.hourFocus.observeAsState(false)
    val secFocus by viewModel.secFocus.observeAsState(false)


    val hour by viewModel.hour.observeAsState(TextFieldValue("00"))
    val min by viewModel.minutes.observeAsState(TextFieldValue("00"))
    val sec by viewModel.seconds.observeAsState(TextFieldValue("00"))


    val startButtonClicked by viewModel.startButtonClicked.observeAsState(false)


    val minProgress by viewModel.minProgressTime.observeAsState(0.0f)
    val minAnimatedProgress by animateFloatAsState(
        targetValue = minProgress,
        animationSpec = SpringSpec(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessVeryLow,
            visibilityThreshold = 1 / 1000f
        )
    )


    val hourProgress by viewModel.hourProgressTime.observeAsState(0.0f)
    val hourAnimatedProgress by animateFloatAsState(
        targetValue = hourProgress,
        animationSpec = SpringSpec(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessVeryLow,
            visibilityThreshold = 1 / 1000f
        )
    )


    val secProgress by viewModel.secProgressTime.observeAsState(0.0f)
    val secAnimatedProgress by animateFloatAsState(
        targetValue = secProgress,
        animationSpec = SpringSpec(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessVeryLow,
            visibilityThreshold = 1 / 1000f
        )
    )
    Surface(color = MaterialTheme.colors.primary, modifier = Modifier.fillMaxWidth()) {


        Column {

            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_stopwatch_icon),
                tint = MaterialTheme.colors.secondary,
                contentDescription = "Stopwatch",
                modifier = Modifier
                    .padding(20.dp)
                    .size(35.dp)
                    .align(alignment = Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.weight(0.5f))
            Row(
                modifier = Modifier.weight(2f)
            ) {

                Box(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .fillMaxWidth()
                        .wrapContentSize(align = Alignment.Center)
                        .weight(1f)

                )
                {

                    Widgets().ClockEditText(
                        "H",
                        hour,
                        hourFocus,
                        viewModel.hour,
                        viewModel.hourFocus,
                        viewModel.hourProgressTime
                    )
                }

                Text(
                    text = ":",
                    color = Color.Gray,
                    style = MaterialTheme.typography.h6.copy(fontSize = 30.sp),
                    modifier = Modifier.align(Alignment.CenterVertically)
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .fillMaxWidth()
                        .wrapContentSize(align = Alignment.Center)
                        .weight(1f)
                )
                {

                    Widgets().ClockEditText(
                        "M",
                        min,
                        minFocus,
                        viewModel.minutes,
                        viewModel.minFocus,
                        viewModel.minProgressTime
                    )

                }

                Text(
                    text = ":",
                    color = Color.Gray,
                    style = MaterialTheme.typography.h6.copy(fontSize = 30.sp),
                    modifier = Modifier.align(Alignment.CenterVertically)
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .fillMaxWidth()
                        .wrapContentSize(align = Alignment.Center)
                        .weight(1f)
                )
                {

                    Widgets().ClockEditText(
                        "S",
                        sec,
                        secFocus,
                        viewModel.seconds,
                        viewModel.secFocus,
                        viewModel.secProgressTime
                    )

                }


            }
            Spacer(modifier = Modifier.weight(0.5f))
            Box(
                modifier = Modifier
                    .weight(6f)
                    .align(alignment = Alignment.Start)
                    .padding(start = 10.dp, end = 10.dp)
            )
            {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(align = Alignment.CenterHorizontally)
                        .size(400.dp)
                ) {


                    Image(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_clock),
                        contentDescription = "Stopwatch",
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(align = Alignment.Center)
                    )

                    this@Column.AnimatedVisibility(
                        visible = hourProgress > 0, enter = fadeIn(),
                        exit = fadeOut()
                    ) {

                        CircularProgressIndicator(
                            progress = hourAnimatedProgress,

                            strokeWidth = 15.dp,
                            modifier = Modifier
                                .fillMaxSize()
                                .wrapContentSize(align = Alignment.Center)
                                .fillMaxSize(0.32f),
                            color = purple300
                        )

                        Text(
                            "H", style = MaterialTheme.typography.h5.copy(
                                color = if (minFocus) Color.White else Color.Black,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                textAlign = TextAlign.Center
                            ),
                            modifier = Modifier
                                .fillMaxSize()
                                .wrapContentSize(align = Alignment.Center)
                                .fillMaxSize(0.32f)
                                .padding(end = 10.dp)


                        )


                    }
                    this@Column.AnimatedVisibility(
                        visible = minProgress > 0, enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        CircularProgressIndicator(
                            progress = minAnimatedProgress,

                            strokeWidth = 10.dp,
                            modifier = Modifier
                                .fillMaxSize()
                                .wrapContentSize(align = Alignment.Center)
                                .fillMaxSize(0.42f),
                            color = orange500
                        )
                        Text(
                            "M", style = MaterialTheme.typography.h5.copy(
                                color = Color.Black,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                textAlign = TextAlign.Center
                            ),
                            modifier = Modifier
                                .fillMaxSize()
                                .wrapContentSize(align = Alignment.Center)
                                .fillMaxSize(0.42f)
                                .padding(end = 10.dp)


                        )
                    }
                    this@Column.AnimatedVisibility(
                        visible = secProgress > 0,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        CircularProgressIndicator(
                            progress = secAnimatedProgress,
                            strokeWidth = 5.dp,
                            modifier = Modifier
                                .fillMaxSize()
                                .wrapContentSize(align = Alignment.Center)
                                .fillMaxSize(0.5f),
                            color = orange300
                        )
                        Text(
                            "S", style = MaterialTheme.typography.h5.copy(
                                color = Color.Black,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                textAlign = TextAlign.Center
                            ),
                            modifier = Modifier
                                .fillMaxSize()
                                .wrapContentSize(align = Alignment.Center)
                                .fillMaxSize(0.5f)
                                .padding(end = 10.dp)


                        )

                    }


                    IconButton(
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(align = Alignment.Center)
                            .size(35.dp),
                        onClick = {

                            if (!startButtonClicked) {
                                //check if the timer is initially set if so save the time to a variable which can be used for restore
                                if (!viewModel.pauseClicked.value!!) {
                                    viewModel.setInitialTime()
                                }
                                viewModel.setTimerData()
                                viewModel.startTimer()
                                viewModel.startButtonClicked.value = true
                            } else {
                                viewModel.pauseClicked.value = true
                                viewModel.stopTimer()
                                viewModel.startButtonClicked.value = false
                            }

                        }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(
                                id =
                                if (startButtonClicked) {
                                    R.drawable.ic_pause
                                } else {
                                    R.drawable.ic_play

                                }
                            ), contentDescription = "play ot pause button",
                            tint = Color.Black
                        )
                    }

                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier
                    .weight(2f)
                    .padding(20.dp)
            ) {


                IconButton(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize(),
                    onClick = {
                        viewModel.stopTimer()
                        viewModel.resetTimerView()
                    }) {
                    Icon(
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(align = Alignment.CenterStart)
                            .size(35.dp),
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_target_circles),
                        contentDescription = "reset",
                        tint = MaterialTheme.colors.secondary
                    )
                }
                IconButton(modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                    onClick = {
                        viewModel.stopTimer()
                        viewModel.reloadTimer()
                    }) {
                    Icon(
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(align = Alignment.CenterEnd)
                            .size(35.dp),
                        imageVector = ImageVector.vectorResource(
                            id = R.drawable.ic_reload
                        ),
                        contentDescription = "reload",
                        tint = MaterialTheme.colors.secondary
                    )
                }


            }
            Spacer(modifier = Modifier.weight(1f))
        }


    }
}


fun checkIfTextIsEmpty(text: String): Int {
    return if (text.isBlank()) {
        0
    } else {
        text.toInt()
    }

}


@ExperimentalAnimationApi
@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp()
    }
}

@ExperimentalAnimationApi
@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp()
    }
}


// for Reference

//                        .pointerInput(Unit) {
//                            detectVerticalDragGestures { change, dragAmount ->
//                                val original = offsetY
////                                val summed = original + dragAmount
//                                val newValue = (original + dragAmount).coerceIn(0f, size.height - 50.dp.toPx())
//
//
//                                change.consumePositionChange()
//                                viewModel.offsety.value = newValue
////                                viewModel.offsety.value = newValue
//                            }
//                        }
//                        .draggable(
//                            orientation = Orientation.Vertical,
//                            state = rememberDraggableState { delta ->
//                                val newValue = offsetY + delta
//                                viewModel.offsety.value = newValue.coerceIn(minPx,maxPx)
//                            }
//                        )
/*.scrollable(
    orientation = Orientation.Vertical,
    // Scrollable state: describes how to consume
    // scrolling delta and update offset
    state = rememberScrollableState { delta ->
//                                var deltaNow =(if(delta==0f) 1f else delta)/500





            if ((abs(abs(delta)-abs(deltaOverall))>100)) {

//                                        var deltaNow = (if (delta == 0f) 1f else delta) / 200
//                                        if (deltaNow > deltaOverall) {
                if ((abs(deltaOverall)-abs(delta) ) > 0) {
                    if (offsetX <= 60) {
                        viewModel.offsetX.value++
                    }
                } else {
                    if (offsetX > 0) {
                        viewModel.offsetX.value--

                    }

//                                        if ((abs(abs(delta) - deltaOverall)) > 200) {
//                                            viewModel.delta.value =
//                                                (if (delta == 0f) 1f else delta) / 200
//                                        }
                }
                viewModel.delta.value = delta
            }
//                                else if(abs(abs(delta)-abs(deltaOverall))>100){
////                                        var deltaNow = (if (delta == 0f) 1f else delta) / 1000
////                                        if (deltaNow < deltaOverall) {
//                                            if (offsetX > 0) {
//                                                viewModel.offsetX.value--
////                                            }
//                                        }
//                                        viewModel.delta.value =delta
////                                        if ((abs(abs(delta) - deltaOverall)) < 1000) {
////                                            viewModel.delta.value =
////                                                (if (delta == 0f) 1f else delta) / 1000
////                                        }
//                                    }
            viewModel.deltaInit.value = delta

        delta
    }*/