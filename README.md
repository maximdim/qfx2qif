# qfx2qif converter in Java
Convert qfx files to qif format

For old finance applications that import .qif files
but can't handle .qfx files

Build with maven:

    mvn clean package

This should produce few .jar files in 'target' folder. One of them would have a '-shaded' in the name. 
That's the one you need to run the converter. 

Usage: 

    java -jar qfx2qif-0.0.1-SNAPSHOT-shaded.jar inputFile [outputFile]

The input file should be a .qfx file. Output file name can be omitted, in such case it will be named after the input file with '-converted.qif' at the end. 
