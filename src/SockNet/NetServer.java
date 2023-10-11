package SockNet;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.Executors;

import CoreAcitive.ApplicationBeanLoader;
import CoreAcitive.BeanContainer;

/*
 소켓 Async Server
 */

public final class NetServer implements Closeable {
	// accept 핸들러
	AcceptCompletionHandler acceptHandler = null;
	
	// Core
	public NetServer(int port) throws IOException {
		// Network 처리
		InetAddress temp = null;
		InetSocketAddress sockAddr = new InetSocketAddress(temp,port);
		AsynchronousChannelGroup channelGroup = AsynchronousChannelGroup.withFixedThreadPool(
				Runtime.getRuntime().availableProcessors(), Executors.defaultThreadFactory());
		
		AsynchronousServerSocketChannel asyncSocketChannel = AsynchronousServerSocketChannel.open(channelGroup);
		asyncSocketChannel.bind(sockAddr);
        
        acceptHandler = new AcceptCompletionHandler(sockAddr, channelGroup, asyncSocketChannel);
        
	}
	
	public void startServer() {
		acceptHandler.accept();
		
		// start Server를 호출한 메인 쓰레드의 제어권 뻇어가기?
		
	}
	
	@Override
	public void close() throws IOException {
		acceptHandler.close();
	}
}
