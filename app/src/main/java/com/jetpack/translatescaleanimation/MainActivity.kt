package com.jetpack.translatescaleanimation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.jetpack.translatescaleanimation.ui.theme.TranslateScaleAnimationTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TranslateScaleAnimationTheme {
                Surface(color = MaterialTheme.colors.background) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = {
                                    Text(
                                        text = "Translation Scale Animation",
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            )
                        }
                    ) {
                        TranslationAnimation()
                    }
                }
            }
        }
    }
}

@Composable
fun TranslationAnimation() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val animatableSize = remember { Animatable(Size.Zero, Size.VectorConverter) }
        val (containerSize, setContainerSize) = remember { mutableStateOf<Size?>(null) }
        val (imageSize, setImageSize) = remember { mutableStateOf<Size?>(null) }
        val density = LocalDensity.current
        val scope = rememberCoroutineScope()

        Button(
            onClick = {
                scope.launch {
                    if (imageSize == null || containerSize == null) return@launch
                    val targetSize = if (animatableSize.value == imageSize) containerSize else imageSize
                    animatableSize.animateTo(
                        targetSize,
                        animationSpec = tween(1000)
                    )
                }
            }
        ) {
            Text(text = "Animate")
        }

        Box(
            modifier = Modifier
                .padding(20.dp)
                .size(300.dp)
                .background(Color.LightGray)
                .onSizeChanged { size ->
                    setContainerSize(size.toSize())
                }
        ) {
            Image(
                imageVector = Icons.Default.Person,
                contentDescription = "Icons",
                modifier = Modifier
                    .then(
                        if (animatableSize.value != Size.Zero) {
                            animatableSize.value.run {
                                Modifier.size(
                                    width = with(density) { width.toDp() },
                                    height = with(density) { height.toDp() }
                                )
                            }
                        } else {
                            Modifier
                        }
                    )
                    .onSizeChanged { intSize ->
                        if (imageSize != null) return@onSizeChanged
                        val size = intSize.toSize()
                        setImageSize(size)
                        scope.launch {
                            animatableSize.snapTo(size)
                        }
                    }
            )
        }
    }
}






















