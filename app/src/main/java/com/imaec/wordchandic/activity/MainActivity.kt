package com.imaec.wordchandic.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.imaec.wordchandic.R
import com.imaec.wordchandic.adapter.MainAdapter
import com.imaec.wordchandic.retrofit.WCDService
import kotlinx.android.synthetic.main.activity_main.*
import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.imaec.wordchandic.BuildConfig
import com.imaec.wordchandic.EndlessRecyclerOnScrollListener
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*

class MainActivity : AppCompatActivity() {

    private val TAG = this::class.java.simpleName

    private lateinit var interstitialAd: InterstitialAd
    private lateinit var interstitialAd2: InterstitialAd
    private lateinit var adapter: MainAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var compositeDisposable: CompositeDisposable

    private var q: String = ""
    private var i = 1
    private var isLoadMore = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adInit()

        init()

        imageSearch.setOnClickListener {
            q = editSearch.text.toString()
            if (q == "") {
                Toast.makeText(this, "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (q.length > 1) {
                Toast.makeText(this, "검색어는 한글자로 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            progressMain.visibility = View.VISIBLE
            search(q, 1)
            showAd()
        }

        editSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                q = editSearch.text.toString()
                if (q == "") {
                    Toast.makeText(this, "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show()
                    return@setOnEditorActionListener true
                } else if (q.length > 1) {
                    Toast.makeText(this, "검색어는 한글자로 입력해주세요.", Toast.LENGTH_SHORT).show()
                    return@setOnEditorActionListener true
                }
                progressMain.visibility = View.VISIBLE
                search(q, 1)
                showAd()
                true
            }
            false
        }

        recyclerMain.addOnScrollListener(object : EndlessRecyclerOnScrollListener(layoutManager) {
            override fun onLoadMore(current_page: Int) {
                if (isLoadMore) search(q, current_page + 1)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    private fun adInit() {
        MobileAds.initialize(this) {}
        interstitialAd = InterstitialAd(this).apply {
            adUnitId =  if (BuildConfig.DEBUG) "ca-app-pub-3940256099942544/1033173712" else "ca-app-pub-7147836151485354/4587331679"
            adListener = object : AdListener() {
                override fun onAdLoaded() {
                    interstitialAd.show()
                }

                override fun onAdClosed() {
                    super.onAdClosed()
                    linear_progress.visibility = View.GONE
                }
            }
        }
        interstitialAd.loadAd(AdRequest.Builder().build())
        adView.loadAd(AdRequest.Builder().build())
    }

    private fun showAd() {
        Random().let {
            val ran = it.nextInt(7) + 1
            if (i == ran) {
                interstitialAd2 = InterstitialAd(this).apply {
                    adUnitId =  if (BuildConfig.DEBUG) "ca-app-pub-3940256099942544/1033173712" else "ca-app-pub-7147836151485354/5940436631"
                    adListener = object : AdListener() {
                        override fun onAdLoaded() {
                            interstitialAd2.show()
                        }
                    }
                }
                interstitialAd2.loadAd(AdRequest.Builder().build())
            }
        }
    }

    private fun init() {
        adapter = MainAdapter().apply {
            setIsLoadMore(isLoadMore)
        }
        layoutManager = LinearLayoutManager(this)
        compositeDisposable = CompositeDisposable()

        recyclerMain.adapter = adapter
        recyclerMain.layoutManager = layoutManager
        recyclerMain.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
    }

    @SuppressLint("CheckResult")
    private fun search(q: String, page: Int) {
        hideKeyboard()
        val service = WCDService.instance
        val disposable = service.callSearch(getString(R.string.open_dic_key), q, page)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMapIterable {
                if (it.listItem == null) isLoadMore = false
                adapter.setIsLoadMore(isLoadMore)

                progressMain.visibility = View.GONE
                if (page == 1) adapter.clearItem()

                it.listItem
            }
            .map {
                it.word = it.word.replace("-", "").replace("^", "")
                if (it.word[0].toString() != q) {
                    isLoadMore = false
                    adapter.setIsLoadMore(isLoadMore)
                    adapter.notifyDataSetChanged()
                }
                it
            }
            .filter { it.word.length > 1 && it.word[0].toString() == q }
            .filter { it.listSense[0].pos != "동사" }
            .subscribe({
                adapter.addItem(it)
            }, {
                progressMain.visibility = View.GONE
                adapter.clearItem()
                adapter.notifyDataSetChanged()
                Toast.makeText(this, "데이터를 가져오는데 실패했습니다.\nERROR : ${it.message}", Toast.LENGTH_SHORT).show()
            }, {
                adapter.notifyDataSetChanged()
                editSearch.setText("")
            })

        compositeDisposable.add(disposable)
    }

    private fun hideKeyboard() {
        try {
            val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            currentFocus?.let {
                imm.hideSoftInputFromWindow(it.windowToken, 0)
            }
        } catch (e: Exception) {
            Log.d("exception :::: ", e.toString())
        }
    }
}
