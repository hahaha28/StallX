package `fun`.inaction.stallx.park

import `fun`.inaction.stallx.R
import `fun`.inaction.stallx.databinding.FragmentMapPlaceChooseBinding
import `fun`.inaction.stallx.utils.LocationHelper
import `fun`.inaction.stallx.utils.logi
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.baidu.location.BDLocation
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.hi.dhl.binding.viewbind
import com.jeremyliao.liveeventbus.LiveEventBus

class MapPlaceChooseFragment:Fragment(R.layout.fragment_map_place_choose),
    BaiduMap.OnMapStatusChangeListener {

    private val TAG = "MapPlaceChooseFragment"

    private val binding by viewbind<FragmentMapPlaceChooseBinding>()

    private lateinit var mapView:TextureMapView
    private lateinit var map:BaiduMap
    private var centerPoint = LatLng(39.963175, 116.400244)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = binding.map
        map = mapView.map

        LocationHelper.curLocation?.let {
            centerPoint = LatLng(it.latitude,it.longitude)
        }

        mapView.showZoomControls(false)

        val mapStatusUpdate =MapStatusUpdateFactory.newLatLngZoom(centerPoint, 18f)
        map.setMapStatus(mapStatusUpdate)
        map.setOnMapStatusChangeListener(this)
        map.setOnMapLoadedCallback {
            createCenterMarker()
//            reverseRequest(mCenter)
        }

        // 当前位置的点击事件
        binding.curPositionBtn.setOnClickListener {
            LocationHelper.curLocation?.let {
                centerPoint = LatLng(it.latitude,it.longitude)
                val mapStatusUpdate = MapStatusUpdateFactory.newLatLngZoom(centerPoint, 18f)
                map.animateMapStatus(mapStatusUpdate)
            }
        }

        // 完成按钮的点击事件
        binding.okBtn.setOnClickListener {
            LiveEventBus.get("choosePosition")
                .post(centerPoint)
            findNavController().popBackStack()
        }

    }

    /**
     * 创建地图中心点marker
     */
    private fun createCenterMarker() {
        val projection: Projection = map.getProjection() ?: return
        val point = projection.toScreenLocation(centerPoint)
        val bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.ic_map_point_2)
            ?: return
        val markerOptions = MarkerOptions()
            .position(centerPoint)
            .icon(bitmapDescriptor)
            .flat(false)
            .fixedScreenPosition(point)
        map.addOverlay(markerOptions)
        bitmapDescriptor.recycle()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDestroy()
    }

    override fun onMapStatusChangeStart(p0: MapStatus?) {
    }

    override fun onMapStatusChangeStart(p0: MapStatus?, p1: Int) {
    }

    override fun onMapStatusChange(p0: MapStatus?) {
    }

    override fun onMapStatusChangeFinish(p0: MapStatus?) {
        p0?.let {
            centerPoint = it.target
            logi(TAG,"当前选定位置为：${centerPoint.latitude},${centerPoint.longitude}")
        }
    }
}