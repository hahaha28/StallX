package `fun`.inaction.network.bean

import java.io.Serializable

data class Mark(
    var name:String,
    var time: Long,
    var longitude: Double,
    var latitude: Double,
    var radius: Float
):Serializable {

}

data class MarkHistoryResult(
    var markList: MutableList<Mark>
):Result()