package SockNet;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import CoreAcitive.DispatcherBot;
import PacketUtils.Packet;
import PacketUtils.PacketUtil;

public class NetClient implements Closeable {
	private Socket socket = null;
	private InputStream is = null;
	private OutputStream os = null;
	
	public NetClient() {
		socket = new Socket();
	}
	
	public void startToConnect(String address, int port) throws Exception {
		InetSocketAddress addr = new InetSocketAddress(address,port);
		
		socket.connect(addr);
		is = socket.getInputStream();
		os = socket.getOutputStream();
	}
	
	// 현재구조는 하나의 요청에 대해 하나의 패킷 응답만 오므로 
	// 해당 패킷을 완전히 수신가능할때까지 처리.
	public Packet recv() throws Exception {
		int recvLen = 0;
		boolean complete = false;
		Packet packet = null;
		short packetLen = 0;
		
		List<byte[]> assembleBuffer = new ArrayList<byte[]>();
		do {
			byte[] buffer = new byte[1024];
			recvLen += is.read(buffer);
			
			assembleBuffer.add(buffer);
			// 최소 패킷헤더 읽을 수 있는 사이즈.
			if(recvLen >= Packet.PACKET_MIN_LEN) {
				if(packetLen <= 0) { // 한번만 파싱하기 위해서
					packetLen |= (((short) buffer[Packet.PACKET_CHECK]) << 8) & 0xFF00;
					packetLen |= (((short) buffer[Packet.PACKET_CHECK + 1])) & 0xFF;
				}
				
				// 온전히 읽을 수 있을 때 파싱
				if(packetLen <= recvLen) {
					complete = true;
					byte[] packetBytes = new byte[packetLen];
					
					if(assembleBuffer.size() == 1) { // 1024보다 작다면 하나의 버퍼만 있다는 의미
						System.arraycopy(assembleBuffer.get(0), 0, packetBytes, 0, packetLen);
					}
					else {
						int pos = 0; // write할 위치
						for(byte[] buf : assembleBuffer) {
							// write할 위치가 packetLen보다 길다면
							if((pos + 1024) > packetLen) { 
								System.arraycopy(buf, 0, packetBytes, pos, packetLen - pos);
								break; // 사실상 마지막이어야함.
							}
							
							System.arraycopy(buf, 0, packetBytes, pos, buf.length);
							pos += buf.length;
						}
					}
					packet = PacketUtil.convertPacketFromBytes(packetBytes);
				}
			}
		}while(!complete);
		
		return packet;
	}
	
	public void send(Packet packet) throws Exception {
		if(packet == null)
			return;
		
		byte[] packetBytes = PacketUtil.convertBytesFromPacket(packet);
		os.write(packetBytes);
		os.flush();
	}
	
	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		socket.close();
		is.close();
		os.close();
	}
}
