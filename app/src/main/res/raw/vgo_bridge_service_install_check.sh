#!/system/bin/sh
echo "VGOBridge installer checker v0.1"
#PATHS
EXAGEAR_INSTALLATION_PATH=/data/data/com.eltechs.ed/files/image
BIN_FOLDER=/usr/bin/
BIN_NAME=vgob.exe
LAUNCHER_NAME=vgo
ANY_INSTALLED=0
FOUND_UNINSTALLED=0
#CHECK1
GLOBAL_PATH="$EXAGEAR_INSTALLATION_PATH""$BIN_FOLDER""$BIN_NAME"
if [ -e $GLOBAL_PATH ]; then
    echo "Global installed : " $GLOBAL_PATH
    ANY_INSTALLED=1
else
    FOUND_UNINSTALLED=1
fi
#CHECK2
GLOBAL_PATH="$EXAGEAR_INSTALLATION_PATH""$BIN_FOLDER""$LAUNCHER_NAME"
if [ -e $GLOBAL_PATH ]; then
    echo "Global installed : " $GLOBAL_PATH
    ANY_INSTALLED=1
else
    FOUND_UNINSTALLED=1
fi
#RE
if [ $ANY_INSTALLED -eq 1 ] && [ $FOUND_UNINSTALLED -eq 0 ]; then
  exit 0
elif [ $ANY_INSTALLED -eq 1 ] && [ $FOUND_UNINSTALLED -eq 1 ]; then
  exit 1
else
  exit 2
fi