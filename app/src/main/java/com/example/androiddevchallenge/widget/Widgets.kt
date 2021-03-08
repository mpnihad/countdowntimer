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
package com.example.androiddevchallenge.widget

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.isFocused
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import com.example.androiddevchallenge.checkIfTextIsEmpty
import com.example.androiddevchallenge.ui.theme.orange200
import com.example.androiddevchallenge.viewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.abs

class Widgets {
    @Composable
    fun ClockEditText(
        heading: String,
        min: TextFieldValue,
        min_focus: Boolean,
        viewModelData: MutableLiveData<TextFieldValue>,
        viewModelFocus: MutableLiveData<Boolean>,
        viewModelProgress: MutableLiveData<Float>
    ) {

        val deltaOverall by
        rememberSaveable { viewModel.delta }
        val color = remember { androidx.compose.animation.Animatable(Color.Transparent) }

        Box(

            Modifier

                .fillMaxWidth(0.55f)
                .fillMaxHeight(0.6f)
                .clip(shape = RoundedCornerShape(25.dp))
                .background(color = color.value)
                .border(
                    BorderStroke(if (min_focus) 0.dp else 2.dp, Color.LightGray),
                    shape = RoundedCornerShape(25.dp)
                )
                .onFocusChanged {

                    viewModelFocus.value = it.isFocused
                    if (it.isFocused) {
                        CoroutineScope(Dispatchers.IO).launch {
                            color.animateTo(orange200)
                        }
                    } else {
                        CoroutineScope(Dispatchers.IO).launch {
                            color.animateTo(Color.Transparent)
                        }
                    }
                }

                .scrollable(
                    orientation = Orientation.Vertical,
                    state = rememberScrollableState { delta ->

                        if (!(viewModel.startButtonClicked.value!!)) {

                            var cuMin = if (min.text.isEmpty()) 0 else min.text.toInt()
                            val deltaNow = (if (delta == 0f) 1f else delta) / 1000
                            if (delta > deltaOverall) {
                                if (deltaNow > deltaOverall) {

                                    if (cuMin < (if (heading == "H") 12 else 60)) {
                                        var changedTime = (++cuMin).toString()
                                        if (changedTime.length == 1) {
                                            changedTime = "0$changedTime"
                                        }
                                        viewModelData.value = TextFieldValue((changedTime))
                                        viewModelProgress.value =
                                            changedTime.toFloat() / (if (heading == "H") 12 else 60)
                                    }
                                }
                                if ((abs(abs(delta) - deltaOverall)) > 1000) {
                                    viewModel.delta.value = (if (delta == 0f) 1f else delta) / 1000
                                }
                            } else {
                                if (deltaNow < deltaOverall) {
                                    if (cuMin > 0) {
                                        var changedTime = (--cuMin).toString()
                                        if (changedTime.length == 1) {
                                            changedTime = "0$changedTime"
                                        }
                                        viewModelData.value = TextFieldValue((changedTime))
                                        viewModelProgress.value =
                                            changedTime.toFloat() / (if (heading == "H") 12 else 60)
                                    }
                                }
                                if ((abs(abs(delta) - deltaOverall)) < 1000) {
                                    viewModel.delta.value = (if (delta == 0f) 1f else delta) / 1000
                                }
                            }
                        }
                        delta
                    }
                )
                .background(Color.Transparent),
            contentAlignment = Alignment.Center
        ) {

            TextField(
                value = min,
                onValueChange = {
                    if (it.text.isEmpty()) {
                        viewModelData.value = it
                        viewModelProgress.value = 0.0f
                    } else {

                        if (it.text.length <= 2) {
                            if ((checkIfTextIsEmpty(it.text)) <= (if (heading == "H") 12 else 60)) {
                                viewModelData.value = it
                                viewModelProgress.value =
                                    it.text.toFloat() / (if (heading == "H") 12 else 60)
                            }
                        }
                    }
                },

                textStyle = MaterialTheme.typography.h5.copy(
                    if (min_focus) {
                        Color.White
                    } else {
                        Color.Black
                    },
                    fontWeight = if (min_focus) {
                        FontWeight.ExtraBold
                    } else {
                        FontWeight.Light
                    },
                    textAlign = TextAlign.Center
                ),

                enabled = !(viewModel.startButtonClicked.value!!),

                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),

                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(align = Alignment.Center),

                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedLabelColor = MaterialTheme.colors.primary,
                    unfocusedLabelColor = Color.Transparent,
                    textColor = Color.DarkGray,
                    disabledIndicatorColor = Color.Transparent,
                )

            )
            Text(
                heading,
                style = MaterialTheme.typography.h5.copy(
                    if (min_focus) {
                        Color.White
                    } else {
                        Color.Black
                    },
                    fontSize = 10.sp, fontWeight = FontWeight.ExtraBold
                ),
                modifier = Modifier.padding(top = 30.dp, start = 20.dp)
            )
        }
    }
}
