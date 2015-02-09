#include "buildcanvas.h"


BuildCanvas::BuildCanvas(QWidget *parent, MenuManager* menuManager):QGraphicsView(parent)
{
    this->menuManager = menuManager;
}

void BuildCanvas::mouseReleaseEvent(QMouseEvent *event)
{

    if(menuManager->itemIsSelected()){
       CommandBlock * command;
       string pathToPixmap;
       CommandBlock::Type type;

     switch(menuManager->getCurrentlySelectedItem()->getType()){


     case MenuItem::DRIVEFORWARD:
                pathToPixmap = ":/Icons/Resources/Drive GUI.png";
                type = CommandBlock::DRIVEFORWARD;
     break;

     case MenuItem::DRIVEBACKWARD:
         pathToPixmap = ":/Icons/Resources/driveBack.png";
         type = CommandBlock::DRIVEBACKWARD;
     break;

     case MenuItem::DRIVELEFT:
         pathToPixmap = ":/Icons/Resources/Drive GUI.png";
         type = CommandBlock::DRIVELEFT;
     break;

     case MenuItem::DRIVERIGHT:
         pathToPixmap = ":/Icons/Resources/Drive GUI.png";
         type = CommandBlock::DRIVERIGHT;
     break;
     case MenuItem::AUTOSTART:
         pathToPixmap = ":/Icons/Resources/Drive GUI.png";
         type = CommandBlock::AUTOSTART;
     break;

     break;
     case MenuItem::TIMEOUT:
         pathToPixmap = ":/Icons/Resources/Timeout.png";
         type = CommandBlock::TIMEOUT;
         break;

     case MenuItem::ELEVATORUP:
         type = CommandBlock::ELEVATORUP;
         break;

     case MenuItem::ELEVATORDOWN:
         type = CommandBlock::ELEVATORDOWN;
         break;

     case MenuItem::GRABTOTE:
         pathToPixmap = ":/Icons/Resources/Arm.png";
         type = CommandBlock::GRABTOTE;
         break;

     case MenuItem::RELEASETOTE:
         type = CommandBlock::RELEASETOTE;
         break;
        }

      command = new CommandBlock(pathToPixmap,type);
      if(type == CommandBlock::AUTOSTART){
          autoStart = command;
          printf("%d \n",autoStart);
      }
      command->setXY(event->pos().x(),event->pos().y()+100);
      command->setMoveable();
      this->scene()->addItem(command);
      activeCommands.push_back(command);

      for(int i =0; i < command->getConnectors()->size(); i++){
          command->setUpConnectors(event->pos().x(),event->pos().y()+100);
          universalConnectors.push_back(command->getConnectors()->at(i));
          //this->scene()->addItem(command->getConnectors()->at(i));


      }

menuManager->getCurrentlySelectedItem()->setNotCurrentlySelectedMenuItem();
menuManager->getCurrentlySelectedItem()->setNotSelected();

   }


    if(selectedConnectors.size() == 2 ){
        if(selectedConnectors.at(0)->getType() == Connector::SEQUNTIAL && selectedConnectors.at(1)->getType() == Connector::SEQUNTIAL){
             activeConnections.push_back(new Connection(selectedConnectors.at(0),
                                   selectedConnectors.at(1)));

            selectedConnectors.at(0)->setInConnection();
            selectedConnectors.at(1)->setInConnection();
        }
      selectedConnectors.at(0)->setNotSelected();
      selectedConnectors.at(1)->setNotSelected();
      //selectedConnectors.clear();
    }


    /*printf("%d",universalConnectors.size());
    //printf("%d",selectedConnectors.size());

        for(int i = 0; i < universalConnectors.size(); i++){
            if(universalConnectors.at(i)->toBeDeleted()){
                universalConnectors.erase(universalConnectors.begin()+i);
            }
        }


          if(menuManager->itemIsSelected()){


           switch(menuManager->getCurrentlySelectedItem()->getType()){


            case MenuItem::DRIVE:
               CommandBlock * CommandBlock = new CommandBlock("/home/lucas/Desktop/Auto_Gui/driveForward.png");
               CommandBlock->setXY(event->pos().x(),event->pos().y()+100);
               CommandBlock->setMoveable();
                this->scene()->addItem(CommandBlock);


                for(int i =0; i < CommandBlock->getConnectors()->size(); i++){
                    CommandBlock->setUpConnectors(event->pos().x(),event->pos().y()+100);
                    universalConnectors.push_back(CommandBlock->getConnectors()->at(i));
                    this->scene()->addItem(CommandBlock->getConnectors()->at(i));
                }

            break;

            }

    menuManager->getCurrentlySelectedItem()->setNotCurrentlySelectedMenuItem();
    menuManager->getCurrentlySelectedItem()->setNotSelected();

         }

          for(int i =0; i < selectedConnectors.size(); i++){

               if(!selectedConnectors.at(i)->isSelected()){
                   universalConnectors.push_back(selectedConnectors.at(i));
                   selectedConnectors.erase(selectedConnectors.begin() + i);
               }
           }

         for(int i = 0; i < universalConnectors.size(); i++){


             if(selectedConnectors.size() == 2){
                 selectedConnectors.at(1)->setNotSelected();
                 universalConnectors.push_back(selectedConnectors.at(1));
                 selectedConnectors.erase(selectedConnectors.begin()+1);
             }
             Connector * currentConnector = universalConnectors.at(i);
             if(currentConnector->isSelected()){
                 if(currentConnector->constantIsReady() && !currentConnector->toBeDeleted() && currentConnector->getConstant() != NULL){
                     scene()->addWidget(currentConnector->getConstant());
                     currentConnector->setNotSelected();
                     currentConnector->setConstantDone();
                 }

                  if(selectedConnectors.size() == 1 ){

                      selectedConnectors.push_back(currentConnector);
                     activeConnections.push_back(new Connection(selectedConnectors.at(0),
                                                  selectedConnectors.at(1)));
                      universalConnectors.erase(universalConnectors.begin()+i);
                     selectedConnectors.at(0)->setNotSelected();
                     selectedConnectors.at(1)->setNotSelected();
                     //selectedConnectors.clear();
                 }else{
                     selectedConnectors.push_back(currentConnector);
                      universalConnectors.erase(universalConnectors.begin()+i);


                 }

             }


         }
         for(int i =0; i < selectedConnectors.size(); i++){

              if(!selectedConnectors.at(i)->isSelected()){
                  universalConnectors.push_back(selectedConnectors.at(i));
                  selectedConnectors.erase(selectedConnectors.begin() + i);
              }
          }

         for(int i = 0; i < activeConnections.size();i++){
             Connection* currentConnection = activeConnections.at(i);
              if(currentConnection->shouldBeRemoved()){
                //this->scene()->removeLine(*currentConnection);

            }else if(!currentConnection->isInScene()){
                this->scene()->addLine(*currentConnection);
                  currentConnection->setInScene();
         }else{

                  currentConnection->update();


              }



         }

     */QGraphicsView::mouseReleaseEvent(event);
}
void BuildCanvas::updateCanvas(){

    //universalConnectors.shrink_to_fit();
    //selectedConnectors.shrink_to_fit();

    for(int i =0; i < universalConnectors.size(); i++){
         Connector * currentConnector = universalConnectors.at(i);
        if(currentConnector->toBeDeleted()){
            this->scene()->removeItem(currentConnector);
            if(currentConnector->getConstant() != NULL){
            this->scene()->removeItem(currentConnector->getConstant()->getLine());
            }
            universalConnectors.erase(universalConnectors.begin()+i);
            delete currentConnector;
        }

        //Rprintf("%d \n",universalConnectors.at(i));
    }

    for(int i = 0; i < activeCommands.size();i++){
        CommandBlock * currentCommand = activeCommands.at(i);
        if(currentCommand->toBeDeleted()){
            this->scene()->removeItem(currentCommand);
            activeCommands.erase(activeCommands.begin()+i);
            //delete currentCommand;

        }

}

    for(int i = 0; i < universalConnectors.size(); i++){

        Connector * currentConnector = universalConnectors.at(i);
        if(currentConnector->isSelected()){
            selectedConnectors.push_back(currentConnector);
            universalConnectors.erase(universalConnectors.begin()+i);

        }


       }
    for(int i = 0; i < activeConnections.size();i++){
        Connection* currentConnection = activeConnections.at(i);
         if(currentConnection->shouldBeRemoved()){
             activeConnections.erase(activeConnections.begin()+i);
             this->scene()->removeItem(currentConnection->getGraphic());
       }else if(!currentConnection->isInScene()){
           QGraphicsLineItem *graphic = this->scene()->addLine(*currentConnection);
           currentConnection->setGraphic(graphic);
             currentConnection->setInScene();
    }else{

             currentConnection->update();


         }
    }


    for(int i = 0; i < universalConnectors.size(); i++){
        Connector * currentConnector = universalConnectors.at(i);
        if(currentConnector->constantIsReady()){
            scene()->addWidget(currentConnector->getConstant());
            QGraphicsLineItem *graphic = scene()->addLine(currentConnector->getX()+6, currentConnector->getY(), currentConnector->getConstant()->x() + currentConnector->getConstant()->width()/2,
                             currentConnector->getConstant()->y() + currentConnector->getConstant()->height());
            currentConnector->getConstant()->setLine(graphic);
            currentConnector->setNotSelected();
            currentConnector->setConstantDone();
    }

    }


      for(int i =0; i < selectedConnectors.size(); i++){

           if(!selectedConnectors.at(i)->isSelected()){
               universalConnectors.push_back(selectedConnectors.at(i));
               selectedConnectors.erase(selectedConnectors.begin() + i);
           }
       }



}

 vector<CommandBlock* > BuildCanvas::orderConnections()
{
    bool firstFound = false;
    vector<CommandBlock* > orderedCommands;
    for(int i = 0; i < activeConnections.size(); i++){

       if(activeConnections.at(i)->getParentTO()->parentItem() == autoStart){
           orderedCommands.insert(orderedCommands.begin(),dynamic_cast<CommandBlock *>(activeConnections.at(i)->getParentFROM()->parentItem()));
           firstFound = true;
           break;
       }else if(activeConnections.at(i)->getParentFROM()->parentItem() == autoStart){
           orderedCommands.insert(orderedCommands.begin(),dynamic_cast<CommandBlock *>(activeConnections.at(i)->getParentTO()->parentItem()));
           firstFound = true;
           break;
       }
    }
    if(firstFound){
        printf("first found \n");
        int  z =0;
        printf(" Number of connections: %d \n",activeConnections.size());
         printf(" Number of commands: %d \n",activeCommands.size());
        while(orderedCommands.size() < (activeCommands.size()-1)){
            z++;
            CommandBlock* currentCommand = orderedCommands.at(orderedCommands.size()-1);
            Connector * rightOfCurrentCommand = currentCommand->getRightSideSequential();
            for(int i = 0; i < activeConnections.size(); i++){
                Connection * currentConnection = activeConnections.at(i);
                if(currentConnection->getParentFROM() == rightOfCurrentCommand && currentCommand != autoStart){
                    orderedCommands.push_back(dynamic_cast<CommandBlock *>(currentConnection->getParentTO()->parentItem()));
                     printf("next found \n");
                 }
                else if(currentConnection->getParentTO() == rightOfCurrentCommand && currentCommand != autoStart){
                    orderedCommands.push_back(dynamic_cast<CommandBlock *>(currentConnection->getParentFROM()->parentItem()));
                     printf("next found \n");

                }

            }
        }

    }

    return orderedCommands;
}
