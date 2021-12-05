/*
 * Copyright 2020-2021 RW-HPS Team and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/RW-HPS/RW-HPS/blob/master/LICENSE
 */

package com.github.dr.webapi.io;

import java.io.*;

import com.github.dr.webapi.io.Packet;

public class IoInputStream {
	public ByteArrayInputStream buffer;
    public DataInputStream stream;

    public IoInputStream(Packet packet) {
        this.buffer = new ByteArrayInputStream(packet.bytes);
		this.stream = new DataInputStream(this.buffer);
    }

    public int readByte() throws IOException {
        return this.stream.readByte();
    }

    public boolean readBoolean() throws IOException {
        return this.stream.readBoolean();
    }

    public int readInt() throws IOException {
        return this.stream.readInt();
    }

    public float readFloat() throws IOException {
        return this.stream.readFloat();
    }

    public long readLong() throws IOException {
        return this.stream.readLong();
    }

    public String readString() throws IOException {
        return this.stream.readUTF();
    }
}
