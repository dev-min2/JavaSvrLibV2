package SockNet;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import PacketUtils.Packet;
import PacketUtils.PacketUtil;

public class NetClient implements Closeable {
	private Socket socket = null;
	private InputStream is = null;
	private OutputStream os = null;
	private RingBuffer recvBuffer = null;
	
	public NetClient() {
		recvBuffer = new RingBuffer();
		socket = new Socket();
	}
	
	public void startToConnect(String address, int port) throws Exception {
		InetSocketAddress addr = new InetSocketAddress(address,port);
		
		socket.connect(addr);
		is = socket.getInputStream();
		os = socket.getOutputStream();
	}
	
	// ��Ŷ 2����üũ.
	// ��Ŷ 1�� + ��Ŷ�̿ϼ� bytes ������ ó�� -> Ȯ�οϷ�.
	// ��Ŷ 2�� ���ÿ� ó�� -> Ȯ�οϷ�.
	public List<Packet> recv() throws Exception {
		List<Packet> packetList = new ArrayList<Packet>(3);
		
		byte[] buffer = recvBuffer.getArrayBuffer();
		int bufferPos = recvBuffer.getPosition();
		int bufferRemainlen = recvBuffer.getRemainLen();
		
		int allAmount = is.read(buffer);
		int readSize = allAmount;
		int pos = bufferPos - bufferRemainlen;
		
		while(true) {
			int processLen = 0;
			if((readSize + bufferRemainlen) < Packet.PACKET_MIN_LEN)
				break;
			
			// �ּ� 4����Ʈ ���� �� ����.
			int packetLenIndex = pos + Packet.PACKET_CHECK; 
			short packetLen = 0;
			
			packetLen |= (((short) buffer[packetLenIndex]) << 8) & 0xFF00;
			packetLen |= (((short) buffer[packetLenIndex + 1])) & 0xFF;
			
			if((readSize + bufferRemainlen) < packetLen)
				break;
			
			// ������� ���� �������� �ϳ��� ��Ŷ�� ���� �� ����.
			byte[] packetBuffer = new byte[packetLen];
			System.arraycopy(buffer, pos, packetBuffer, 0, packetLen);
			
			packetList.add(PacketUtil.convertPacketFromBytes(packetBuffer));
			
			processLen = packetLen;
			readSize -= processLen;
			pos += processLen;
		}
		
		if(readSize > 0) {
			recvBuffer.setRemainLen(readSize);
			recvBuffer.setPosition(allAmount);
		}
		else {
			recvBuffer.clean();
		}
		
		return packetList;
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
