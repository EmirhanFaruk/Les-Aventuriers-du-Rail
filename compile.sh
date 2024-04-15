# Create the output folder if it doesn't exist
mkdir -p bin

# Compile all source files in src/java/com and its subfolders
find src/com/ -name "*.java" -print | xargs javac -d bin

echo "Compilation done!"