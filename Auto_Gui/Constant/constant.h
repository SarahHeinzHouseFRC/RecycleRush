#ifndef CONSTANT_H
#define CONSTANT_H
#include <QTextEdit>
#include "Objects/autonomousguiobject.h"
class Constant : public QTextEdit
{
public:

    enum Type{
        INT,
        DOUBLE,
        STATE
    };

    Constant(AutonomousGuiObject *parent, Type type);
    bool canBeAdded();
    std::string getValue();
    Type type;
    void setLine(QGraphicsLineItem* line);
    QGraphicsLineItem* getLine();

private:
    QGraphicsLineItem * line;


};

#endif // CONSTANT_H
