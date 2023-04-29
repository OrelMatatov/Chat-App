import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket clientSocket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    String clientUserName;
    String roomNumber;
    public ClientHandler(Socket clientSocket){

        try{
            this.clientSocket = clientSocket;
            bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            this.clientUserName = bufferedReader.readLine();
            this.roomNumber = bufferedReader.readLine();
            clientHandlers.add(this);
            broadcastMsg("[SERVER]: " + clientUserName + " has entered to room " +roomNumber+ "!");
        } catch (IOException e){
            closeEverything();
        }

    }

    @Override
    public void run() {
        //Separate Thread --> listening for messages
        String msgFromClient;
        while(!clientSocket.isClosed()){
            try{
                msgFromClient = bufferedReader.readLine();
                if(!checkServerCommands(msgFromClient))
                    broadcastMsg(msgFromClient);
            } catch (IOException e){
                closeEverything();
            }

        }
    }

    public boolean checkServerCommands(String msg){
        if(msg.toLowerCase().contains("[server]:leave")){
            broadcastMsg("[SERVER]: " +clientUserName + " has left the room!");
            roomNumber = msg.split(":")[2];
            broadcastMsg("[SERVER]: " +clientUserName + " has joined the room!");
            return true;
        }
        return false;
    }

    public void broadcastMsg(String msg){

        for(ClientHandler clientHandler: clientHandlers){
            try{
                if(!clientHandler.clientUserName.equals(clientUserName) && clientHandler.roomNumber.equals(roomNumber)){
                    clientHandler.bufferedWriter.write(msg);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            } catch (IOException e){e.printStackTrace();}
        }
    }

    public void removeClientHandler(){
        clientHandlers.remove(this);
        broadcastMsg("[SERVER]: " +clientUserName+ " has left the chat");
    }

    private void closeEverything() {
        removeClientHandler();
        try{
            if(clientSocket != null)
                clientSocket.close();
            if(bufferedReader != null)
                bufferedReader.close();
            if(bufferedWriter != null)
                bufferedWriter.close();
        }catch (IOException e){e.printStackTrace();}
    }
}
