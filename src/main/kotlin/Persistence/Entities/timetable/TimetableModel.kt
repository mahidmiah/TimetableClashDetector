package Persistence.Entities.timetable;

import Persistence.annotations.Column
import Persistence.annotations.ColumnTypes
import Persistence.model.Model

class TimetableModel(
    @field:Column(type = ColumnTypes.INTEGER) var id_timetable: Int?,
    @field:Column(type = ColumnTypes.INTEGER) var id_course: Int?
): Model("timetables") {

}
