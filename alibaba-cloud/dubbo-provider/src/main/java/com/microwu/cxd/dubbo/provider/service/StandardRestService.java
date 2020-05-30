/*
 * Copyright 2013-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.microwu.cxd.dubbo.provider.service;

import com.microwu.cxd.dubbo.service.RestService;
import com.microwu.cxd.dubbo.service.User;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.server.PathParam;
import java.util.HashMap;
import java.util.Map;

import static com.microwu.cxd.dubbo.utils.LoggerUtil.log;

/**
 * Default {@link RestService}.
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 */
@Service
public class StandardRestService implements RestService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public String param(String param) {
		log("/param", param);
		return param;
	}

	@Override
	public String params(int a,String b) {
		log("/params", a + b);
		return a + b;
	}

	@Override
	public String headers(String header, String header2, Integer param) {
		String result = header + " , " + header2 + " , " + param;
		log("/headers", result);
		return result;
	}

	@Override
	public String pathVariables(@PathParam("p1") String path1,
			@PathParam("p2") String path2, String param) {
		String result = path1 + " , " + path2 + " , " + param;
		log("/path-variables", result);
		return result;
	}

	// @CookieParam does not support : https://github.com/OpenFeign/feign/issues/913
	// @CookieValue also does not support

	@Override
	public String form(String form) {
		return String.valueOf(form);
	}

	@Override

	public User requestBodyMap(Map<String, Object> data, String param) {
		User user = new User();
		user.setId(((Integer) data.get("id")).longValue());
		user.setName((String) data.get("name"));
		user.setAge((Integer) data.get("age"));
		log("/request/body/map", param);
		return user;
	}

	@Override
	public Map<String, Object> requestBodyUser(User user) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", user.getId());
		map.put("name", user.getName());
		map.put("age", user.getAge());
		return map;
	}

}
