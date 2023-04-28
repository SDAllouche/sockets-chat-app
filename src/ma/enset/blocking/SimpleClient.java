package ma.enset.blocking;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class SimpleClient {

    public static void main(String[] args) throws IOException {

        Socket socket=new Socket("localhost",1234);
        InputStream is=socket.getInputStream();
        OutputStream os=socket.getOutputStream();
        Scanner scanner=new Scanner(System.in);
        System.out.println("Give a new Number :");
        int request=scanner.nextInt();
        os.write(request);
        int response = is.read();
        System.out.println(response);

    }
}
