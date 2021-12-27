package ClashDetectionScala

import Timetable.Activity

import scala.collection.mutable.ListBuffer
import scala.jdk.CollectionConverters.*

class ScalaDetector(timetable: Timetable.Timetable) {

  def getTableSlots(clashes: Set[Int]): List[List[Int]] ={

    var slots: List[List[Int]] = List()
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

  def detect(): List[List[Int]] ={
    var clashes: List[Int] = List()
    for (year <- timetable.getTable.values().asScala){
      for (term <- year.getTerms.values().asScala){
        for (week <- term.getWeeks.values().asScala){
          for (day <- week.getDays.values().asScala){
            for (slot <- day.getTimeSlot.values().asScala){
              if (slot != null){
                if (slot.size() > 1){
                  var compulsory = false
                  for (activity <- slot.asScala){
                    if (timetable.getModules.get(activity.getModule).getIsOptional == false){
                      compulsory = true
                    }
                  }
                  if (compulsory){
                    for (activity <- slot.asScala){
                      clashes = (activity.getID) :: clashes
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
    getTableSlots(clashes.toSet)
  }


}
