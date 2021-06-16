package com.zlyandroid.weather.ui.fragment

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.android.flexbox.FlexboxLayoutManager
import com.zlyandroid.basic.common.base.BaseMvpFragment
import com.zlyandroid.weather.R
import com.zlyandroid.weather.db.model.CityModel
import com.zlyandroid.weather.event.CloseFragmentEvent
import com.zlyandroid.weather.http.Location
import com.zlyandroid.weather.ui.presenter.SearchResultPresenter
import com.zlyandroid.weather.ui.view.SearchResultView
import kotlinx.android.synthetic.main.fragment_searchresult.*

class SearchResultFragment: BaseMvpFragment<SearchResultView, SearchResultPresenter>(), SearchResultView  {


    lateinit var mAdapter: BaseQuickAdapter<Location, BaseViewHolder>

    companion object {
        fun getInstance(): SearchResultFragment = SearchResultFragment()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            mAdapter.setNewData(ArrayList())
        }
    }

    override fun createPresenter(): SearchResultPresenter = SearchResultPresenter()

    override fun getLayoutID(): Int = R.layout.fragment_searchresult

    override fun initView() {
        rv.setNestedScrollingEnabled(true)
        rv.setHasFixedSize(true)
        mAdapter =  object : BaseQuickAdapter<Location, BaseViewHolder>(R.layout.item_searchresult) {
            override fun convert(helper: BaseViewHolder, item: Location) {
                helper.setText(R.id.tv_name, item.name)
            }
        }
        mAdapter.setOnItemClickListener  { adapter, view, position ->
            val item: Location = mAdapter.getItem(position) ?: return@setOnItemClickListener
            mPresenter?.getDBNow(item.name,item.id)
        }
        rv.run {
            layoutManager = FlexboxLayoutManager(context)
            adapter =mAdapter
        }
    }

    override fun initData() {

    }

    fun search(key:String){
        mPresenter?.lookup(key)

    }



    override fun getSearchResultSuccess(data: List<Location>) {
        mAdapter.setNewData(data)
    }

    override fun getSearchResultFailed() {

    }

    override fun getSuccess(data: CityModel) {
        CloseFragmentEvent.postCloseFragmentEvent(data.cityName)
    }

    override fun getFailed() {

    }
}