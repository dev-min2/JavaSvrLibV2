package SockNet;

import java.nio.ByteBuffer;

//Recv에 필요한 데이터를 담아주기 위해 확장.
public class RecvAttachment {
	private ByteBuffer recvBuffer;
	private Session session;
	private RingBuffer ringBuffer;
	
	public RecvAttachment(ByteBuffer recvBuffer, Session session, RingBuffer ringBuffer) {
		this.recvBuffer = recvBuffer;
		this.session = session;
		this.ringBuffer = ringBuffer;
	}

	public ByteBuffer getRecvBuffer() {
		return recvBuffer;
	}

	public Session getSession() {
		return session;
	}
	
	public RingBuffer getRingBuffer() {
		return ringBuffer;
	}
}
