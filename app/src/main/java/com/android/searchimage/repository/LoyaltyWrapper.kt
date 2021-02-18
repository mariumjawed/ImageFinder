package com.android.searchimage.repository

import com.android.searchimage.model.Hit
import java.io.Serializable

class LoyaltyWrapper<T>(
    val total: String,
    val totalHits: String,
    val hits: List<Hit>
) : Serializable