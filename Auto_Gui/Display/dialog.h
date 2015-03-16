#ifndef DIALOG_H
#define DIALOG_H

#include <QDialog>
#include <QtGui>
#include <QtCore>
#include <QMenu>
#include "Objects/commandblock.h"
#include "Objects//menuitem.h"
#include "Managers/menumanager.h"
#include <thread>
#include "Display/buildcanvas.h"
#include <QProgressBar>
#include <fstream>
#include <boost/algorithm/string/join.hpp>
#include <unordered_map>
#include <sstream>



namespace Ui {
class Dialog;
}

class Dialog : public QDialog
{
    Q_OBJECT
    
public:
    explicit Dialog(QWidget *parent = 0);
    ~Dialog();
    MenuManager menuManagerMain;
    QThreadPool pool;
    QProgressBar * generateBar;

private:
    Ui::Dialog *ui;
    QGraphicsScene *driveMenuScene;
    QGraphicsScene *buildScene;
    BuildCanvas *buildView;
    AutonomousGuiObject *object;
    vector<MenuItem*> driveBlocks;
    vector<MenuItem*> constantBlocks;
    vector<AutonomousGuiObject> visionBlocks;
    vector<AutonomousGuiObject> timeoutBlocks;
    QTimer *timer;
    vector<vector<Connector*> > connections;
    void loadGuiElelements();


private slots:
    void on_graphicsView_destroyed();
    void updateMenuManager();

    void on_generateButton_released();
};

#endif // DIALOG_H