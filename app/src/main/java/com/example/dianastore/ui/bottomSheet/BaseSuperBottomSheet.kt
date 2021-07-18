package com.example.dianastore.ui.bottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment
import com.example.dianastore.R
import com.example.dianastore.util.AppProgressDialog

abstract class BaseSuperBottomSheet(var alwaysExpanded: Boolean = true, var isFitToContents: Boolean = false) : SuperBottomSheetFragment() {

    lateinit var progress : AppProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
        setupProgress()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

//        dialog!!.setOnShowListener(DialogInterface.OnShowListener {
//
//        })
        return inflater.inflate(setLayout(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        initListener(view)
    }

    private fun setupProgress() {
        progress = AppProgressDialog(requireContext())
        progress.setCancelable(false)
        progress.setCanceledOnTouchOutside(false)
    }

    override fun isSheetAlwaysExpanded() = alwaysExpanded

    /**
     *
     *  Contract Interface
     * */
    abstract fun setLayout(): Int
    abstract fun initView(view: View)
    abstract fun initListener(view: View)
}