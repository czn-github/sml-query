package org.hw.sml.context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.hw.sml.core.Rslt;
import org.hw.sml.core.SqlMarkupAbstractTemplate;
import org.hw.sml.core.resolver.SqlResolver;
import org.hw.sml.core.resolver.SqlResolvers;
import org.hw.sml.model.SMLParam;
import org.hw.sml.model.SqlTemplate;
import org.hw.sml.queryplugin.JsonMapper;
import org.hw.sml.support.cache.CacheManager;
import org.hw.sml.support.el.El;
import org.hw.sml.tools.MapUtils;
import org.springframework.jdbc.core.JdbcTemplate;



public class SmlContextUtils {

	private  SqlMarkupAbstractTemplate sqlMarkupAbstractTemplate;	
	public  SmlContextUtils(SqlMarkupAbstractTemplate sqlMarkupAbstractTemplate){
		this.sqlMarkupAbstractTemplate=sqlMarkupAbstractTemplate;
	}
	
	public  CacheManager getCacheManager(){
		return sqlMarkupAbstractTemplate.getCacheManager();
	}
	
	public  JdbcTemplate getJdbc(String dbid){
		return sqlMarkupAbstractTemplate.getJdbc(dbid);
	}
	public  JsonMapper getJsonMapper(){
		return sqlMarkupAbstractTemplate.getJsonMapper();
	}
	public  El getEl(){
		return sqlMarkupAbstractTemplate.getEl();
	}
	public  SqlMarkupAbstractTemplate getJdbcFTemplate(){
		return sqlMarkupAbstractTemplate;
	}
	public  SqlResolvers getSqlResolvers(){
		return sqlMarkupAbstractTemplate.getSqlResolvers();
	}
	public  void registSqlReolvers(SqlResolver sqlResolver){
		getSqlResolvers().getExtResolvers().add(sqlResolver);
		getSqlResolvers().init();
	}
	//
	@SuppressWarnings("unchecked")
	public  <T> T query(Map<String,String> params){
		return (T)query(params.get("ifId"),params);
	}
	@SuppressWarnings("unchecked")
	public  <T> T query(String ifId,Map<String,String> params){
		SqlTemplate st=sqlMarkupAbstractTemplate.getSqlTemplate(ifId);
		return (T)query(st,params);
	}
	@SuppressWarnings("unchecked")
	public <T> T query(String ifId,String paramsStr){
		if(paramsStr.trim().startsWith("{")&&paramsStr.trim().endsWith("}"))
			return (T)query(ifId,sqlMarkupAbstractTemplate.getJsonMapper().toObj(paramsStr,Map.class));
		return (T)query(ifId,MapUtils.transMapFromStr(paramsStr));
	}
	@SuppressWarnings("unchecked")
	public <T> T query(String paramsStr){
		if(paramsStr.trim().startsWith("{")&&paramsStr.trim().endsWith("}"))
			return (T)query(sqlMarkupAbstractTemplate.getJsonMapper().toObj(paramsStr,Map.class));
		return (T)query(MapUtils.transMapFromStr(paramsStr));
	}
	@SuppressWarnings("unchecked")
	public  <T> T query(SqlTemplate st,Map<String,String> params){
		reInitSqlParam(st, getJdbc(st.getDbid()), params);
		return (T)sqlMarkupAbstractTemplate.builder(st);
	}
	public  Rslt queryRslt(SqlTemplate st,Map<String,String> params){
		reInitSqlParam(st, getJdbc(st.getDbid()), params);
		return sqlMarkupAbstractTemplate.queryRslt(st);
	}
	public  Rslt queryRslt(String ifId,Map<String,String> params){
		SqlTemplate st=sqlMarkupAbstractTemplate.getSqlTemplate(ifId);
		reInitSqlParam(st, getJdbc(st.getDbid()), params);
		return sqlMarkupAbstractTemplate.queryRslt(st);
	}
	
	
	public  int clear(String keyStart){
		if(!isNotBlank(keyStart))
			return getCacheManager().clearKeyStart(SqlMarkupAbstractTemplate.CACHE_PRE);
		return getCacheManager().clearKeyStart(SqlMarkupAbstractTemplate.CACHE_PRE+":"+keyStart);
	}
	public  int getCacheSize(String keyStart){
		if(!isNotBlank(keyStart))
			return getCacheManager().getKeyStart(SqlMarkupAbstractTemplate.CACHE_PRE+":").size();
		return getCacheManager().getKeyStart(SqlMarkupAbstractTemplate.CACHE_PRE+":"+keyStart+":").size();
	}
	
	
	public static void reInitSqlParam(SqlTemplate st, JdbcTemplate jdbc,
			Map<String, String> params) {
		List<SMLParam> lst=st.getSmlParams().getSmlParams();
		for(SMLParam sp:lst){
			String name=sp.getName();
			String value=params.get(name);
			if(isNotBlank(value)){
				sp.handlerValue(value);
			}else{
				sp.handlerValue(sp.getDefaultValue());
			}
		}
	}
	
	
	public static boolean isNotBlank(Object val) {
		return val != null && String.valueOf(val).trim().length() > 0;
	}
	//--ext
	public static String queryFromUrl(String contentType,String accept,String url,String requestBody) throws IOException{
		PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
            URL realUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            if(accept!=null)
            conn.setRequestProperty("accept", accept);
            if(contentType!=null)
            conn.setRequestProperty("content-type",contentType);
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("SOAPAction","");
            conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            out = new PrintWriter(conn.getOutputStream());
            out.print(requestBody);
            out.flush();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
        return result;
	}
	public static String queryFromUrl(String url,String requestBody) throws IOException{
		return queryFromUrl("application/json;charset=UTF-8", "application/json;charset=UTF-8", url, requestBody);
	}
	
}
