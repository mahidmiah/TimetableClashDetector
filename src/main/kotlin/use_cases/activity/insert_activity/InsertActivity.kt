package use_cases.activity.insert_activity

import Persistence.Entities.activity.ActivityModel
import Persistence.Entities.course_module.CourseModuleModel
import Persistence.Entities.module.ModuleModel
import use_cases.course_module.insert_course_module.UseCaseError


class InsertActivityResult(val activityModel: ActivityModel)

class InsertActivity() {

    var year: Int? = null;
    var term: Int? = null;
    var week: Int? = null;
    var weekDay: Int? = null;
    var courseModuleId: Int? = null;
    var startTime: Double? = null;
    var endTime: Double? = null;
    var activityCategoryId: Int? = null;


    fun insert() : InsertActivityResult{
        val act = ActivityModel()
        act.id_activity = null;
        act.year = year;
        act.term = term
        act.week = week
        act.day_week = weekDay
        act.id_course_module = courseModuleId
        act.act_starttime = startTime
        act.act_endtime = endTime
        act.id_act_category = activityCategoryId
        act.posted_by = 1


        if (act.year == null) {
            throw UseCaseError("RequiredValue", "Please select a year")
        }

        if (act.term == null) {
            throw UseCaseError("RequiredValue", "Please select a term")
        }

        if (act.week == null) {
            throw UseCaseError("RequiredValue", "Please select a week")
        }
        if (act.day_week == null) {
            throw UseCaseError("RequiredValue", "Please select a Day of the Week")
        }
        if (act.id_course_module == null) {
            throw UseCaseError("RequiredValue", "Please select a Course Module")
        }
        if (act.act_starttime == null) {
            throw UseCaseError("RequiredValue", "Please select a start time")
        }
        if (act.act_endtime == null) {
            throw UseCaseError("RequiredValue", "Please select a end time/duration.")
        }
        if (act.id_act_category == null) {
            throw UseCaseError("RequiredValue", "Please select a activity type.")
        }


        act.save()


        return InsertActivityResult(act)

    }
}