package `fun`.inaction.stallx.search

import `fun`.inaction.stallx.R
import `fun`.inaction.stallx.adapters.SearchResultAdapter
import `fun`.inaction.stallx.databinding.FragmentSearchBinding
import `fun`.inaction.stallx.search.viewmodel.SearchViewModel
import `fun`.inaction.stallx.utils.LocationHelper
import `fun`.inaction.stallx.utils.MapUtil
import `fun`.inaction.stallx.utils.NavUtil
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.search.sug.SuggestionResult
import com.hi.dhl.binding.viewbind


class SearchFragment : Fragment(R.layout.fragment_search) {

    private val binding by viewbind<FragmentSearchBinding>()

    private val args: SearchFragmentArgs by navArgs()

    private val viewModel:SearchViewModel by viewModels()

    /**
     * RecyclerView 的adapter
     */
    private val adapter = SearchResultAdapter()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // recyclerView 设置
        with(binding.recyclerView){
            this.adapter = this@SearchFragment.adapter
            layoutManager = LinearLayoutManager(context)
        }

        // 搜索结果数据监听
        viewModel.searchResult.observe(viewLifecycleOwner){
            adapter.setNewInstance(it)
        }

        // 搜索输入框监听
        binding.searchEditText.setTextChangedListener { key ->
            viewModel.search(key,args.curCity)
        }


        //  搜索结果 点击事件
        adapter.setOnItemClickListener { adapter, view, position ->

            val data = adapter.data[position] as SuggestionResult.SuggestionInfo

            viewModel.cache(data)
//            LocationHelper.curLocation?.let {
//                val curLocation = LatLng(it.latitude,it.longitude)
//                NavUtil.startCarNav(requireActivity(),curLocation,data.pt)
//            }
            findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToParkSearchResultFragment(
                data.city,data.pt.longitude.toString(),data.pt.latitude.toString()
            ))
        }

        // 返回按钮点击事件
        binding.returnBtn.setOnClickListener { Navigation.findNavController(it).popBackStack() }

        Log.e("tag","2 onViewCreated")

    }


}