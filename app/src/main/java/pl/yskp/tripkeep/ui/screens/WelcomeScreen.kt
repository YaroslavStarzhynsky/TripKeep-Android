package pl.yskp.tripkeep.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.yskp.tripkeep.R
import pl.yskp.tripkeep.ui.theme.TripKeepOrange

@Composable
fun WelcomeScreen(
    onContinueClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Root container - No statusBarsPadding here to allow image to go behind status bar
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // 1. Header Section: Immersive Image + Wave
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(450.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.welcome_bg),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Wave Overlay at the bottom of the image area
            WaveShape(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .align(Alignment.BottomCenter)
            )
        }

        // 2. Content Section: Text on white background
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 32.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "TripKeep",
                style = MaterialTheme.typography.displayMedium.copy(
                    fontSize = 40.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            )
            
            Text(
                text = "Twój Pamiętnik Podróży",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = Color.Gray,
                    fontWeight = FontWeight.Normal
                )
            )
        }

        // 3. Footer Section: Button pushed to the bottom
        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onContinueClick,
            modifier = Modifier
                .align(Alignment.End)
                .padding(end = 24.dp, bottom = 24.dp)
                .navigationBarsPadding(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Gray
            )
        ) {
            Text(
                text = "Kontynuuj",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 18.sp,
                    color = Color.Gray
                ),
                modifier = Modifier.padding(end = 8.dp)
            )
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(TripKeepOrange, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun WaveShape(modifier: Modifier = Modifier) {
    androidx.compose.foundation.Canvas(modifier = modifier) {
        // Ensure the area below the curve is filled with white to cover the image
        val fillPath = Path().apply {
            moveTo(0f, size.height)
            cubicTo(
                size.width * 0.3f, size.height - 120f,
                size.width * 0.7f, size.height + 60f,
                size.width, size.height - 40f
            )
            lineTo(size.width, size.height + 100f)
            lineTo(0f, size.height + 100f)
            close()
        }
        drawPath(fillPath, color = Color.White)
    }
}
