package use_cases.course_module.remove_course_module

import Persistence.Entities.course_module.CourseModuleModel
import Persistence.Entities.module.ModuleModel
import Persistence.model.DeleteResult
import Persistence.model.Model
import use_cases.course_module.insert_course_module.UseCaseError
import kotlin.jvm.Throws


class RemoveCourseModuleByIdResult(val courseModuleDeleteResult: DeleteResult, val moduleDeleteResult: DeleteResult)
class RemoveCourseModule {
    @Throws(UseCaseError::class)
    fun removeById(targetId: Int) : RemoveCourseModuleByIdResult {
        val courseModuleModel = CourseModuleModel().selectById(targetId)
        if (courseModuleModel == null) {
            throw UseCaseError("CourseModuleNotFound",
                "Could not Course Module with the ID: " + targetId)
        }

        val deleteRes = courseModuleModel.deleteById(targetId)

        if (deleteRes.affectedRows == 0) {
            throw UseCaseError("CourseNotDeleted", "An error occurred while deleting the course module.")
        }
        val deleteModuleRes = ModuleModel().deleteById(courseModuleModel.id_module);

        return RemoveCourseModuleByIdResult(deleteRes, deleteModuleRes)




    }
}