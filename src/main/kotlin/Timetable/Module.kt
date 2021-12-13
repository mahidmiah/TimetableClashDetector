package Timetable

class Module (ID: Int, Name: String, IsOptional: Boolean) {

    var ID: Int? = null
    var Name: String? = null
    var IsOptional: Boolean? = null

    init {
        this.ID = ID
        this.Name = Name
        this.IsOptional = IsOptional
    }

}