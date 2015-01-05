#include "RecycleRushPixyVision.h"

RecycleRushPixyVision::RecycleRushPixyVision(Pixy *pixy){

  _pixy = pixy;
  
}

void RecycleRushPixyVision::init(){

_pixy->init();

}
void RecycleRushPixyVision::update(){
  if(readFromPixy()){
    for(int i =0; i < blocks; i++){
      for(int j =0; j < fieldObjects.size();j++){
        if(_pixy->blocks[i].signature == fieldObjects[j]->getSignature()){
        fieldObjects[j]->update(_pixy->blocks[i].height);
        }
      }
    }
  }
}
boolean RecycleRushPixyVision::readFromPixy(){


blocks = _pixy->getBlocks();

  if(blocks){

    return true;
  }
}

void RecycleRushPixyVision::addGameObject(SHARP_PixyObject* object){

fieldObjects.push_back(object);

}
