/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package steppermclient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 *
 * @author Sniderc3
 */
public class Client implements Runnable
{
    private ClientFrame parent = null;
    private boolean isActive = true;
    private Socket smtpSocket = null;   
    private OutputStream OutStream = null;
    private InputStream InStream = null;
    private MessageProcessor processor = null;
    private String Username = null;
            
    
    public Client(ClientFrame prnt, String usernm)
    {
        this.Username = usernm;
        this.parent = prnt;
        processor = new MessageProcessor(this, Username);        
    }
    
    public void run()
    {
       parent.write("Thread initiated");
       try 
       {    
            //SSLSocketFactory sslsocketfactory = (SSLSocketFactory) 
            //        SSLSocketFactory.getDefault();
            //SSLSocket smtpSocket = (SSLSocket) sslsocketfactory.createSocket(
             //   "127.0.0.1", 11500);

            //smtpSocket.startHandshake();
            smtpSocket = new Socket("raspberrypi", 8000);            
            OutStream = smtpSocket.getOutputStream();
            InStream = smtpSocket.getInputStream();

            while (smtpSocket.isConnected()) 
            { 
                //Set buffer size
                byte [] data = new byte[4096];                
                int bytesread = InStream.read(data);                
                byte [] holder = Arrays.copyOfRange(data, 0, bytesread);
                processor.ParseTheMessage(holder);                         
            }
       } 
       catch (UnknownHostException e) 
       {
            System.err.println("Don't know about host: hostname");
       } 
       catch (IOException e) 
       {
            System.err.println("Couldn't get I/O for the "
                    + "connection to: hostname");
       }  
        
    }
      
    public void SendTextMessage(String message)
    {
        if (smtpSocket != null) 
        {
            try 
            {               
                byte [] data = message.getBytes();                
                OutStream.write(data);
                OutStream.flush();      
            } 
            catch (IOException e) {
                System.err.println("IOException:  " + e);
            }
        }
    }
    
    public void CloseClient()
    {
        try 
        {
            InStream.close();
            OutStream.close();
            smtpSocket.close(); 
        }
        catch(IOException e)
        {
            System.out.println(e);
        }
    }    
    
    public void PostMessage(String message)
    {
        parent.write(message);
    }
    
}
