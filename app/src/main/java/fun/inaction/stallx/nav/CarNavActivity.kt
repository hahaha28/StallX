package `fun`.inaction.stallx.nav

import `fun`.inaction.stallx.R
import `fun`.inaction.stallx.adapters.RoutePlanAdapter
import `fun`.inaction.stallx.custom.RouteResultWindow
import `fun`.inaction.stallx.databinding.ActivityCarNavBinding
import `fun`.inaction.stallx.utils.hide
import `fun`.inaction.stallx.utils.loge
import `fun`.inaction.stallx.utils.logi
import `fun`.inaction.stallx.utils.show
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.baidu.mapapi.model.LatLng
import com.baidu.navisdk.adapter.BNRoutePlanNode
import com.baidu.navisdk.adapter.BaiduNaviManagerFactory
import com.baidu.navisdk.adapter.IBNRoutePlanManager
import com.hi.dhl.binding.viewbind
import com.kongzue.dialog.v3.TipDialog
import com.kongzue.dialog.v3.WaitDialog

class CarNavActivity : AppCompatActivity() {

    private val TAG = "CarGuideActivity"

    private val binding by viewbind<ActivityCarNavBinding>()

    private val routeResult = mutableListOf<RouteResultWindow.RoutePlan>()
    private val adapter = RoutePlanAdapter()

    /**
     * 起点
     */
    private lateinit var startLatLng: LatLng

    /**
     * 终点
     */
    private lateinit var endLatLng: LatLng

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_nav)

        startLatLng = intent.getParcelableExtra("startLatLng")!!
        endLatLng = intent.getParcelableExtra("endLatLng")!!

        WaitDialog.show(this,"正在算路中")

        BaiduNaviManagerFactory.getMapManager().attach(binding.mapContent)

        BaiduNaviManagerFactory.getRouteGuideManager().setNaviListener(object : NaviListener() {
            override fun onNaviGuideEnd() {
                Log.e("tag","end!!!")
            }
        })

    }

    private fun routePlan() {
        val startNode = BNRoutePlanNode.Builder()
            .latitude(startLatLng.latitude)
            .longitude(startLatLng.longitude)
            .build()
        val endNode = BNRoutePlanNode.Builder()
            .latitude(endLatLng.latitude)
            .longitude(endLatLng.longitude)
            .build()
        BaiduNaviManagerFactory.getRoutePlanManager().routePlan(listOf(startNode, endNode),
            IBNRoutePlanManager.RoutePlanPreference.ROUTE_PLAN_PREFERENCE_DEFAULT,
            null,
            object : Handler(Looper.myLooper()!!) {
                override fun handleMessage(msg: Message) {
                    WaitDialog.dismiss()
                    when (msg.what) {
                        IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_START -> {
                            logi(TAG, "算路开始！")
                        }
                        IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_SUCCESS -> {
                            logi(TAG, "算路成功")
                            val fragment = CarRouteResultFragment()
                            val transaction = supportFragmentManager.beginTransaction()
                            transaction.add(R.id.frameLayout, fragment)
//                            transaction.addToBackStack(null)
                            transaction.commit()

                            showRouteResultTab()
                        }
                        IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_FAILED -> {
                            loge(TAG, "算路失败")
                            TipDialog.show(this@CarNavActivity,"算路失败！",TipDialog.TYPE.ERROR)
                                .setTipTime(2000)
                                .setOnDismissListener {
                                    this@CarNavActivity.finish()
                                }

                        }
                        else -> {
                        }
                    }
                }
            })
    }

    private fun showRouteResultTab() {
        val routePlanInfo = BaiduNaviManagerFactory.getRouteResultManager().routePlanInfo

        routePlanInfo.routeTabInfos.forEach {
            routeResult.add(
                RouteResultWindow.RoutePlan(
                    it.pusLabelName,
                    it.passTime,
                    it.length,
                    it.lights
                )
            )
        }
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(this, routeResult.size)
        adapter.setNewInstance(routeResult)
        adapter.setOnItemClickListener { adapter, view, position ->
            this.adapter.selectIndex = position
            BaiduNaviManagerFactory.getRouteGuideManager().selectRoute(position)
        }
        binding.startNavBtn.setOnClickListener {
            BaiduNaviManagerFactory.getRouteResultManager().startNavi()
            val fragment = CarNavFragment()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.add(R.id.frameLayout, fragment)
            transaction.commit()
            hideRouteResultTab()
        }

        binding.routePlanTabLayout.show()
    }

    private fun hideRouteResultTab() {
        binding.routePlanTabLayout.hide()
    }

    override fun onResume() {
        super.onResume()
        if (BaiduNaviManagerFactory.getBaiduNaviManager().isInited) {
            BaiduNaviManagerFactory.getMapManager().onResume()
            // onResume 后，重新规划
            routePlan()
        }
    }

    override fun onPause() {
        super.onPause()
        BaiduNaviManagerFactory.getMapManager().onPause()

    }

    override fun onDestroy() {
        super.onDestroy()
        BaiduNaviManagerFactory.getMapManager().detach(binding.mapContent)

    }


    companion object {
        fun start(context: Activity, startLatLng: LatLng, endLatLng: LatLng) {
            val intent = Intent(context, CarNavActivity::class.java)
            intent.putExtra("startLatLng", startLatLng)
            intent.putExtra("endLatLng", endLatLng)
            context.startActivity(intent)
        }
    }

}