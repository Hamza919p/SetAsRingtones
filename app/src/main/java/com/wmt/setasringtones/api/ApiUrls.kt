package com.wmt.setasringtones.api

object ApiUrls {
    const val BaseURL: String = "https://www.setasringtones.com/api/v1/"

    const val token: String =  "/995c07928b9b0bd5ab683c59e93263ba"
    const val getBestToneURL: String = BaseURL + "top-ringtones/"
    const val getLatestToneURL: String = BaseURL + "latest-ringtones/"
    const val getFeatureToneURL: String = BaseURL + "feature-ringtones/"
    const val getCategoriesURL: String = BaseURL + "categories"
    const val getCategoryURL: String = BaseURL + "category/"
    const val registerDeviceURL: String = BaseURL + "register-device"
    const val getSearchResultsURL: String = BaseURL + "ringtone/"
    const val getChannelsURL: String = BaseURL + "channels/"
    const val getChannelURL: String = BaseURL + "channel/"
    const val getLatestURL: String = "latest/"
    const val getBestURL: String = "top/"
    const val getRelatedTonesURL: String = BaseURL + "related-ringtones/"
    const val getDeepLinkUrl: String = BaseURL + "ringtone/"
}