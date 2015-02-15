#include "connector.h"
#include <QMenu>
#include <QToolTip>

Connector::Connector(Connector::Location location, Type type, string name)
{

    this->location = location;
    selected = false;
    constantReady = false;
    shouldRemove = false;
    constant = NULL;
    partOfConnection = false;
    this->type = type;
    this->name = name;

    this->setAcceptHoverEvents(true);

    setFlag(ItemStacksBehindParent);
    switch(type){

    case INT:
        this->pathToPixmap = ":/Icons/Resources/intConnector.png";
        break;
     case DOUBLE:
        this->pathToPixmap = ":/Icons/Resources/doubleConnector.png";
        break;
     case SEQUNTIAL:
        this->pathToPixmap = ":/Icons/Resources/sequentialConnector.png";
        break;
     case STATE:
        this->pathToPixmap = ":/Icons/Resources/intConnector.png";
        break;
    }

}

string Connector::getName()
{

    return name;

}

string Connector::getValue()
{
    if(constant != NULL){
    value = constant->getValue();
    }
     return value;

}
Connector::Location Connector::getLocation(){


    return location;


}

void Connector::paint(QPainter *painter, const QStyleOptionGraphicsItem *option, QWidget *widget)
{

    if(!selected){
        this->pixmap.load(QString::fromStdString(pathToPixmap));

    }
    else{
        this->pixmap.load(":/Icons/Resources/connectorSelected.png");
    }
    painter->drawPixmap(xCoord, yCoord, pixmap.width(),pixmap.height(),pixmap);
    update();
}

Constant *Connector::getConstant()
{

    return constant;
}



void Connector::mousePressEvent(QGraphicsSceneMouseEvent *event)
{
    if(event->button() == Qt::RightButton){


        QPoint globalPos;
        globalPos.setX(this->getX()+180);
        globalPos.setY(this->getY()+100);
        selected= true;
            // for QAbstractScrollArea and derived classes you would use:
            // QPoint globalPos = myWidget->viewport()->mapToGlobal(pos);

            QMenu myMenu;
            myMenu.addAction("Add Constant");
            myMenu.addAction("Remove Constant");

            QAction* selectedItem = myMenu.exec(globalPos);
            if(selectedItem !=NULL && this->type != SEQUNTIAL){
                if(selectedItem->iconText().toStdString() == "Add Constant"){
                    if(constant == NULL){
                        switch(this->type){
                        case INT:
                            this->constant = new Constant(this, Constant::INT);
                            break;
                        case DOUBLE:
                            this->constant = new Constant(this, Constant::DOUBLE);
                            break;
                        case STATE:
                            this->constant = new Constant(this, Constant::STATE);
                            break;

                        }

                    }

                    constantReady = true;
                    selected = false;
                }else if(selectedItem->iconText().toStdString() == "Remove Constant" && constant != NULL){
                     selected = false;
                     delete constant;
                     delete constant->getLine();
                     constant = NULL;

                 }
            }

    }else{

        if(selected == true){
            selected = false;
        }else{

            selected = true;

        }
    }
    update();
    QGraphicsItem::mousePressEvent(event);



}

void Connector::hoverEnterEvent(QGraphicsSceneHoverEvent *event)
{


    //QGraphicsScene::QGraphicsSceneHoverEvent(event);
}


bool Connector::constantIsReady()
{

    if(constantReady == NULL){

        return NULL;
    }else{

    return constantReady;
    }
}

void Connector::setInConnection()
{

    partOfConnection = true;
}

bool Connector::isPartOfConnection()
{

    return partOfConnection;
}

bool Connector::setConstantDone()
{

    constantReady = false;
}

Connector::Type Connector::getType()
{

    return type;
}
Connector::~Connector(){


    delete constant;

}

