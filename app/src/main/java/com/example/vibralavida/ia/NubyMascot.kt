package com.example.vibralavida.ia

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun NubyMascot(
    @DrawableRes poses: List<Int>,
    modifier: Modifier = Modifier,
    playGreetingAnimation: Boolean = true
) {

    if (
        poses.isEmpty()
    ) {

        return
    }

    var currentPoseIndex by
        remember {

            mutableIntStateOf(
                0
            )
        }

    LaunchedEffect(
        playGreetingAnimation,
        poses
    ) {

        if (
            !playGreetingAnimation
        ) {

            return@LaunchedEffect
        }

        currentPoseIndex =
            0

        delay(
            650
        )

        for (
        index in
        1 until poses.size
        ) {

            currentPoseIndex =
                index

            delay(
                650
            )
        }
    }

    Box(
        modifier =
            modifier,
        contentAlignment =
            Alignment.Center
    ) {

        AnimatedContent(
            targetState =
                poses[currentPoseIndex],
            transitionSpec = {

                fadeIn() togetherWith fadeOut()
            },
            label =
                "nuby_pose_animation"
        ) {
                drawableRes ->

            Image(
                painter =
                    painterResource(
                        id = drawableRes
                    ),
                contentDescription =
                    "Nuby, mascota virtual",
                modifier =
                    Modifier.size(
                        180.dp
                    ),
                contentScale =
                    ContentScale.Fit
            )
        }
    }
}
