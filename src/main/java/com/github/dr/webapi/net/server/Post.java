
/*
 * Copyright 2020-2021 RW-HPS Team and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/RW-HPS/RW-HPS/blob/master/LICENSE
 */

package com.github.dr.webapi.net.server;

import com.github.dr.webapi.util.file.FileUtil;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.github.dr.webapi.net.server.Core.getPostDate;
import static com.github.dr.webapi.util.DateUtil.getLocalTimeFromU;

/**
 * @author Dr
 */
public class Post {

	private static final String POST = "/api/post/";
	private static final String WEB = "/api/get/";

	protected void register(ServletContextHandler context) {
        context.addServlet(new ServletHolder(new Log()), POST + "log");
	}

    private class Log extends HttpServlet {
        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
            String postData = getPostDate(request);
            FileUtil.File("/log").toPath(getLocalTimeFromU(28800000,1)).writeFile(postData,false);
            response.getWriter().close();
        }
    }

}
