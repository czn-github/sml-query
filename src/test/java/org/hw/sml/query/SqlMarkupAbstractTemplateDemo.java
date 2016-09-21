package org.hw.sml.query;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.hw.sml.core.SqlMarkupAbstractTemplate;
import org.hw.sml.core.SqlMarkupTemplate;
import org.junit.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;



public class SqlMarkupAbstractTemplateDemo {
	@Test
	public  void testQuery() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://23.247.25.117:3306/hw");
		dataSource.setUsername("root");
		dataSource.setPassword("hlw");
		//库集
		Map<String,DataSource> dss=new HashMap<String,DataSource>();
		dss.put("defJt", dataSource);
		//对象
		SqlMarkupAbstractTemplate jf=new SqlMarkupTemplate();
		jf.setDss(dss);//
		jf.init();
		//使用核心方法-迪士尼客流量查询
		try{
			Object data=jf.getSmlContextUtils().query("if-test", "");
			System.out.println(data);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//对象销毁操作
			jf.destroy();
		}
	}
}
