package com.jvrcoding.weatherapp.common


object DefaultConfigs {
    fun getDefaultParams(): Map<String, Any> {
        return hashMapOf(
            ConfigKeys.FORCE_UPDATE to false,
            ConfigKeys.MESSAGE to "Username can't be empty."
        )
    }

    object ConfigKeys {
        const val FORCE_UPDATE = "force_update"
        const val MESSAGE = "message"
    }
}
