package com.ahmedlhanafy.androiddispatch

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.launchInComposition
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageAsset
import androidx.compose.ui.graphics.asImageAsset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ContextAmbient
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.request.ImageRequest


@Composable
private fun loadImage(url: String): ImageAsset? {
    val image = remember(url) { mutableStateOf<ImageAsset?>(null) }
    val context = ContextAmbient.current

    launchInComposition(url) {
        val imageLoader = ImageLoader.Builder(context)
            .availableMemoryPercentage(0.25)
            .crossfade(true)
            .build()

        val request = ImageRequest.Builder(context)
            .data(url)
            .target { drawable ->
                image.value = drawable.toBitmap().asImageAsset()
            }
            .build()
        imageLoader.execute(request)

    }

    return image.value
}

@Composable
fun NetworkImage(
    url: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    placeholder: @Composable (modifier: Modifier) -> Unit
) {
    val image = loadImage(url);

    if (image != null) {
        Image(modifier = modifier, asset = image, contentScale = contentScale)
    } else {
        placeholder(modifier)
    }
}
