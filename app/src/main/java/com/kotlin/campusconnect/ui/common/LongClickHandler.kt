package com.kotlin.campusconnect.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LongClickHandler(
    private val onLongClick: () -> Unit,
    private val scope: CoroutineScope
) {
    private var job: Job? = null

    @Composable
    fun onLongClick() = scope.launch { delay(500); onLongClick() }

    @Composable
    fun onClickReleased() = job?.cancel()
}

@Composable
fun rememberLongClickHandler(onLongClick: () -> Unit) =
    remember { LongClickHandler(onLongClick, rememberCoroutineScope()) }