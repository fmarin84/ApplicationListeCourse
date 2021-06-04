package fr.examen.appnodejs.api

import java.io.Serializable

data class Notification(var id:Int,
                        var titre:String,
                        var text:String,
                        var created_at:String,
                        var isLue:Boolean,
                        var listshareid:Int,
                        var fk_useraccount_id:Int
): Serializable

