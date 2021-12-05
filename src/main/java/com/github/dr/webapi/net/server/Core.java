/*
 * Copyright 2020-2021 RW-HPS Team and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/RW-HPS/RW-HPS/blob/master/LICENSE
 */

package com.github.dr.webapi.net.server;

import com.github.dr.webapi.data.global.Data;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.zip.GZIPOutputStream;

/**
 * Util
 * @author Dr
 */
public class Core {

    /**
     * API验证核心 验证请求时效
     * @param response
     */
    protected static boolean handlerAsd(HttpServletRequest request, HttpServletResponse response) throws IOException {
        /*
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = getGzipWriter(request, response);
        setHandler(response);
        Map<String, Object> result = new HashMap<String, Object>(4);
        String tonkenAsd = request.getHeader("Tonken-ASD");
        if (null != tonkenAsd && tonkenAsd.equals("Private.tx.xyz")) {
            return true;
        }
        // 400
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        result.put("State", code("INCOMPLETE_PARAMETERS"));
        out.println(stringToUtf8(JSON.toJSONString(result)));
        out.close();
        return false;
        */
        return true;
    }

    /**
     * GZIP
     * @param      request      The request
     * @param      response     The response
     * @return     流
     */
    protected static PrintWriter getGzipWriter(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String encodings = request.getHeader("Accept-Encoding");
        String flag = request.getParameter("disableGzip");
        boolean isGzipSupported = ((encodings != null) && (encodings.contains("gzip")));
        boolean isGzipDisabled = ((flag != null) && (!"false".equalsIgnoreCase(flag)));
        if (isGzipSupported && !isGzipDisabled) {
            response.setHeader("Content-Encoding", "gzip");
            return new PrintWriter(new GZIPOutputStream(response.getOutputStream()));
        } else {
            return response.getWriter();
        }
    }

    /**
     * 设置标头
     * @param      response  The response
     */
    protected static void setHandler(HttpServletResponse response) {
        response.setHeader("Server", "Api-Web");
    }

    protected static String getPostDate(HttpServletRequest request) {
        BufferedReader in = null;
        StringBuffer result = new StringBuffer();
        String line = null;
        try {
            request.setCharacterEncoding("UTF-8");
            /**
             * 很遗憾 JETTY不支持GZIP流 普通的只支持UTF-8解码
             * 我会考虑采取GzipHandler的 (没希望)
             * RSA加密的内容还没16K大
             */
            in = new BufferedReader(new InputStreamReader(request.getInputStream(), Data.UTF_8));
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (IOException e) {
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    //
                }
            }
        }
        return result.toString();
    }
}