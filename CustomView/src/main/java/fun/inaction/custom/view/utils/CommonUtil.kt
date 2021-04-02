package `fun`.inaction.custom.view.utils

import android.content.Context
import android.util.TypedValue

/**
 * 把 dp 转换为 px
 */
fun dp2px(dp: Float, context: Context): Float = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    dp,
    context.resources.displayMetrics
)
