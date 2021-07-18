package com.example.dianastore.ui.fragment


import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.inyongtisto.myhelper.extension.toGone
import com.inyongtisto.myhelper.extension.toVisible

import com.example.dianastore.R
import com.example.dianastore.activity.productActivity.ProductViewModel
import com.example.dianastore.adapter.AdapterProductHome
import com.example.dianastore.adapter.AdapterSliderHome
import com.example.dianastore.app.ApiConfig
import com.example.dianastore.helper.Helper
import com.example.dianastore.helper.Prefs
import com.example.dianastore.model.ResponModel
import com.example.dianastore.model.TabelProduct
import com.inyongtisto.myhelper.extension.showErrorDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    lateinit var s: Prefs
    private lateinit var vm: ProductViewModel
    private var isFirstLoad = true
    private var isFirstLoadHome = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)
        init(view)
        vm = ViewModelProvider(this).get(ProductViewModel::class.java)
        s = Prefs(requireActivity())

        load()
        setupData()
        observer()
        mainButton()

        return view
    }

    private fun mainButton() {
    }

    var dataHome = ResponModel()
    private fun load() {
        dataHome = s.getObject(s.home, ResponModel::class.java) ?: ResponModel()
        displayHome()

        vm.getProduct()
        getHome()
    }

    private fun observer() {
        vm.listProduct.observe(requireActivity()) {
            displayProduk(it)
            displayTerbaru(it)
        }

        vm.isLoading.observe(requireActivity()) {
            if (!it) pd.toGone() else pd.toVisible()
        }

        vm.errorResponse.observe(requireActivity()) {
            requireActivity().showErrorDialog(it)
        }

        vm.successResponse.observe(requireActivity()) {

        }
    }

    private fun getHome() {
        ApiConfig.instanceRetrofit.getHome().enqueue(object : Callback<ResponModel> {
            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                pdBerita.toGone()
                pdSlider.toGone()
                if (response.isSuccessful) {
                    val respon = response.body()!!
                    if (respon.success == 1) {
                        s.setObject(s.home, respon)
                        dataHome = respon
                        displayHome()
                    } else setError(respon.message)
                } else {
                    setError(response.message())
                }
            }

            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                setError(t.message!!)
            }
        })
    }

    fun displayHome() {
        displaySlider()
    }

    fun setError(string: String) {
        pdBerita.toGone()
        pdSlider.toGone()
        tvErrorBerita.toVisible()
        tvErrorSlider.toVisible()
        requireActivity().showErrorDialog(string)
    }

    private fun setupData() {
        val user = s.getUser() ?: return
        val array = user.name.split(" ")
        var inisial = array[0].substring(0, 1)
        if (array.size > 1) inisial += array[1].substring(0, 1)
        tvInisial.text = inisial
        tvName.text = user.name
        tvSalam.text = Helper.getSalam().salam()
    }

    private fun displayProduk(list: List<TabelProduct>) {
        if (list.isEmpty() && !isFirstLoad) tvError.toVisible() else tvError.toGone()
        isFirstLoad = false

        val layoutManager = GridLayoutManager(activity, 3)

        rvProduk.adapter = AdapterProductHome(list as ArrayList)
        rvProduk.layoutManager = layoutManager
    }

    private fun displaySlider() {
        val list = dataHome.sliders
        if (list.isEmpty() && !isFirstLoadHome) tvErrorSlider.toVisible() else tvErrorSlider.toGone()
        isFirstLoadHome = false

        val adapter = AdapterSliderHome(list, requireActivity())
        slider.adapter = adapter
        slider.setPadding(40, 0, 40, 0)

        //auto Slide
        val timerSlider = Timer()
        val delay: Long = 4500
        val period: Long = 10000
        var currentPage = 0

        val handler = Handler()
        val update = Runnable {
            if (currentPage == adapter.count) {
                currentPage = 0
            }
            slider.setCurrentItem(currentPage++, true)
        }

        timerSlider.schedule(object : TimerTask() {
            override fun run() {
                handler.post(update)
            }
        }, delay, period)

    }

    private fun displayTerbaru(list: List<TabelProduct>) {
        if (list.isEmpty() && !isFirstLoadHome) tvErrorBerita.toVisible() else tvErrorBerita.toGone()
        isFirstLoadHome = false

        if (list.isEmpty() && !isFirstLoad) tvError.toVisible() else tvError.toGone()
        isFirstLoad = false

        val layoutManager = GridLayoutManager(activity, 2)

        rvBerita.adapter = AdapterProductHome(list as ArrayList)
        rvBerita.layoutManager = layoutManager

//        val layoutManager0 = LinearLayoutManager(requireActivity())
//        layoutManager0.orientation = LinearLayoutManager.VERTICAL
//        rvBerita.layoutManager = layoutManager0
//        rvBerita.setHasFixedSize(false)
//        rvBerita.adapter = AdapterBerita(list)
    }

    lateinit var slider: ViewPager
    lateinit var rvProduk: RecyclerView
    lateinit var rvBerita: RecyclerView
    lateinit var tvInisial: TextView
    lateinit var tvSalam: TextView
    lateinit var tvName: TextView
    lateinit var pd: ProgressBar
    lateinit var tvError: TextView

    lateinit var pdBerita: ProgressBar
    lateinit var tvErrorBerita: TextView
    lateinit var btnSemua: TextView

    lateinit var pdSlider: ProgressBar
    lateinit var tvErrorSlider: TextView


    fun init(view: View) {
        tvErrorBerita = view.findViewById(R.id.tv_errorTerbaru)
        pdBerita = view.findViewById(R.id.pd_terbaru)
        btnSemua = view.findViewById(R.id.btn_semua)

        tvErrorSlider = view.findViewById(R.id.tv_errorSlider)
        pdSlider = view.findViewById(R.id.pd_slider)

        tvError = view.findViewById(R.id.tv_error)
        pd = view.findViewById(R.id.pd)
        tvSalam = view.findViewById(R.id.tv_salam)
        tvInisial = view.findViewById(R.id.tv_inisial)
        tvName = view.findViewById(R.id.tv_name)
        slider = view.findViewById(R.id.slider)
        rvProduk = view.findViewById(R.id.rv_produk)
        rvBerita = view.findViewById(R.id.rv_terbaru)
    }

}
