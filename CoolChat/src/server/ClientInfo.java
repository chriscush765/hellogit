package server;

import java.net.Socket;

public class ClientInfo
{
    public Socket mSocket = null;
    public ClientListener mClientListener = null;
    public ClientSender mClientSender = null;
    public String name = null;
}