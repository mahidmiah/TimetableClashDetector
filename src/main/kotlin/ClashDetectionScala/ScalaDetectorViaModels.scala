package ClashDetectionScala

import Persistence.Entities.activity.ActivityModel

import scala.collection.mutable
import scala.jdk.CollectionConverters.*

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
    return clashingAct

  }

  def checkSingularClashRecursive(clashingMap: Map[Int, List[ActivityModel]], actModel: ActivityModel, activityModels: List[ActivityModel]): Map[Int, List[ActivityModel]] = {


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

    return checkSingularClashRecursive(clashingMap)

  }
  def checkForClashes(activityModels: java.util.List[ActivityModel]): java.util.Map[Integer, java.util.Map[Integer, ActivityModel]] = {

    var actModelsBuffer = activityModels.asScala;
    var actModelsList: List[ActivityModel] = actModelsBuffer.foldLeft(List[ActivityModel]()){(accumulator, a) =>
      accumulator :+ a
    }
    var clashesMap: Map[Int, Map[Int, ActivityModel]] = Map.empty[Int, Map[Int, ActivityModel]];
    var clashesMapList: Map[Int, List[ActivityModel]] = Map.empty[Int, List[ActivityModel]];

    var clashes:  List[List[Int]] = List(List());
    var activityModelsAsScala: scala.collection.mutable.Buffer[ActivityModel] = activityModels.asScala


    for (actModel <- actModelsList) {
      val actModelClashes = checkSingularClash(actModel, actModelsList.take(1))
      var clashesElemMap: Map[Int, ActivityModel] = Map.empty[Int, ActivityModel];

      val actModelClashesMap = actModelClashes.foldLeft(Map.empty[Int, ActivityModel]){(accumulator, a) =>
        accumulator + (a.getId_activity().asInstanceOf[Int] -> a)
      }
      
      /*
      for (clashingElem <- actModelClashes) {
        clashesElemMap = clashesElemMap ++ Map(clashingElem.getId_activity, clashingElem)
      }
      */
      clashesMap = clashesMap ++ Map[Int, Map[Int, ActivityModel]](actModel.getId_activity().asInstanceOf[Int] -> actModelClashesMap)

    }

    for ((k,v) <- clashesMap) println(s"key: $k, value: $v")
    var clashesMapToJava:java.util.Map[Integer, java.util.Map[Integer, ActivityModel]] = mutable.HashMap().asJava

    for ((actId, clashingActsHashMap) <- clashesMap) {
      var currentActClash: java.util.Map[Integer, ActivityModel] = mutable.HashMap().asJava
      for ((clashingActId, clashingActModel) <- clashingActsHashMap) {
        currentActClash.put(clashingActId, clashingActModel)
      }
      clashesMapToJava.put(actId, currentActClash);
    }


    return clashesMapToJava;

  }

}
