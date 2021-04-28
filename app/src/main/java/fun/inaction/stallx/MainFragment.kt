package `fun`.inaction.stallx

import `fun`.inaction.buttongroup.ButtonGroup
import `fun`.inaction.stallx.databinding.FragmentMainBinding
import `fun`.inaction.stallx.utils.*
import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.hi.dhl.binding.viewbind
import com.permissionx.guolindev.PermissionX


class MainFragment : Fragment(R.layout.fragment_main) {

    val binding by viewbind<FragmentMainBinding>()

    private lateinit var mapHelper: MapHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapHelper = MapHelper(binding.mapView, viewLifecycleOwner.lifecycle)

        binding.mapView.showZoomControls(false)
        requestPermission {
            LocationHelper.start()
        }

        // 初始化按钮组
        initButtonGroup()

        // 头像点击事件
        binding.avatarImage.setOnClickListener {
            UserBaseUtil.logout()

        }

        // ”输入目的地“ 的点击事件
        binding.inputTargetButton.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToSearchFragment(
                mapHelper.getCurPosition()?.city,
                "北京"
            )
            it.findNavController().navigate(action)
        }

        // “当前位置”按钮的点击事件
        binding.curPositionBtn.setOnClickListener {
            mapHelper.toCurPosition()
        }

    }

    /**
     * 初始化底部按钮组
     */
    private fun initButtonGroup() {
        binding.buttonGroup.setData(
            listOf(
                ButtonGroup.Item(R.drawable.ic_collect, "收藏"),
                ButtonGroup.Item(R.drawable.ic_mark, "标记"),
                ButtonGroup.Item(R.drawable.ic_history, "历史"),
                ButtonGroup.Item(R.drawable.ic_near, "附近"),
                ButtonGroup.Item(R.drawable.ic_report, "上报"),

                )
        )
        binding.buttonGroup.setOnItemClickListener {
            when(it){
                0 -> findNavController().navigate(R.id.action_mainFragment_to_collectionListFragment)
                1 -> findNavController().navigate(R.id.action_mainFragment_to_markListFragment)
                2 -> findNavController().navigate(R.id.action_mainFragment_to_parkHistoryFragment)
                3 -> {
                    LocationHelper.curLocation?.let {
                        findNavController().navigate(MainFragmentDirections.actionMainFragmentToParkSearchResultFragment(
                            it.city,it.longitude.toString(),it.latitude.toString()
                        ))
                    }
                }
                4 -> findNavController().navigate(R.id.action_mainFragment_to_addParkFragment)
            }
        }
    }

    /**
     * 权限申请
     */
    private fun requestPermission(successToDo: () -> Unit) {
        PermissionX.init(this)
            .permissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            .explainReasonBeforeRequest()
            .onExplainRequestReason { scope, deniedList ->
                val message = "本应用需要您同意以下权限才能正常使用"
                scope.showRequestReasonDialog(deniedList, message, "确定", "取消")
            }
            .onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(
                    deniedList,
                    "您需要去应用程序设置当中手动开启权限，否则无法正常使用",
                    "我已明白",
                    "取消"
                )
            }
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    successToDo()
                }
            }
    }

}