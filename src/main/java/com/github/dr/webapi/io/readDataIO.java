/*
 * Copyright 2020-2021 RW-HPS Team and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/RW-HPS/RW-HPS/blob/master/LICENSE
 */

package com.github.dr.webapi.io;

import com.github.dr.webapi.net.NetData;

import java.io.*;
import java.util.zip.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static com.github.dr.webapi.io.NetIo.sendPacket;
import static com.github.dr.webapi.util.encryption.Game.connectak;

/**
 * @author Dr
 */
public class readDataIO {
    public static NetData.RwPingData readServerData(DataInputStream in) throws IOException {
        String pkgname1 = in.readUTF();
        int T1 = in.readInt();
        int version1 = in.readInt();
        int version2 = in.readInt();
        String pkgname2 = in.readUTF();
        String connectsha = in.readUTF();
        int connectkey = in.readInt();

        return new NetData.RwPingData(pkgname1, T1, version1, version2, pkgname2, connectsha, connectkey);
    }

    public static void registerServer(Socket socket,DataInputStream stream,Object[] pam) throws IOException {
        IoOutputStream out = new IoOutputStream();
        int a = readServerData(stream).connectkey;
        out.writeString("com.corrodinggames.rts");
        out.writeInt(4);
        out.writeInt(151);
        out.writeInt(151);
        out.writeString(pam[0].toString());
        if (pam[1] != null) {
            out.writeBoolean(true);
            out.writeString(pam[1].toString());
        } else {
            out.writeBoolean(false);
        }
        out.writeString("com.corrodinggames.rts.java");
        out.writeString("AF50F4B772F33FCB4082561E44C87FDBFF0E62C5B80B1772C17573D0A7832C53");
        out.writeInt(1198432602);
        out.writeString(connectak(a));
        sendPacket(socket,out.createPacket(110));
    }

    public static void readServerInfo(DataInputStream stream,Object[] obj) throws IOException {
        obj[0] = stream.readUTF();
        stream.readInt();
        stream.readInt();
        obj[1] = stream.readUTF();
        obj[2] = stream.readInt();
        stream.readInt();
        stream.readBoolean();
        stream.readInt();
        stream.readByte();
        stream.readBoolean();
        stream.readBoolean();
        stream.readInt();
        stream.readInt();
        stream.readInt();
        obj[3] = stream.readFloat();
        obj[4] = stream.readBoolean();
        stream.readBoolean();
        stream.readBoolean();
        /**/
        stream.readUTF();
        int n2;
        int n3 = stream.readInt();
        byte[] arrby = new byte[n3];
        for (int i2 = 0; i2 < n3 && (n2 = stream.read(arrby, i2, n3 - i2)) != -1; i2 += n2) {
        }
        //
        DataInputStream stream2 = new DataInputStream(new ByteArrayInputStream(arrby));
        stream2.readInt();
        int count = stream2.readInt();
        StringBuilder data = new StringBuilder();
        List<String> list = new ArrayList<String>(128);
        for (int i=0;i < count;i++) {
            data.delete( 0, data.length() );
            data.append(stream2.readUTF()).append("%#%").append(stream2.readInt());
            stream2.readBoolean();
            if (stream2.readBoolean()) {
                data.append("%#%").append(stream2.readUTF());
            }
            stream2.readLong();
            stream2.readLong();
            list.add(data.toString());
        }
        obj[5] = list;
    }

    public static void readServerTeam(DataInputStream stream,Object[] obj) throws IOException {
        stream.readInt();
        stream.readBoolean();
        obj[6] = stream.readInt();
        int playercount = 0;
        int player = (int)obj[6];
        /**/
        stream.readUTF();
        int n2;
        int n3 = stream.readInt();
        byte[] arrby = new byte[n3];
        for (int i2 = 0; i2 < n3 && (n2 = stream.read(arrby, i2, n3 - i2)) != -1; i2 += n2) {
        }
        //new DataInputStream(new ByteArrayInputStream(arrby))
        DataInputStream stream2 = new DataInputStream((InputStream)new BufferedInputStream((InputStream)new GZIPInputStream((InputStream)new ByteArrayInputStream(arrby))));
        for (int i=0;i<player;i++) {
            if (stream2.readBoolean()) {
                playercount++;
                stream2.readInt();
                stream2.readByte();
                stream2.readInt();
                stream2.readInt();
                if (stream2.readBoolean()) {
                    stream2.readUTF();
                }
                stream2.readBoolean();
                stream2.readInt();
                stream2.readLong();
                stream2.readBoolean();
                stream2.readInt();
                stream2.readInt();
                stream2.readByte();
                stream2.readBoolean();
                stream2.readBoolean();
                stream2.readBoolean();
                stream2.readBoolean();
                stream2.readInt();
                if (stream2.readBoolean()) {
                    stream2.readUTF();
                }
                stream2.readInt();
            }
        }
        obj[7] = playercount;
        obj[8] = stream.readInt();
        stream.readInt();
        stream.readBoolean();
        stream.readInt();
        stream.readByte();
        stream.readInt();
        stream.readInt();
        obj[9] = stream.readInt();
    }
}
