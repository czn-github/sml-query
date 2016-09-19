package org.hw.sml.queryplugin;
/**
 * @author wen
 */
public interface JsonMapper {
	public <T> T toObj(String json,Class<T> t);
}
