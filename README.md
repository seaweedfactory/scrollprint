# ScrollPrint
Basic cross-platform utility for printing scrolls from a receipt printer device.

ScrollPrint uses a thermal printer and arduino uno to print scrolls from a text file. Each line of the text file should contain 32 characters, no blank lines allowed. Save the file with no formatting; use the MSDOS text format option if available. An example text file is included.

# RXTX Library
This program requires RXTX binaries to be installed. See the following for installation instructions:

http://rxtx.qbang.org/wiki/index.php/Installation_for_Windows
http://rxtx.qbang.org/wiki/index.php/Installation_on_MacOS_X

#Java Graphical Interface
A graphical interface is available by running the ScrollPrint.jar file. From the command line run the following:

java -jar ScrollPrint.jar

The source for this application is included. The project was written using Netbeans 7.1.

#Arduino Sketch
The sketch labeled scrollPrint.ino must be downloaded to the arduino uno. This sketch relies upon the Thermal Printer Library from adafruit. The interface circuit and library can be found at adafruit:

http://learn.adafruit.com/mini-thermal-receipt-printer/overview


#ASCII Converters
If printing ASCII art, try the following converters:

http://picascii.com/
Feed it images 96 pixels wide to get 32 characters back

http://www.text-image.com/convert/ascii.html
Set character width to 32, extra contrast helps

It helps to squish the long axis of anything you are printing by about 75 percent due to line spacing.


Send any questions or comments to seaweedfactory@gmail.com
