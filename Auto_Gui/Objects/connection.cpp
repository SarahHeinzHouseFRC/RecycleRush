#include "connection.h"

Connection::Connection(Connector * connectionTO, Connector * connectionFROM)
{
    this->connectorTO = connectionTO;
    this->connectorFROM = connectionFROM;
    setLine(connectorTO->getX(), connectorTO->getY(), connectorFROM->getX(),connectorFROM->getY());
    shouldRemove = false;

}
bool Connection::isInScene()
{

    return inScene;
}

bool Connection::shouldBeRemoved()
{

    return shouldRemove;
}

void Connection::setInScene()
{
    inScene = true;
}
void Connection::update(){

    if(connectorTO->toBeDeleted() || connectorFROM->toBeDeleted()){
        shouldRemove = true;
    }

}


void Connection::setGraphic(QGraphicsLineItem *graphic)
{

    this->graphic = graphic;

}

Connector *Connection::getParentTO()
{

    return connectorTO;
}

Connector *Connection::getParentFROM()
{

    return connectorFROM;
}

QGraphicsLineItem *Connection::getGraphic()
{

    return graphic;


}



