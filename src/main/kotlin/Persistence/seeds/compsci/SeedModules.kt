package Persistence.seeds.compsci
import Persistence.DBConnection.DBConnector
import Persistence.Entities.module.ModuleModel
import Persistence.Entities.module.ResultSetToModule
import Persistence.model.SelectAll

class SeedModules {
    companion object {
        const val jvmCode = "JVM";
        const val hciCode = "HCI";
    }
    fun seed(dbConn: DBConnector){
        val modules = ModuleModel().selectAll()

        var jvm = modules.find { m -> m.code == jvmCode }
        var hci = modules.find { m -> m.code == hciCode }

        if (jvm == null) {
            jvm = ModuleModel(null, jvmCode, jvmCode)
            val insertRes = ModuleModel(null, jvmCode, jvmCode).connSave(dbConn)
            jvm.id_module = insertRes.generatedKeys[0];
        }
        if (hci == null) {
            hci = ModuleModel(null, hciCode, hciCode)
            val insertRes = hci.connSave(dbConn)
            hci.id_module = insertRes.generatedKeys[0];
        }



    }
}