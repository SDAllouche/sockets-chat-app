package ma.enset.blocking;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleServer {

    public static void main(String[] args) throws IOException {

        ServerSocket ss=new ServerSocket(1234);
        System.out.println("------------------");
        System.out.println("I m waiting new connection");
        Socket socket = ss.accept();
        InputStream is=socket.getInputStream();
        OutputStream os=socket.getOutputStream();
        System.out.println("I m waiting a request");
        int data=is.read();
        int response=data*23;
        System.out.println("Sending response");
        os.write(response);
        socket.close();
    }

    public SimpleServer() throws IOException {
    }
}
