package utils

object StringRes {
    val appTitle: String = "ADB Command Utility"
    val enterInputText: String
    val pullLocalDatabse: String
    val pushLocalDatabse: String

    init {
        when {
            System.getProperty("user.language").equals("es") -> {
                enterInputText = "Ingresa el texto"
                pullLocalDatabse = "Descargar LocalDatabase"
                pushLocalDatabse = "Subir LocalDatabase"
            }

            else -> {
                enterInputText = "Enter Input Text"
                pullLocalDatabse = "Download Local Database"
                pushLocalDatabse = "Push Local Database"
            }
        }
    }


}