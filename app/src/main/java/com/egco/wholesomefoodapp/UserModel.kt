package com.egco.wholesomefoodapp

class UserModel(var foodallergy : List<String>, var name:String, val username:String) {
    override fun toString(): String {
        return "Category [title: ${this.username}, author: ${this.name}, categories: ${this.foodallergy}]"
    }
}

