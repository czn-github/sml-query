package org.hw.sml.core;

import java.io.Serializable;
import java.util.List;

public class Rslt implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<String> headMetas;
	
	private List<List<Object>> datas;
	
	

	public Rslt() {
		super();
	}



	public Rslt(List<String> headMetas, List<List<Object>> datas) {
		super();
		this.headMetas = headMetas;
		this.datas = datas;
	}



	public List<String> getHeadMetas() {
		return headMetas;
	}



	public void setHeadMetas(List<String> headMetas) {
		this.headMetas = headMetas;
	}



	public List<List<Object>> getDatas() {
		return datas;
	}



	public void setDatas(List<List<Object>> datas) {
		this.datas = datas;
	}



	@Override
	public String toString() {
		return "Rslt [headMetas=" + headMetas + ", datas=" + datas + "]";
	}

	
	
}
