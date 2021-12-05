/*
 * Copyright 2020-2021 RW-HPS Team and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/RW-HPS/RW-HPS/blob/master/LICENSE
 */

package com.github.dr.webapi.net;

import com.github.dr.webapi.io.IoOutputStream;
import com.github.dr.webapi.util.log.exp.NetException;
import com.github.dr.webapi.util.log.exp.RwGamaException.KickException;
import com.github.dr.webapi.util.log.exp.RwGamaException.PasswdException;
import net.rudp.ReliableSocket;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

import static com.github.dr.webapi.io.NetIo.*;

/**
 * @author Dr
 */
public class Net {
	public static void rwConnectServer(Consumer<NetData.RwConnectData> listener, Object[] obj, String ip, int port) throws IOException,NetException,KickException,PasswdException {
		try (Socket socket = new Socket()) {
			long cstart = System.currentTimeMillis();
			socket.connect(new InetSocketAddress(InetAddress.getByName(ip), port), port);
			long cend = System.currentTimeMillis();
			IoOutputStream o = new IoOutputStream();
		    o.writeString("com.corrodinggames.rts");
		    o.writeInt(1);
		    o.writeInt(151);
		    o.writeInt(151);
		    sendPacket(socket,o.createPacket(160));
			long start = System.currentTimeMillis();
		    Object[] data = readServer(socket,106,obj);
		    if (data == null)
		    	throw new NetException("ERROR");
		    NetData.RwConnectData host = new NetData.RwConnectData(data);
		    host.ping = (int) (System.currentTimeMillis() - start);
		    host.cping = (int) (cend - cstart);
		    listener.accept(host);
        }
	}

	public static void rwConnectRelayServer(Consumer<NetData.RwConnectData> listener, Object[] obj) throws IOException,NetException,KickException,PasswdException {
		try (Socket socket = new Socket()) {
			long cstart = System.currentTimeMillis();
			socket.connect(new InetSocketAddress(InetAddress.getByName(obj[1].toString().charAt(0)+".relay.corrodinggames.com"), 5123), 2000);
			long cend = System.currentTimeMillis();
			IoOutputStream o = new IoOutputStream();
			o.writeString("com.corrodinggames.rts");
			o.writeInt(3);
			o.writeInt(151);
			o.writeInt(151);
			//o.writeInt(1);
			o.writeBoolean(true);
			o.writeString(obj[1].toString());
			o.writeString(obj[0].toString());
			sendPacket(socket,o.createPacket(160));
			long start = System.currentTimeMillis();
			Object[] data = readServer(socket,106,obj);
			if (data == null)
				throw new NetException("ERROR");
			NetData.RwConnectData host = new NetData.RwConnectData(data);
			host.ping = (int) (System.currentTimeMillis() - start);
			host.cping = (int) (cend - cstart);
			listener.accept(host);
		}
	}


	public static void rwConnectRelayServer0( Object[] obj) throws IOException,NetException,KickException,PasswdException {
		try (Socket socket = new Socket()) {
			long cstart = System.currentTimeMillis();
			socket.connect(new InetSocketAddress(InetAddress.getByName(obj[1].toString().charAt(0)+".relay.corrodinggames.com"), 5123), 5123);
			long cend = System.currentTimeMillis();
			IoOutputStream o = new IoOutputStream();
			o.writeString("com.corrodinggames.rts");
			o.writeInt(3);
			o.writeInt(151);
			o.writeInt(151);
			//o.writeInt(1);
			o.writeBoolean(true);
			o.writeString(obj[1].toString());
			o.writeString(obj[0].toString());
			sendPacket(socket,o.createPacket(160));
			long start = System.currentTimeMillis();
			Object[] data = readServer(socket,106,obj);
			if (data == null)
				throw new NetException("ERROR");
		}
	}


	public static void rwConnectServerUdp(Consumer<NetData.RwConnectData> listener, Object[] obj, String ip, int port) throws IOException,NetException,KickException,PasswdException {
		try (ReliableSocket socket = new ReliableSocket()) {
			long cstart = System.currentTimeMillis();
			socket.connect(new InetSocketAddress(InetAddress.getByName(ip), port));
			long cend = System.currentTimeMillis();
			IoOutputStream o = new IoOutputStream();
		    o.writeString("com.corrodinggames.rts");
		    o.writeInt(1);
		    o.writeInt(151);
		    o.writeInt(151);
		    sendPacket(socket,o.createPacket(160));
			long start = System.currentTimeMillis();
		    Object[] data = readServer(socket,106,obj);
		    if (data == null)
		    	throw new NetException("ERROR");
		    NetData.RwConnectData host = new NetData.RwConnectData(data);
		    host.ping = (int) (System.currentTimeMillis() - start);
		    host.cping = (int) (cend - cstart);
		    host.tu = "UDP";
		    listener.accept(host);
        }
	}

	public static void rwReadServerUnit(Consumer<NetData.RwServerUnit> listener, Object[] obj, String ip, int port) throws IOException,NetException,KickException,PasswdException {
		try (Socket socket = new Socket()) {
			socket.connect(new InetSocketAddress(InetAddress.getByName(ip), port), port);
			IoOutputStream o = new IoOutputStream();
			o.writeString("com.corrodinggames.rts");
			o.writeInt(1);
			o.writeInt(151);
			o.writeInt(151);
			sendPacket(socket,o.createPacket(160));
			Object[] data = readServer(socket,106,obj);
			if (data == null)
				throw new NetException("ERROR");
			NetData.RwServerUnit host = new NetData.RwServerUnit(data);
			listener.accept(host);
		}
	}

	public static void rwDummy(String name, String ip, int port) {
		Timer timer = new Timer();
		try (Socket socket = new Socket()) {
			socket.connect(new InetSocketAddress(InetAddress.getByName(ip), port), port);
			IoOutputStream o = new IoOutputStream();
	        o.writeString("com.corrodinggames.rts");
	        o.writeInt(1);
	        o.writeInt(151);
	        o.writeInt(151);
	        sendPacket(socket,o.createPacket(160));
	        
	        readServer(socket,0,new Object[]{name});
			timer.schedule(new HeartBeat(socket), 0, 1000);

	        //in.readInt();

			Thread.sleep(60000);
			timer.cancel();
	    } catch (Exception e) {
			timer.cancel();		    	
	    }
	}

	public static void rwChat(String name, String msg, String ip, int port) throws IOException,KickException,PasswdException {
		try (Socket socket = new Socket()) {
			socket.connect(new InetSocketAddress(InetAddress.getByName(ip), port), port);
			IoOutputStream o = new IoOutputStream();
	        o.writeString("com.corrodinggames.rts");
	        o.writeInt(1);
	        o.writeInt(151);
	        o.writeInt(151);
	        sendPacket(socket,o.createPacket(160));

        	readServer(socket,161,new Object[] {name});

	        IoOutputStream o11 = new IoOutputStream();
            o11.writeString(msg);
        	o11.writeByte(3);
            sendPacket(socket,o11.createPacket(140));
	    }
	}

	private static class HeartBeat extends TimerTask {

		Socket socket;

		public HeartBeat(Socket socket) {
			this.socket = socket;
		}

        @Override
        public void run() {
            try{
            IoOutputStream o = new IoOutputStream();
	        o.writeLong(System.currentTimeMillis());
            o.writeInt(1);
	        sendPacket(socket,o.createPacket(109));
            } catch (Exception e){
            	return;
        	}
        }
    }

}