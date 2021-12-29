package ClashDetectionScala

import Persistence.Entities.activity.ActivityModel

import scala.collection.mutable
import scala.jdk.CollectionConverters.*


class ExperirmentalScalaDetectorViaModels {

  /**
   * @Deprecated
   * @param activityModels
   * @return
   */
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

  /**
   * @Deprecated
   * @param activityModels
   * @return
   */
  def checkSingularClashRecursive(clashingMap: Map[Int, Map[Int, ActivityModel]], actModel: ActivityModel, activityModels: List[ActivityModel]): Map[Int, Map[Int, ActivityModel]] = {
    System.out.println("LENGTH BEGIN: " + activityModels.size);
    if (activityModels.size == 0) {
      val newClashingMap: Map[Int, Map[Int, ActivityModel]] = clashingMap + (actModel.getId_activity().asInstanceOf[Int] -> Map())

      return newClashingMap
    }
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
          (
            (startTime >= a.getAct_starttime()) && startTime < a.getAct_endtime() ||
              (endTime > a.getAct_endtime() && endTime <= a.getAct_endtime() )
            ) ||
            (
              (a.getAct_starttime() >= startTime) && a.getAct_starttime() < endTime ||
                (a.getAct_endtime() > endTime && a.getAct_endtime() <= endTime )
              )
          )

      )
    )

    val clashingActMap = clashingAct.foldLeft(Map.empty[Int, ActivityModel]){(accumulator, a) =>
      accumulator + (a.getId_activity().asInstanceOf[Int] -> a)
    }

    val newClashingMap: Map[Int, Map[Int, ActivityModel]] = clashingMap + (actModel.getId_activity().asInstanceOf[Int] -> clashingActMap)
    val newActModel = activityModels.apply(0)
    val newActModelsList = activityModels.drop(1)
    System.out.println("LENGTH END: " + newActModelsList.size);
    System.out.println("activityModels END: " + activityModels);
    System.out.println("newActModelsList END: " + newActModelsList);
    return checkSingularClashRecursive(newClashingMap, newActModel, newActModelsList)

  }

  /**
   * @Deprecated
   * @param activityModels
   * @return
   */
  def getClashes(activityModels: java.util.List[ActivityModel]): java.util.Map[Integer, java.util.Map[Integer, ActivityModel]] = {

    var actModelsBuffer = activityModels.asScala;
    var activityModelsList: List[ActivityModel] = actModelsBuffer.foldLeft(List[ActivityModel]()){(accumulator, a) =>
      accumulator :+ a
    }
    var clashesMap: Map[Int, Map[Int, ActivityModel]] = Map.empty[Int, Map[Int, ActivityModel]];
    var clashesMapList: Map[Int, List[ActivityModel]] = Map.empty[Int, List[ActivityModel]];

    val newActModel = activityModelsList.apply(0)
    val newActModelsList = activityModelsList.drop(1)
    clashesMap = clashesMap + (newActModel.getId_activity().asInstanceOf[Int] -> Map());
    var clashesMapResult = checkSingularClashRecursive(clashesMap, newActModel, newActModelsList)

    for ((k,v) <- clashesMapResult) println(s"key: $k, value: $v")
    var clashesMapToJava:java.util.Map[Integer, java.util.Map[Integer, ActivityModel]] = mutable.HashMap().asJava

    for ((actId, clashingActsHashMap) <- clashesMapResult) {
      var currentActClash: java.util.Map[Integer, ActivityModel] = mutable.HashMap().asJava
      for ((clashingActId, clashingActModel) <- clashingActsHashMap) {
        currentActClash.put(clashingActId, clashingActModel)
      }
      clashesMapToJava.put(actId, currentActClash);
    }


    return clashesMapToJava;

  }

  def isClashingFromClashMap(clashMap: java.util.Map[Integer, java.util.Map[Integer, ActivityModel]], activityId: Int) : Boolean = {
    val clashMapScala = clashMap.asScala;
    val newListBuffer: mutable.Buffer[Int] = mutable.Buffer();
    for ((actId, actClashes) <- clashMapScala) {
      for ((clashId, actModel) <- actClashes.asScala) {
        if (clashId == activityId || actId == activityId) {
          return true;
        }
      }

    }

    return false;


  }

  def getTableSlots(timetable: Timetable.Timetable, clashes: java.util.Set[Integer]): List[List[Integer]] ={
    val clashesScala = clashes.asScala;
    var slots: List[List[Integer]] = List()
    var slotNum: Int = 0

    for (year <- timetable.getTable.values().asScala){
      for (term <- year.getTerms.values().asScala){
        for (week <- term.getWeeks.values().asScala){
          for (day <- week.getDays.values().asScala){
            for (slot <- day.getTimeSlot.values().asScala){
              if (slot != null){
                if (slot.size() > 1){
                  for (activity <- slot.asScala){
                    if (clashes.contains(activity.getID)){
                      slots = (List(year.getYearNumber, term.getTermNumber, week.getWeekNumber, slotNum, day.getDayNumber + 1)) :: slots
                    }
                  }
                }
                else {
                  if (clashes.contains(slot.get(0).getID)){
                    slots = (List(year.getYearNumber, term.getTermNumber, week.getWeekNumber, slotNum, day.getDayNumber + 1)) :: slots
                  }
                }
              }
              slotNum += 1
            }

          }
        }
      }
    }
    slots
  }

  def getTableSlotsAsJava(timetable: Timetable.Timetable, clashes: java.util.Set[Integer]): java.util.List[java.util.List[Integer]] = {
    val slots = getTableSlots(timetable, clashes);
    var slotsToJava: java.util.List[java.util.List[Integer]] = List().asJava;

    val slotsJavaElements = slots.map(a => a.asJava);


    return slotsJavaElements.asJava;
  }

  def simpleGetActivitiesClashes(activities: java.util.List[ActivityModel]):  List[(ActivityHandler, ActivityHandler)] = {
    var actModelsBuffer = activities.asScala;

    // Convert Buffer to List
    var activityModelsList: List[ActivityHandler] = actModelsBuffer.foldLeft(List[ActivityHandler]()){(accumulator, a) =>
      accumulator :+ ActivityHandler(a)
    }



    val clashingList = (for (x <- activityModelsList; y <- activityModelsList if x clash(y)) yield (x, y))
    return clashingList;

  }

  def simpleGetActivitiesIdsClashesSet(activities: java.util.List[ActivityModel]):  Set[Integer] = {

    var clashesList = simpleGetActivitiesClashes(activities);
    //var clashesSet: scala.collection.immutable.Set[Int] = Set();

    return clashesList.foldLeft(scala.collection.immutable.Set[Integer]()){(accumulator, clash) =>
      accumulator + (clash._1.actModel.getId_activity())
    }


  }

  def simpleGetActivitiesIdsClashesSetAsJava(activities: java.util.List[ActivityModel]): java.util.Set[Integer] = {
    var clashesSet =  simpleGetActivitiesIdsClashesSet(activities)
    return clashesSet.asJava;
  }



  def simpleIsActivityCrashing(targetActivityModel: ActivityModel, activities: java.util.List[ActivityModel]): Boolean = {
    var actModelsBuffer = activities.asScala;
    // Convert Buffer to List
    var activityModelsList: List[ActivityHandler] = actModelsBuffer.foldLeft(List[ActivityHandler]()){(accumulator, a) =>
      accumulator :+ ActivityHandler(a)
    }

    return activityModelsList.find(a => (activityModelsList.find(b => a.clash(b))).isEmpty == false).isEmpty == false
  }

  /**
   * @Deprecated Do not use
   * @param activityModels
   * @return
   */
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
