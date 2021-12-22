package Persistence

import Persistence.Entities.course.CourseModel
import Persistence.model.Model
import java.sql.ResultSet

abstract class ResultSetToModel<TModel> {
    fun rsListToModel(rs: ResultSet) : ArrayList<TModel>{
        val arrayList: ArrayList<TModel> = arrayListOf()
        while(rs.next()){
            arrayList.add(this.rsToModel(rs))
        }
        return arrayList
    }
   abstract fun rsToModel(rs: ResultSet) : TModel
}