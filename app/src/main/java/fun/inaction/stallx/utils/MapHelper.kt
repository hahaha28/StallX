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

    private var curLocation:BDLocation? = null

    private val locationListener: (BDLocation)->Unit = {
        // 第一次定位成功，移动到当前位置
        if(curLocation == null){
            curLocation = it
            toCurPosition()
        }
        // 显示当前位置指针
        val locData = MyLocationData.Builder()
            .accuracy(it.radius)
            .direction(it.direction)
            .latitude(it.latitude)
            .longitude(it.longitude)
            .build()
        mapView.map.setMyLocationData(locData)

        locationChangeListener(it)
        curLocation = it
    }

    var locationChangeListener: (BDLocation)->Unit = {}


    init {
        // 监听Activity声明周期
        lifecycle.addObserver(this)
        // 开启地图定位图层
        map.isMyLocationEnabled = true
        // 监听定位
        LocationHelper.addLocationListener(locationListener)


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
     * 检查GPS是否打开
     */
    fun checkGPS():Boolean{
        val locationManager = MyApplication.context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    /**
     * 获取当前位置
     */
    fun getCurPosition(): BDLocation = if (curLocation == null) {
        BDLocation()
    } else {
        curLocation!!
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
//        onDestroy()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        mapView.onDestroy()
        LocationHelper.removeLocationListener(locationListener)
        map.isMyLocationEnabled = false
        Log.i(TAG, "onDestroy: ")
    }

}