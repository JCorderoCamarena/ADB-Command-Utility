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
    val pullLogTxt: String
    val selectListOfFilesToDownload: String
    init {
        when {
            System.getProperty("user.language").equals("es") -> {
                enterInputText = "Ingresa el texto"
                pullLocalDatabse = "Descargar LocalDatabase al Desktop"
                pushLocalDatabse = "Subir LocalDatabase del Desktop"
                enterSerial = "Ingresa el serial (10 digitos)"
                runDumpsys = "Ejecutar Dumpsys"
                runDumpsysCheckboxLabel = "Mostrar dumpsys para el activity actual"
                clearDumpsys = "Limpiar dumpsys"
                setAdbPathLabel = "Ingresa el path de ADB"
                pullLogTxt = "Descargar log.txt"
                selectListOfFilesToDownload = "Selecciona los archivos a descargar, se descargaran en una carpeta en el desktop con un timestamp de subfijo"
            }

            else -> {
                enterInputText = "Enter Input Text"
                pullLocalDatabse = "Download Local Database to Dektop"
                pushLocalDatabse = "Push Local Database from Desktop"
                enterSerial = "Enter serial (10 digits)"
                runDumpsys = "Run Dumpsys"
                runDumpsysCheckboxLabel = "Show dumspsys for current activity"
                clearDumpsys = "Clear"
                setAdbPathLabel = "Enter ADB Path"
                pullLogTxt = "Download log.txt"
                selectListOfFilesToDownload = "Check the files to download, these will be downloaded to a folder in your desktop with a timestamp suffix"
            }
        }
    }


}