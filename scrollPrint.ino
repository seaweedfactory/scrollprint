/*************************************************************************
Streaming Serial Printer for Scrolls
*************************************************************************/

/*************************************************************************
  This is an Arduino library for the Adafruit Thermal Printer.
  Pick one up at --> http://www.adafruit.com/products/597
  These printers use TTL serial to communicate, 2 pins are required.

  Adafruit invests time and resources providing this open source code.
  Please support Adafruit and open-source hardware by purchasing products
  from Adafruit!

  Written by Limor Fried/Ladyada for Adafruit Industries.
  MIT license, all text above must be included in any redistribution.
 *************************************************************************/

#include "SoftwareSerial.h"
#include "Adafruit_Thermal.h"
#include <avr/pgmspace.h>

int printer_RX_Pin = 5;  // This is the green wire
int printer_TX_Pin = 6;  // This is the yellow wire

Adafruit_Thermal printer(printer_RX_Pin, printer_TX_Pin);

char line[33];     //line buffer
char character;    //current character
int ccount;        //buffer index

void setup()
{
  Serial.begin(9600);
  pinMode(7, OUTPUT); digitalWrite(7, LOW); // To also work w/IoTP printer
  printer.begin();
  printer.println("");  //Print feeds when initialized
  printer.flush();
  ccount = 0;
}

void loop() 
{
  //Try to read serial data forever
  while (Serial.available() > 0) 
  {
    character = Serial.read();
    line[ccount] = character;
    
    //If we have a full line, print 
    if(ccount == 31)
    {
      line[32] = 0;
      printer.println(line);
      ccount = 0;
    }
    else
    {
      ccount = ccount + 1;
    }
  }
}
