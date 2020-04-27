package com.itis.kfupass.models

import java.io.Serializable

class User : Serializable {
    var id: String? = null
    var grID: String? = null
    var login: String? = null
    var name: String? = null
    var rating: String? = null
    var age: String? = null
    var city: String? = null
    var about: String? = null
    var email: String? = null
    var imageURL: String? = "default"
    var libraryName: String = "MyLibrary"
}