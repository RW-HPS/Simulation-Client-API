/*
 * Copyright 2020-2021 RW-HPS Team and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/RW-HPS/RW-HPS/blob/master/LICENSE
 */

package com.github.dr.webapi.net.server;

import com.github.dr.webapi.util.encryption.Base64;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.ErrorPageErrorHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.github.dr.webapi.util.file.LoadConfig.customLoad;

/**
 * @author Dr
 */
public class Start {

	private final Server server = new Server();

	public Start() {
		try {
			ServletContextHandler serverPath = new ServletContextHandler(ServletContextHandler.SESSIONS);

			/**
			 * 设置根目录
			 * 便于监听/*
			 * 以覆盖默认ERROR WEB
			 */
			serverPath.setContextPath("/");
			serverPath.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed", "false");
			serverPath.setErrorHandler(new CustomErrorHandler()); 
			
			/**
			 * 监听/api/
			 */
			new com.github.dr.webapi.net.server.Get().register(serverPath);
			new com.github.dr.webapi.net.server.Post().register(serverPath);

	        // 绑定两个资源句柄
	        ContextHandlerCollection contexts = new ContextHandlerCollection();
	        contexts.setHandlers(new Handler[] {
	        	serverPath
	        });

			/**
			 * SSL
			 */
			SslContextFactory.Server sslContextFactory = new SslContextFactory.Server();
			sslContextFactory.setKeyStorePath("/root/api/ssl.jks");
			//sslContextFactory.setKeyStorePath("C:\\/api-mindustry-top.jks");
			//sslContextFactory.setKeyStorePath(Start.class.getResource("/api-mindustry-top.jks").getFile());
			//sslContextFactory.setKeyStorePassword("api@mindustry&dr");
			//sslContextFactory.setKeyManagerPassword("api@mindustry&dr");
			sslContextFactory.setKeyStorePassword("0");
			sslContextFactory.setKeyManagerPassword("0");

			/**
			 * SSL-HTTP 1.1 不想支持2(需要新依赖 org.eclipse.jetty.http2)
			 * 启用重定向 302
			 */
			HttpConfiguration httpConf = new HttpConfiguration();
			int port = 443;
			httpConf.setSecurePort(port);
			httpConf.setSecureScheme("https");
			HttpConfiguration httpsConf = new HttpConfiguration(httpConf);
			httpsConf.addCustomizer(new SecureRequestCustomizer());
			ServerConnector serverConnector = new ServerConnector(server,new SslConnectionFactory(sslContextFactory, "http/1.1"),new HttpConnectionFactory(httpsConf));
			//ServerConnector serverConnector = new ServerConnector(server, prepareSsl(alpn),alpn, http2ConnectionFactory, httpConnectionFactory);
			serverConnector.setPort(port);
			serverConnector.setReuseAddress(true);

			server.addConnector(serverConnector);
		    server.setHandler(contexts);
			server.start();
			//✓
			server.join();
		} catch (Exception e) {
		}
	}

	private class CustomErrorHandler extends ErrorPageErrorHandler {
		@Override
		public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
			response.setHeader("Server", "Api-Web");
			response.getWriter().println(new Base64().decodeString(customLoad("Html.Api")));
			// response.getWriter().println(String.valueOf(response.getStatus()));
			// response.getWriter().println(request.getRequestURI());
			// response.getWriter().println(request.getServletPath());
		}
	}
}
