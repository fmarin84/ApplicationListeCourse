package fr.examen.appnodejs

object Constants {
    // Endpoints
    const val BASE_URL = "http://ec2-23-20-194-1.compute-1.amazonaws.com:3333/"
    const val LOGIN_URL = "useraccount/authenticate"
    const val LIST_URL = "list"
    const val LIST_ARCHIVE_URL = "list/archive"
    const val ITEM_URL = "item"
    const val LIST_SHARE_ADD_URL = "/share/{listId}/{userId}/{state}"
    const val USERS_URL = "/useraccount/users"
    const val LIST_SHARE_DELETE_URL = "/share/delete/{listId}/{userId}"
    const val LIST_SHARE_URL = "/list/share"
    const val CURRENT_USER_URL = "/useraccount"
    const val NOTIFICATION_GET_URL = "/notification/user/{userId}"
    const val NOTIFICATION_URL = "/notification"
    const val PAYMENT_URL = "/payment"
    const val ROLE_USER_URL = "/useraccount/add/role_user/{userId}/{roleId}"

}