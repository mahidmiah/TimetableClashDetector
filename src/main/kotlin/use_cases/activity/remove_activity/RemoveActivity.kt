package use_cases.activity.remove_activity

import Persistence.Entities.activity.ActivityModel
import Persistence.model.Model
import use_cases.course_module.insert_course_module.UseCaseError
import kotlin.jvm.Throws

class RemoveActivity() {
    @Throws(UseCaseError::class)
    fun removeById(targetId: Int) : Model.Companion.DeleteResult {
        val deleteResult = ActivityModel().deleteById(targetId)

        if (deleteResult.affectedRows == 0) {
            throw UseCaseError("ActivityNotDeleted", "An error occurred while deleting the Activity.")
        }
        return deleteResult;
    }
}