package com.android.searchimage.views.ui.activities

import android.os.Bundle
import com.android.searchimage.R
import com.android.searchimage.views.ui.fragment.SearchFragment

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addFragment(SearchFragment.newInstance())
    }

    override val viewId: Int
        get() = R.layout.activity_main
    override val fragmentId: Int
        get() = R.id.contFrag


}