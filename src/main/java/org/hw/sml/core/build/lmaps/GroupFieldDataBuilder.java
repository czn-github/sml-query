package org.hw.sml.core.build.lmaps;

import java.util.List;
import java.util.Map;

import org.hw.sml.tools.MapUtils;

public class GroupFieldDataBuilder extends AbstractDataBuilder{
	public Object build(List<Map<String, Object>> datas) {
		Map<Object,Map<String,Object>> result= MapUtils.groupMpSingle(datas,rebuildParam.getGroupname(),rebuildParam.getOriFields(),rebuildParam.getNewFields());
		return result;
	}
}
