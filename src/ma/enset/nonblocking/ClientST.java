package ma.enset.nonblocking;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class ClientST {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("localhost",4444));
        Scanner scanner=new Scanner(System.in);
        new Thread(()->{
            while (true){
                ByteBuffer byteBuffer=ByteBuffer.allocate(1024);
                try {
                    int dataSize=socketChannel.read(byteBuffer);
                    if (dataSize == -1) {
                        socketChannel.close();
                        return;
                    }
                    String message =new String(byteBuffer.array()).trim();
                    System.out.println("Response :\n"+message);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
        while(true){
            String request = scanner.nextLine();
            ByteBuffer byteBuffer=ByteBuffer.allocate(1024);
            byteBuffer.put(request.getBytes());
            byteBuffer.flip();
            socketChannel.write(byteBuffer);
        }
    }
}
