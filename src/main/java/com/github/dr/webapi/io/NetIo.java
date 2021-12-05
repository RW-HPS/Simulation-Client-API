/*
 * Copyright 2020-2021 RW-HPS Team and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/RW-HPS/RW-HPS/blob/master/LICENSE
 */

package com.github.dr.webapi.io;

import com.github.dr.webapi.util.encryption.Game;
import com.github.dr.webapi.util.encryption.Sha;
import com.github.dr.webapi.util.log.Log;
import com.github.dr.webapi.util.log.exp.RwGamaException.KickException;
import com.github.dr.webapi.util.log.exp.RwGamaException.KickPullException;
import com.github.dr.webapi.util.log.exp.RwGamaException.KickStartException;
import com.github.dr.webapi.util.log.exp.RwGamaException.PasswdException;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import static com.github.dr.webapi.util.StringFilteringUtil.cutting;

/**
 * @author Dr
 */
public class NetIo {
    public static void sendPacket(Socket socket, Packet p) throws IOException {
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        out.writeInt(p.bytes.length);
        out.writeInt(p.type);
        out.write(p.bytes);
        out.flush();
    }

    private static String readString(ByteBuffer buffer){
        short length = (short)(buffer.get() & 0xff);
        byte[] bytes = new byte[length];
        buffer.get(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static Object[] readServer(Socket socket, int type, Object[] pam) throws KickException,PasswdException {
        Object[] obj = new Object[32];
        while (!socket.isClosed()) {
            try {
                DataInputStream in = new DataInputStream(socket.getInputStream());
                int size = in.readInt();
                Packet packet = new Packet(in.readInt());
                packet.bytes = new byte[size];
                int bytesRead = 0;
                while (bytesRead < size) {
                    int readIn = in.read(packet.bytes, bytesRead, size - bytesRead);
                    if (readIn == -1) {
                        break;
                    }
                    bytesRead += readIn;
                }
                int result = doActive(socket,obj,packet,type,pam);
                if (result == 0) {
                    try {
                        socket.close();
                    } catch (Exception ignored) {
                    }
                    return obj;
                }
            } catch (IOException e) {
                Log.info(e);
                return null;
            }
        }
        return null;
    }

    private static int doActive(Socket socket, Object[] obj, Packet packet, int type, Object[] pam) throws IOException,KickException,PasswdException {
        // 0完成 1继续 2ERR 3KICK
        DataInputStream stream = new DataInputStream(new ByteArrayInputStream(packet.bytes));
        //Log.debug(packet.type, Arrays.toString(packet.bytes));
        switch (packet.type) {
            case 106:
                //if (type == 106) {
                    readDataIO.readServerInfo(stream,obj);
                    return 0;
                //}
                //return 2;
            case 113:
            case 117:
                //Log.debug(stream.readUTF());
                throw new PasswdException("ERROR");
            case 115:
                readDataIO.readServerTeam(stream,obj);
                return 1;
            case 150:
                String resulttext = stream.readUTF();
                Log.debug(resulttext);
                if (resulttext.contains("started") || resulttext.contains("启动") || resulttext.contains("开始")) {
                    throw new KickStartException("ERROR");
                }
                if (resulttext.contains("free") || resulttext.contains("满") || resulttext.contains("战队") || resulttext.contains("加")) {
                    throw new KickPullException("ERROR");                    
                }
                throw new KickException("ERROR");
            // 预注册 -> 注册
            case 161:
                readDataIO.registerServer(socket,stream,pam);
                return 1;
            case 151:
                IoOutputStream out = new IoOutputStream();
                out.writeInt(stream.readInt());

                int i = stream.readInt();
                out.writeInt(i);
                Log.debug(i);
                String msg = "";
                int i1=0;
                int i2=0;
                if (stream.readBoolean()) {
                    i1 = stream.readInt();
                }
                if (stream.readBoolean()) {
                    i2 = stream.readInt();
                }

                switch (i) {
                    case 0 :
                        msg = "" + i1;
                        break;
                    case 1 :
                        msg = "" + i2;
                        break;
                    case 2 :
                        msg = Game.connectak(i1);
                        break;
                    case 3 :
                    case 4 :
                        msg = cutting(new BigInteger(1, new Sha().sha256Arry(i1+"|"+i2)).toString(16).toUpperCase(Locale.ROOT),14);
                        break;
                    case 5 :
                    case 6 :
                        String c1 = stream.readUTF();
                        String c2 = stream.readUTF();
                        int i3 = stream.readInt();
                        if (i == 6)
                            c2 = c2 + i1;
                        if (i3 > 10000000) {
                            msg = "max";
                        } else {
                            msg = "-1";

                            Log.debug(c1);
                            Log.debug(c2);
                            Log.debug(i3);
                            for (int b1 = 0; b1 <= i3; b1++) {
                                if (cutting(new BigInteger(1, new Sha().sha256Arry(c2 +""+ b1)).toString(16).toUpperCase(Locale.ROOT),14).equalsIgnoreCase(c1)) {
                                    msg = "" + b1;
                                    break;
                                }
                            }
                            Log.debug(msg);
                        }
                        break;
                    case 7 :
                        String str = stream.readUTF();
                        int i4 = stream.readInt();
                        if (i4 > 10000) {
                            msg = "max";
                        } else {
                            msg = "";
                            for (int b1 = 0; b1 < i4; b1++)
                                msg = msg + str;
                        }
                        break;
                }
                out.writeString(msg);
                out.writeFloat(5);

                sendPacket(socket,out.createPacket(152));
                return 1;
            case 2001:
                obj[0] = stream.readUTF();
                return 0;
        }
        return 4;
    }
}