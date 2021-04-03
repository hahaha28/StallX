package `fun`.inaction.stallx.utils

import `fun`.inaction.stallx.MyApplication
import android.content.Context
import android.location.LocationManager
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng

class MapHelper(
    private val mapView: TextureMapView,
    lifecycle: Lifecycle
) : LifecycleObserver {

    private val TAG = "MapHelper"

    /**
     * 地图控制对象
     */
    val map: BaiduMap
        get() = mapView.map

    /**
     * 当前位置
     */
    private var curLocation: BDLocation? = null

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
                        Log.v(TAG,
                            "onReceiveLocation: 获取到位置信息" +
                                    "（${location.longitude}，${location.latitude}）" +
                                    "，精度：${location.radius}"
                        )
                        // 显示当前位置指针
                        val locData = MyLocationData.Builder()
                            .accuracy(location.radius)
                            .direction(location.direction)
                            .latitude(location.latitude)
                            .longitude(location.longitude)
                            .build()
                        mapView.map.setMyLocationData(locData)

                        // 如果是第一次获取位置，就移动到当前位置
                        if (curLocation == null) {
                            animateToPosition(location.latitude, location.longitude)
                        }

                        // 记录定位
                        curLocation = location
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
     * 定位客户端
     */
    private val locationClient = LocationClient(MyApplication.context)


    init {
        // 监听Activity声明周期
        lifecycle.addObserver(this)
        // 开启地图定位图层
        map.isMyLocationEnabled = true
        // 定位初始化
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

    }

    /**
     * 开启定位（连续定位）
     */
    fun startLocation() {
        // 开启定位
        locationClient.start()
    }

    /**
     * 停止定位
     */
    fun stopLocation() {
        locationClient.stop()
    }

    /**
     * 移动地图到当前位置
     * @param zoom 地图缩放大小，默认为18
     * @param waitForLocation 是否等待定位结果，如果为true，则等定位查出来再移动
     */
    fun toCurPosition(zoom: Float = 18f, waitForLocation: Boolean = false) {
        if (waitForLocation) {
            curLocation = null
            return
        }
        curLocation?.let {
            animateToPosition(it.latitude, it.longitude, zoom)
            Log.i(TAG, "toCurPosition： (${it.longitude}，${it.latitude})")
        }
        if (curLocation == null) {
            Log.i(TAG, "toCurPosition: 无位置信息")
        }
    }

    /**
     * 移动地图显示中心点
     * @param latitude 纬度
     * @param longitude 经度
     * @param zoom 地图的放大倍数，默认18
     */
    fun animateToPosition(latitude: Double, longitude: Double, zoom: Float = 18f) {
        Log.i(TAG, "animateToPosition: 移动地图到（${longitude}，${latitude}）")
        val latLng = LatLng(latitude, longitude)
        val mapStatusUpdate = MapStatusUpdateFactory.newLatLngZoom(latLng, zoom)
        map.animateMapStatus(mapStatusUpdate)
    }

    /**
     * 获取当前位置
     */
    fun getCurPosition(): BDLocation = if (curLocation == null) {
        BDLocation()
    } else {
        curLocation!!
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

    /**
     * 检查GPS是否打开
     */
    fun checkGPS():Boolean{
        val locationManager = MyApplication.context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }



    // Activity或Fragment 生命周期监听函数

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        mapView.onResume()
        Log.i(TAG, "onResume: ")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        mapView.onPause()
        Log.i(TAG, "onPause: ")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        logi(TAG,"onStop")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        mapView.onDestroy()
        locationClient.stop()
        map.isMyLocationEnabled = false
        Log.i(TAG, "onDestroy: ")
    }

}