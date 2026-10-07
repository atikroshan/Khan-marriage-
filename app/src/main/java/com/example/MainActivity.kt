package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.ui.KhanApp
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.RoyalBlack

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        Scaffold(
          modifier = Modifier
            .fillMaxSize()
            .background(RoyalBlack),
          containerColor = RoyalBlack
        ) { innerPadding ->
          Box(
            modifier = Modifier
              .fillMaxSize()
              .safeDrawingPadding()
          ) {
            KhanApp()
          }
        }
      }
    }
  }
}
