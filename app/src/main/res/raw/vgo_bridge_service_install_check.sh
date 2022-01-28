#!/system/bin/sh
echo "VGOBridge installer checker v0.1"
#PATHS
EXAGEAR_INSTALLATION_PATH=/data/data/com.eltechs.ed/files/image
LOCAL_CONTAINER_PATH_PREFIX=/home/
LOCAL_CONTAINER_PATH_POSTFIX=/.wine/drive_c/windows/system32/
GLOBAL_SYSTEM_PATH=/opt/guestcont-pattern/.wine/drive_c/windows/system32/
SOURCE=$1/installerBinary.exe
BIN_NAME=vgob.exe
NON_ROOT_USER_NAME=$(stat -c '%U' $EXAGEAR_INSTALLATION_PATH/bin/bash)

ANY_INSTALLED=0
FOUND_UNINSTALLED=0

for dir in "$EXAGEAR_INSTALLATION_PATH""$LOCAL_CONTAINER_PATH_PREFIX"*; do
    if [ -d "$dir" ]; then
        if [ -e "$dir""$LOCAL_CONTAINER_PATH_POSTFIX""$BIN_NAME" ]; then
            echo "Installed : " "$dir""$LOCAL_CONTAINER_PATH_POSTFIX""$BIN_NAME"
            ANY_INSTALLED=1
        else
            FOUND_UNINSTALLED=1
        fi
    fi
done

GLOBAL_PATH="$EXAGEAR_INSTALLATION_PATH""$GLOBAL_SYSTEM_PATH""$BIN_NAME"
if [ -e $GLOBAL_PATH ]; then
    echo "Global installed : " $GLOBAL_PATH
    ANY_INSTALLED=1
else
    FOUND_UNINSTALLED=1
fi

if [ $ANY_INSTALLED -eq 1 ] && [ $FOUND_UNINSTALLED -eq 0 ]; then
  exit 0
elif [ $ANY_INSTALLED -eq 1 ] && [ $FOUND_UNINSTALLED -eq 1 ]; then
  exit 1
else
  exit 2
fi