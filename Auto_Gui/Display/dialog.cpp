#include "dialog.h"
#include "ui_dialog.h"
#include <libssh/sftp.h>

using std::make_pair;

Dialog::Dialog(QWidget *parent) :
    QDialog(parent),
    ui(new Ui::Dialog)
{
    ui->setupUi(this);
    //testScene->addItem(new DriveCommand("/home/lucas/Desktop/Auto_Gui/driveForward.png"));

    driveMenuScene = new QGraphicsScene(this);
    buildScene = new QGraphicsScene(this);
    buildView = new BuildCanvas(this, &menuManagerMain);
    buildView->setScene(buildScene);
    buildView->setGeometry(10,120,790,360);
    buildView->setSceneRect(10,120,770,340);

    //ui->driveGraphicsView->setScene(driveMenuScene);

    //buildScene->addItem(new MenuItem("/home/lucas/Desktop/Auto_Gui/driveForward.png", MenuItem::DRIVE));

    //ui->graphicsView->setContextMenuPolicy(Qt::CustomContextMenu);
    //connect(ui->graphicsView,SIGNAL(customContextMenuRequested(const QPoint)),this,SLOT(ShowContextMenu(QPoint)));

    loadGuiElelements();

    timer = new QTimer(this);
    connect(timer, SIGNAL(timeout()), this, SLOT(updateMenuManager()));
    timer->start(10);
    generateBar = this->ui->progressBar;

}

Dialog::~Dialog()
{
    delete ui;
}

void Dialog::loadGuiElelements()
{
    driveBlocks.push_back(new MenuItem(":/Icons/Resources/Drive GUI.png", MenuItem::DRIVEFORWARD, ui->driveTab));
    driveBlocks.push_back(new MenuItem(":/Icons/Resources/driveBack.png", MenuItem::DRIVEBACKWARD,ui->driveTab));
    driveBlocks.push_back(new MenuItem(":/Icons/Resources/driveRight.png", MenuItem::DRIVELEFT, ui->driveTab));
    driveBlocks.push_back(new MenuItem(":/Icons/Resources/driveLeft.png", MenuItem::DRIVERIGHT,ui->driveTab));
    driveBlocks.push_back(new MenuItem(":/Icons/Resources/driveForward.png", MenuItem::DRIVEFORWARD, ui->driveTab));

     driveBlocks.push_back(new MenuItem(":/Icons/Resources/Arm.png", MenuItem::GRABTOTE, ui->armTab));

    driveBlocks.push_back(new MenuItem("/home/lucas/Desktop/Auto_Gui/driveForward.png", MenuItem::AUTOSTART, ui->startStopTab));

    driveBlocks.push_back(new MenuItem(":/Icons/Resources/Timeout.png", MenuItem::TIMEOUT,ui->timeoutTab));



    menuManagerMain.changeCurrentMenu(&driveBlocks);

    int k = 0;
    for(int i = 0; i < driveBlocks.size(); i++)
    {
        if(i > 0){
            if(driveBlocks.at(i)->parent() != driveBlocks.at(i-1)->parent()) k = 0;}

        driveBlocks.at(i)->setGeometry(10+(k*80),10,61,61);
        k++;

    }

}

void Dialog::on_graphicsView_destroyed()
{

}
void Dialog::updateMenuManager(){
    timer->start(100);
    menuManagerMain.updateSelectedItem();
    buildView->updateCanvas();
}

void Dialog::on_generateButton_released()
{;
    string keys[] = {"ID","Drive Speed","Elevator Speed","Time Out", "Drive Distance", "Elevator Distance"};
    int sizeOfKeys = sizeof(keys) / sizeof(keys[0]);

    unordered_multimap<std::string,std::vector<string>*> outPutStuff;

    for(int i = 0; i < sizeOfKeys; i++){
        outPutStuff.insert(std::make_pair<string , vector<string>*>
                            (std::string(keys[i]),
                             new std::vector<string>(sizeOfKeys)));
    }

   vector<unordered_multimap<string,string>*> commandIOVector;
   ofstream csvFile;
   string sendCommmandCode;

    vector<CommandBlock* > orderedCommands = buildView->orderConnections();
    vector<string> commandCode;
        for(int i = 0; i < orderedCommands.size(); i++){
        orderedCommands.at(i)->getInputs();
        //ommandCode.push_back(std::to_string(orderedCommands.at(i)->getID()));
        commandIOVector.push_back(orderedCommands.at(i)->sendOutputs());
     }
        csvFile.open("/home/lucas/Desktop/auto.csv");

        sendCommmandCode = boost::algorithm::join(keys,",");
        csvFile << sendCommmandCode << endl;

       /* for(int i = 0; i < commandIOVector.size();i++){

            for(int k = 0; k  < sizeof(keys) / sizeof(keys[0]); k++){ 
                std::unordered_map<std::string,string>::const_iterator place  = currentCommandBlock->find(std::string(keys[k]));
                std::unordered_map<std::string,vector<string>*>::const_iterator placeVector = outPutStuff.find(std::string(keys[k]));
                if(place == currentCommandBlock->end()){
                    placeVector->second->push_back("0");
                    //printf("%d \n", 0);
                }else{
                    placeVector->second->push_back(place->second);
                }
            }
        }


        for(int i = 0; i < outPutStuff.size();i++){
            std::unordered_map<std::string,vector<string>*>::const_iterator place  = outPutStuff.find(std::string(keys[i]));
            vector<string> outPutString;
            for(int k = 0; k < place->second->size(); k++){
                if(place->second->at(k) != ""){
                outPutString.push_back(place->second->at(k));
                }
            }



       }*/



        for(int i = 0; i < commandIOVector.size(); i++){
            vector<string> outPutString;
            stringstream send;
            unordered_multimap<string,string>* currentCommand = commandIOVector.at(i);
                    for(int k =0; k < sizeOfKeys; k++){
                        std::unordered_map<std::string,string>::const_iterator place  = currentCommand->find(std::string(keys[k]));
                if(place == currentCommand->end()){
                    outPutString.push_back("0");
                }else{
                outPutString.push_back(place->second);
                }

            }
                    copy(outPutString.begin(), outPutString.end(), ostream_iterator<string>(send, ","));
                    csvFile << send.str() << endl;

        }

        csvFile.close();


        //sftp to the roboRIO

        // TODO: Find a better way to ftp files to the roboRIO

        /* -> cheaty way*/ : system("python :/Scripts/Python Scripts/ftpCSV.py");



}
