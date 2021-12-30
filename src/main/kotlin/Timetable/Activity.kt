package Timetable

import Persistence.Entities.activity_category.ActivityCategoryModel

class Activity(ID: Int, ModuleID: Int, ModuleName: String, StartTime: Double, Duration: Double, ActivityType: Int, Year: Int, Term: Int, Week: Int, Day: Int) {

    val ModuleName: String
    val ID: Int
    val Module: Int
    val StartTime: Double
    val Duration: Double
    val ActivityType: Int

    val Year: Int
    val Term: Int
    val Week: Int
    val Day: Int

    var lessonTypes = mutableMapOf(0 to "Lecture", 1 to "Lab", 2 to "Tutorial", 3 to "Exam")

    init {
        this.ID = ID
        this.Module = ModuleID
        this.ModuleName = ModuleName
        this.StartTime = StartTime
        this.Duration = Duration
        this.ActivityType = ActivityType
        this.Year = Year
        this.Term = Term
        this.Week = Week
        this.Day = Day

        this.defineLessonTypesViaDB()
    }

    /**
     * Sets `lessonTypes` based on the activity categories' records in the database table.
     */
    fun defineLessonTypesViaDB(){

        val records = ActivityCategoryModel().selectAll()

        val map: MutableMap<Int, String> = mutableMapOf()

        for (doc in records) {
            map[doc.id_act_category!!] = doc.label!!
        }
        this.lessonTypes = map;
        //println("SET LESSON TYPES: " + this.lessonTypes)

    }

    override fun toString(): String {
        return "ID: ${this.ID} - ${this.ModuleName} - (${lessonTypes[this.ActivityType]})"
    }

}