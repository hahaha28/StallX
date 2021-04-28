package `fun`.inaction.network.service

import `fun`.inaction.network.bean.CollectionResult
import `fun`.inaction.network.bean.IsCollection
import `fun`.inaction.network.bean.Result
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface CollectionService {

    @GET("/collection/get")
    fun getCollections(): Call<CollectionResult>

    @POST("/collection/add")
    fun addCollection(@Query("parkID")parkID:String):Call<Result>

    @POST("/collection/delete")
    fun deleteCollection(@Query("collectionID")id:String):Call<Result>

    @POST("/collection/delete_indirect")
    fun deleteCollectionIndirect(@Query("parkID")id:String):Call<Result>

    @POST("/collection/judge")
    fun isCollected(@Query("parkID")id:String):Call<IsCollection>

}