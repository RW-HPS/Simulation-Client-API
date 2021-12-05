/*
 * Copyright 2020-2021 RW-HPS Team and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/RW-HPS/RW-HPS/blob/master/LICENSE
 */

package com.github.dr.webapi.net.server;

import com.alibaba.fastjson.JSON;
import com.github.dr.webapi.data.global.Data;
import com.github.dr.webapi.net.Net;
import com.github.dr.webapi.net.NetData;
import com.github.dr.webapi.util.ReExp;
import com.github.dr.webapi.util.encryption.Base64;
import com.github.dr.webapi.util.encryption.Sha;
import com.github.dr.webapi.util.log.exp.NetException;
import com.github.dr.webapi.util.log.exp.RwGamaException.KickException;
import com.github.dr.webapi.util.log.exp.RwGamaException.KickPullException;
import com.github.dr.webapi.util.log.exp.RwGamaException.KickStartException;
import com.github.dr.webapi.util.log.exp.RwGamaException.PasswdException;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static com.github.dr.webapi.net.server.Core.*;
import static com.github.dr.webapi.util.ExtractUtil.stringToUtf8;
import static com.github.dr.webapi.util.IsUtil.isNumeric;
import static com.github.dr.webapi.util.IsUtil.notisBlank;
import static com.github.dr.webapi.util.RandomUtil.generateStrInt;
import static com.github.dr.webapi.util.log.Error.code;

/**
 * @author Dr
 * Web
 */
public class Get {

    private static final String GET = "/api/get/";

    protected void register(ServletContextHandler context) {
        context.addServlet(new ServletHolder(new RwPing()), GET + "rwping");
        context.addServlet(new ServletHolder(new RwRelayPing()), GET + "rwrelayping");
        context.addServlet(new ServletHolder(new RwUnit()), GET + "rwunit");
        context.addServlet(new ServletHolder(new RwDummys()), GET + "rwdummys");
        context.addServlet(new ServletHolder(new RwChat()), GET + "rwchat");
        //context.addServlet(new ServletHolder(new Login()), GET+"user/login");
    }

