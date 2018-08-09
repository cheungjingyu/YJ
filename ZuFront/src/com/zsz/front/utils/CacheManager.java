package com.zsz.front.utils;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;
import com.zsz.tools.CommonUtils;

import redis.clients.jedis.Jedis;

public class CacheManager {
	private static final CacheManager instance=new CacheManager();
	public static CacheManager getManager(){
		return instance;
	}
	/**
	 * 在缓存中保存以name为key，以value（gson处理）为值得键值对，有效期是liveSeconds秒
	 * @param name
	 * @param liveSenconds
	 * @param value
	 */
	public  void setValue(String name,int liveSeconds,Object value){
		Jedis jedis=FrontUtils.createJedis();
		Gson gson=CommonUtils.createGson();
		String json=gson.toJson(value);
		try{
		jedis.setex(name, liveSeconds, json);
		}finally{
			jedis.close();
		}
		
	}
	/**
	 * 在缓存中取key为name的值，并且用clz类型进行反序列化
	 * @param name
	 * @param clz
	 * @return 如果没有读到数据，返回null
	 */
	 
	public  Object getValue(String name,Class clz){
		Jedis jedis=FrontUtils.createJedis();
		Gson gson=CommonUtils.createGson();
		try{
		String value=jedis.get(name);
		if(StringUtils.isEmpty(value)){
			return null;
		}else{
			return gson.fromJson(value,clz);
		}
		}finally{
			jedis.close();
		}
	}
	

}
