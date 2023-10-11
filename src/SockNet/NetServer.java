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
	
	private InetSocketAddress addr;
	private AsynchronousChannelGroup channelGroup;
	
	//클라이언트 연결 수락
	private AsynchronousServerSocketChannel asyncServerSocketChannel;
	private final AtomicInteger sessionCnt = new AtomicInteger(0);
	private HashMap<Integer,Session> sessionByID = new HashMap<Integer,Session>();
	
	private Object sessionLock = new Object();
	
	public NetServer(int port) throws IOException
	{
		InetAddress temp = null;
        addr = new InetSocketAddress(temp,port);        
	}
	
	public void startServer() {
		if(addr == null)
			return;
		
		try {
			channelGroup = AsynchronousChannelGroup.withFixedThreadPool(Runtime.getRuntime().availableProcessors(), Executors.defaultThreadFactory());
			asyncServerSocketChannel = AsynchronousServerSocketChannel.open(channelGroup);
			asyncServerSocketChannel.bind(addr);
		}catch(Exception e) {
			e.printStackTrace();
			return;
		}
		DispatchMessageManager.getInstance().init( this );

		asyncServerSocketChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>(){
			@Override
			public void completed(AsynchronousSocketChannel result, Void attachment) {
				try {
					Session session = new Session();
					int sessionid = sessionCnt.incrementAndGet();
					synchronized(sessionLock){
						sessionByID.put(sessionid, session);
					}
					session.Init(result, sessionid);
					
					DispatchMessageManager.getInstance().connectSession(sessionid);
				} 
				catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				finally {
					asyncServerSocketChannel.accept(null, this); // �떎�쓬 accept.
				}
			}
 
			@Override
			public void failed(Throwable exc, Void attachment) {
				if(asyncServerSocketChannel.isOpen()) {
					try {
						stopServer();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}); //accept()
		
		// start Server를 호출한 메인 쓰레드의 제어권 뻇어가기.
		
		
	}
	
	public boolean stopServer() throws IOException {
		if(asyncServerSocketChannel != null && asyncServerSocketChannel.isOpen()) {
			DispatchMessageManager.getInstance().stopThread();
			synchronized(sessionLock) {
				for(Entry<Integer,Session> ety : sessionByID.entrySet()) {
					Session s = ety.getValue();
					if(s != null)
						s.closeSession();
				}
			}
			asyncServerSocketChannel.close();
		}
		
		return true;
	}
	
	public Session getSession(int sessionId) {
		Session session = null;
		synchronized(sessionLock) {
			session = sessionByID.get(sessionId);
		}
		
		return session;
	}
	
	public void delSession(int sessionId) {
		synchronized(sessionLock) {
			Session se = sessionByID.get(sessionId);
			if(se != null) {
				sessionByID.remove(sessionId);
			}
		}
	}
	
	public void checkPing() {
		
	}
}
