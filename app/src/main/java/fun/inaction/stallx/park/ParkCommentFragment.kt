package `fun`.inaction.stallx.park

import `fun`.inaction.network.OkCallback
import `fun`.inaction.network.ServiceCreator
import `fun`.inaction.network.bean.Result
import `fun`.inaction.network.service.ParkService
import `fun`.inaction.stallx.R
import `fun`.inaction.stallx.databinding.FragmentParkCommentBinding
import `fun`.inaction.stallx.utils.UserBaseUtil
import `fun`.inaction.stallx.utils.appCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.hi.dhl.binding.viewbind
import com.kongzue.dialog.v3.TipDialog

class ParkCommentFragment:Fragment(R.layout.fragment_park_comment) {

    private val binding by viewbind<FragmentParkCommentBinding>()

    private val args by navArgs<ParkCommentFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.okBtn.setOnClickListener {
            val score = binding.scoreView.getScore()
            val comment = binding.comment.text.toString()
            ServiceCreator.create<ParkService>().commentPark(
                UserBaseUtil.getUserID()!!,
                UserBaseUtil.getUserName()!!,
                args.parkID,
                score.toFloat(),
                comment
            ).enqueue(object : OkCallback<Result>() {
                override fun onSuccess(result: Result) {
                    super.onSuccess(result)
                    TipDialog.show(appCompatActivity(),"评论成功",TipDialog.TYPE.SUCCESS)
                        .setTipTime(1000)
                        .setOnDismissListener {
                            findNavController().popBackStack()
                        }
                }

                override fun onFailureFinally() {
                    super.onFailureFinally()
                    TipDialog.show(appCompatActivity(),"评论失败",TipDialog.TYPE.ERROR)
                        .setTipTime(2000)
                }
            })
        }

    }
}