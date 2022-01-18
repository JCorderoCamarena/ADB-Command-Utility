# ADB-Command-Utility
Desktop application to easily interact with the common commands used when working in specific projects.

## Instalation:
### MacOS
- Once you have the dmg file.
- Attempt to install it by double-click it, an error will be shown when trying to open it.
- After that and based on this [link](https://apple.stackexchange.com/questions/243687/allow-applications-downloaded-from-anywhere-in-macos-sierra), you need to run:
```
sudo xattr -r -d com.apple.quarantine /Applications/ADB-Command-Utility.app
```
- Install de app again if it was removed.
- You should be ready to go.



### Application to easily run commands such as:
- Pull LocalDatabase.db
- Push LocalDatabase.db
- Input text (adb shell input text)
- Set Serial Number
- Dumpsys activity
