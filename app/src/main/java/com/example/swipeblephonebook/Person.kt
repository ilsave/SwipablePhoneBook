package com.example.swipeblephonebook

import android.os.Parcelable
import java.io.Serializable

data class Person(var name: String? = null, var number: String? = null) : Serializable