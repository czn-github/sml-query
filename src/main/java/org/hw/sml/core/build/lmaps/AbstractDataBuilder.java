package org.hw.sml.core.build.lmaps;

import java.util.List;
import java.util.Map;

import org.hw.sml.context.SmlContextUtils;
import org.hw.sml.core.RebuildParam;
import org.hw.sml.core.build.DataBuilder;
import org.hw.sml.model.SqlTemplate;

public abstract class AbstractDataBuilder implements DataBuilder{
	
	protected SmlContextUtils smlContextUtils;
	
	protected SqlTemplate sqlTemplate;
	
	public AbstractDataBuilder() {
		super();
	}
	public List<Map<String,Object>> unBuild(Object data){
		return null;
	}
	
	public AbstractDataBuilder(RebuildParam rebuildParam) {
		super();
		this.rebuildParam = rebuildParam;
	}
	
	public String[] getNoFields(String[] fields){
		String[] refs=new String[fields.length];
		for(int i=0;i<fields.length;i++){
			String field=fields[i];
			if(field.contains("@")){
				refs[i]=field.split("@")[0];
			}else{
				refs[i]=field;
			}
		}
		return refs;
	}
	
	
	protected RebuildParam rebuildParam;

	public RebuildParam getRebuildParam() {
		return rebuildParam;
	}

	public void setRebuildParam(RebuildParam rebuildParam) {
		this.rebuildParam = rebuildParam;
	}

	
	public SqlTemplate getSqlTemplate() {
		return sqlTemplate;
	}

	public void setSqlTemplate(SqlTemplate sqlTemplate) {
		this.sqlTemplate = sqlTemplate;
	}
	public SmlContextUtils getSmlContextUtils() {
		return smlContextUtils;
	}
	public void setSmlContextUtils(SmlContextUtils smlContextUtils) {
		this.smlContextUtils = smlContextUtils;
	}
	
	
	
}
