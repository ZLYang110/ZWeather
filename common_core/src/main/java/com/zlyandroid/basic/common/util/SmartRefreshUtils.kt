package com.zlyandroid.basic.common.util

import com.scwang.smartrefresh.layout.api.RefreshLayout

object SmartRefreshUtils {
    private const val FIRST_PAGE = 0

    private var mRefreshLayout: RefreshLayout? = null
    private var mRefreshListener: RefreshListener? = null
    private var mLoadMoreListener: LoadMoreListener? = null

    private const val currentPage = FIRST_PAGE
    private const val perPageCount = 0

    fun with(layout: RefreshLayout): SmartRefreshUtils {
        return SmartRefreshUtils(layout)
    }

    private fun SmartRefreshUtils(layout: RefreshLayout) :SmartRefreshUtils{
        mRefreshLayout = layout
        mRefreshLayout!!.setEnableAutoLoadMore(false)
       // mRefreshLayout!!.setEnableOverScrollBounce(true)

        return this
    }

    fun pureScrollMode(): SmartRefreshUtils? {
        mRefreshLayout!!.setEnableRefresh(false)////是否启用下拉刷新功能
        mRefreshLayout!!.setEnableLoadMore(false)//是否启用上拉加载功能
        mRefreshLayout!!.setEnablePureScrollMode(true)///是否启用纯滚动模式
        mRefreshLayout!!.setEnableNestedScroll(true)//是否启用嵌套滚动
       // mRefreshLayout!!.setEnableOverScrollBounce(true);//是否启用越界回弹
        mRefreshLayout!!.setEnableOverScrollDrag(true)//是否启用越界拖动
        return this
    }

    fun setRefreshListener(refreshListener: RefreshListener): SmartRefreshUtils? {
        mRefreshListener = refreshListener
        if (refreshListener == null) {
            mRefreshLayout!!.setEnableRefresh(false)
        } else {
            mRefreshLayout!!.setEnablePureScrollMode(false)
            mRefreshLayout!!.setEnableRefresh(true)
            mRefreshLayout!!.setOnRefreshListener { refreshLayout ->
                refreshLayout.finishRefresh(5000, false, false)
                mRefreshListener!!.onRefresh()
            }
        }
        return this
    }

    fun setLoadMoreListener(loadMoreListener: LoadMoreListener?): SmartRefreshUtils? {
        mLoadMoreListener = loadMoreListener
        if (loadMoreListener == null) {
            mRefreshLayout!!.setEnableLoadMore(false)
        } else {
            mRefreshLayout!!.setEnablePureScrollMode(false)
            mRefreshLayout!!.setEnableLoadMore(true)
            mRefreshLayout!!.setOnLoadMoreListener { refreshLayout ->
                refreshLayout.finishLoadMore(5000)
                mLoadMoreListener!!.onLoadMore()
            }
        }
        return this
    }

    fun autoRefresh() {
        mRefreshLayout!!.autoRefresh()
    }

    fun autoLoadMore() {
        mRefreshLayout!!.autoLoadMore()
    }

    fun success() {
        mRefreshLayout!!.finishRefresh(true)
        mRefreshLayout!!.finishLoadMore(true)
    }

    fun fail() {
        mRefreshLayout!!.finishRefresh(false)
        mRefreshLayout!!.finishLoadMore(false)
    }
    interface RefreshListener {
        fun onRefresh()
    }

    interface LoadMoreListener {
        fun onLoadMore()
    }
}

