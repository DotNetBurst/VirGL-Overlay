#!/system/bin/sh
echo "VGOBridge installer v0.2"
#PATHS 
EXAGEAR_INSTALLATION_PATH=/data/data/com.eltechs.ed/files/image
LOCAL_CONTAINER_PATH_PREFIX=/home/
LOCAL_CONTAINER_PATH_POSTFIX=/.wine/drive_c/windows/system32/
GLOBAL_SYSTEM_PATH=/opt/guestcont-pattern/.wine/drive_c/windows/system32/
SOURCE=$2/installerBinary.exe
BIN_NAME=vgob.exe
NON_ROOT_USER_NAME=$(stat -c '%U' $EXAGEAR_INSTALLATION_PATH/bin/bash)

echo "Source folder : $SOURCE"
echo "User : $NON_ROOT_USER_NAME"

CheckFall() {
	EXITCODE=$?
	test $EXITCODE -eq 0 || exit $EXITCODE;
}

VGOBridgeInstall() {
  if [ $1 -eq 0 ]; then
    echo "VGOBridge will be installed in $2"
    cp "$SOURCE" "$2"
    CheckFall
    chmod +x "$2"
    CheckFall
    chown "$NON_ROOT_USER_NAME" "$2"
    CheckFall
    chgrp "$NON_ROOT_USER_NAME" "$2"
    CheckFall
  elif [ $1 -eq 1 ]; then
    echo "VGOBridge will be uninstalled from $2"
    rm "$2"
  fi
}

for dir in "$EXAGEAR_INSTALLATION_PATH$LOCAL_CONTAINER_PATH_PREFIX"*; do
    if [ -d "$dir" ]; then
        VGOBridgeInstall "$1" "$dir""$LOCAL_CONTAINER_PATH_POSTFIX""$BIN_NAME"
    fi
done

VGOBridgeInstall "$1" $EXAGEAR_INSTALLATION_PATH$GLOBAL_SYSTEM_PATH$BIN_NAME

exit 0
