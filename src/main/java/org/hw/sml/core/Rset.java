package org.hw.sml.core;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.JdbcUtils;

public class Rset implements ResultSetExtractor<Rslt>{
	
	public Rslt extractData(ResultSet rs) throws SQLException,
			DataAccessException {
		 Rslt rt=new Rslt();
		 final List<List<Object>> listData = new ArrayList<List<Object>>();
		 rt.setDatas(listData);
		 ResultSetMetaData rsmd = rs.getMetaData();
	        int iterNum = rsmd.getColumnCount();
	        List<String> header =new ArrayList<String>();
	        for (int i = 0; i < iterNum; i++) {
	          String columnLabel = rsmd.getColumnLabel(i + 1);
	          header.add(columnLabel);
	        }
	        rt.setHeadMetas(header);
	        while (rs.next()) {
	          List<Object> data = new ArrayList<Object>();
	          for (int i = 1; i <= iterNum; i++)
	          {
	            data.add(JdbcUtils.getResultSetValue(rs,i));
	          }
	          listData.add(data);
	        }
	        return rt;
	}
	
 };