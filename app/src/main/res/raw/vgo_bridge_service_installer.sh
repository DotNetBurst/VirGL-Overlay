#!/system/bin/sh
echo " "
echo " "
echo "VGOBridge installer v0.3 by DotNetBurst"
echo "This program designed to install and uninstall vgob.exe"
echo "Vgob.exe - a native bridge service to provide new input in exagear!"
echo "dotnetburst@gmail.com"
echo " "

#CONST PATHS
ANDROID_DATA_PATH=/data/data
BIN_FOLDER=/files/image/usr/bin
BASH_EXEC=/files/image/bin/bash

#VARIABLES
BIN_NAME=vgob.exe
LAUNCHER_NAME=vgo
INSTALL_MODE=$1
FILES_DIR=$2

#OUTLOG
Log() {
  echo 'VGOBInstaller : '"$1"
}


#FAIL CHECK
CheckFallDie() {
	EXITCODE=$?
	test $EXITCODE -eq 0 || exit $EXITCODE;
}
CheckFall() {
	EXITCODE=$?
	if [ $EXITCODE -ne 0 ]; then
    return 0;
  fi
  return 1;
}

#RIGHTS
VGOBridgeSetRights() {
  Log "Rights will be set for [$NON_ROOT_USER_NAME] user"
  chmod +x "$1"
  CheckFallDie
  chown "$NON_ROOT_USER_NAME" "$1"
  CheckFallDie
  chgrp "$NON_ROOT_USER_NAME" "$1"
  CheckFallDie
  Log "Now service executable and owned by exagear!"
}
#INSTALL UNINSTALL METHOD
VGOBridgeInstall() {
  IMODE=$1
  SPATH=$2/$4
  IPATH=$3/$4

  if [ "$IMODE" -eq 0 ]; then
    Log "Installing in $IPATH"
    cp "$SPATH" "$IPATH"
    if CheckFall -eq 1; then
      echo "Cant install in selected directory, its not an exagear emulator path ... skipping!"
      return 1;
    fi

    VGOBridgeSetRights "$IPATH"
    CheckFallDie
    Log "Install done!"
  elif [ "$IMODE" -eq 1 ]; then
    Log "Uninstalling from $IPATH"
    rm "$IPATH"
  fi
  return 0;
}

#FIND EXA AND INSTALL
Log "Scanning paths..."
for f in "$ANDROID_DATA_PATH"/*; do
    if [ -d "$f" ]; then
      case "$f" in
        *eltechs*)
          EXAGEAR_INSTALLATION_PATH=$f
          Log "Service location : $EXAGEAR_INSTALLATION_PATH"
          NON_ROOT_USER_NAME=$(stat -c '%U' "$EXAGEAR_INSTALLATION_PATH""$BASH_EXEC")

          #INSTALL LAUNCHER
          if $(VGOBridgeInstall "$INSTALL_MODE" "$FILES_DIR" "$EXAGEAR_INSTALLATION_PATH""$BIN_FOLDER" "$LAUNCHER_NAME") -eq 1; then
            continue
          fi
          #INSTALL APP
          VGOBridgeInstall "$INSTALL_MODE" "$FILES_DIR" "$EXAGEAR_INSTALLATION_PATH""$BIN_FOLDER" "$BIN_NAME"
          ;;
      esac
    fi
done

Log "Done!"
exit 0
