package org.hw.sml.core;

import java.util.List;
import java.util.Map;

import org.hw.sml.context.SmlContextUtils;
import org.hw.sml.core.build.DataBuilderHelper;
import org.hw.sml.core.build.SmlTools;
import org.hw.sml.core.resolver.Rst;
import org.hw.sml.core.resolver.SqlResolvers;
import org.hw.sml.model.SMLParams;
import org.hw.sml.model.SqlTemplate;
import org.hw.sml.queryplugin.JsonMapper;
import org.hw.sml.queryplugin.SqlMarkup;
import org.hw.sml.support.Source;
import org.hw.sml.support.el.El;
import org.hw.sml.support.el.JsEl;
import org.hw.sml.tools.Assert;

public abstract class SqlMarkupAbstractTemplate extends Source implements SqlMarkup {
	/**
	 *日志开关
	 */
	protected boolean isLogger=true;
	
	protected JsonMapper jsonMapper;
	
	protected int cacheMinutes;
	
	protected El el;
	
	protected SqlResolvers sqlResolvers;

	protected SmlContextUtils smlContextUtils;
	
	public void init(){
		super.init();
		this.smlContextUtils=new SmlContextUtils(this);
		if(this.jsonMapper==null){
			logger.warn("not dependency json mapper, can't used json config!");
		}
		if(el==null){
			el=new JsEl();
		}
		if(this.sqlResolvers==null){
			SqlResolvers sqlResolvers=new SqlResolvers(getEl());
			sqlResolvers.init();
			this.sqlResolvers=sqlResolvers;
			logger.info("sqlResolvers start... has resolvers [{}]",(this.sqlResolvers.getSqlResolvers().size()));
		}
		if(this.cacheManager==null){
			super.cacheManager=getCacheManager();
		}
	}
	

	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> querySql(SqlTemplate st){
		SqlResolvers sqlResolvers=getSqlResolvers();
		long parserStart=System.currentTimeMillis();
		Rst rst=sqlResolvers.resolverLinks(st.getMainSql(),st.getSmlParams());
		long parseEnd=System.currentTimeMillis();
		List<Object> paramsObject=rst.getParamObjects();
		String key=CACHE_PRE+":"+st.getId()+":mergeSql"+rst.getSqlString()+paramsObject.toString();
		if(getCacheManager().get(key)!=null){
			return (List<Map<String,Object>>) getCacheManager().get(key);
		}
		if(isLogger&&(st.getSmlParams().getSmlParam("igLog")==null||st.getSmlParams().getSmlParam("igLog").getValue().equals("false")))
		logger.info("ifId[{}]-sql[{}],params{},sqlParseUseTime[{}ms]",st.getId(),rst.getSqlString(),paramsObject.toString(),parseEnd-parserStart);
		Assert.isTrue(rst.getSqlString()!=null&&rst.getSqlString().length()>0, "querySql config error parser is null");
		List<Map<String,Object>> result= getJdbc(st.getDbid()).queryForList(rst.getSqlString(),paramsObject.toArray(new Object[]{}));
		if(st.getIsCache()==1)
		getCacheManager().set(key, result, st.getCacheMinutes());
		return result;
	}
	protected void reInitSqlTemplate(SqlTemplate st){
			if(st.getConditionInfo()!=null){
				//以json格式返回
				if(st.getConditionInfo().startsWith("{")&&st.getConditionInfo().endsWith("}")){
					if(jsonMapper!=null){
						st.setSmlParams(jsonMapper.toObj(st.getConditionInfo(),SMLParams.class));
					}
				}else{
					st.setSmlParams(SmlTools.toSplParams(st.getConditionInfo()));
				}
			}
			if(st.getRebuildInfo()!=null){
				if(st.getRebuildInfo().startsWith("{")&&st.getRebuildInfo().endsWith("}")){
					if(jsonMapper!=null){
						st.setRebuildParam(jsonMapper.toObj(st.getRebuildInfo(),RebuildParam.class));
					}
				}else{
					st.setRebuildParam(SmlTools.toRebuildParam(st.getRebuildInfo()));
				}
			}
	}
	public Object builder(SqlTemplate sqlTemplate){
		return DataBuilderHelper.build(sqlTemplate.getRebuildParam(),querySql(sqlTemplate),smlContextUtils,sqlTemplate);
	}
	
	public Rslt queryRslt(SqlTemplate st){
		SqlResolvers sqlResolvers=getSqlResolvers();
		Rst rst=sqlResolvers.resolverLinks(st.getMainSql(), st.getSmlParams());
		String sqlString=rst.getSqlString();
		List<Object> paramsObject=rst.getParamObjects();
		if(isLogger&&(st.getSmlParams().getSmlParam("igLog")==null||st.getSmlParams().getSmlParam("igLog").getValue().equals("false")))
		logger.info("sql[{}],params{}",rst.getSqlString(),paramsObject.toString());
		return getJdbc(st.getDbid()).query(sqlString,paramsObject.toArray(new Object[]{}), new Rset());
	}

	


	public int getCacheMinutes() {
		return cacheMinutes;
	}


	public void setCacheMinutes(int cacheMinutes) {
		this.cacheMinutes = cacheMinutes;
	}

	public JsonMapper getJsonMapper() {
		return jsonMapper;
	}

	public void setJsonMapper(JsonMapper jsonMapper) {
		this.jsonMapper = jsonMapper;
	}

	public El getEl() {
		return el;
	}

	public void setEl(El el) {
		this.el = el;
	}

	
	public SqlResolvers getSqlResolvers() {
		return sqlResolvers;
	}
	public void setSqlResolvers(SqlResolvers sqlResolvers) {
		this.sqlResolvers = sqlResolvers;
	}

	public boolean getIsLogger() {
		return isLogger;
	}

	public void setIsLogger(boolean isLogger) {
		this.isLogger = isLogger;
	}


	public SmlContextUtils getSmlContextUtils() {
		return smlContextUtils;
	}


	public void setSmlContextUtils(SmlContextUtils smlContextUtils) {
		this.smlContextUtils = smlContextUtils;
	}
	
	

}
