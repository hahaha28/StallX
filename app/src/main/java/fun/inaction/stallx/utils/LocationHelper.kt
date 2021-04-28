package `fun`.inaction.stallx.utils

import `fun`.inaction.stallx.MyApplication
import android.util.Log
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption

/**
 * 定位 帮助类
 */
object LocationHelper {

    private const val TAG = "LocationHelper"

    private lateinit var locationClient: LocationClient

    /**
     * 当前位置
     */
    private var _curLocation: BDLocation? = null

    val curLocation: BDLocation?
        get() = _curLocation

    private val listenerList = mutableListOf<(BDLocation) -> Unit>()

    /**
     * 位置监听
     */
    private val locationListener = object : BDAbstractLocationListener() {
        override fun onReceiveLocation(location: BDLocation?) {
            location?.let { location ->

                logLocationInfo(location.locType)
                when (location.locType) {
                    BDLocation.TypeGpsLocation,
                    BDLocation.TypeNetWorkLocation,
                    BDLocation.TypeOffLineLocation,
                    -> {
                        // 定位成功
                        Log.v(
                            TAG,
                            "onReceiveLocation: 获取到位置信息" +
                                    "（${location.longitude}，${location.latitude}）" +
                                    "，精度：${location.radius}"
                        )
                        _curLocation = location
                        if(location.radius != 0f) {
                            listenerList.forEach {
                                it(location)
                            }
                        }


                    }
                    else -> {
                        // 定位失败

                    }
                }

            }
            if (location == null) {
                Log.e(TAG, "onReceiveLocation: location = null")
            }
        }
    }

    /**
     * 启动连续定位
     */
    fun start() {
        locationClient = LocationClient(MyApplication.context)
        locationClient.registerLocationListener(locationListener)
        locationClient.locOption = LocationClientOption().apply {
            // 可选，设置定位模式，默认高精度 LocationMode.Hight_Accuracy：高精度；
            setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
            // 设置返回经纬度坐标类型
            setCoorType("bd09ll")
            // 设置定位时间间隔
            setScanSpan(1000)
//            isOnceLocation = true
            // 设置使用gps
            isOpenGps = true
            // 设置需要获得地址信息
            setIsNeedAddress(true)
            // 设置GPS有效时1s一次输出结果
            isLocationNotify = true
            // 设置需要POI信息
//            setIsNeedLocationPoiList(true)
        }
        locationClient.start()
    }

    /**
     * 添加定位监听
     */
    fun addLocationListener(listener: (BDLocation) -> Unit) {
        listenerList.add(listener)
    }

    /**
     * 移除定位监听
     */
    fun removeLocationListener(listener: (BDLocation) -> Unit) {
        listenerList.remove(listener)
    }

    fun stop() {
        locationClient.stop()
    }

    /**
     * 打印定位情况
     */
    private fun logLocationInfo(locType: Int) {
        if (locType == BDLocation.TypeGpsLocation) { // GPS定位结果
            Log.v(TAG, "onReceiveLocation: GPS定位成功")
        } else if (locType == BDLocation.TypeNetWorkLocation) { // 网络定位结果
            Log.v(TAG, "onReceiveLocation: 网络定位成功")
        } else if (locType == BDLocation.TypeOffLineLocation) { // 离线定位结果
            Log.v(TAG, "onReceiveLocation: 离线定位成功")
        } else if (locType == BDLocation.TypeServerError) {
            Log.e(TAG, "onReceiveLocation: 服务端网络定位失败")
        } else if (locType == BDLocation.TypeNetWorkException) {
            Log.e(TAG, "onReceiveLocation: 网络不通导致定位失败，请检查网络是否通畅")
        } else if (locType == BDLocation.TypeCriteriaException) {
            Log.e(
                TAG,
                "onReceiveLocation: 无法获取有效定位依据导致定位失败，" +
                        "一般是由于手机的原因，处于飞行模式下一般会造成这种结果，" +
                        "可以试着重启手机",
            )
        }
    }

}