package com.management.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cn.hutool.json.JSONObject;

public class StudentCache {
	public static final Map<String, JSONObject> USER_CACHE = new ConcurrentHashMap<>();
}
