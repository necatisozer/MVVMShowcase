package com.necatisozer.mvvmshowcase.icon

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

val Search: ImageVector by
  lazy(LazyThreadSafetyMode.NONE) {
    ImageVector.Builder(
        name = "Search",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 960f,
        viewportHeight = 960f,
      )
      .apply {
        path(fill = SolidColor(Color(0xFF000000))) {
          moveTo(784f, 840f)
          lineTo(532f, 588f)
          quadToRelative(-30f, 24f, -69f, 38f)
          reflectiveQuadToRelative(-83f, 14f)
          quadToRelative(-109f, 0f, -184.5f, -75.5f)
          reflectiveQuadTo(120f, 380f)
          reflectiveQuadToRelative(75.5f, -184.5f)
          reflectiveQuadTo(380f, 120f)
          reflectiveQuadToRelative(184.5f, 75.5f)
          reflectiveQuadTo(640f, 380f)
          quadToRelative(0f, 44f, -14f, 83f)
          reflectiveQuadToRelative(-38f, 69f)
          lineToRelative(252f, 252f)
          close()
          moveTo(380f, 560f)
          quadToRelative(75f, 0f, 127.5f, -52.5f)
          reflectiveQuadTo(560f, 380f)
          reflectiveQuadToRelative(-52.5f, -127.5f)
          reflectiveQuadTo(380f, 200f)
          reflectiveQuadToRelative(-127.5f, 52.5f)
          reflectiveQuadTo(200f, 380f)
          reflectiveQuadToRelative(52.5f, 127.5f)
          reflectiveQuadTo(380f, 560f)
        }
      }
      .build()
  }

@Preview
@Composable
private fun MaterialSymbolsOutlinedSearchPreview() {
  Box(modifier = Modifier.padding(12.dp)) { Image(imageVector = Search, contentDescription = null) }
}
