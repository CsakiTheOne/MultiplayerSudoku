package com.wholesomeware.multiplayersudoku.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.wholesomeware.multiplayersudoku.model.Player

@Composable
fun PlayerDisplay(
    modifier: Modifier = Modifier,
    player: Player,
    adminControlsEnabled: Boolean = false,
    onKickRequest: () -> Unit = {},
    isMini: Boolean = false,
) {
    Box(modifier = modifier) {
        if (isMini) {
            AssistChip(
                onClick = {},
                label = { Text(text = player.name) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = null,
                    )
                },
                trailingIcon = {
                    if (adminControlsEnabled) {
                        Icon(
                            modifier = Modifier.clickable { onKickRequest() },
                            imageVector = Icons.AutoMirrored.Default.Logout,
                            contentDescription = null,
                        )
                    }
                },
                colors = AssistChipDefaults.assistChipColors(
                    leadingIconContentColor = Color(player.color),
                    trailingIconContentColor = Color(player.color),
                    labelColor = Color(player.color),
                ),
            )
        }
        else {
            Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    modifier = Modifier.padding(8.dp),
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    tint = Color(player.color),
                )
                Text(
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f),
                    text = player.name,
                    color = Color(player.color),
                )
                if (adminControlsEnabled) {
                    IconButton(onClick = { onKickRequest() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.Logout,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}