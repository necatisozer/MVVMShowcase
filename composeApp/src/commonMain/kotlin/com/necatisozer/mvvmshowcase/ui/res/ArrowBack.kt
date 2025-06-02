package com.necatisozer.mvvmshowcase.ui.res

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

val ArrowBack: ImageVector by
  lazy(LazyThreadSafetyMode.NONE) {
    ImageVector.Builder(
        name = "ArrowBack",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 960f,
        viewportHeight = 960f,
      )
      .apply {
        path(fill = SolidColor(Color(0xFF000000))) {
          moveToRelative(313f, 520f)
          lineToRelative(224f, 224f)
          lineToRelative(-57f, 56f)
          lineToRelative(-320f, -320f)
          lineToRelative(320f, -320f)
          lineToRelative(57f, 56f)
          lineToRelative(-224f, 224f)
          horizontalLineToRelative(487f)
          verticalLineToRelative(80f)
          close()
        }
      }
      .build()
  }

@Preview
@Composable
private fun ArrowBackPreview() {
  Box(modifier = Modifier.padding(12.dp)) {
    Image(imageVector = ArrowBack, contentDescription = null)
  }
}
