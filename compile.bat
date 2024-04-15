@echo off

REM Compile all source files in src/java/com and its subfolders recursively into the bin folder
for /r src\com %%f in (*.java) do (
    echo Compiling "%%f"
    javac -d bin -cp src "%%f"
)

echo Compilation done!
