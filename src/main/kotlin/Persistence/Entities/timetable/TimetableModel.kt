package Persistence.Entities.timetable;

import Persistence.annotations.Column
import Persistence.annotations.ColumnTypes
import Persistence.model.Model
import Persistence.model.ModelSQLite
import java.sql.ResultSet

class TimetableModel(
    @field:Column(type = ColumnTypes.INTEGER) var id_timetable: Int? = null,
    @field:Column(type = ColumnTypes.INTEGER) var id_course: Int? = null
): ModelSQLite<TimetableModel>("timetables", "id_timetable") {
    override fun createFromResultSet(rs: ResultSet): TimetableModel {
        return ResultSetToTimetable().rsToModel(rs)
    }

}



