#include "SHARP_PixyObject.h"
#define RATE_OF_RUN 1
#define SENSOR_HEIGHT 1 //mm
#define PIXY_FOCAL_LENGTH 2.88//mm
#define PIXY_IMAGE_HEIGHT 400

SHARP_PixyObject::SHARP_PixyObject(int signature, int realHeight, char name[4]){

_signature = signature;
_realHeight = realHeight;
_name = name;


}

void SHARP_PixyObject::update(int heightInPixels){
char buf[64];



//sprintf(buf, "Distance to %s : %d",_name,getDistance(heightInPixels));

Serial.println(getDistance(heightInPixels));

}

double SHARP_PixyObject::getDistance(int heightInPixels){
  

double distance = ((PIXY_FOCAL_LENGTH * _realHeight * PIXY_IMAGE_HEIGHT) / (heightInPixels * SENSOR_HEIGHT)) / 100;


return distance;
}


int SHARP_PixyObject::getSignature(){

return _signature;
}


char*  SHARP_PixyObject::getName(){

return _name;
}
