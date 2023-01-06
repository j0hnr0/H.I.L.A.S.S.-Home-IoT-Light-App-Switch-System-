package com.example.hilass

data class UserData(
    val bulbOnOffBedroom: Boolean = false,
    val manualAutoBedroom: Boolean = false,
    val ambientLightingBedroom: Boolean = false,
    val automaticLightingBedroom: Boolean = false,
    val dayLightBedroom: Boolean = false,
    val notificationBedroom: Boolean = false,

    val bulbOnOffLivingRoom: Boolean = false,
    val manualAutoLivingRoom: Boolean = false,
    val ambientLightingLivingRoom: Boolean = false,
    val automaticLightingLivingRoom: Boolean = false,
    val dayLightLivingRoom: Boolean = false,
    val notificationLivingRoom: Boolean = false,

    val bulbOnOffKitchen: Boolean = false,
    val manualAutoKitchen: Boolean = false,
    val ambientLightingKitchen: Boolean = false,
    val automaticLightingKitchen: Boolean = false,
    val dayLightKitchen: Boolean = false,
    val notificationKitchen: Boolean = false
)
