package ClashDetectionScala

class ScalaDetector(timetable: Timetable.Timetable) {

  def detect(): Unit ={
    println("Scala being called!")

    val simpleIncrementer = new Function[Int, Int] {
      override def apply(arg: Int): Int = arg + 100
    }

    println(simpleIncrementer(50))
  }

}
