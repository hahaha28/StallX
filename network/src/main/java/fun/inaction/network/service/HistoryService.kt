package `fun`.inaction.network.service

import `fun`.inaction.network.bean.HistoryResult
import `fun`.inaction.network.bean.Result
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface HistoryService {

    @POST("/history/add")
    fun addHistory(@Query("parkID")parkID:String,@Query("parkName")parkName:String):Call<Result>

    @GET("/history/get")
    fun getHistory():Call<HistoryResult>

}