package Persistence

import Persistence.Entities.course.CourseModel
import Persistence.model.Model
import java.sql.ResultSet

abstract class ResultSetToModel<T: Model> {
    fun rsListToModel(rs: ResultSet) : ArrayList<T>{
        val arrayList: ArrayList<T> = arrayListOf()
        while(rs.next()){
            arrayList.add(this.rsToModel(rs))
        }
        return arrayList
    }
   abstract fun rsToModel(resultSet: ResultSet) : T
}