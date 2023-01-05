package com.example.hilass

data class UserData(
    val bulbOnOffBedroom: Boolean = false,
    val manualAutoBedroom: Boolean = false,
    val ambientLightingBedroom: Boolean = false,
    val automaticLightingBedroom: Boolean = false,
    val dayLightBedroom: Boolean = false,
    val notificationBedroom: Boolean = false
)
