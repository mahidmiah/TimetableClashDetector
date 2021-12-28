package use_cases.course_module.insert_course_module


open class UseCaseError(val errorCode: String, message: String, val clientTitleMessage: String? = null, val clientMessage: String? = null): Exception(message) {

    fun getMessageToDisplay(): String {
        if (clientMessage != null) {
            return clientMessage
        }
        if (message != null) {
            return message as String
        }
        return "An error has occurred.";
    }


    fun getTitleToDisplay() : String {
        return this.clientTitleMessage ?: "Error";

    }
}