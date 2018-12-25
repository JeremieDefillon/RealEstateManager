package com.gz.jey.realestatemanager.utils

import android.content.Context
import com.qingmei2.rximagepicker.entity.Result
import com.qingmei2.rximagepicker.entity.sources.Camera
import com.qingmei2.rximagepicker.entity.sources.Gallery
import com.qingmei2.rximagepicker.ui.ICustomPickerConfiguration
import com.qingmei2.rximagepicker_extension_zhihu.ui.ZhihuImagePickerActivity
import io.reactivex.Observable

interface ZhihuImagePicker {
    // normal style
    /**
     * TO OPEN GALLERY AS NORMAL
     * @param context Context
     * @param config ICustomPickerConfiguration
     * @return Observable<Result>
     */
    @Gallery(componentClazz = ZhihuImagePickerActivity::class,
            openAsFragment = false)
    fun openGalleryAsNormal(context: Context,
                            config: ICustomPickerConfiguration): Observable<Result>


    /**
     * TO OPEN GALLERY AS DRACULA
     * @param context Context
     * @param config ICustomPickerConfiguration
     * @return Observable<Result>
     */
    // dracula style
    @Gallery(componentClazz = ZhihuImagePickerActivity::class,
            openAsFragment = false)
    fun openGalleryAsDracula(context: Context,
                             config: ICustomPickerConfiguration): Observable<Result>


    /**
     * TO OPEN CAMERA
     * @param context Context
     * @return Observable<Result>
     */
    // take photo
    @Camera
    fun openCamera(context: Context): Observable<Result>
}