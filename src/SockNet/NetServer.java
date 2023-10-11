package SockNet;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/*
 소켓 Async Server
 */

public class NetServer {
	
	// accept 핸들러
	AcceptCompletionHandler acceptHandler = null;
	
	
	public NetServer(int port) throws IOException {
		InetAddress temp = null;
		InetSocketAddress sockAddr = new InetSocketAddress(temp,port);
		AsynchronousChannelGroup channelGroup = AsynchronousChannelGroup.withFixedThreadPool(
				Runtime.getRuntime().availableProcessors(), Executors.defaultThreadFactory());
		
		AsynchronousServerSocketChannel asyncSocketChannel = AsynchronousServerSocketChannel.open(channelGroup);
		asyncSocketChannel.bind(sockAddr);
        
        // 핸들러 생성
        acceptHandler = new AcceptCompletionHandler(sockAddr, channelGroup, asyncSocketChannel);
	}
	
	public void startServer() {
		acceptHandler.accept();
		
		
		// start Server를 호출한 메인 쓰레드의 제어권 뻇어가기.
	}
}
