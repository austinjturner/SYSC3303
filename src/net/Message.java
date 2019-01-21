package net;

import java.io.*;

public class Message {
	protected int requestType;
	protected byte[] data;

	public Message(int requestType, byte[] data) {
		this.data = data;
		this.requestType = requestType;
	}

	public Message(int requestType) {
		this(requestType, new byte[0]);
	}

	public int getRequestType() {
		return this.requestType;
	}

	public byte[] getBytes() {
		return this.data;
	}

	protected void dumpDataObject(Object o) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		try {
			out = new ObjectOutputStream(bos);   
			out.writeObject(o);
			out.flush();	
			this.data = bos.toByteArray();
		} catch (Exception e){
			e.printStackTrace();
		}finally {
			try {
				bos.close();
			} catch (IOException ex) {
				// ignore close exception
				ex.printStackTrace();
				System.out.println("SERIALIZING ERROR"); 
			}
		}
	}

	protected Object getDataObject() {
		Object o = null;
		ByteArrayInputStream bis = new ByteArrayInputStream(this.data);
		ObjectInput in = null;
		try {
			in = new ObjectInputStream(bis);
			o = in.readObject();
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
				// ignore close exception
			}
		}
		return o;
	}
}
