package org.hw.sml.core.build.lmaps;

import java.util.List;
import java.util.Map;

import org.hw.sml.FrameworkConstant;
import org.hw.sml.model.Result;
import org.hw.sml.tools.MapUtils;

public class PageDataBuilder extends AbstractDataBuilder {
	public Object build(List<Map<String, Object>> datas) {
		String[] oriFields=rebuildParam.getOriFields();
		String[] newFields=rebuildParam.getNewFields();
		Long count=Long.parseLong(String.valueOf(datas.get(0).get(datas.get(0).keySet().iterator().next())));
		Result result=new Result();
		result.setCount(count);
		if(count>0){
			sqlTemplate.getSmlParams().getSmlParam(FrameworkConstant.PARAM_QUERYTYPE).setValue("select");
			List<Map<String,Object>> data=smlContextUtils.getJdbcFTemplate().querySql(sqlTemplate);
			if(oriFields!=null&&newFields!=null){
				data=MapUtils.rebuildMp(data, oriFields,newFields);
			}
			result.setDatas(data);
		}
		return result;
	}
}