package `fun`.inaction.network.bean

import android.os.Parcelable
import androidx.versionedparcelable.ParcelField
import kotlinx.parcelize.Parcelize

@Parcelize
data class Park(
    var _id:String? = null,
    var name:String,
    var longitude:Double,
    var latitude:Double,
    var city:String,
    var totalStallNum:Int = 0,
    var curStallNum:Int = 0,
    var type:Int,
    var isCharged:Int,
    var chargingRules:String,
    var imgUrl:String? = null,
    var rate:Float = 0f,
    var commentsNum:Int = 0,
    var comments:MutableList<Comment>? = null
) : Parcelable

/**
 * 停车场评论
 */
@Parcelize
data class Comment(
    var userID:String,
    var userName:String,
    var comment:String,
    var timestamp:Long,
    var rate:Float
) : Parcelable
