package ClashDetectionScala

import scala.jdk.CollectionConverters._

class ScalaDetector(timetable: Timetable.Timetable) {

  def detect(): Unit ={
    println("Scala being called!")

//    val detector = new Function[ Timetable.Timetable, List[List[Int]] ] {
//      override def apply(arg: Int): Int = 100
//    }

    for (year <- timetable.getTable.values().asScala){
      for (term <- year.getTerms.values().asScala){
        for (week <- term.getWeeks.values().asScala){
          for (day <- week.getDays.values().asScala){
            for (slot <- day.getTimeSlot.values().asScala){
              if (slot != null){
                println(slot.size() + " === " + slot)
              }
            }
          }
        }
      }
    }

//    println(detector(timetable))
  }

}
