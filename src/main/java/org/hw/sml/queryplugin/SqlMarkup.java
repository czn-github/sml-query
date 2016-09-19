package org.hw.sml.queryplugin;

import java.util.List;
import java.util.Map;

import org.hw.sml.core.Rslt;
import org.hw.sml.model.SqlTemplate;
import org.hw.sml.plugin.Plugin;
import org.hw.sml.support.cache.CacheManager;
import org.springframework.jdbc.core.JdbcTemplate;


public interface  SqlMarkup extends Plugin{

	 static final String CACHE_PRE="jdbc";
	
	 JdbcTemplate getJdbc(String dbid);
	
	 SqlTemplate getSqlTemplate(String id);
	
	 List<Map<String,Object>> querySql(SqlTemplate st);
	
	 Object builder(SqlTemplate st);
	
	 CacheManager getCacheManager();
	
	 Rslt queryRslt(SqlTemplate st);
}
