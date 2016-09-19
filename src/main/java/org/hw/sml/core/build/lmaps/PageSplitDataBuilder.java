package org.hw.sml.core.build.lmaps;

import java.util.List;
import java.util.Map;

import org.hw.sml.tools.MapUtils;

public class PageSplitDataBuilder extends AbstractDataBuilder {
	public Object build(List<Map<String, Object>> datas) {
		String[] oriFields=rebuildParam.getOriFields();
		String[] newFields=rebuildParam.getNewFields();
		return MapUtils.rebuildMp(datas, oriFields,newFields);
	}

}