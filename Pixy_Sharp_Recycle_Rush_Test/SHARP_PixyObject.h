#include <SHARP_PixyObject.h>

#ifndef SHARP_PixyObject_H
#define SHARP_PixyObject_H

#include "Arduino.h"
#include <SPI.h>  

class SHARP_PixyObject
{

public: 
  SHARP_PixyObject(int signature, int realHeight, char name[4]);
  void update(int heightInPixels);
  int getSignature();
  double getDistance(int heightInPixles);
  char* getName();
  
private:

  int _realHeight;  
  int _signature;
  
  char * _name;
};



#endif
