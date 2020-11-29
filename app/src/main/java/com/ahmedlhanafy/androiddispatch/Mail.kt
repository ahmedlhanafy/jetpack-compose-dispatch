package com.ahmedlhanafy.androiddispatch

import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.ExperimentalFocus
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.ui.tooling.preview.Preview
import com.ahmedlhanafy.androiddispatch.ui.AndroidDispatchTheme

val emailList = listOf(
    Email(
        imageUrl = "https://play-lh.googleusercontent.com/qNoGiZk9VgG6N6e3UyqdenQpa4oE8_cB18WRNUu759veYN-uP0oPH3l9BEgk5GLAEY2b=s360-rw",
        sender = "Qubstudio",
        title = "Welcome!",
        description = "Hello, we are happy to invite you to",
        time = "07:59",
        favourite = true
    ),
    Email(
        imageUrl = "https://play-lh.googleusercontent.com/sc6mcHqKsavBiCqN6d-JUFRfeBfN01di3HZWKScZnbUQvcHrcegg8l5hD1Wp-a1Hm8Gq=s360-rw",
        sender = "monobank",
        title = "Transfer from monobank",
        description = "You made transactions at 10:37 from",
        time = "10:38",
        favourite = true
    ),
    Email(
        imageUrl = "https://play-lh.googleusercontent.com/J4FVjKbtZsATTJQdzHVin1ztCbcNPInlyJsv6l_ef24VoNBuW1CFh6TuSTvK0vFHcBQ=s360-rw",
        sender = "Projector",
        title = "Discount for all UI/UX lectures",
        description = "50% Discount for all design lectures",
        time = "07:32",
        favourite = false
    ),
    Email(
        imageUrl = "https://play-lh.googleusercontent.com/qvk6H6ZR2811TjZ2a2PWisbSe49wMSWvh_n_u9_0lQhJNxvtPMWgeUbHLsla5z9C2w=s360-rw",
        sender = "Dribbble",
        title = "New follower",
        description = "Andrew Klinsky followed you",
        time = "07:32",
        favourite = true
    ),
)

@ExperimentalFocus
@ExperimentalMaterialApi
@Composable
fun Mail() {
    val list = remember { mutableStateListOf(*emailList.toTypedArray()) }

    Column {
        Text(
            "Mailbox",
            modifier = Modifier.padding(16.dp),
            style = TextStyle(color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.Black)
        )
        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("Search mail") },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        )
        Spacer(Modifier.height(16.dp))

        LazyColumnFor(items = list) {
            key(it) {
                SwipeableBox(leftOnAction = {
                    list.remove(it)
                }) {

                    EmailRow(email = it)
                }

            }
        }
    }
}

data class Email(
    val imageUrl: String,
    val sender: String,
    val title: String,
    val description: String,
    val time: String,
    val favourite: Boolean
)


@Composable
fun EmailRow(email: Email) {
    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
        NetworkImage(url = email.imageUrl, Modifier.size(64.dp).clip(CircleShape)) {
            Box(it)
        }
        Spacer(Modifier.width(16.dp))
        Column(Modifier.weight(1f)) {
            Text(email.sender, style = TextStyle(color = Color.White, fontWeight = FontWeight.Bold))
            Text(
                email.title,
                style = TextStyle(
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                email.description,
                style = TextStyle(color = Color.Gray, fontSize = 16.sp),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }

        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxHeight()
        ) {
            Text(
                email.time,
                style = TextStyle(
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(Modifier.height(16.dp))
            if (email.favourite) {
                Icon(asset = Icons.Default.Star, tint = Color(0xFFFFC107))
            }
        }
    }
}

@Preview
@Composable
fun EmailRowPreview() {
    AndroidDispatchTheme {
        EmailRow(emailList.first())
    }
}