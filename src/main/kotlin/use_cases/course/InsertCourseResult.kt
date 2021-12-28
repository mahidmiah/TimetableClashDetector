package use_cases.course

import Persistence.Entities.course.CourseModel
import Persistence.Entities.course_type.CourseTypeModel
import Persistence.Entities.timetable.TimetableModel

class InsertCourseResult(val courseModel: CourseModel, val timetableModel: TimetableModel, val courseTypeModel: CourseTypeModel) {
}