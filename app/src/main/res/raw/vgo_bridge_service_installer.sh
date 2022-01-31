#!/system/bin/sh
echo "VGOBridge installer v0.2"
#PATHS
EXAGEAR_INSTALLATION_PATH=/data/data/com.eltechs.ed/files/image
BIN_FOLDER=/usr/bin
INSTALL_MODE=$1
FILES_DIR=$2
#VARIABLES
BIN_NAME=vgob.exe
LAUNCHER_NAME=vgo
NON_ROOT_USER_NAME=$(stat -c '%U' $EXAGEAR_INSTALLATION_PATH/bin/bash)
#INFO
echo "Source folder : $SOURCE"
echo "User : $NON_ROOT_USER_NAME"
#FAIL CHECK
CheckFall() {
	EXITCODE=$?
	test $EXITCODE -eq 0 || exit $EXITCODE;
}
#RIGHTS
VGOBridgeSetRights() {
  chmod +x "$1"
  CheckFall
  chown "$NON_ROOT_USER_NAME" "$1"
  CheckFall
  chgrp "$NON_ROOT_USER_NAME" "$1"
  CheckFall
}
#INSTALL UNINSTALL METHOD
VGOBridgeInstall() {
  IMODE=$1
  SPATH=$2/$4
  IPATH=$3/$4

  if [ "$IMODE" -eq 0 ]; then
    cp "$SPATH" "$IPATH"
    CheckFall
    VGOBridgeSetRights "$IPATH"
    CheckFall
  elif [ "$IMODE" -eq 1 ]; then
    rm "$IPATH"
  fi
}
#INSTALL LAUNCHER
VGOBridgeInstall "$INSTALL_MODE" "$FILES_DIR" "$EXAGEAR_INSTALLATION_PATH""$BIN_FOLDER" "$LAUNCHER_NAME"
#INSTALL APP
VGOBridgeInstall "$INSTALL_MODE" "$FILES_DIR" "$EXAGEAR_INSTALLATION_PATH""$BIN_FOLDER" "$BIN_NAME"

exit 0
