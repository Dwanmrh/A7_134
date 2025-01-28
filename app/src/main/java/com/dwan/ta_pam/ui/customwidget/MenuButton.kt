package com.dwan.ta_pam.ui.customwidget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MenuButton(
    onPasienClick: () -> Unit,
    onTerapisClick: () -> Unit,
    onJenisTerapiClick: () -> Unit,
    onSesiTerapiClick: () -> Unit
) {
    val darkRedColor = Color(0xFF8B0000)

    Surface(
        color = darkRedColor,
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp) // Pastikan tinggi cukup agar tidak ada celah bawah
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            IconTextButton(
                onClick = onPasienClick,
                text = "Pasien",
                icon = Icons.Default.Person,
                modifier = Modifier.weight(1f)
            )

            IconTextButton(
                onClick = onTerapisClick,
                text = "Terapis",
                icon = Icons.Default.MedicalServices,
                modifier = Modifier.weight(1f)
            )

            IconTextButton(
                onClick = onJenisTerapiClick,
                text = "Jenis Terapi",
                icon = Icons.Default.LocalHospital,
                modifier = Modifier.weight(1f)
            )

            IconTextButton(
                onClick = onSesiTerapiClick,
                text = "Sesi Terapi",
                icon = Icons.Default.Schedule,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun IconTextButton(
    onClick: () -> Unit,
    text: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    TextButton(
        onClick = onClick,
        modifier = modifier
            .padding(12.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White.copy(alpha = 0.1f))
            .rippleClickable(),
        colors = ButtonDefaults.textButtonColors(
            contentColor = Color.White
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun Modifier.rippleClickable(): Modifier = composed {
    this.then(
        Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = rememberRipple(bounded = true, color = Color.White)
        ) {}
    )
}