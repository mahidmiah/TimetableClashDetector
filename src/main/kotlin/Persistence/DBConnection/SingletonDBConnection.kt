package Persistence.DBConnection

import java.io.File
import Vars.ConfigVars

/**
 * Singleton to hold a single DBConnection throughout the runtime
 */
class SingletonDBConnection {
    companion object {
        @JvmStatic var instance: DBConnection ? = null


        fun setConnection(dbConnection: DBConnection){
            SingletonDBConnection.instance = dbConnection
        }

        fun getConnection(): DBConnection {
            if (SingletonDBConnection.instance == null) {
                SingletonDBConnection.instance = DBConnection(ConfigVars().dbLocation)
            }
            return SingletonDBConnection.instance!!;
        }
    }

}