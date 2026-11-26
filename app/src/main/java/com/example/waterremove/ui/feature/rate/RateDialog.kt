package com.example.waterremove.ui.feature.rate

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.net.toUri
import com.example.waterremove.R
import com.example.waterremove.ui.components.showToast
import com.example.waterremove.ui.theme.Blue
import com.example.waterremove.ui.theme.BlueLight
import com.example.waterremove.ui.theme.Gray200
import com.example.waterremove.ui.theme.Gray400
import com.example.waterremove.ui.theme.Star
import com.example.waterremove.ui.theme.WaterRemoveTheme

fun Context.openPlayStore() {
    try {
        startActivity(Intent(Intent.ACTION_VIEW, "market://details?id=$packageName".toUri()))
    } catch (_: ActivityNotFoundException) {
        startActivity(Intent(Intent.ACTION_VIEW, "https://play.google.com/store/apps/details?id=$packageName".toUri()))
    }
}


@Composable
fun RateFlow(onDismiss: () -> Unit, onRated: () -> Unit) {
    val context = LocalContext.current
    var showFeedback by rememberSaveable { mutableStateOf(false) }
    val thanks = stringResource(R.string.toast_feedback_thanks)

    if (showFeedback) {
        FeedbackDialog(
            onDismiss = onDismiss,
            onSend = {
                context.showToast(thanks)
                onRated()
                onDismiss()
            },
        )
    } else {
        RateDialog(
            onDismiss = onDismiss,
            onRate = { stars ->
                onRated() // đã chọn sao và bấm Rate now → tính là đã rate, dù mấy sao
                if (stars >= 4) {
                    context.openPlayStore()
                    onDismiss()
                } else {
                    showFeedback = true
                }
            },
            onFeedback = { showFeedback = true },
        )
    }
}

@Composable
fun RateDialog(onDismiss: () -> Unit, onRate: (Int) -> Unit, onFeedback: () -> Unit) {
    var stars by rememberSaveable { mutableIntStateOf(5) }
    val (emoji, desc) = when (stars) {
        5 -> "😍" to R.string.rate_desc_5
        4 -> "😊" to R.string.rate_desc_4
        3 -> "🙂" to R.string.rate_desc_3
        2 -> "😟" to R.string.rate_desc_2
        else -> "😣" to R.string.rate_desc_1
    }

    Dialog(onDismissRequest = onDismiss) {
        DialogCard(onClose = onDismiss) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(BlueLight),
                contentAlignment = Alignment.Center,
            ) { Text(emoji, fontSize = 34.sp) }
            Spacer(Modifier.height(12.dp))
            Text(stringResource(R.string.rate_title), fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(6.dp))
            Text(
                stringResource(desc),
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(14.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                (1..5).forEach { i ->
                    Icon(
                        Icons.Filled.Star, null,
                        tint = if (i <= stars) Star else Gray400,
                        modifier = Modifier
                            .size(34.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                            ) { stars = i },
                    )
                }
            }
            Spacer(Modifier.height(18.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                if (stars <= 3) {
                    Button(
                        onClick = onFeedback,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Gray200, contentColor = Blue),
                    ) { Text(stringResource(R.string.feedback)) }
                }
                Button(
                    onClick = { onRate(stars) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                ) { Text(stringResource(R.string.rate_now)) }
            }
        }
    }
}

@Composable
fun FeedbackDialog(onDismiss: () -> Unit, onSend: (String) -> Unit) {
    var text by rememberSaveable { mutableStateOf("") }
    Dialog(onDismissRequest = onDismiss) {
        DialogCard(onClose = onDismiss) {
            Text(stringResource(R.string.feedback), fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(6.dp))
            Text(
                stringResource(R.string.feedback_desc),
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(14.dp))
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                placeholder = { Text(stringResource(R.string.feedback_hint)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp),
                shape = RoundedCornerShape(12.dp),
            )
            Spacer(Modifier.height(16.dp))
            // Chỉ enable Send khi user đã nhập nội dung
            Button(
                onClick = { onSend(text.trim()) },
                enabled = text.isNotBlank(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
            ) { Text(stringResource(R.string.action_send)) }
        }
    }
}

@Composable
private fun DialogCard(onClose: () -> Unit, content: @Composable () -> Unit) {
    Surface(shape = RoundedCornerShape(20.dp), color = MaterialTheme.colorScheme.surface) {
        Box {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) { content() }
            IconButton(onClick = onClose, modifier = Modifier.align(Alignment.TopEnd)) {
                Icon(Icons.Filled.Close, stringResource(R.string.action_close), Modifier.size(20.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RatePreview() {
    WaterRemoveTheme { RateDialog({}, {}, {}) }
}

@Preview(showBackground = true)
@Composable
private fun FeedbackPreview() {
    WaterRemoveTheme { FeedbackDialog({}, {}) }
}
