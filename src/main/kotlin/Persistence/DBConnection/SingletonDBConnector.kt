package Persistence.DBConnection

import Vars.ConfigVars

/**
 * Singleton to hold a single DBConnection throughout the runtime
 */
class SingletonDBConnector {
    companion object {
        @JvmStatic var instance: DBConnector ? = null


        fun setConnection(dbConnector: DBConnector){
            SingletonDBConnector.instance = dbConnector
        }

        fun getConnector(): DBConnector {
            if (SingletonDBConnector.instance == null) {
                SingletonDBConnector.instance = DBConnector(ConfigVars().dbLocation)
            }
            return SingletonDBConnector.instance!!;
        }
    }

}