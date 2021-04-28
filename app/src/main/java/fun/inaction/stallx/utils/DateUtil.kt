package `fun`.inaction.stallx.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtil {

    fun getCurrentTime():String{
        val sdf = SimpleDateFormat("MM月dd日 HH时mm分")
        return sdf.format(Date())
    }

}