package Persistence.Entities.activity

import Persistence.ResultSetToModel
import java.sql.ResultSet

class ResultSetToActivity: ResultSetToModel<ActivityModel>() {
    override fun rsToModel(rs: ResultSet): ActivityModel {
        return ActivityModel(
            id_activity = rs.getInt("id_activity"),
            id_course_module = rs.getInt("id_course_module"),
            id_act_category = rs.getInt("id_act_category"),
            act_starttime=rs.getDouble("act_starttime"),
            act_endtime = rs.getDouble("act_endtime"),
            week=rs.getInt("week"),
            day_week = rs.getInt("day_week"),
            posted_by = rs.getInt("posted_by"),
            term = rs.getInt("term"),
            year = rs.getInt("year")
        )
    }
}