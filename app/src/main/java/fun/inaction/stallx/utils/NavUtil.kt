package `fun`.inaction.stallx.utils

import `fun`.inaction.stallx.nav.CarNavActivity
import `fun`.inaction.stallx.nav.WalkNavActivity
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.walknavi.WalkNavigateHelper
import com.baidu.mapapi.walknavi.adapter.IWEngineInitListener
import com.baidu.mapapi.walknavi.adapter.IWRoutePlanListener
import com.baidu.mapapi.walknavi.model.WalkRoutePlanError
import com.baidu.mapapi.walknavi.params.WalkNaviLaunchParam
import com.baidu.mapapi.walknavi.params.WalkRouteNodeInfo

object NavUtil {
    private const val TAG = "NavUtil"

    /**
     * 开始步行导航
     */
    fun startWalkNav(context:Activity,from: LatLng, to: LatLng,onSuccess:()->Unit,onError:(String)->Unit) {
        // 初始化引擎
        WalkNavigateHelper.getInstance().initNaviEngine(context, object : IWEngineInitListener {
            override fun engineInitSuccess() {
                val startNode = WalkRouteNodeInfo()
                startNode.location = from
                val endNode = WalkRouteNodeInfo()
                endNode.location = to
                val mParam = WalkNaviLaunchParam().startNodeInfo(startNode).endNodeInfo(endNode)

                WalkNavigateHelper.getInstance().routePlanWithRouteNode(mParam
                    ,object : IWRoutePlanListener {
                        override fun onRoutePlanStart() {
                            logi(TAG,"步行导航开始算路")
                        }

                        override fun onRoutePlanSuccess() {
                            logi(TAG,"步行导航，算路成功")
                            onSuccess()
                            val intent = Intent(context,WalkNavActivity::class.java)
                            context.startActivity(intent)
                        }

                        override fun onRoutePlanFail(p0: WalkRoutePlanError?) {
                            val reason:String = when(p0){
                                WalkRoutePlanError.DISTANCE_MORE_THAN_50KM -> "距离大于50千米"
                                WalkRoutePlanError.INVAILD_PERMISSION -> "没有权限"
                                WalkRoutePlanError.FORWARD_AK_ERROR -> "ak错误"
                                WalkRoutePlanError.IS_NOT_SUPPORT_INDOOR_NAVI -> "不支持室内导航"
                                WalkRoutePlanError.PARSE_FAIL -> "解析错误"
                                WalkRoutePlanError.NET_ERR -> "网络错误"
                                WalkRoutePlanError.SERVER_UNUSUAL -> "服务不可用"
                                WalkRoutePlanError.DISTANCE_LESS_THAN_30M -> "距离小于30米"
                                else -> "未知错误"
                            }
                            loge(TAG,"步行导航，算路失败,${p0?.name}")
                            onError("算路失败！\n $reason")
                        }
                    })
            }

            override fun engineInitFail() {
                onError("步行导航引擎初始化失败")
            }
        })

    }

    /**
     * 开始驾车导航
     */
    fun startCarNav(context:Activity,from:LatLng,to:LatLng){
        CarNavActivity.start(context,from,to)
    }

}