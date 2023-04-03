package com.example.hilass

import com.google.firebase.Timestamp
import java.time.Instant

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

    val bulbLifeSpanBedroom: Long? = null,
    val bulbLifeSpanKitchen: Long? = null,
    val bulbLifeSpanLivingRoom: Long? = null,

    val timestampBedroom: Long? = null,
    val timestampKitchen: Long? = null,
    val timestampLivingRoom: Long? = null,

    val fcmToken: String = ""
)
