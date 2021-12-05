/*
 * Copyright 2020-2021 RW-HPS Team and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/RW-HPS/RW-HPS/blob/master/LICENSE
 */

package com.github.dr.webapi.net;


import java.util.ArrayList;
import java.util.List;

import static com.github.dr.webapi.util.Convert.castList;
import static com.github.dr.webapi.util.ExtractUtil.removeDuplicationByTreeSet;

public class NetData {
	public static class RwConnectData {
		public final String pkgname;
		public final String mapname;
		public final int playercount, maxplayercount;
		public int ping, cping;
		public final int credits;
		public final float income;
		public final boolean noNukes;
		public final String mist,initUnit;
		public final String UnitData;
		public String tu = "TCP";

		@SuppressWarnings("unchecked")
		public RwConnectData(Object[] obj){
	        pkgname = (String)obj[0];
	        this.mapname = (String)obj[1];
			switch ((int)obj[2]) {
				case 1:
					credits = 0;
					break;
				case 2:
					credits = 1000;
					break;
				case 3:
					credits = 2000;
					break;
				case 4:
					credits = 5000;
					break;
				case 5:
					credits = 10000;
					break;
				case 6:
					credits = 50000;
					break;
				case 7:
					credits = 100000;
					break;
				case 8:
					credits = 200000;
					break;
				default:
					credits = 4000;
					break;

			}
			income = (float)obj[3];
			noNukes = (boolean)obj[4];
	        this.playercount =  (int)obj[7];
	        this.maxplayercount =  (int)obj[6];
			switch ((int)obj[8]) {
				case 0:
					mist = "No fog";
					break;
				case 1:
					mist = "Basic fog";
					break;
				case 2:
					mist = "Line of Sight";
					break;
				default:
					mist = "Unknown";
					break;

			}
			switch ((int)obj[9]) {
				case 1:
					initUnit = "Normal (1 builder)";
					break;
				case 2:
					initUnit = "Small Army";
					break;
				case 3:
					initUnit = "3 Engineers";
					break;
				case 4:
					initUnit = "3 Engineers (No Command Center)";
					break;
				case 5:
					initUnit = "Experimental Spider";
					break;
				default:
					initUnit = "Unknown";
					break;

			}

			String unit = "Default";
			List<String> vv = new ArrayList<>();
			for (String str : (List<String>)obj[5]) {
				String[] unitData = str.split("%#%");
				if (unitData.length > 2) {
					vv.add(unitData[2]);
				}
			}
			for (String str : removeDuplicationByTreeSet(vv)) {
				unit = unit + " , " + str;
			}
			UnitData = unit;
	    }
	}

	public static class RwServerUnit {
		public final String unitData;

		public RwServerUnit(Object[] obj) {
			StringBuilder data = new StringBuilder();
			List<String> list = castList(obj[5],String.class);
			for (String str : list) {
				data.append(str).append("\n");
			}
			unitData = data.toString();
		}
	}

	public static class RwPingData {
		public final String pkgname1, pkgname2;
		public final int T1;
		public final int version1, version2;
		public final String connectsha;
		public final int connectkey;
		public int ping;

	    public RwPingData(String pkgname1, int T1, int version1, int version2, String pkgname2, String connectsha, int connectkey){
	        this.pkgname1 = pkgname1;
	        this.T1 = T1;
	        this.version1 = version1;
	        this.version2 = version2;
	        this.pkgname2 = pkgname2;
	        this.connectsha = connectsha;
	        this.connectkey = connectkey;
	    }
	}
}