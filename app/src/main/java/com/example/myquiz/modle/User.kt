package com.example.myquiz.modle

class User {
    var name = ""
    var email = ""
    var password = ""
    constructor()
    constructor(name: String, email: String, password: String) {
        this.name = name
        this.email = email
        this.password = password
    }

}