package com.example.onlineinternautpmegithubio

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun App() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .safeContentPadding()
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "En desarrollo",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold
            )
        }

        /*var showContent by remember { mutableStateOf(false) }
        Row(
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            AnimationExample(
                modifier = Modifier
                    .weight(0.333f)
            )
            ThreeJsAnimation(
                modifier = Modifier
                    .weight(0.333f)
            )
            Column(
                modifier = Modifier
                    .weight(0.333f)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .safeContentPadding()
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Button(onClick = { showContent = !showContent }) {
                    Text(stringResource(Res.string.app_name))
                }
                AnimatedVisibility(showContent) {
                    val greeting = remember { Greeting().greet() }
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Image(painterResource(Res.drawable.compose_multiplatform), null)
                        Text("Compose: $greeting")
                    }
                }
            }
        }*/
    }
}