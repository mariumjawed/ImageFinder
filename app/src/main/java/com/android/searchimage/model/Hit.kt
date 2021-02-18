package com.android.searchimage.model
import com.android.searchimage.constants.WebServiceConstants.SIZE_MEDIUM
import java.io.Serializable

class Hit(
    val previewURL: String?,
    val largeImageURL: String?,
    @Transient var valueSize: Int = SIZE_MEDIUM
) :Serializable {

}




