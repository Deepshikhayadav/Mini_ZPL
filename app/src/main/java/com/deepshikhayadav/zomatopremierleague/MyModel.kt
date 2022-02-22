package com.deepshikhayadav.zomatopremierleague

import com.google.firebase.firestore.Exclude

class MyModel (

    @Exclude
    var id:String?=null,
    var team1:String?=null,
    var team2:String?=null,
    var time:String?=null,
    var game:String?=null,
    var winner:String?=null
)