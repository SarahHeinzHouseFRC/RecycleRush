#include <SPI.h>  
#include <Pixy.h>

#include "SHARP_PixyObject.h"
#include "RecycleRushPixyVision.h"

Pixy pixy;

RecycleRushPixyVision vision(&pixy);

SHARP_PixyObject Box(1, 89, "Box");



//Player player1(1);
void setup()
{

  Serial.begin(9600);
  Serial.print("Starting...\n");

  vision.addGameObject(&Box);
  vision.init();
}

void loop()
{ 
  
  delay(1000);
  vision.update();
  
}
