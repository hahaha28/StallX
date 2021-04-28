package `fun`.inaction.network.bean

data class CollectionResult(
    val collectionData:MutableList<Collection>
):Result()

data class Collection(
    val _id:String,
    val userID:String,
    val parkID:String,
    val parkName:String,
    val timestamp:Long
)
