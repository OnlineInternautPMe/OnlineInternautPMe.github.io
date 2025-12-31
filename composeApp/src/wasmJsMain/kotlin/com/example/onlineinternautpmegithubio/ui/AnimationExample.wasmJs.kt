package com.example.onlineinternautpmegithubio.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
actual fun AnimationExample(modifier: Modifier) {
    var isExpanded by remember { mutableStateOf(true) }

    // 1. Define animated values
    val width by animateDpAsState(
        targetValue = if (isExpanded) 200.dp else 60.dp,
        animationSpec = tween(durationMillis = 500)
    )

    val backgroundColor by animateColorAsState(
        targetValue = if (isExpanded) Color(0xFF6200EE) else Color(0xFF03DAC6),
        animationSpec = tween(durationMillis = 500)
    )

    // 2. Apply values to UI
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(width)
                .height(60.dp)
                .background(backgroundColor, shape = RoundedCornerShape(30.dp))
                .clickable { isExpanded = !isExpanded },
            contentAlignment = Alignment.Center
        ) {
            if (isExpanded) {
                Text("Click Me", color = Color.White)
            }
        }
    }
}