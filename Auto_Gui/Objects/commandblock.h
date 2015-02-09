#ifndef COMMANDBLOCK_H
#define COMMANDBLOCK_H
#include "connector.h"
#include <unordered_map>
#include <QMenu>

using namespace std;

class CommandBlock : public AutonomousGuiObject
{
public:

    enum Type{

        DRIVEFORWARD,
        DRIVEBACKWARD,
        DRIVELEFT,
        DRIVERIGHT,
        AUTOSTART,
        TIMEOUT,
        ELEVATORUP,
        ELEVATORDOWN,
        GRABTOTE,
        RELEASETOTE
    };

    CommandBlock(string pathToPixmap, CommandBlock::Type type);
    void getInputs();
    unordered_multimap<string,string>* sendOutputs();
    void setUpConnectors(int x, int y);
    vector<Connector*>* getConnectors();
    std::string getName();
    Connector* getLeftSideSequential();
    Connector* getRightSideSequential();
    int getID();

    ~CommandBlock();
private:
    Connector* leftSequential;
    Connector * rightSequential;
    int ID;

protected:
    unordered_multimap<string,string> commandIO;
    vector<Connector*> connectors;
    void mousePressEvent(QGraphicsSceneMouseEvent *event);
};

#endif // COMMANDBLOCK_H
