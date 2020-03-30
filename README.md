# image2htmltable

Converting an image to an HTML table made of 1px x 1px cells has never been easier

## Build

### Prerequisites:
- JDK 11
- Maven

### Compiling
From the project root folder run the following command:
```
mvn package
```
Maven should take care of the compilation process, creating
a JAR file in <project-root>/target/

## Usage

### From CLI

```
java -jar <compiled-jar> /path/to/img
```

### From GUI

Launch the jar file, it will ask you which image you want to convert
with a dialog window

### Output

Both CLI and GUI interfaces output the generated HTML file in the same
directory of the original image file, with the same file name + ".html"