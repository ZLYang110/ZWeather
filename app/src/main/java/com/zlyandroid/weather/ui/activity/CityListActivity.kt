package com.zlyandroid.weather.ui.activity

import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView.OnEditorActionListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zlyandroid.basic.common.base.BaseMvpActivity
import com.zlyandroid.basic.common.util.KeyBoardUtil
import com.zlyandroid.basic.common.util.PreUtils
import com.zlyandroid.basic.common.util.log.L
import com.zlyandroid.weather.R
import com.zlyandroid.weather.core.AppCon
import com.zlyandroid.weather.core.Data
import com.zlyandroid.weather.core.amap.AMapLocationEngine
import com.zlyandroid.weather.db.model.CityModel
import com.zlyandroid.weather.event.CloseFragmentEvent
import com.zlyandroid.weather.event.UpdataCityNowEvent
import com.zlyandroid.weather.ui.fragment.SearchResultFragment
import com.zlyandroid.weather.ui.presenter.CityListPresenter
import com.zlyandroid.weather.ui.view.CityListView
import com.zlyandroid.weather.widget.KeyboardChangeListener
import kotlinx.android.synthetic.main.activity_citylist.*
import kotlinx.android.synthetic.main.activity_citylist.weatherView
import kotlinx.android.synthetic.main.activity_weather.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * @author zhangliyang
 * @date 2021/5/18
 * GitHub: https://github.com/ZLYang110
 * desc:城市列表
 */
class CityListActivity : BaseMvpActivity<CityListView, CityListPresenter>(), CityListView {

     lateinit var mAdapter:BaseQuickAdapter<CityModel, BaseViewHolder>
    var isSearch=false //是否在搜索中

    private lateinit var mFragmentManager: FragmentManager
    private lateinit var mSearchResultFragment: SearchResultFragment

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, CityListActivity::class.java)
            context.startActivity(intent)
        }
    }

    //定位结束
    @Subscribe(threadMode = ThreadMode.MAIN )
    fun onCloseFragmentEvent(event: CloseFragmentEvent) {
        UpdataCityNowEvent.postUpdataCityNowEvent(event.cityName,false)
        finish()
    }

    override fun isRegisterEventBus(): Boolean = true
    override fun createPresenter(): CityListPresenter = CityListPresenter()

    override fun getLayoutID(): Int = R.layout.activity_citylist

    override fun initView() {
        weatherView.setWeatherType(Data.showBg)
        rv_citylist.setNestedScrollingEnabled(true)
        rv_citylist.setHasFixedSize(true)
        mAdapter =  object : BaseQuickAdapter<CityModel, BaseViewHolder>(R.layout.item_name) {
            override fun convert(helper: BaseViewHolder, item: CityModel) {
                if(item.isSelect){
                    helper.setTextColor(R.id.tv_name,getColor(R.color.text_invert_alpha))
                }else{
                    helper.setTextColor(R.id.tv_name,getColor(R.color.white))
                }
                helper.setText(R.id.tv_name, item.cityName)
            }
        }
        rv_citylist.run {
            //layoutManager = FlexboxLayoutManager(context)
            layoutManager = GridLayoutManager(context, 3)
            adapter =mAdapter
        }

        mAdapter.setOnItemClickListener  { adapter, view, position ->
            val item: CityModel = mAdapter.getItem(position) ?: return@setOnItemClickListener
            if(!item.isSelect){
                UpdataCityNowEvent.postUpdataCityNowEvent(item.cityName,false)
                finish()
            }

        }
        et_search.setOnFocusChangeListener { view, b ->
            if(b){
                isSearch = true
                setUpdataBg(true)
            }else{
                setUpdataBg(false)
            }
        }
        et_search.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
               var key=et_search.text.toString()
                if(!TextUtils.isEmpty(key)){
                    mSearchResultFragment.search(key)
                }
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
        et_search.setOnEditorActionListener(OnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                KeyBoardUtil.hideKeyBoard(getActivity(),et_search)
                return@OnEditorActionListener true
            }
            false
        })
        KeyboardChangeListener.SoftKeyBoardListener(getActivity(),object :KeyboardChangeListener.OnSoftKeyBoardChangeListener{
            override fun keyBoardShow(height: Int) {
            }
            override fun keyBoardHide(height: Int) {
                if(TextUtils.isEmpty(et_search.text.toString())){
                    isSearch = false
                    et_search.clearFocus()
                }
            }

        })
        initFragment()
    }

    override fun initData() {
        mPresenter?.getList(getActivity())
       // mPresenter?.removeAll()
        abc.leftIconView.setOnClickListener(this)
    }

    override fun onClick2(v: View) {
        super.onClick2(v)
        when (v.id) {
            abc.leftIconView.id -> {
                if(isSearch){
                    isSearch = false
                    et_search.clearFocus()
                }else{
                    finish()
                }
            }
        }
    }
    private fun setUpdataBg(bool:Boolean){
        if(bool){
            abc.titleTextView.setText("城市搜索")
            abc.setBackgroundColor(getColor(R.color.white))
            abc.leftIconView.setColorFilter(getColor(R.color.black))
            abc.titleTextView.setTextColor(getColor(R.color.black))
            rl_search.setBackgroundColor(getColor(R.color.white))
            abc.bottomLine.setBackgroundColor(getColor(R.color.background))
            showChatFragment()
        }else{
            abc.titleTextView.setText("城市列表")
            abc.leftIconView.setColorFilter(getColor(R.color.white))
            abc.titleTextView.setTextColor(getColor(R.color.white))
            abc.setBackgroundColor(getColor(R.color.transparent))
            rl_search.setBackgroundColor(getColor(R.color.transparent))
            abc.bottomLine.setBackgroundColor(getColor(R.color.bar_line))
            hideFragment()
            et_search.setText("")
        }
    }

    //======================== 搜索记录=========================================
    private fun initFragment() {
        mFragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = mFragmentManager.beginTransaction()
        val chatFragment: Fragment? = mFragmentManager.findFragmentByTag(SearchResultFragment::class.java.getName())
        if (chatFragment == null) {
            mSearchResultFragment = SearchResultFragment.getInstance()
            transaction.add(R.id.fl, mSearchResultFragment, SearchResultFragment::class.java.getName())
        } else {
            mSearchResultFragment = chatFragment as SearchResultFragment
        }
        transaction.hide(mSearchResultFragment)
        transaction.commit()

    }
    private fun showChatFragment() {
        val t = mFragmentManager.beginTransaction()
        t.show(mSearchResultFragment)
        t.commit()
    }

    private fun hideFragment() {
        if (isFinishing) return
        val t = mFragmentManager.beginTransaction()
        t.hide(mSearchResultFragment)
        t.commit()
    }


    override fun getCityListSuccess(data: List<CityModel>) {
        mAdapter.setNewData(data)
    }

    override fun getCityListFailed() {

    }

    override fun updataCitySuccess(data: CityModel) {

    }

    override fun updataCityFailed() {

    }

    override fun removeSuccess() {

    }

    override fun removeFailed() {

    }


}