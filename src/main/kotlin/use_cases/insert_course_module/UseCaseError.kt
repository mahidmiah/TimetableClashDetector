package use_cases.insert_course_module


open class UseCaseError(val errorCode: String, message: String, val clientTitleMessage: String = "Error", val clientMessage: String? = null): Exception(message) {
    fun getMessageToDisplay(): String {
        if (clientMessage != null) {
            return clientMessage
        }
        if (message != null) {
            return message as String
        }
        return "";
    }

    fun getTitleToDisplay() : String{
        return this.clientTitleMessage
    }
}