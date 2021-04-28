package `fun`.inaction.stallx.park

import `fun`.inaction.network.OkCallback
import `fun`.inaction.network.ServiceCreator
import `fun`.inaction.network.bean.Park
import `fun`.inaction.network.bean.Result
import `fun`.inaction.network.call
import `fun`.inaction.network.service.ParkService
import `fun`.inaction.stallx.R
import `fun`.inaction.stallx.databinding.FragmentParkAddBinding
import `fun`.inaction.stallx.utils.*
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.baidu.mapapi.model.LatLng
import com.hi.dhl.binding.viewbind
import com.jeremyliao.liveeventbus.LiveEventBus
import com.kongzue.dialog.v3.BottomMenu
import com.kongzue.dialog.v3.MessageDialog
import com.kongzue.dialog.v3.TipDialog
import com.kongzue.dialog.v3.WaitDialog
import com.qw.photo.CoCo
import com.qw.photo.callback.CoCoAdapter
import com.qw.photo.pojo.PickResult
import com.qw.photo.pojo.TakeResult
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.BufferedSink
import okio.Okio
import retrofit2.Call
import java.io.File
import java.net.URLConnection

class AddParkFragment:Fragment(R.layout.fragment_park_add) {

    private val TAG = "AddParkFragment"

    private val binding by viewbind<FragmentParkAddBinding>()

    /**
     * 选择的位置
     */
    private var choosePosition:LatLng? = null

    /**
     * 停车场照片图
     */
    private var parkPhotoRequestBody:RequestBody? = null

    /**
     * 停车场类型
     */
    private var parkType = 0

    /**
     * 收费类型
     */
    private var isCharged = -1


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 设置停车场类型选项框
        val typeItems = listOf("地下停车场", "地上停车场", "路边停车位", "未划线停车位")
        val typeAdapter = ArrayAdapter(requireActivity(), R.layout.list_item, typeItems)
        binding.parkTypeBtn.setAdapter(typeAdapter)

        // 设置收费类型选项框
        binding.chargeTypeBtn.setAdapter(
            ArrayAdapter(
                requireContext(), R.layout.list_item, listOf("未知", "不收费", "收费")
            )
        )

        // 选择位置按钮的点击事件
        binding.choosePositionBtn.setOnClickListener {
            findNavController().navigate(R.id.action_addParkFragment_to_mapPlaceChooseFragment)
        }

        // 监听位置选择
        LiveEventBus.get("choosePosition", LatLng::class.java)
            .observeSticky(viewLifecycleOwner) {
                choosePosition = it
                binding.okChip.show()
                binding.noChooseChip.hide()
            }

        // 选择照片按钮点击事件
        binding.choosePhotoBtn.setOnClickListener {
            BottomMenu.show(requireActivity() as AppCompatActivity, listOf("拍照", "从相册选择")){ _, index->
                when(index){
                    0 -> {
                        takePhoto()
                    }
                    1 -> {
                        chooseFromAlbum()
                    }
                }
            }
        }

        // 停车场类型 的点击事件
        binding.parkTypeBtn.setOnItemClickListener { _, _, position, _ ->
            parkType = position+1
        }

        // 收费类型 的点击事件
        binding.chargeTypeBtn.setOnItemClickListener { _, _, position, _ ->
            isCharged = position
        }

        // 完成按钮的点击事件
        binding.okBtn.setOnClickListener {
            if(checkFinish()){
                WaitDialog.show(appCompatActivity(),"请稍后...")
                val park = Park(
                    name = binding.parkName.text.toString(),
                    longitude = choosePosition!!.longitude,
                    latitude = choosePosition!!.latitude,
                    city = LocationHelper.curLocation!!.city,
                    totalStallNum = binding.totalStallNum.text.toString().toInt(),
                    type = parkType,
                    isCharged = isCharged,
                    chargingRules = binding.chargingRules.text.toString()
                )
                addParkRequest(park)
            }
        }
    }

    private fun addParkRequest(park:Park){
        val imagePart = MultipartBody.Part.createFormData("image","fileName",parkPhotoRequestBody!!)
        ServiceCreator.create<ParkService>().addPark(park,imagePart).enqueue(object :
            OkCallback<Result>() {
            override fun onSuccess(result: Result) {
                super.onSuccess(result)
                TipDialog.show(requireActivity() as AppCompatActivity,"成功",TipDialog.TYPE.SUCCESS)
                    .setTipTime(1000)
                    .setOnDismissListener {
                        findNavController().popBackStack()
                    }
            }

            override fun onFailure(call: Call<Result>, t: Throwable) {
                super.onFailure(call, t)
                loge(TAG,t.localizedMessage)
            }

            override fun onFailureFinally() {
                super.onFailureFinally()
                TipDialog.show(requireActivity() as AppCompatActivity,"失败",TipDialog.TYPE.ERROR)
                    .setTipTime(2000)
            }
        })
    }

    /**
     * 检查是否填完信息
     */
    private fun checkFinish():Boolean{
        if(binding.parkName.text.toString() == ""){
            showMsgDialog("请输入停车场名")
        }else if(choosePosition == null){
            showMsgDialog("请选择停车场位置")
        }else if(parkPhotoRequestBody == null){
            showMsgDialog("请上传照片")
        }else if(parkType == 0){
            showMsgDialog("请选择停车场类型")
        }else if(isCharged == -1){
            showMsgDialog("请选择收费类型")
        }else if(binding.totalStallNum.text.toString() == ""){
            showMsgDialog("请输入停车位数量，未知请输入0")
        }else{
            return true
        }
        return false

    }

    private fun showMsgDialog(msg:String){
        MessageDialog.show(requireActivity() as AppCompatActivity,"提示",msg)
    }

    private fun takePhoto(){
        val file = File(requireContext().cacheDir, "parkTempPhoto.jpg")
        CoCo.with(this)
            .take(file)
            .start(object : CoCoAdapter<TakeResult>() {
                override fun onSuccess(data: TakeResult) {
                    parkPhotoRequestBody = RequestBody.create(
                        MediaType.parse("image/jpg"), data.savedFile!!.absoluteFile
                    )
                    binding.photoOkChip.show()
                    Log.e("tag","照片选择完成")
                }

                override fun onFailed(exception: Exception) {
                    super.onFailed(exception)
                    loge(TAG,"拍照失败！${exception.toString()}")
                    showToast("拍照失败！")
                }
            })
    }

    private fun chooseFromAlbum(){
        CoCo.with(this)
            .pick()
            .start(object : CoCoAdapter<PickResult>() {
                override fun onSuccess(data: PickResult) {

                    val inputStream = requireContext().contentResolver.openInputStream(data.originUri)

                    val type = data.localPath!!.substringAfter('.')
                    logi(TAG,"image type = ${type}")
                    parkPhotoRequestBody = object : RequestBody() {
                        override fun contentType(): MediaType? {
                            return MediaType.parse("image/${type}")
                        }

                        override fun writeTo(sink: BufferedSink) {
                            Okio.source(inputStream).use { source -> sink.writeAll(source) }
                        }
                    }
                    binding.photoOkChip.show()
                }
            })
    }

}