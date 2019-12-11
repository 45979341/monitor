package com.zhuzhou.framework.utils.stomp;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.*;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * 依赖Jackson框架封装的工具类
 * 用于Object与JSON，XML之间的互转
 */
@Slf4j
public class JsonUtils {

	private static ObjectMapper objMapper;

	private static XmlMapper xmlMapper;

	static{
		objMapper = new ObjectMapper();
		Hibernate5Module hm = new Hibernate5Module();
		hm.configure(Hibernate5Module.Feature.USE_TRANSIENT_ANNOTATION, false);
		objMapper.registerModule(hm);
		objMapper.setSerializationInclusion(Include.NON_NULL);
		objMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		objMapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
		JacksonXmlModule module = new JacksonXmlModule();
		module.setDefaultUseWrapper(false);
		xmlMapper = new XmlMapper(module);
		xmlMapper.setSerializationInclusion(Include.NON_DEFAULT);
		xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}


	/**
	 * Object to JSON
	 * @param obj
	 * @return
	 * @throws JsonProcessingException
	 */
	public static String toJson(Object obj){
		if(obj != null) {
			try {
				return objMapper.writeValueAsString(obj);
			} catch (JsonProcessingException e) {
				log.error(e.getMessage(), e);
			}
		}
		return null;
	}

	/**
	 * JSON to Object
	 * @param json
	 * @param clazz
	 * @return
	 */
	public static <T> T fromJson(String json, Class<T> clazz) {
		if(!isEmpty(json)) {
			try {
				return objMapper.readValue(json, clazz);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
		return null;
	}

	/**
	 * Object to XML
	 * @param object
	 * @return
	 */
	public static String toXml(Object object) {
		if (object != null) {
			try {
				return xmlMapper.writeValueAsString(object);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
		return null;
	}

	/**
	 * XML to Object
	 * @param xmlStr
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public static <T> T fromXml(String xmlStr, Class<T> clazz) throws Exception{
		if(!isEmpty(xmlStr)){
			return xmlMapper.readValue(xmlStr, clazz);
		}
		return null;
	}

	/**
	 * json -> List
	 */
	public static <T> List<T> fromJsonList(String json, Class<T> clazz){
		if(!isEmpty(json)) {
			JavaType javaType = objMapper.getTypeFactory().constructParametricType(List.class, clazz);
			try {
				return objMapper.readValue(json, javaType);
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}
		return null;
	}

	/**
	 * json -> Map<String,String>
	 */
	public static Map<String, String> fromJsonMap(String json){
		if(!isEmpty(json)) {
			JavaType javaType = objMapper.getTypeFactory().constructParametricType(Map.class, String.class, String.class);
			try {
				return objMapper.readValue(json, javaType);
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}
		return null;
	}

	public static Map<String, Object> fromJsonMapObject(String json){
		if(!isEmpty(json)) {
			JavaType javaType = objMapper.getTypeFactory().constructParametricType(Map.class, String.class, Object.class);
			try {
				return objMapper.readValue(json, javaType);
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}
		return null;
	}

	/**
	 * json -> Map<String,String>
	 */
	public static Map<String, String> fromJsonTreeMap(String json){
		if(!isEmpty(json)) {
			JavaType javaType = objMapper.getTypeFactory().constructParametricType(TreeMap.class, String.class, String.class);
			try {
				return objMapper.readValue(json, javaType);
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}
		return new HashMap<>(0);
	}

	/**
	 * json -> List<Map<String, String>>
	 */
	public static List<Map<String, String>> fromJsonListTreeMap(String json){
		if(!isEmpty(json)) {
			JavaType javaType = objMapper.getTypeFactory().constructParametricType(TreeMap.class, String.class, String.class);
			CollectionType collectionType = objMapper.getTypeFactory().constructCollectionType(List.class, javaType);
			try {
				return objMapper.readValue(json, collectionType);
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}
		return Collections.EMPTY_LIST;
	}


}
