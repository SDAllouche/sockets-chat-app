package ma.enset.blocking;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MultiThreadChatServer extends Thread{

    int clientCount;
    private List<Conversation> conversations=new ArrayList<>();

    public static void main(String[] args) {
        new MultiThreadChatServer().start();
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket=new ServerSocket(1234);
            while (true){
                Socket socket = serverSocket.accept();
                ++clientCount;
                Conversation conversation=new Conversation(socket,clientCount);
                conversations.add(conversation);
                conversation.start();
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
                String message,request;
                while ((request=br.readLine())!=null){
                    List<Integer> clientTo=new ArrayList<>();
                    if (request.contains("=>")) {
                        String [] items=request.split("=>");
                        String clients=items[0];
                        message=items[1];
                        if (clients.contains(",")) {
                            String [] clientId=clients.split(",");
                            for (String id:clientId) {
                                clientTo.add(Integer.parseInt(id));
                            }
                        } else{
                            clientTo.add(Integer.parseInt(clients));
                        }
                    } else{
                        clientTo=conversations.stream().map(c->c.clientId).collect(Collectors.toList());
                        message=request;
                    }
                    broadcastMessage(message,this,clientTo);
                }
            } catch (IOException e) { throw new RuntimeException(e);}
        }
    }

    public void broadcastMessage(String message,Conversation from,List<Integer> clients) throws IOException {
        for (Conversation c:conversations) {
            if (c != from && clients.contains(c.clientId)) {
                Socket socket=c.socket;
                OutputStream os=socket.getOutputStream();
                PrintWriter pw=new PrintWriter(os,true);
                pw.println(message);
            }
        }
    }
}