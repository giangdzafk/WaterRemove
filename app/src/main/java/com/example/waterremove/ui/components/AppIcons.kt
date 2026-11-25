package com.example.waterremove.ui.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp


object AppIcons {

    private fun icon(name: String, block: ImageVector.Builder.() -> Unit) =
        ImageVector.Builder(name, 24.dp, 24.dp, 24f, 24f).apply(block).build()

    private fun ImageVector.Builder.stroked(block: androidx.compose.ui.graphics.vector.PathBuilder.() -> Unit) =
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
            pathBuilder = block,
        )

    val Pause: ImageVector by lazy {
        icon("Pause") {
            path(fill = SolidColor(Color.Black)) {
                moveTo(6f, 5f); lineTo(10f, 5f); lineTo(10f, 19f); lineTo(6f, 19f); close()
                moveTo(14f, 5f); lineTo(18f, 5f); lineTo(18f, 19f); lineTo(14f, 19f); close()
            }
        }
    }

    val WaterDrop: ImageVector by lazy {
        icon("WaterDrop") {
            path(fill = SolidColor(Color.Black)) {
                moveTo(12f, 2.5f)
                curveTo(12f, 2.5f, 5f, 10f, 5f, 14.5f)
                curveTo(5f, 18.4f, 8.1f, 21.5f, 12f, 21.5f)
                curveTo(15.9f, 21.5f, 19f, 18.4f, 19f, 14.5f)
                curveTo(19f, 10f, 12f, 2.5f, 12f, 2.5f)
                close()
            }
        }
    }

    /** Loa + sóng âm. */
    val Speaker: ImageVector by lazy {
        icon("Speaker") {
            path(fill = SolidColor(Color.Black)) {
                moveTo(3f, 9f); lineTo(7f, 9f); lineTo(12f, 4f); lineTo(12f, 20f); lineTo(7f, 15f); lineTo(3f, 15f); close()
            }
            stroked {
                moveTo(15.5f, 8.5f); curveTo(17.5f, 10.5f, 17.5f, 13.5f, 15.5f, 15.5f)
                moveTo(18.5f, 5.5f); curveTo(22.2f, 9.2f, 22.2f, 14.8f, 18.5f, 18.5f)
            }
        }
    }

    /** Sách — icon hướng dẫn (guide). */
    val Guide: ImageVector by lazy {
        icon("Guide") {
            stroked {
                moveTo(6f, 3f); lineTo(18f, 3f); lineTo(18f, 21f); lineTo(6f, 21f); close()
                moveTo(9f, 7f); lineTo(15f, 7f)
                moveTo(9f, 11f); lineTo(15f, 11f)
                moveTo(9f, 15f); lineTo(13f, 15f)
            }
        }
    }

    /** Quả địa cầu — Language. */
    val Globe: ImageVector by lazy {
        icon("Globe") {
            stroked {
                moveTo(21f, 12f)
                arcTo(9f, 9f, 0f, isMoreThanHalf = true, isPositiveArc = true, x1 = 3f, y1 = 12f)
                arcTo(9f, 9f, 0f, isMoreThanHalf = true, isPositiveArc = true, x1 = 21f, y1 = 12f)
                moveTo(3f, 12f); lineTo(21f, 12f)
                moveTo(12f, 3f)
                curveTo(9.5f, 6f, 9.5f, 18f, 12f, 21f)
                moveTo(12f, 3f)
                curveTo(14.5f, 6f, 14.5f, 18f, 12f, 21f)
            }
        }
    }

    /** Khiên — Privacy policy. */
    val Shield: ImageVector by lazy {
        icon("Shield") {
            stroked {
                moveTo(12f, 3f); lineTo(19f, 6f); lineTo(19f, 11f)
                curveTo(19f, 15.5f, 16f, 19f, 12f, 21f)
                curveTo(8f, 19f, 5f, 15.5f, 5f, 11f)
                lineTo(5f, 6f); close()
                moveTo(9f, 12f); lineTo(11f, 14f); lineTo(15f, 10f)
            }
        }
    }

    /** Lấp lánh — icon "đang làm sạch". */
    val Sparkle: ImageVector by lazy {
        icon("Sparkle") {
            path(fill = SolidColor(Color.Black)) {
                moveTo(10f, 3f); lineTo(12f, 9f); lineTo(18f, 11f); lineTo(12f, 13f)
                lineTo(10f, 19f); lineTo(8f, 13f); lineTo(2f, 11f); lineTo(8f, 9f); close()
                moveTo(18f, 2f); lineTo(19f, 5f); lineTo(22f, 6f); lineTo(19f, 7f)
                lineTo(18f, 10f); lineTo(17f, 7f); lineTo(14f, 6f); lineTo(17f, 5f); close()
            }
        }
    }

    /** Đám bụi — Dust. */
    val Dust: ImageVector by lazy {
        icon("Dust") {
            path(fill = SolidColor(Color.Black)) {
                moveTo(7f, 18f)
                arcTo(4f, 4f, 0f, isMoreThanHalf = false, isPositiveArc = true, x1 = 7.5f, y1 = 10.03f)
                arcTo(5f, 5f, 0f, isMoreThanHalf = false, isPositiveArc = true, x1 = 16.9f, y1 = 10.5f)
                arcTo(3.75f, 3.75f, 0f, isMoreThanHalf = false, isPositiveArc = true, x1 = 17f, y1 = 18f)
                close()
            }
            path(fill = SolidColor(Color.Black)) {
                moveTo(5f, 21f); lineTo(6.5f, 21f); lineTo(6.5f, 22.5f); lineTo(5f, 22.5f); close()
                moveTo(11f, 20.5f); lineTo(12.5f, 20.5f); lineTo(12.5f, 22f); lineTo(11f, 22f); close()
                moveTo(17f, 21f); lineTo(18.5f, 21f); lineTo(18.5f, 22.5f); lineTo(17f, 22.5f); close()
            }
        }
    }

    /** Sóng âm — dùng trang trí card Test sound. */
    val Wave: ImageVector by lazy {
        icon("Wave") {
            stroked {
                moveTo(2f, 12f); lineTo(5f, 12f); lineTo(7f, 6f); lineTo(10f, 18f)
                lineTo(13f, 4f); lineTo(16f, 20f); lineTo(18f, 9f); lineTo(20f, 12f); lineTo(22f, 12f)
            }
        }
    }
}
