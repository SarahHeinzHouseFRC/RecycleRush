#include "constant.h"

Constant::Constant(AutonomousGuiObject *parent, Type type):QTextEdit()
{
    this->type = type;
    this->setGeometry(parent->getX()-7,parent->getY()-30,100,100);
    this->setMaximumWidth(25);
    this->setMaximumHeight(25); 
    this->setHorizontalScrollBarPolicy(Qt::ScrollBarAlwaysOff);
    this->setVerticalScrollBarPolicy(Qt::ScrollBarAlwaysOff);
    this->setAlignment(Qt::AlignCenter);
    this->setAutoFillBackground(true);


    switch(type){

    case INT:
        this->setStyleSheet("border: 4px solid blue");
    break;

    case DOUBLE:
        this->setStyleSheet("border: 4px solid orange");
    break;

    case STATE:
        this->setStyleSheet("border: 4px solid pink");
    break;

 }
}


std::string Constant::getValue()
{

    return this->toPlainText().toStdString();
}

void Constant::setLine(QGraphicsLineItem *line)
{
    this->line = line;

}

QGraphicsLineItem *Constant::getLine()
{

    return line;
}
