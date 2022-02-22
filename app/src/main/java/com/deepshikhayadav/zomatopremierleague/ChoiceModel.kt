package com.deepshikhayadav.zomatopremierleague

import com.google.firebase.firestore.Exclude


data class ChoiceModel (
    @Exclude
    var id:String?=null,
    var Choice:String?=null,
    var Win:String?=null,
    var prize:String?=null,
    var category: String?=null
    )