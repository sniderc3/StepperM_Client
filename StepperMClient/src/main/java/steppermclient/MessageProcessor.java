/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package steppermclient;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Sniderc3
 */
public class MessageProcessor 
{
    Client parent = null;
    String Username = null;
    
    public MessageProcessor(Client clnt, String Username)
    {
        this.parent = clnt;
        this.Username = Username;
    }    
    
    public void ParseTheMessage(byte [] message)
    {
        String msg = new String(message);
        List<String> words = Arrays.asList(msg.split(","));
        
        switch(words.get(0))
        {
            case "Transmit Username":
                SendUsername();
                break;
            case "Game List":
                ProcessGameList(words);
                break;
            case "username":
                ReceivedMessage(words);
                break;
            default:
                System.out.println("Cannot process incoming string command");
                break;
        }
    }
    
    private void SendUsername()
    {        
        parent.SendTextMessage("Username, " + Username + ",");
    }
    
    private void ProcessGameList(List<String> words)
    {
        String GameList = "Games: ";
        Iterator<String> iterator = words.iterator();
        iterator.next();
        while(iterator.hasNext())
        {
            GameList += iterator.next() + ", ";            
        }
        
        parent.PostMessage(GameList);
    }
    
    private void ReceivedMessage(List<String> message)
    {       
       parent.PostMessage("Received message from " + message.get(1));
    }
}

