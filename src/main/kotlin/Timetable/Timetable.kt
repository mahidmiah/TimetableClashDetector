package Timetable

class Timetable (ID: Int, Name: String, StartYear: Int, EndYear: Int, Undergraduate: Boolean) {

    var CourseType: String
    var table: MutableMap<Int, Year>
    var modules: MutableMap<Int, Module>
    var Acitivites: MutableMap<Int, Activity>

    init {
        if (Undergraduate == true){
            this.CourseType = "undergraduate"
            this.table = mutableMapOf(1 to Year(1), 2 to Year(2), 3 to Year(3)) //Undergraduate has 3 years.
        }
        else{
            this.CourseType = "postgraduate"
            this.table = mutableMapOf(1 to Year(1)) //Postgraduate has 3 years.
        }
        this.modules = mutableMapOf()
        this.Acitivites = mutableMapOf()

        println("Initialized Timetable: - ID: $ID, Course Name: $Name, Start Year: $StartYear, End Year: $EndYear, Course Type: $CourseType")
    }

    fun print(){
        //Prints whole table.
        for (year in this.table.values){
            for (term in year.terms.values){
                for (week in term.weeks.values){
                    for (day in week.days.values){
                        println("Year: ${year.yearNumber} - Term: ${term.termNumber} - Week: ${week.weekNumber} - Day: ${day.dayNumber} - Schedule: ${day.TimeSlot}")
                    }
                    println("")
                }
                println("#################################################################################")
            }
        }
    }

    fun addModule(ID: Int, Name: String, IsOptional: Boolean){
        this.modules[ID] = Module(ID, Name, IsOptional)
    }

//    fun removeModule(ID: Int){
//        //When removing a module, this function will iterate through the whole timetable and remove any activities associated with the given module that is being removed.
//        for (term in this.table.values){
//            for (day in term.days){
//                var time = 9.0
//                while (time < 21.5){
//                    if (day.TimeSlot[time] != null){
//                        if (day.TimeSlot[time]?.size!! > 1){
//                            for (act in day.TimeSlot[time]!!){
//                                if (act.Module == ID){
//                                    day.TimeSlot[time]?.remove(act)
//                                    println("TimeSlot: $time, removed Module: $ID Activity")
//                                }
//                            }
//                        }
//                        else {
//                            if (day.TimeSlot[time]?.get(0)?.Module  == ID){
//                                day.TimeSlot[time]?.removeAt(0)
//                                println("TimeSlot: $time, removed Module: $ID Activity")
//                            }
//                        }
//                    }
//                    time += 0.5
//                }
//            }
//        }
//        //Removes the module from the timetables list of modules.
//        this.modules.remove(ID)
//    }

    fun addActivity(ID: Int, Year: Int, Term: Int, Week: Int, DayOfWeek: Int, ModuleID: Int, StartTime: Double, Duration: Double, ActivityType: Int){
        val activity = Activity(ID, ModuleID, StartTime, Duration, ActivityType, Year, Term, Week, DayOfWeek)
        this.Acitivites[ID] = activity
        if (this.table[Year]?.terms?.get(Term)?.weeks?.get(Week)?.days?.get(DayOfWeek)?.TimeSlot?.get(StartTime) == null){
            var x = 0.0
            while (x < Duration + 0.5){
                this.table[Year]?.terms?.get(Term)?.weeks?.get(Week)?.days?.get(DayOfWeek)?.TimeSlot?.set(StartTime + x, mutableListOf(activity))
                x += 0.5
            }
        }
        else{
            val existingActivities = this.table[Year]?.terms?.get(Term)?.weeks?.get(Week)?.days?.get(DayOfWeek)?.TimeSlot?.get(StartTime)
            existingActivities?.add(activity)
            var x = 0.0
            while (x < Duration + 0.5){
                this.table[Year]?.terms?.get(Term)?.weeks?.get(Week)?.days?.get(DayOfWeek)?.TimeSlot?.set(StartTime + x, existingActivities)
                x += 0.5
            }
        }
    }

    fun removeActivity(ID: Int){
        val activity = this.Acitivites[ID]
        val timeslot = this.table[activity?.Year]?.terms?.get(activity?.Term)?.weeks?.get(activity?.Week)?.days?.get(activity?.Day)?.TimeSlot

        var x = activity!!.StartTime
        val endTime = activity.StartTime + activity.Duration

        while (x < endTime + 0.5){
            if (timeslot?.get(x)?.size!! > 1){
                for(act in timeslot[x]!!){
                    if (act.ID == ID){
                        timeslot[x]?.remove(act)
                    }
                }
            }
            else{
                if (timeslot[x]?.get(0)?.ID == ID){
                    timeslot[x] = null
                }
            }
            x += 0.5
        }
        this.Acitivites.remove(ID)
    }

}



