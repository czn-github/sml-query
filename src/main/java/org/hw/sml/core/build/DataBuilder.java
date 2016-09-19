package org.hw.sml.core.build;

import java.util.List;
import java.util.Map;

public interface DataBuilder {

	public Object build(List<Map<String,Object>> datas);
	
	public List<Map<String,Object>> unBuild(Object remapper);
}
