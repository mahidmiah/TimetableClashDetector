package Persistence.model

interface ModelValidator {
    val id: String;
    fun validate();
}