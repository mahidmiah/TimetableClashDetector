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

    override fun toString(): String {
        return "ID: ${this.ID} - ${this.Name} - Optional: ${this.IsOptional}"
    }

}