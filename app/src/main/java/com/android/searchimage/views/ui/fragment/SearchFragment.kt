package com.android.searchimage.views.ui.fragment

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.GridLayout.VERTICAL
import android.widget.RadioGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.android.searchimage.R
import com.android.searchimage.constants.WebServiceConstants.ILLUSTRATOR
import com.android.searchimage.constants.WebServiceConstants.PHOTOS
import com.android.searchimage.constants.WebServiceConstants.SIZE_LARGE
import com.android.searchimage.constants.WebServiceConstants.SIZE_MEDIUM
import com.android.searchimage.constants.WebServiceConstants.VECTOR
import com.android.searchimage.databinding.FragmentSearchBinding
import com.android.searchimage.helper.dismissDialog
import com.android.searchimage.helper.hideSoftKeyboard
import com.android.searchimage.helper.show
import com.android.searchimage.model.Hit
import com.android.searchimage.repository.LoyaltyWrapper
import com.android.searchimage.viewmodels.SearchViewModel
import com.android.searchimage.views.adapter.SearchAdapter
import com.android.searchimage.views.callbacks.OnItemClickListener
import com.android.searchimage.views.ui.activities.BaseActivity
import com.google.android.material.button.MaterialButton

class SearchFragment : Fragment(), OnItemClickListener {

    private lateinit var viewModel: SearchViewModel
    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var searchArray: ArrayList<Hit>
    private lateinit var dialogFilter: Dialog
    private var sizeValue: Int = SIZE_MEDIUM
    private var typeValue: Int = PHOTOS
    private var isSearchBarEmpty = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()
        if (isSearchBarEmpty) {
            binding.btnclear.visibility = View.GONE
        } else {
            binding.btnclear.visibility = View.VISIBLE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        binding.lifecycleOwner = this
        searchArray = ArrayList()
        searchAdapter = SearchAdapter(context!!, searchArray, this)
        binding.rvImage.apply {
            layoutManager = GridLayoutManager(context, 2, VERTICAL, false)
            adapter = searchAdapter
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when (typeValue) {
            PHOTOS -> viewModel.search("20297750-ce9e628a33c4058a87fa90aa1", "", "photo")
            ILLUSTRATOR -> viewModel.search(
                "20297750-ce9e628a33c4058a87fa90aa1",
                "",
                "illustration"
            )
            VECTOR -> viewModel.search("20297750-ce9e628a33c4058a87fa90aa1", "", "vector")
        }
        viewModel.searchData?.observe(this, WebResponseObserver())
        viewModel.isViewLoading?.observe(this, Loading())
        setClick()
        binding.txtsearch.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                s: CharSequence?, start: Int, before: Int,
                count: Int
            ) {
                if (binding.txtsearch.text!!.toString().equals("", ignoreCase = true)) {
                    isSearchBarEmpty = true
                    binding.btnclear.visibility = View.GONE
                    binding.imgSearch.visibility = View.VISIBLE
                } else {
                    isSearchBarEmpty = false
                    when (typeValue) {
                        PHOTOS -> viewModel.search("20297750-ce9e628a33c4058a87fa90aa1", s.toString(), "photo")
                        ILLUSTRATOR -> viewModel.search(
                            "20297750-ce9e628a33c4058a87fa90aa1",
                            s.toString(),
                            "illustration"
                        )
                        VECTOR -> viewModel.search("20297750-ce9e628a33c4058a87fa90aa1", s.toString(), "vector")
                    }
                    binding.btnclear.visibility = View.VISIBLE
                    binding.imgSearch.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(
                s: CharSequence?, start: Int, count: Int,
                after: Int
            ) {
            }

            override fun afterTextChanged(s: Editable?) {}
        })


    }

    private fun setClick() {
        binding.imgFilter.setOnClickListener(View.OnClickListener {
            hideSoftKeyboard(context, binding.btnclear)
            addFilter()
        })

        binding.btnclear.setOnClickListener {
            binding.txtsearch.setText("");
            isSearchBarEmpty = true;
            binding.btnclear.visibility = View.GONE;
            hideSoftKeyboard(context, binding.btnclear)
        }

    }

    companion object {

        @JvmStatic
        fun newInstance() =
            SearchFragment().apply {

            }
    }

    private fun addFilter() {
        dialogFilter = Dialog(activity!!)
        dialogFilter.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialogFilter.setContentView(R.layout.dialog_filter)
        dialogFilter.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialogFilter.show()
        val imgCloseDialog = dialogFilter.findViewById<AppCompatImageView>(R.id.imgCloseDialog)
        val btnApply = dialogFilter.findViewById<MaterialButton>(R.id.btnApply)
        val radioGroupSize = dialogFilter.findViewById<RadioGroup>(R.id.radioGroupSize)
        val radioGroupType = dialogFilter.findViewById<RadioGroup>(R.id.radioGroupType)
        radioGroupSize.setOnCheckedChangeListener { group, i ->
            when (i) {
                R.id.radioMedium -> sizeValue = SIZE_MEDIUM
                R.id.radioLarge -> sizeValue = SIZE_LARGE
            }
        }

        radioGroupType.setOnCheckedChangeListener { group, i ->
            when (i) {
                R.id.radioPhotos -> typeValue = PHOTOS
                R.id.radioIllustration -> typeValue = ILLUSTRATOR
                R.id.radioVector -> typeValue = VECTOR
            }
        }
        imgCloseDialog.setOnClickListener { dialogFilter.dismiss() }
        btnApply.setOnClickListener {

            when (typeValue) {
                PHOTOS -> viewModel.search("20297750-ce9e628a33c4058a87fa90aa1", "", "photo")
                ILLUSTRATOR -> viewModel.search(
                    "20297750-ce9e628a33c4058a87fa90aa1",
                    "",
                    "illustration"
                )
                VECTOR -> viewModel.search("20297750-ce9e628a33c4058a87fa90aa1", "", "vector")
            }
            dialogFilter.dismiss()
        }

    }


    internal inner class WebResponseObserver : Observer<LoyaltyWrapper<ArrayList<Hit>>> {
        override fun onChanged(webResponse: LoyaltyWrapper<ArrayList<Hit>>) {
            searchArray.clear()
            when (sizeValue) {
                SIZE_MEDIUM -> {
                    for (item in webResponse.hits) {
                        searchArray.add(
                            Hit(
                                item.previewURL, item.largeImageURL,
                                SIZE_MEDIUM
                            )
                        )
                    }
                }
                SIZE_LARGE -> {
                    for (item in webResponse.hits) {
                        searchArray.add(
                            Hit(
                                item.previewURL, item.largeImageURL,
                                SIZE_LARGE
                            )
                        )
                    }
                }
            }

            binding.rvImage.adapter = searchAdapter
            searchAdapter.notifyDataSetChanged()
            dismissDialog()
        }
    }

    internal inner class Loading : Observer<Boolean> {
        override fun onChanged(it: Boolean) {
            when (it) {
                true -> show(activity as BaseActivity)
                false -> dismissDialog()
            }
        }
    }

    override fun onItemClick(position: Int, `object`: Any?, view: View?, adapterName: String?) {

    }
}