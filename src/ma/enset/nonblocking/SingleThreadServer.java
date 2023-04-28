package ma.enset.nonblocking;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class SingleThreadServer {
    public static void main(String[] args) throws IOException {
        Selector selector=Selector.open();
        ServerSocketChannel serverSocketChannel =ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress("0.0.0.0", 4444));
        int validOps=serverSocketChannel.validOps();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (true){
            int readyCount=selector.select();
            if(readyCount==0) continue;
            Set<SelectionKey> selectionKeys=selector.selectedKeys();
            Iterator<SelectionKey> keys=selectionKeys.iterator();
            while(keys.hasNext()){
                SelectionKey selectionKey=keys.next();
                if(selectionKey.isAcceptable()){
                    handleAccept(selector,serverSocketChannel,selectionKey);
                }
                if(selectionKey.isReadable()){
                    randleReadWrite(selector,selectionKey);
                }
                keys.remove();
            }
        }
    }

    private static void handleAccept(Selector selector, ServerSocketChannel serverSocketChannel, SelectionKey selectionKey) throws IOException {
        SocketChannel socketChannel=serverSocketChannel.accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector,SelectionKey.OP_READ);
        System.out.println(String.format("New Connection from "+socketChannel.getRemoteAddress()));
    }

    private static void randleReadWrite(Selector selector, SelectionKey selectionKey) throws IOException {
        SocketChannel socketChannel= (SocketChannel) selectionKey.channel();
        ByteBuffer byteBuffer=ByteBuffer.allocate(1024);
        int dataSize=socketChannel.read(byteBuffer);
        if (dataSize == -1) {
            Socket socket = socketChannel.socket();
            SocketAddress remoteAddr = socket.getRemoteSocketAddress();
            System.out.println(String.format("The client %s has been disconnected ",socketChannel.getRemoteAddress()));
            socketChannel.close();
            selectionKey.cancel();
            return;
        }
        String message =new String(byteBuffer.array()).trim();
        System.out.println(String.format("New request from %s :\n %s",socketChannel.getRemoteAddress(),message));
        String reponse=new StringBuffer(message).reverse().toString().toUpperCase();
        ByteBuffer byteBufferResponse=ByteBuffer.allocate(1024);
        byteBufferResponse.put(reponse.getBytes());
        byteBufferResponse.flip();
        socketChannel.write(byteBufferResponse);
    }
}

