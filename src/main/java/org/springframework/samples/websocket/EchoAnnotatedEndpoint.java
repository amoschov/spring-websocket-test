/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.springframework.samples.websocket;

import java.io.IOException;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.websocket.server.endpoint.SpringConfigurator;

@ServerEndpoint(value = "/echoAnnotatedEndpoint", configurator = SpringConfigurator.class)
public class EchoAnnotatedEndpoint {

	private static Logger logger = LoggerFactory.getLogger(EchoAnnotatedEndpoint.class);

	private final EchoService echoService;

	@Autowired
	public EchoAnnotatedEndpoint(EchoService echoService) {
		this.echoService = echoService;
	}

	@OnOpen
	public void newSession() {
		logger.debug("Opened new session in instance " + this);
	}

	@OnMessage
	public void echoTextMessage(Session session, String msg, boolean last) {
		try {
			session.getBasicRemote().sendText(this.echoService.getMessage(msg), last);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}