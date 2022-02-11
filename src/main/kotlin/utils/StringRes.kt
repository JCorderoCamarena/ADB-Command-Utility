package utils

object StringRes {
    val appTitle: String = "ADB Command Utility"
    val enterInputText: String
    val pullLocalDatabse: String
    val pushLocalDatabse: String
    val enterSerial: String
    val runDumpsys: String
    val runDumpsysCheckboxLabel: String
    val clearDumpsys: String
    val setAdbPathLabel: String

    init {
        when {
            System.getProperty("user.language").equals("es") -> {
                enterInputText = "Ingresa el texto"
                pullLocalDatabse = "Descargar LocalDatabase"
                pushLocalDatabse = "Subir LocalDatabase"
                enterSerial = "Ingresa el serial (10 digitos)"
                runDumpsys = "Ejecutar Dumpsys"
                runDumpsysCheckboxLabel = "Mostrar dumpsys para el activity actual"
                clearDumpsys = "Limpiar dumpsys"
                setAdbPathLabel = "Ingresa el path de ADB"
            }

            else -> {
                enterInputText = "Enter Input Text"
                pullLocalDatabse = "Download Local Database"
                pushLocalDatabse = "Push Local Database"
                enterSerial = "Enter serial (10 digits)"
                runDumpsys = "Run Dumpsys"
                runDumpsysCheckboxLabel = "Show dumspsys for current activity"
                clearDumpsys = "Clear"
                setAdbPathLabel = "Enter ADB Path"
            }
        }
    }


}