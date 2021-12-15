package Vars

import java.io.File

class ConfigVars {
    val externalFolderName: String = "JVM_GROUP_6"
    var externalDirectory: String = ""
    var dbLocation: String = ""
    var resourcesDirectory: String = ""
    fun setupExternalDirectory(){

        if (this.externalDirectory != "") {
            return
        }
        val userDirectory = System.getProperty("user.home")
        val documentsDirectory = userDirectory + "/" + "Documents";
        var folderToBeCreated = userDirectory + "/" + this.externalFolderName;
        if (File(documentsDirectory).exists()) {
            folderToBeCreated = documentsDirectory + "/" + this.externalFolderName;
        }

        File(folderToBeCreated).mkdirs()
        this.externalDirectory = folderToBeCreated
    }

    fun setupDBLocation(){
        if (this.dbLocation != "") {
            return
        }
        this.setupResourcesFolder()
        val dbLocation = this.externalDirectory + "/greenwich_timetables.db"
        File(dbLocation).createNewFile()
        this.dbLocation = dbLocation
    }

    fun setupResourcesFolder(){
        if (this.resourcesDirectory != "") {
            return
        }
        val cl: ClassLoader = ClassLoader.getSystemClassLoader()
        val gitkeepFile = cl.getResource("./.gitkeep")
        println(gitkeepFile)
        if (gitkeepFile != null) {
            val tempResourcesDirectory: String = gitkeepFile.path.toString().replace(".gitkeep", "")
            this.resourcesDirectory = tempResourcesDirectory
            return
        }
        throw Exception(".gitkeep not found on resources folder")



    }
    init {
        this.setupExternalDirectory()
        this.setupResourcesFolder()
        this.setupDBLocation()

    }

}