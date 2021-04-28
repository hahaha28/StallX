package `fun`.inaction.network.service

import `fun`.inaction.network.bean.Mark
import `fun`.inaction.network.bean.MarkHistoryResult
import `fun`.inaction.network.bean.Result
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MarkService {

    /**
     * 添加标记
     */
    @POST("/mark/add")
    fun addMark(@Body mark:Mark):Call<Result>

    /**
     * 获取历史标记
     */
    @GET("/mark/get/history")
    fun getMarkHistory():Call<MarkHistoryResult>

}