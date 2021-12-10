package Timetable

class Timetable (ID: Int, Name: String, StartYear: Int, EndYear: Int, CourseType: Int) {

    var activites: MutableMap<Int, Activity>
    var modules: MutableMap<Int, com.comp1815.Timetable.Module>
    var table: MutableMap<Int, Week>

    init {
        this.activites = mutableMapOf()
        this.modules = mutableMapOf()
        this.table = mutableMapOf(1 to Week(), 2 to Week())
        println("Initialized Timetable: - ID: $ID, Course Name: $Name, Start Year: $StartYear, End Year: $EndYear, Course Type: $CourseType")
    }

    fun print(){
        //Prints whole table.
        for (term in this.table.keys){
            println("------ $term ------")
            for (day in 0 until this.table[term]?.days?.size!!){
                val TimeSlots = this.table[term]?.days?.get(day)?.TimeSlot
                if (TimeSlots != null) {
                    for (i in TimeSlots.keys){
                        val actsList : MutableList<Int> = mutableListOf()
                        TimeSlots[i]?.forEach { acts ->
                            actsList.add(acts.ID)
                        }
                        println("Day: $day, Time Slot: $i - Activity ID(s): ${actsList.toString()}")
                    }
                }
            }
            println("#################################################################################")
        }
        //Prints all modules
        println("------ Modules ------")
        for (module in this.modules.keys){
            println("Module ID: $module - ${this.modules[module]} - ( Name: ${this.modules[module]?.Name}, IsOptional: ${this.modules[module]?.IsOptional} )")
        }
    }

    fun addModule(ID: Int, Name: String, IsOptional: Boolean){
        this.modules[ID] = com.comp1815.Timetable.Module(ID, Name, IsOptional)
    }

    fun removeModule(ID: Int){

        for (term in this.table.values){
            for (day in term.days){
                var time = 9.0
                while (time < 21.5){
                    if (day.TimeSlot[time] != null){
                        if (day.TimeSlot[time]?.size!! > 1){
                            for (act in day.TimeSlot[time]!!){
                                if (act.Module == ID){
                                    day.TimeSlot[time]?.remove(act)
                                    println("If statement: $time, removed $ID")
                                }
                            }
                        }
                        else {
                            if (day.TimeSlot[time]?.get(0)?.Module  == ID){
                                day.TimeSlot[time]?.removeAt(0)
                                println("Else statement: $time, removed module $ID activity")
                            }
                        }
                    }
                    time += 0.5
                }
            }
        }

        this.modules.remove(ID)
    }

    fun addActivity(ID: Int, Term: Int, DayOfWeek: Int, ModuleID: Int, StartTime: Double, Duration: Double, ActivityType: Int){
        if (this.table[Term]?.days?.get(DayOfWeek)?.TimeSlot?.get(StartTime) == null){
            val act = Activity(ID, ModuleID, StartTime, Duration, ActivityType)
            var x = 0.0
            while (x < Duration + 0.5){
                this.table[Term]?.days?.get(DayOfWeek)?.TimeSlot?.set((StartTime + x), mutableListOf(act))
                x += 0.5
            }
        }
        else{
            var x = 0.0
            while (x < Duration + 0.5){
                val newActivity = Activity(ID, ModuleID, StartTime, Duration, ActivityType)
                val existingActivities = this.table[Term]?.days?.get(DayOfWeek)?.TimeSlot?.get(StartTime + x)

                if (existingActivities != null) {
                    existingActivities.add(newActivity)
                }
                else{
                    this.table[Term]?.days?.get(DayOfWeek)?.TimeSlot?.set((StartTime + x), mutableListOf(newActivity))
                }
                x += 0.5
            }
        }
    }

    fun removeActivity(ID: Int){
        this.activites.remove(ID)
    }

}



