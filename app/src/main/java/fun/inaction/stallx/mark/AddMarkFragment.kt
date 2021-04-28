package `fun`.inaction.stallx.mark

import `fun`.inaction.network.OkCallback
import `fun`.inaction.network.ServiceCreator
import `fun`.inaction.network.bean.Mark
import `fun`.inaction.network.bean.Result
import `fun`.inaction.network.call
import `fun`.inaction.network.service.MarkService
import `fun`.inaction.stallx.R
import `fun`.inaction.stallx.databinding.FragmentAddMarkBinding
import `fun`.inaction.stallx.utils.DateUtil
import `fun`.inaction.stallx.utils.MapHelper
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.baidu.location.BDLocation
import com.hi.dhl.binding.viewbind
import com.jeremyliao.liveeventbus.LiveEventBus
import com.kongzue.dialog.util.DialogSettings
import com.kongzue.dialog.v3.InputDialog
import com.kongzue.dialog.v3.TipDialog
import com.kongzue.dialog.v3.WaitDialog

class AddMarkFragment:Fragment(R.layout.fragment_add_mark) {

    private val binding by viewbind<FragmentAddMarkBinding>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val mapHelper = MapHelper(binding.map,lifecycle)
        binding.map.showZoomControls(false)

        // 当前位置按钮点击事件
        binding.curPositionBtn.setOnClickListener { mapHelper.toCurPosition() }

        // 位置监听
        mapHelper.locationChangeListener = {
            binding.longitude.text = it.longitude.toString()
            binding.altitude.text = it.altitude.toString()
            binding.radius.text = it.radius.toString()
        }

        // ”标记“按钮点击事件
        binding.markButton.setOnClickListener {
            InputDialog.show(activity as AppCompatActivity,"提示","请为标记输入一个备注名","确定","取消")
                .setInputText(DateUtil.getCurrentTime())
                .setOnOkButtonClickListener { baseDialog, v, inputStr ->
                    baseDialog.doDismiss()
                    WaitDialog.show(activity as AppCompatActivity,"")
                    val location = mapHelper.getCurPosition()
                    val mark = Mark(inputStr,System.currentTimeMillis(),location.longitude,location.latitude,location.radius)
                    addMark(mark)
                    true
                }



        }
    }

    private fun addMark(mark:Mark){
        ServiceCreator.create<MarkService>().addMark(mark).enqueue(object : OkCallback<Result>() {

            override fun onSuccess(result: Result) {
                LiveEventBus.get("NewMark").post(mark)
                WaitDialog.dismiss()
                TipDialog.show(activity as AppCompatActivity,"标记成功",TipDialog.TYPE.SUCCESS)
                    .setTipTime(1000)
                findNavController().popBackStack()
            }

            override fun onFailureFinally() {
                super.onFailureFinally()
                WaitDialog.dismiss()
            }


        })
    }




}