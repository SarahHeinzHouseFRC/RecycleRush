#ifndef RecycleRushPixyVision_H
#define RecycleRushPixyVision_H

#include <Pixy.h>
#include "SHARP_PixyObject.h"
#include "vector.h"

class RecycleRushPixyVision
{

public:

  

   RecycleRushPixyVision(Pixy* pixy);
   void update();
   void init();
   void addGameObject(SHARP_PixyObject* object);
   
   
   

private:
    
  boolean readFromPixy();
  
  Pixy* _pixy;
  Vector<SHARP_PixyObject*> fieldObjects;
  uint16_t blocks;
 };  

#endif


