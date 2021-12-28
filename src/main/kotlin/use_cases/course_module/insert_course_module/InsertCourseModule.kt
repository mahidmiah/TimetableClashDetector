package use_cases.course_module.insert_course_module

import Persistence.Entities.course_module.CourseModuleModel
import Persistence.Entities.module.ModuleModel
import Persistence.Entities.timetable.TimetableModel
import kotlin.jvm.Throws


class ExistentModule(val model: ModuleModel) :
    UseCaseError("ExistentModule",
        "There is already a module with the name: ${model.code}",
        "Existent Module") {}


class InsertCourseModuleResult(val module: ModuleModel, val courseModuleModel: CourseModuleModel)
class InsertCourseModule(val timetableId: Int, val name: String, val isOptional: Boolean) {
    @Throws(UseCaseError::class)
    fun insert() : InsertCourseModuleResult  {

        val timetableModel = TimetableModel().selectById(timetableId)
        if (timetableModel == null) {
            throw UseCaseError("TimetableNotFound",
                "Timetable not found. Tried ID: " + timetableId,
            "Timetable not found")
        }
        val course = timetableModel.fetchThisCourse()
        if (course == null){

            throw UseCaseError("CourseNotFound",
                "Course not found. Tried ID: " + timetableModel.id_course,
                "course not found")
        }

        val newModule = ModuleModel()
        newModule.code = name;
        newModule.name = name;

        val existentModule = newModule.selectOneByCode(name)
        if (existentModule != null) {
            throw ExistentModule(existentModule)
        }
        newModule.save()


        val newCourseModule = CourseModuleModel()
        newCourseModule.id_module = newModule.id_module
        newCourseModule.id_course = course.id_course
        newCourseModule.available_year = 1
        newCourseModule.is_optional = if (isOptional) 1 else 0
        newCourseModule.save()

        return InsertCourseModuleResult(newModule, newCourseModule)


    }
}