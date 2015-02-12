using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace NetConsole_GUI
{
    public partial class MainForm : Form
    {
        const int UDP_IN_PORT = 6666, UDP_OUT_PORT = 6668;

        UdpClient listener;
        IPEndPoint groupEndPoint;

        Socket sendingSocket;
        IPAddress broadcastAddress;
        IPEndPoint broadcastEndPoint;

        Boolean doListen = true;

        public MainForm()
        {
            InitializeComponent();

            this.Size = new Size(Width, Height);
            
            listener = new UdpClient(UDP_IN_PORT);
            groupEndPoint = new IPEndPoint(IPAddress.Any, UDP_IN_PORT);

            sendingSocket = new Socket(AddressFamily.InterNetwork, SocketType.Dgram, ProtocolType.Udp);
            broadcastAddress = IPAddress.Parse("255.255.255.255");
            broadcastEndPoint = new IPEndPoint(IPAddress.Any, UDP_OUT_PORT);

            sendingSocket.Bind(broadcastEndPoint);

            txt_Received.Text += "Created socket to listen on port " + UDP_IN_PORT + ".\n";
            txt_Received.Text += "Ready to send on port on " + UDP_OUT_PORT + " to address " + broadcastAddress.ToString() + ".\n";

            string receivedData;
            byte[] receiveByteArray;

            Thread listenerThread = new Thread(() =>
                {
                    while(doListen)
                    {
                        try
                        {
                            receiveByteArray = listener.Receive(ref groupEndPoint);
                            receivedData = Encoding.ASCII.GetString(receiveByteArray, 0, receiveByteArray.Length);
                            updateReceivedText(receivedData);
                        }
                        catch (Exception ex)
                        {
                            Console.WriteLine(ex.ToString());
                        }
                    }

                    listener.Close();
                });

            listenerThread.IsBackground = true;
            listenerThread.Start();
        }

        private void btn_SendCommand_Click(object sender, EventArgs e)
        {
            if(txt_SendText.TextLength == 0)
            {
            }
            else 
            {
                byte[] sendBuffer = Encoding.ASCII.GetBytes(txt_SendText.Text);

                try 
                {
                    sendingSocket.SendTo(sendBuffer, broadcastEndPoint);
                }
                catch(Exception ex)
                {
                    updateReceivedText("[NetConsole] Exception while sending, " + ex.ToString());
                }
            }
        }

        /// <summary>
        /// Helper method to determin if invoke required, if so will rerun method on correct thread.
        /// if not do nothing.
        /// </summary>
        /// <param name="c">Control that might require invoking</param>
        /// <param name="a">action to preform on control thread if so.</param>
        /// <returns>true if invoke required</returns>
        public bool ControlInvokeRequired(Control c, Action a)
        {
            if (c.InvokeRequired) c.Invoke(new MethodInvoker(delegate { a(); }));
            else return false;

            return true;
        }

        public void updateReceivedText(String text)
        {
            if (ControlInvokeRequired(txt_Received, () => updateReceivedText(text)))
            {
                return;
            }

            int startPos = txt_Received.TextLength;

            txt_Received.AppendText(text);

            txt_Received.Select(startPos, txt_Received.TextLength);

            if(text.ToLower().Contains("error") || text.ToLower().Contains("exception"))
            {
                txt_Received.SelectionColor = Color.Red;
            }
        }

        private void txt_Received_TextChanged(object sender, EventArgs e)
        {
            txt_Received.Select(txt_Received.Text.Length, 0);
            txt_Received.ScrollToCaret();
        }

        private void MainForm_Resize(object sender, System.EventArgs e)
        {
            mainFlowPanel.Size = this.Size;
            secondaryFlowPanel.Size = new Size(mainFlowPanel.Width, secondaryFlowPanel.Height);

            txt_Received.Size = new Size(mainFlowPanel.Width - 22, mainFlowPanel.Size.Height - 86);

            txt_SendText.Width = secondaryFlowPanel.Size.Width - btn_SendCommand.Width - 35;

            txt_SendText.Height = mainFlowPanel.Size.Height - secondaryFlowPanel.Size.Height;
        }
    }
}
