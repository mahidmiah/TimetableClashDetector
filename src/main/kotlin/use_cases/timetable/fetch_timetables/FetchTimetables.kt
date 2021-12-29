package use_cases.timetable.fetch_timetables

import Persistence.DBConnection.SingletonDBConnector
import Persistence.Entities.timetable.TimetableModel
import Timetable.Timetable;
import Timetable.persistence.fetchTimetable.FetchCourseTimetable

class FetchTimetables {
    fun fetch() : MutableList<Timetable>{
        val timetableModels = TimetableModel().selectAll()
        //val courses = timetableModels.map { e -> e.fetchThisCourse() }
        val timetablesForGui: MutableList<Timetable> = arrayListOf();

        val fetchCourseTimetable = FetchCourseTimetable(SingletonDBConnector.getConnector())

        for (timetableDoc in timetableModels) {
            val course = timetableDoc.fetchThisCourse()
            if (course != null) {
                val timetableGui = fetchCourseTimetable.fetchWithYear(course.name!!, course.start_year!!);
                if (timetableGui != null) {
                    timetablesForGui.add(timetableGui);
                }
            }

        }

        return timetablesForGui;

    }

    fun fetchAsArrayList() : java.util.ArrayList<Timetable> {
        val mutableList = this.fetch();
        val timetablesForGui: java.util.ArrayList<Timetable> = java.util.ArrayList<Timetable>();

        for (elem in mutableList) {
            timetablesForGui.add(elem);
        }
        return timetablesForGui;
    }
}