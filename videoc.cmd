SET SCRIPT_DIR=%~dp0

ECHO Programm Requires Java 21

SET LIB_DIR=%SCRIPT_DIR%

SET CLASSPATH=%LIB_DIR%videoconverter-0.1.4-SNAPSHOT.jar;%LIB_DIR%kotlin-stdlib-1.9.0.jar;%LIB_DIR%kotlinx-serialization-core-jvm-1.9.0.jar;%LIB_DIR%kotlinx-serialization-json-jvm-1.9.0.jar;%LIB_DIR%daras_library-0.1.4-SNAPSHOT.jar

java -cp "%CLASSPATH%" de.shadowdara.videoconverter.MainKt %*
