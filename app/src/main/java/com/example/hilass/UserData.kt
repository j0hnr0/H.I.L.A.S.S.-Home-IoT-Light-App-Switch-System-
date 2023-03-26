package com.example.hilass

data class UserData(
    val bulbOnOffBedroom: Boolean = false,
    val manualAutoBedroom: Boolean = false,
    val swBedroomCustomize: Boolean = false,
    val swBedroomMovementOnly: Boolean = false,
    val swBedroomPerson: Boolean = false,
    val swBedroomMode: Boolean = false,
    val swBedroomAmbientLighting: Boolean = false,
    val swBedroomNightLight: Boolean = false,
    val swBedroomNotification: Boolean = false,

    val bulbOnOffLivingRoom: Boolean = false,
    val manualAutoLivingRoom: Boolean = false,
    val swLivingRoomCustomize: Boolean = false,
    val swLivingRoomMovementOnly: Boolean = false,
    val swLivingRoomPerson: Boolean = false,
    val swLivingRoomMode: Boolean = false,
    val swLivingRoomAmbientLighting: Boolean = false,
    val swLivingRoomNightLight: Boolean = false,
    val swLivingRoomNotification: Boolean = false,


    val bulbOnOffKitchen: Boolean = false,
    val manualAutoKitchen: Boolean = false,
    val swKitchenCustomize: Boolean = false,
    val swKitchenMovementOnly: Boolean = false,
    val swKitchenPerson: Boolean = false,
    val swKitchenMode: Boolean = false,
    val swKitchenAmbientLighting: Boolean = false,
    val swKitchenNightLight: Boolean = false,
    val swKitchenNotification: Boolean = false,


    val sendNotificationBedroom: Boolean = false,
    val sendNotificationLivingRoom: Boolean = false,
    val sendNotificationKitchen: Boolean = false,

    val bulbLifeSpanBedroom: Int = 0,
    val bulbLifeSpanKitchen: Int = 0,
    val bulbLifeSpanLivingRoom: Int = 0,

    val fcmToken: String = ""
)
