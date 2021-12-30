package ClashDetectionScala

import Persistence.Entities.activity.ActivityModel

import scala.collection.mutable
import scala.jdk.CollectionConverters.*

class ActivityHandler(val actModel: ActivityModel) {


  def optimisticDateComparison(other: ActivityHandler): Boolean = {
    val year: Int = actModel.getYear();
    val week: Int = actModel.getWeek();
    val weekDay: Int = actModel.getDay_week();
    val term: Int = actModel.getTerm();
    val startTime: Double = actModel.getAct_starttime();
    val endTime: Double = actModel.getAct_endtime();
    val otherModel = other.actModel;

    return  (
      (
        (startTime >= otherModel.getAct_starttime()) && startTime < otherModel.getAct_endtime() ||
          (endTime > otherModel.getAct_endtime() && endTime <= otherModel.getAct_endtime() )
        ) ||
        (
          (otherModel.getAct_starttime() >= startTime) && otherModel.getAct_starttime() < endTime ||
            (otherModel.getAct_endtime() > endTime && otherModel.getAct_endtime() <= endTime )
          )
      )
  }

  def pessimisticDateComparison(other: ActivityHandler): Boolean = {
    val year: Int = actModel.getYear();
    val week: Int = actModel.getWeek();
    val weekDay: Int = actModel.getDay_week();
    val term: Int = actModel.getTerm();
    val startTime: Double = actModel.getAct_starttime();
    val endTime: Double = actModel.getAct_endtime();
    val otherModel = other.actModel;

    return  (
      (
        (startTime >= otherModel.getAct_starttime()) && startTime <= otherModel.getAct_endtime() ||
          (endTime >= otherModel.getAct_endtime() && endTime <= otherModel.getAct_endtime() )
        ) ||
        (
          (otherModel.getAct_starttime() >= startTime) && otherModel.getAct_starttime() <= endTime ||
            (otherModel.getAct_endtime() >= endTime && otherModel.getAct_endtime() <= endTime )
          )
      )
  }

  def clash(other: ActivityHandler): Boolean = {

    val year: Int = actModel.getYear();
    val week: Int = actModel.getWeek();
    val weekDay: Int = actModel.getDay_week();
    val term: Int = actModel.getTerm();
    val startTime: Double = actModel.getAct_starttime();
    val endTime: Double = actModel.getAct_endtime();
    val otherModel = other.actModel;
    val actCourseModule = this.actModel.getId_course_module();
    println(s"COMPARING  ${actModel.getId_activity()} vs ${otherModel.getId_activity()}");
    println("Optional Comparison: " + (actCourseModule != otherModel.getId_course_module() || // Only validate optional if it's different modules
      (this.actModel.isOptional() == false || otherModel.isOptional() == false)));
    println(s"Pessimistic Comparison: " + pessimisticDateComparison(other));
    return (
      (this.actModel.getId_activity() != otherModel.getId_activity()) &&
        (actCourseModule == otherModel.getId_course_module() || // Only validate optional if it's different modules
        (this.actModel.isOptional() == false || otherModel.isOptional() == false)) && // Only calidate if one of the dif is NOT OPTIONAL
      (otherModel.getYear() == year) &&
      (otherModel.getTerm() == term) &&
      (otherModel.getWeek() == week) &&
      (otherModel.getDay_week() == weekDay) &&
      pessimisticDateComparison(other)

      )
  }
}
class ScalaDetectorViaModels {

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
                System.out.println("FOR EACH SLOT: " + slot)
                if (slot.size() > 0){
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
    System.out.println("SLOTS SCALA: " + slots )
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

}
