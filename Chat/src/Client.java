import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String userName;

    private String roomNumber;

    public Client(Socket socket, String userName, String roomNumber){
        this.socket = socket;
        this.userName = userName;
        this.roomNumber = roomNumber;

        try{
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        } catch (IOException e){closeEverything();}
    }

    public void sendMessage(){
        try{
            bufferedWriter.write(userName); //--> clientHandler listening to this from the constructor
            bufferedWriter.newLine();
            bufferedWriter.flush();

            bufferedWriter.write(roomNumber);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner = new Scanner(System.in);
            while(socket.isConnected()){
                String msg = scanner.nextLine();
                //User requesting to leave the room and join other one
                if(msg.toLowerCase().contains("[server]:leave")){
                    String newRoom = msg.split(":")[2];
                    if(newRoom.equals(roomNumber)){
                        System.out.println("[SERVER]: You Already In This Room!");
                    }
                    else{
                        System.out.println("[SERVER]: You Just Left Room " + roomNumber + " And Joined Room " + newRoom);
                        System.out.println("ROOM: " +newRoom);
                        roomNumber = newRoom;
                        bufferedWriter.write(msg);
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                    }

                }
                else{
                    bufferedWriter.write(userName +": " + msg);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                }

            }

        } catch (IOException exc){
            closeEverything();
        }

    }

    private void closeEverything() {
        try{
            if(socket != null)
                socket.close();
            if(bufferedReader != null)
                bufferedReader.close();
            if(bufferedWriter != null)
                bufferedWriter.close();
        }catch (IOException e){e.printStackTrace();}
    }

    public void listenForNewMessages(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msg;
                while(socket.isConnected()){
                    try{
                        msg = bufferedReader.readLine();
                        System.out.println(msg);
                    }catch (IOException e){
                        closeEverything();
                    }
                }

            }
        }).start();
    }


    public static void main(String[] args){
        System.out.print("Please enter username: ");
        Scanner scannerName = new Scanner(System.in);
        String userName = scannerName.nextLine();

        System.out.print("Please enter room number you would like to join: ");
        Scanner scannerRoomNumber = new Scanner(System.in);
        String roomNumber = scannerRoomNumber.nextLine();
        System.out.println("ROOM: "+roomNumber);

        try{
            Socket socket = new Socket("localhost", 4999);
            Client client = new Client(socket, userName, roomNumber);
            client.listenForNewMessages();
            client.sendMessage(); //--> clientHandler is listening!
        } catch (IOException e){e.printStackTrace();}

    }
}
