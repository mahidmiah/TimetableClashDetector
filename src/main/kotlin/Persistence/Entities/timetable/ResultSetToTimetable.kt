package Persistence.Entities.timetable

import Persistence.ResultSetToModel
import java.sql.ResultSet

class ResultSetToTimetable: ResultSetToModel<TimetableModel>() {
    override fun rsToModel(rs: ResultSet): TimetableModel {
        return TimetableModel(
            id_course = rs.getInt("id_course"),
            id_timetable = rs.getInt("id_timetable")
        )
    }
}