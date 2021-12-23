package use_cases.insert_course_module

import Persistence.Entities.course.CourseModel
import Persistence.Entities.course_module.CourseModuleModel
import Persistence.Entities.course_type.CourseTypeModel
import Persistence.Entities.module.ModuleModel
import Persistence.Entities.timetable.TimetableModel
import Timetable.Timetable


class ExistentModule(model: ModuleModel) :
    Exception("There is already a model with the name: ${model.code}") {}

class CourseNotFound(message: String) :
    Exception(message) {}
class TimetableNotFound(message: String) :
    Exception(message) {}


class InsertCourseModuleResult(val module: ModuleModel, val courseModuleModel: CourseModuleModel)
class InsertCourseModule(val timetableId: Int, val name: String, val isOptional: Boolean) {
    fun insert() : InsertCourseModuleResult  {

        val timetableModel = TimetableModel().selectById(timetableId)
        if (timetableModel == null) {
            throw TimetableNotFound("Timetable not found. Tried ID: " + timetableId)
        }
        val course = timetableModel.fetchThisCourse()
        if (course == null){
            throw CourseNotFound("Course not found. Tried ID: " + timetableModel.id_course)
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