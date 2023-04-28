package ma.enset.blocking;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiThreadServer extends Thread{

    int clientCount;

    public static void main(String[] args) {
        new MultiThreadServer().start();
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket=new ServerSocket(1234);
            while (true){
                Socket socket = serverSocket.accept();
                ++clientCount;
                new Conversation(socket,clientCount).start();
            }
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    class Conversation extends Thread{

        private Socket socket;
        private int clientId;
        Conversation(Socket socket,int clientId) {
            this.clientId=clientId;
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                InputStream is=socket.getInputStream();
                InputStreamReader isr=new InputStreamReader(is);
                BufferedReader br=new BufferedReader(isr);
                PrintWriter pw=new PrintWriter(socket.getOutputStream(),true);
                System.out.println("New Client connection => "+clientId+" IP : "+socket.getRemoteSocketAddress().toString());
                pw.println("Welcome ... ");
                String request;
                while ((request=br.readLine())!=null){
                    String response="Request length => "+request.length();
                    pw.println(response);
                }
            } catch (IOException e) { throw new RuntimeException(e);}
        }
    }
}