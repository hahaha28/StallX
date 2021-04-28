package `fun`.inaction.network.bean

data class HistoryResult(
    val historyData:MutableList<History>
):Result()

data class History(
    val parkID:String,
    val parkName:String,
    val timestamp:Long
)
