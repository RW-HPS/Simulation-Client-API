/*
 * Copyright 2020-2021 RW-HPS Team and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/RW-HPS/RW-HPS/blob/master/LICENSE
 */

package com.github.dr.webapi.data.global;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Dr
 */
public class Data {

    public static final Charset UTF_8 = StandardCharsets.UTF_8;

	public static final List<String> maps = new ArrayList<String>(Arrays.asList(new String[] {"[p2]Beach landing (2p) [by hxyy]","[p2]Big Island (2p)","[p2]Dire_Straight (2p) [by uber]","[p2]Fire Bridge (2p) [by uber]","[p2]Ice Island (2p)","[p2]Lake (2p)","[p2]Small_Island (2p)","[p2]Two_cold_sides (2p)","[p3]Hercules_(2vs1p) [by_uber]","[p3]King of the Middle (3p)_demo","[p4]Depth charges (4p) [by hxyy]","[p4]Desert (4p)","[p4]Ice Lake (4p) [by hxyy]","[p4]Island freeze (4p) [by hxyy]","[p4]Lava Maze (4p)","[p4]Lava Vortex (4p)","[p4]Nuclear war (4p) [by hxyy]","[p6]Shore to Shore (6p)","[p6]Valley Pass (6p)","[p8]Bridges Over Lava (8p)","[p8]Coastline (8p) [by hxyy]","[p8]Huge Subdivide (8p)","[p8]Interlocked (8p)","[p8]Interlocked Large (8p)","[p8]Isle Ring (8p)","[p8]Large Ice Outcrop (8p)","[p8]Lava Bio-grid(8p)","[p8]Lava Divide(8p)","[p8]Many Islands (8p)","[p8]Random Islands (8p)","[p8]Two Sides (8p)","[z;p10]Two Sides Remake (10p)","[z;p10]Valley Arena (10p) [by_uber]","[z;p10]Many Islands Large (10p)","[z;p10]Crossing Large (10p)"}));

	public static final String getGameVer(String str) {
		switch (str) {
			case "com.corrodinggames.rts":
            	return "铁锈-原版";
            case "com.corrodinggames.rts.tieji":
            	return "铁锈-黑暗森林";
            case "com.corrodinggames.rts.tieji64":
            	return "铁锈-黑暗森林p5";
        	case "com.corrodinggames.rts.tjbeta":
            	return "铁锈-黑暗森林测试";
        	case "com.corrodinggamesUFP.rts":
            	return "铁锈-星联版";
        	case "com.corrodinggames.rts.qz":
            	return "铁锈-星联全汉化";
        	case "com.corrodinggames.rts.yl":
            	return "铁锈-影流美化";
        	case "com.corrodinggames.Xling":
            	return "铁锈-星河模组";
        	case "com.corrodinggames.Xlini":
            	return "铁锈-星火美化";
			case "com.corrodinggames.galaxy":
				return "铁锈-星河征途-Galaxy-旧";
			case "com.corrodinggames.rts.ling":
				return "铁锈-星河征途-Galaxy";
        	case "com.Ghostwml.rts":
            	return "铁锈-荣耀争霸";
        	case "com.corrodinggames.rtt":
            	return "铁锈-狼痕锈迹";
        	case "com.corrodinggames.rts.un":
            	return "铁锈-UN汉化版";
			case "com.corrodinggames.rw_hps":
				return "铁锈-SFE服务器";
			case "com.corrodinggames.rwhps":
				return "铁锈-SFE服务器";
            default:
            	return "铁锈-未知版本";
        }
	}
}
