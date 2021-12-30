package Timetable

class Timetable (val ID: Int? = null, Name: String, StartYear: Int, EndYear: Int, Undergraduate: Boolean) {

    var displayLabel: String
    var endYear: Int
    var startYear: Int
    var name: String
    var CourseType: String
    var table: MutableMap<Int, Year>
    var modules: MutableMap<Int, Module>
    var Activities: MutableMap<Int, Activity>

    init {
        if (Undergraduate){
            this.CourseType = "undergraduate"
            this.table = mutableMapOf(1 to Year(1), 2 to Year(2), 3 to Year(3)) //Undergraduate has 3 years.
        }
        else{
            this.CourseType = "postgraduate"
            this.table = mutableMapOf(1 to Year(1)) //Postgraduate has 3 years.
        }
        this.modules = mutableMapOf()
        this.Activities = mutableMapOf()

        this.name = Name
        this.startYear = StartYear
        this.endYear = EndYear

        this.displayLabel = "Course: ${this.name} - (${this.startYear} - ${this.endYear}) - ${this.CourseType}"

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
                    println()
                }
                println("#".repeat(50))
            }
        }
        println("-".repeat(300))
    }

    fun altPrint(){
        println("::::::::::::: TIMETABLE PRINT :::::::::::::::");
        println("Timetable Years Length: " + this.table.values.size)
        for (year in this.table.values){
            var finalMessage = "";
            val messageArray: ArrayList<String> = ArrayList<String>();
             messageArray.add("Year ${year.yearNumber}");
            for (term in year.terms.values){
                messageArray.add("Term: ${term.termNumber}");
                for (week in term.weeks.values){
                    messageArray.add("Week: ${week.weekNumber}");
                    for (day in week.days.values){
                        messageArray.add("Day: ${day.dayNumber}");
                        messageArray.add("Schedule: ${day.TimeSlot}");
                    }

                }

            }
            finalMessage = messageArray.joinToString("- ");
            println(finalMessage)

        }
    }

    fun addModule(ID: Int, Name: String, IsOptional: Boolean){
        this.modules[ID] = Module(ID, Name, IsOptional)
    }

    fun removeModule(ID: Int){
        //When removing a module, this function will remove all activities associated with the given module that is being removed.
        val IDs = mutableListOf<Int>()
        for (activity in this.Activities.values){
            if (activity.Module == ID){
                IDs.add(activity.ID)
            }
        }
        for (x in IDs){
            removeActivity(x)
        }
        //Removes the module from the timetables list of modules.
        this.modules.remove(ID)
    }

    fun addActivity(ID: Int, Year: Int, Term: Int, Week: Int, DayOfWeek: Int, ModuleID: Int, StartTime: Double, Duration: Double, ActivityType: Int){
        val activity = Activity(ID, ModuleID, this.modules[ModuleID]?.Name!!, StartTime, Duration, ActivityType, Year, Term, Week, DayOfWeek)
        this.Activities[ID] = activity
        var x = 0.0
        while (x < Duration + 0.5){
            if (this.table[Year]?.terms?.get(Term)?.weeks?.get(Week)?.days?.get(DayOfWeek)?.TimeSlot?.get(StartTime + x) == null){
                this.table[Year]?.terms?.get(Term)?.weeks?.get(Week)?.days?.get(DayOfWeek)?.TimeSlot?.set(StartTime + x, mutableListOf(activity))
            }
            else{
                val existingActivities = this.table[Year]?.terms?.get(Term)?.weeks?.get(Week)?.days?.get(DayOfWeek)?.TimeSlot?.get(StartTime + x)
                existingActivities?.add(activity)
                this.table[Year]?.terms?.get(Term)?.weeks?.get(Week)?.days?.get(DayOfWeek)?.TimeSlot?.set(StartTime + x, existingActivities)
            }
            x += 0.5
        }

    }

    fun removeActivity(ID: Int){
        val activity = this.Activities[ID]
        val timeslot = this.table[activity?.Year]?.terms?.get(activity?.Term)?.weeks?.get(activity?.Week)?.days?.get(activity?.Day)?.TimeSlot

        var x = activity!!.StartTime
        val endTime = activity.StartTime + activity.Duration

        while (x < endTime + 0.5){
            if (timeslot?.get(x)?.size!! > 1){ //Checks if the value (a list) is greater than 1, meaning there are multiple activities at the same timeslot (Clash)
                var toRemove = mutableListOf<Activity>()
                for(act in timeslot[x]!!){ //Loops through each activity in the timeslot and checks its ID against the one that is being removed.
                    if (act.ID == ID){
                        toRemove.add(act) // Counters the ConcurrentModificationException
                    }
                }
                timeslot[x]?.removeAll(toRemove) //The given activity is removed from the timeslots list of activities.
            }
            else{ //If the else statement runs it means there is only 1 activity in the timeslot (no clash)
                if (timeslot[x]?.get(0)?.ID == ID){
                    timeslot[x] = null
                }
            }
            x += 0.5
        }
        this.Activities.remove(ID)
    }

}



