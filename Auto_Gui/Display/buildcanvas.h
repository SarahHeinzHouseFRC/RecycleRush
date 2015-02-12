#ifndef BUILDCANVAS_H
#define BUILDCANVAS_H
#include "Managers/menumanager.h"
#include "QGraphicsView"
#include <QMouseEvent>
#include <Constant/constant.h>
#include <QGraphicsProxyWidget>
#include <QTimer>
#include <QProgressBar>
#include "Objects/connection.h"
#include "Objects/commandblock.h"
class BuildCanvas : public QGraphicsView
{
public:
    BuildCanvas(QWidget* parent, MenuManager *menuManager);
    void updateCanvas();
     vector<CommandBlock *> orderConnections();

signals:

protected:
    void mouseReleaseEvent(QMouseEvent *event);

private:
    MenuManager *menuManager;
    vector<Connector*> universalConnectors;
    vector<Connector* > selectedConnectors;
    vector<Connection *> activeConnections;
    vector<CommandBlock *> activeCommands;
    CommandBlock* autoStart;
    QTimer *timer;

};


#endif // BUILDCANVAS_H