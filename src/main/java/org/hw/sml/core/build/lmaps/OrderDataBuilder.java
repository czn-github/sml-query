package org.hw.sml.core.build.lmaps;

import java.util.List;
import java.util.Map;

import org.hw.sml.tools.MapUtils;

public class OrderDataBuilder extends AbstractDataBuilder{

	@Override
	public Object build(List<Map<String, Object>> datas) {
		MapUtils.sort(datas,rebuildParam.getOrderName(),rebuildParam.getOrderType());
		int topN=rebuildParam.getTopN();
		datas=datas.subList(0,datas.size()>topN?topN:datas.size());
		return datas;
	}
}
