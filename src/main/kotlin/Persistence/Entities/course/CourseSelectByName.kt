package Persistence.Entities.course

import Persistence.DBConnection.DBConnector
import java.sql.ResultSet
import java.sql.Statement

class CourseSelectByName {
    fun selectByName(dbConnector: DBConnector, model: CourseModel, qname: String) : ArrayList<CourseModel>{
        return dbConnector.rawSelectWithModel<CourseModel>(
            "SELECT name,start_year,end_year,id_course_type FROM ${model.tableName} WHERE name=${qname}",
            CourseResultSetToModel())
    }
}