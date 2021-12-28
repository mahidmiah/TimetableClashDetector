package use_cases.course

import Persistence.Entities.course.CourseModel
import Persistence.Entities.course_type.CourseTypeModel
import Persistence.Entities.timetable.TimetableModel
import Persistence.model.InsertResult
import use_cases.course_module.insert_course_module.UseCaseError
import kotlin.jvm.Throws


class InsertCourse(
    courseName: String? = null,
    startYear: Int? = null,
    endYear: Int? = null,
    courseType: String? = null) {
    var courseName: String? = null;
    var startYear: Int? = null;
    var endYear: Int? = null;
    var courseType: String? = null;

    @Throws(UseCaseError::class)
    fun insert() : InsertCourseResult{
        if (courseName != null && startYear != null && endYear != null && courseType != null) {
            val  newCourse: CourseModel = CourseModel();
            val courseTypeDoc = CourseTypeModel().selectOneByLabel(courseType!!);
            if (courseTypeDoc == null) {
                throw UseCaseError("CourseTypeNotFound", "Invalid Course Type")
            }
            newCourse.id_course = null
            newCourse.id_course_type = courseTypeDoc.id_course_type!!;
            newCourse.start_year = startYear
            newCourse.end_year = endYear
            newCourse.name = courseName

            val res = newCourse.save();

            if (res.affectedRows == 0){
                throw UseCaseError("CourseNotSaved", "There was an error while creating a new Course")
            }

            val timetable = TimetableModel();
            timetable.id_course = newCourse.id_course
            timetable.id_timetable = null;

            val timetableRes = timetable.save();
            if (timetableRes.affectedRows == 0){
                throw UseCaseError("Timetable Not Saved", "There was an error while creating a new Timetable.")
            }

            return InsertCourseResult(newCourse, timetable, courseTypeDoc);

        }
        throw UseCaseError("MissingParameters", "Please ensure all fields have correct input!", "Add New Course Error")

    }
}