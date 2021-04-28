package `fun`.inaction.network.service

import `fun`.inaction.network.bean.Result
import `fun`.inaction.network.bean.user.LoginResult
import retrofit2.Call
import retrofit2.http.*

interface UserService {

    /**
     * 获取验证码
     */
    @GET("/captcha/send")
    fun getCaptcha(@Query("tel") tel: String): Call<Result>

    /**
     * 登录或注册
     */
    @FormUrlEncoded
    @POST("/user/login")
    fun login(
        @Field("tel") tel: String,
        @Field("verifyCode") verifyCode: String
    )
            : Call<LoginResult>

}