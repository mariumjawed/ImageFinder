package com.android.searchimage.views.ui.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.GridLayout.VERTICAL
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.android.searchimage.R
import com.android.searchimage.constants.WebServiceConstants.ILLUSTRATOR
import com.android.searchimage.constants.WebServiceConstants.KEY
import com.android.searchimage.constants.WebServiceConstants.PHOTOS
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
    private var typeValue: Int = PHOTOS
    private var searchQuery: String = ""
    private var lastSelected = PHOTOS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
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
            PHOTOS -> viewModel.search(KEY, searchQuery, "photo")
            ILLUSTRATOR -> viewModel.search(
                KEY,
                searchQuery,
                "illustration"
            )
            VECTOR -> viewModel.search(KEY, searchQuery, "vector")
        }
        viewModel.searchData?.observe(this, WebResponseObserver())
        viewModel.isViewLoading?.observe(this, Loading())
        setClick()
    }

    private fun setClick() {
        binding.imgFilter.setOnClickListener(View.OnClickListener {
            hideSoftKeyboard(context, binding.btnclear)
            addFilter()
        })

        binding.txtsearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                search()
                true
            }
            false
        }

        binding.imgSearch.setOnClickListener {
            search()
        }

        binding.btnclear.setOnClickListener {
            binding.txtsearch.setText("")
            //  isSearchBarEmpty = true
            searchQuery = ""
            binding.btnclear.visibility = View.GONE
            binding.imgSearch.visibility = View.VISIBLE
            hideSoftKeyboard(context, binding.btnclear)
            when (typeValue) {
                PHOTOS -> viewModel.search(KEY, searchQuery, "photo")
                ILLUSTRATOR -> viewModel.search(
                    KEY,
                    searchQuery,
                    "illustration"
                )
                VECTOR -> viewModel.search(KEY, searchQuery, "vector")
            }

        }

    }

    private fun search() {
        searchQuery = binding.txtsearch.text.toString()
        binding.imgSearch.visibility = View.GONE
        binding.btnclear.visibility = View.VISIBLE
        when (typeValue) {
            PHOTOS -> viewModel.search(
                KEY,
                searchQuery,
                "photo"
            )
            ILLUSTRATOR -> viewModel.search(
                KEY,
                searchQuery,
                "illustration"
            )
            VECTOR -> viewModel.search(
                KEY,
                searchQuery,
                "vector"
            )
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
        val radioGroupType = dialogFilter.findViewById<RadioGroup>(R.id.radioGroupType)
        val radioPhotos = dialogFilter.findViewById<RadioButton>(R.id.radioPhotos)
        val radioIllustration = dialogFilter.findViewById<RadioButton>(R.id.radioIllustration)
        val radioVector = dialogFilter.findViewById<RadioButton>(R.id.radioVector)
        lastSelected = typeValue
        when (typeValue) {
            PHOTOS -> radioPhotos.isChecked = true
            ILLUSTRATOR -> radioIllustration.isChecked = true
            VECTOR -> radioVector.isChecked = true
        }

        radioGroupType.setOnCheckedChangeListener { group, i ->
            when (i) {
                R.id.radioPhotos -> typeValue = PHOTOS
                R.id.radioIllustration -> typeValue = ILLUSTRATOR
                R.id.radioVector -> typeValue = VECTOR
            }
        }
        imgCloseDialog.setOnClickListener {
            typeValue = lastSelected
            dialogFilter.dismiss()
        }
        btnApply.setOnClickListener {
            searchQuery = binding.txtsearch.text.toString()
            if(searchQuery.isNotEmpty()){
                binding.imgSearch.visibility = View.GONE
                binding.btnclear.visibility = View.VISIBLE
            }
            when (typeValue) {
                PHOTOS -> viewModel.search(KEY, searchQuery, "photo")
                ILLUSTRATOR -> viewModel.search(
                    KEY,
                    searchQuery,
                    "illustration"
                )
                VECTOR -> viewModel.search(KEY, searchQuery, "vector")
            }
            dialogFilter.dismiss()
        }

    }


    internal inner class WebResponseObserver : Observer<LoyaltyWrapper<ArrayList<Hit>>> {
        override fun onChanged(webResponse: LoyaltyWrapper<ArrayList<Hit>>) {
            searchArray.clear()
            searchArray.addAll(webResponse.hits)
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