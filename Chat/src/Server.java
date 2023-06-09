import java.io.*;
import java.net.*;

public class Server {

    ServerSocket serverSocket;
    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    private void startServer(){
        try {
            while(!serverSocket.isClosed()){
                Socket socket = serverSocket.accept(); // A new client has entered
                System.out.println("A new client has connected");
                ClientHandler clientHandler = new ClientHandler(socket);
                new Thread(clientHandler).start();
            }
        } catch (IOException e){e.printStackTrace();};
    }

    private void closeServer(){
        try{
            if(serverSocket != null){
                serverSocket.close();
            }
        } catch (IOException e){e.printStackTrace();}
    }


    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket( 4999);
        Server server = new Server(serverSocket);
        server.startServer();
    }
}
