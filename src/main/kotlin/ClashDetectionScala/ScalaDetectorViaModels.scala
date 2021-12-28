package ClashDetectionScala

import Persistence.Entities.activity.ActivityModel

class ScalaDetectorViaModels {

  def checkSingularClash(actModel: ActivityModel, activityModels: List[ActivityModel]): List[ActivityModel] = {
    var clashes: List[ActivityModel] = List();
    val year: Int = actModel.getYear();
    val week: Int = actModel.getWeek();
    val weekDay: Int = actModel.getDay_week();
    val term: Int = actModel.getTerm();
    val startTime: Double = actModel.getAct_starttime();
    val endTime: Double = actModel.getAct_endtime();
    val clashingAct = activityModels.filter(a => (
        (a.getYear() == year) &&
        (a.getTerm() == term) &&
        (a.getWeek() == week) &&
        (a.getDay_week() == weekDay) &&
          (
            (startTime >= a.getAct_starttime()) && startTime < a.getAct_endtime() ||
            (endTime > a.getAct_endtime() && endTime <= a.getAct_endtime() )
          )

      )
    )

    return clashes
  }
  def checkForClashes(activityModels: List[ActivityModel]): List[List[Int]] = {
    var clashes:  List[List[Int]] = List(List());


    return clashes

  }

}
