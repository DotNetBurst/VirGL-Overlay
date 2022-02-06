#!/system/bin/sh
#CONST PATHS
ANDROID_DATA_PATH=/data/data
BIN_FOLDER=/files/image/usr/bin/

#VARIABLES
BIN_NAME=vgob.exe
LAUNCHER_NAME=vo
ANY_INSTALLED=0
FOUND_UNINSTALLED=0

#FIND EXA AND INSTALL
for f in "$ANDROID_DATA_PATH"/*; do
    if [ -d "$f" ]; then
      case "$f" in
        *eltechs*)
          EXAGEAR_INSTALLATION_PATH=$f

          if [ -e "$EXAGEAR_INSTALLATION_PATH""$BIN_FOLDER" ]; then
            if [ -e "$EXAGEAR_INSTALLATION_PATH""$BIN_FOLDER""$BIN_NAME" ]; then
              ANY_INSTALLED=1
            else
              FOUND_UNINSTALLED=1
            fi
            if [ -e "$EXAGEAR_INSTALLATION_PATH""$BIN_FOLDER""$LAUNCHER_NAME" ]; then
              ANY_INSTALLED=1
            else
              FOUND_UNINSTALLED=1
            fi
          fi
          ;;
      esac
    fi
done

#RE
if [ $ANY_INSTALLED -eq 1 ] && [ $FOUND_UNINSTALLED -eq 0 ]; then
  exit 0
elif [ $ANY_INSTALLED -eq 1 ] && [ $FOUND_UNINSTALLED -eq 1 ]; then
  exit 1
else
  exit 2
fi