    private class RwPing extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
            response.setStatus(HttpServletResponse.SC_OK);
            if (handlerAsd(request,response)) {
                PrintWriter out = getGzipWriter(request, response);
                setHandler(response);
                String ipdata = request.getParameter("ip");
                String passwddata = request.getParameter("passwd");
                final Map<String, Object> result = new HashMap<String, Object>(4);
                if (ipdata != null && !ipdata.equals("")) {
                    final Map<String, Object> data = new HashMap<String, Object>(16);
                    final String [] arr = ipdata.split("\\s+");

                    Consumer<NetData.RwConnectData> listener = rt -> {
                        data.put("PkgName",(Object) rt.pkgname);
                        data.put("AutoGameName",Data.getGameVer((String) rt.pkgname));
                        String mapname = (String) rt.mapname;
                        mapname = mapname.substring(0, mapname.length() - 4);
                        data.put("MapName",Data.maps.contains(mapname) ? mapname : "Custom");
                        data.put("PlayerCount",(Object) rt.playercount);
                        data.put("PlayerLimit",(Object) rt.maxplayercount);
                        data.put("ConnectPing",(Object) rt.cping);
                        data.put("PrPing",(Object) rt.ping);
                        data.put("TU",(Object) rt.tu);
                        data.put("Credits",(Object) rt.credits);
                        data.put("Income",(Object) rt.income);
                        data.put("NoNukes",(Object) rt.noNukes);
                        data.put("Mist",(Object) rt.mist);
                        data.put("InitUnit",(Object) rt.initUnit);
                        data.put("UnitData",(Object) rt.UnitData);
                    };
                    final String ip = arr[0].contains(":") ? arr[0].split(":")[0] : arr[0];
                    final int port = arr[0].contains(":") ? isNumeric(arr[0].split(":")[1]) ? Integer.parseInt(arr[0].split(":")[1]) : 5123 : 5123;
                    Object[] objdata = new Object[2];
                    objdata[0] = generateStrInt("Unnamed",3);
                    if (passwddata != null) {
                        byte[] paramArrayOfbyte = new Sha().sha256Arry(passwddata);
                        objdata[1] = String.format("%0" + (paramArrayOfbyte.length * 2) + "X", new Object[] { new BigInteger(1, paramArrayOfbyte) });;
                    } else {
                        objdata[1] = null;
                    }
                    com.github.dr.webapi.util.ReExp.Data pingdata = new ReExp() {
                        @Override
                        protected Object runs() throws Exception {
                            //throw new IOException();
                            try {
                                Net.rwConnectServer(listener,objdata,ip,port);
                                return "Y";
                            } catch (NetException e) {
                                result.put("State", code("DATA_NULL"));
                                return null;
                            } catch (KickException e) {
                                if (e instanceof KickStartException) {
                                    result.put("State", code("SERVER_START"));
                                } else if (e instanceof KickPullException) {
                                    result.put("State", code("SERVER_FULL"));
                                } else {
                                    result.put("State", code("SERVER_KICK"));
                                }
                                return null;
                            } catch (PasswdException e) {
                                result.put("State", code("PASSWORD_ERROR"));
                                return null;
                            }
                        }
                        @Override
                        protected com.github.dr.webapi.util.ReExp.Data defruns() {
                            //result.put("State", code("SERVER_CLOSE"));
                            com.github.dr.webapi.util.ReExp.Data pingdata0 = new ReExp() {
                                @Override
                                protected Object runs() throws Exception {
                                    try {
                                        Net.rwConnectServerUdp(listener,objdata,ip,port);
                                        return "Y";
                                    } catch (NetException e) {
                                        result.put("State", code("DATA_NULL"));
                                        return null;
                                    } catch (KickException e) {
                                        if (e instanceof KickStartException) {
                                            result.put("State", code("SERVER_START"));
                                        } else if (e instanceof KickPullException) {
                                            result.put("State", code("SERVER_FULL"));
                                        } else {
                                            result.put("State", code("SERVER_KICK"));
                                        }
                                        return null;
                                    } catch (PasswdException e) {
                                        result.put("State", code("PASSWORD_ERROR"));
                                        return null;
                                    }
                                }
                                @Override
                                protected com.github.dr.webapi.util.ReExp.Data defruns() {
                                    result.put("State", code("SERVER_CLOSE"));
                                    return null;
                                }
                            }.setSleepTime(10).setRetryFreq(3).setException(IOException.class).countExecute("Ping"+ip+port);
                            return pingdata0;
                        }
                    }.setSleepTime(10).setRetryFreq(3).setException(IOException.class).countExecute("Ping"+ip+port);
                    if (pingdata != null && pingdata.result != null) {
                        result.put("State", code("SUCCESS"));
                        data.put("TryCount",(Object) pingdata.cout);
                        data.put("Failures",(Object) pingdata.failures);
                        result.put("Result", new Base64().encode(JSON.toJSONString(data)));
                    }
                } else {
                    result.put("State", code("INCOMPLETE_PARAMETERS"));
                }
                out.println(stringToUtf8(JSON.toJSONString(result)));
                out.close();
            }
        }
    }

    private class RwRelayPing extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
            response.setStatus(HttpServletResponse.SC_OK);
            if (handlerAsd(request,response)) {
                PrintWriter out = getGzipWriter(request, response);
                setHandler(response);
                String id = request.getParameter("id");
                final Map<String, Object> result = new HashMap<String, Object>(4);
                if (notisBlank(id)) {
                    final Map<String, Object> data = new HashMap<String, Object>(16);

                    Consumer<NetData.RwConnectData> listener = rt -> {
                        data.put("PkgName",(Object) rt.pkgname);
                        data.put("AutoGameName",Data.getGameVer((String) rt.pkgname));
                        String mapname = (String) rt.mapname;
                        mapname = mapname.substring(0, mapname.length() - 4);
                        data.put("MapName",Data.maps.contains(mapname) ? mapname : "Custom");
                        data.put("PlayerCount",(Object) rt.playercount);
                        data.put("PlayerLimit",(Object) rt.maxplayercount);
                        data.put("ConnectPing",(Object) rt.cping);
                        data.put("PrPing",(Object) rt.ping);
                        data.put("TU",(Object) rt.tu);
                        data.put("Credits",(Object) rt.credits);
                        data.put("Income",(Object) rt.income);
                        data.put("NoNukes",(Object) rt.noNukes);
                        data.put("Mist",(Object) rt.mist);
                        data.put("InitUnit",(Object) rt.initUnit);
                        data.put("UnitData",(Object) rt.UnitData);
                    };
                    Object[] objdata = new Object[2];
                    objdata[0] = generateStrInt("Unnamed",3);
                    objdata[1] = id;
                    com.github.dr.webapi.util.ReExp.Data pingdata = new ReExp() {
                        @Override
                        protected Object runs() throws Exception {
                            //throw new IOException();
                            try {
                                Net.rwConnectRelayServer(listener,objdata);
                                return "Y";
                            } catch (NetException e) {
                                result.put("State", code("DATA_NULL"));
                                return null;
                            } catch (KickException e) {
                                if (e instanceof KickStartException) {
                                    result.put("State", code("SERVER_START"));
                                } else if (e instanceof KickPullException) {
                                    result.put("State", code("SERVER_FULL"));
                                } else {
                                    result.put("State", code("SERVER_KICK"));
                                }
                                return null;
                            } catch (PasswdException e) {
                                result.put("State", code("PASSWORD_ERROR"));
                                return null;
                            }
                        }
                        @Override
                        protected com.github.dr.webapi.util.ReExp.Data defruns() {
                            result.put("State", code("SERVER_CLOSE"));
                            return null;
                        }
                    }.setSleepTime(10).setRetryFreq(3).setException(IOException.class).countExecute("Ping"+id);
                    if (pingdata != null && pingdata.result != null) {
                        result.put("State", code("SUCCESS"));
                        data.put("TryCount",(Object) pingdata.cout);
                        data.put("Failures",(Object) pingdata.failures);
                        result.put("Result", new Base64().encode(JSON.toJSONString(data)));
                    }
                } else {
                    result.put("State", code("INCOMPLETE_PARAMETERS"));
                }
                out.println(stringToUtf8(JSON.toJSONString(result)));
                out.close();
            }
        }
    }

    private class RwUnit extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
            response.setStatus(HttpServletResponse.SC_OK);
            if (handlerAsd(request,response)) {
                PrintWriter out = getGzipWriter(request, response);
                setHandler(response);
                String ipdata = request.getParameter("ip");
                String passwddata = request.getParameter("passwd");
                final Map<String, Object> result = new HashMap<String, Object>(4);
                if (ipdata != null && !ipdata.equals("")) {
                    final String [] arr = ipdata.split("\\s+");

                    Consumer<NetData.RwServerUnit> listener = rt -> {
                        result.put("Result", new Base64().encode(rt.unitData));
                    };
                    final String ip = arr[0].contains(":") ? arr[0].split(":")[0] : arr[0];
                    final int port = arr[0].contains(":") ? isNumeric(arr[0].split(":")[1]) ? Integer.parseInt(arr[0].split(":")[1]) : 5123 : 5123;
                    Object[] objdata = new Object[2];
                    objdata[0] = generateStrInt("Unnamed",3);
                    if (passwddata != null) {
                        byte[] paramArrayOfbyte = new Sha().sha256Arry(passwddata);
                        objdata[1] = String.format("%0" + (paramArrayOfbyte.length * 2) + "X", new Object[] { new BigInteger(1, paramArrayOfbyte) });;
                    } else {
                        objdata[1] = null;
                    }
                    com.github.dr.webapi.util.ReExp.Data pingdata = new ReExp() {
                        @Override
                        protected Object runs() throws Exception {
                            try {
                                Net.rwReadServerUnit(listener,objdata,ip,port);
                                return "Y";
                            } catch (NetException e) {
                                result.put("State", code("DATA_NULL"));
                                return null;
                            } catch (KickException e) {
                                if (e instanceof KickStartException) {
                                    result.put("State", code("SERVER_START"));
                                } else if (e instanceof KickPullException) {
                                    result.put("State", code("SERVER_FULL"));
                                } else {
                                    result.put("State", code("SERVER_KICK"));
                                }
                                return null;
                            } catch (PasswdException e) {
                                result.put("State", code("PASSWORD_ERROR"));
                                return null;
                            }
                        }
                        @Override
                        protected com.github.dr.webapi.util.ReExp.Data defruns() {
                            result.put("State", code("SERVER_CLOSE"));
                            return null;
                        }
                    }.setSleepTime(10).setRetryFreq(3).setException(IOException.class).countExecute("Ping"+ip+port);
                    if (pingdata != null && pingdata.result != null) {
                        result.put("State", code("SUCCESS"));
                    }
                } else {
                    result.put("State", code("INCOMPLETE_PARAMETERS"));
                }
                out.println(stringToUtf8(JSON.toJSONString(result)));
                out.close();
            }
        }
    }

    private class RwDummys extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
            response.setStatus(HttpServletResponse.SC_OK);
            if (handlerAsd(request,response)) {
                PrintWriter out = getGzipWriter(request, response);
                setHandler(response);
                String ipdata = request.getParameter("ip");
                final Map<String, Object> result = new HashMap<String, Object>(4);
                if (ipdata != null && !ipdata.equals("")) {
                    final String [] arr = ipdata.split("\\s+");

                    final String ip = arr[0].contains(":") ? arr[0].split(":")[0] : arr[0];
                    final int port = arr[0].contains(":") ? isNumeric(arr[0].split(":")[1]) ? Integer.parseInt(arr[0].split(":")[1]) : 5123 : 5123;
                    com.github.dr.webapi.util.ReExp.Data pingdata = new ReExp() {
                        @Override
                        protected Object runs() throws Exception {
                            for (int i = 0;i < 10;i++) {
                                new Thread(() -> Net.rwDummy(generateStrInt("Unnamed",3),ip,port)).start();
                            }
                            return "Y";
                        }
                        @Override
                        protected com.github.dr.webapi.util.ReExp.Data defruns() {return null;}
                    }.setSleepTime(10).setRetryFreq(3).setException(IOException.class).countExecute("Ping"+ip+port);
                    if (pingdata != null && pingdata.result != null) {
                        result.put("State", code("SUCCESS"));
                    }
                } else {
                    result.put("State", code("INCOMPLETE_PARAMETERS"));
                }
                out.println(stringToUtf8(JSON.toJSONString(result)));
                out.close(); 
            }
        }
    }

    private class RwChat extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
            response.setStatus(HttpServletResponse.SC_OK);
            if (handlerAsd(request,response)) {
                PrintWriter out = getGzipWriter(request, response);
                setHandler(response);
                String ipdata = request.getParameter("ip");
                String chatdata = request.getParameter("chat");
                final Map<String, Object> result = new HashMap<String, Object>(4);
                if (ipdata != null && !ipdata.equals("") && chatdata != null && !chatdata.equals("")) {
                    final String [] arr = ipdata.split("\\s+");

                    final String ip = arr[0].contains(":") ? arr[0].split(":")[0] : arr[0];
                    final int port = arr[0].contains(":") ? isNumeric(arr[0].split(":")[1]) ? Integer.parseInt(arr[0].split(":")[1]) : 5123 : 5123;
                    com.github.dr.webapi.util.ReExp.Data pingdata = new ReExp() {
                        @Override
                        protected Object runs() throws Exception {
                            Net.rwChat(generateStrInt("Unnamed",3),chatdata,ip,port);
                            return "Y";
                        }
                        @Override
                        protected com.github.dr.webapi.util.ReExp.Data defruns() {
                            result.put("State", code("SERVER_CLOSE"));
                            return null;
                        }
                    }.setSleepTime(10).setRetryFreq(3).setException(IOException.class).countExecute("Ping"+ip+port);
                    if (pingdata != null && pingdata.result != null) {
                        result.put("State", code("SUCCESS"));
                    }
                } else {
                    result.put("State", code("INCOMPLETE_PARAMETERS"));
                }
                out.println(stringToUtf8(JSON.toJSONString(result)));
                out.close(); 
            }
        }
    }
}