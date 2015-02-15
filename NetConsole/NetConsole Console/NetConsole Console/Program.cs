using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading.Tasks;

namespace NetConsole_Console
{
    class Program
    {
        const int UDP_IN_PORT = 6666, UDP_OUT_PORT = 6668;

        static void Main(string[] args)
        {
            UdpClient listener = new UdpClient(UDP_IN_PORT);
            IPEndPoint groupEndPoint = new IPEndPoint(IPAddress.Any, UDP_IN_PORT);

            string receivedData;
            byte[] receiveByteArray;

            Boolean done = false;

            try
            {
                while(!done)
                {
                    receiveByteArray = listener.Receive(ref groupEndPoint);

                    Console.WriteLine("Broadcast Received {0}", groupEndPoint.ToString());
                    receivedData = Encoding.ASCII.GetString(receiveByteArray, 0, receiveByteArray.Length);
                    Console.WriteLine("data follows \n{0}\n\n", receivedData);
                }
            }
            catch(Exception ex)
            {
                Console.WriteLine(ex.ToString());
            }

            listener.Close();

            Socket sendingSocket = new Socket(AddressFamily.InterNetwork, SocketType.Dgram, ProtocolType.Udp);
            IPAddress broadcastAddress = IPAddress.Parse("255.255.255.255");
            IPEndPoint broadcastEndPoint = new IPEndPoint(broadcastAddress, UDP_OUT_PORT);
        }
    }
}
