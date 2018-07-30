package com.kalix.fabric8.kongclient.biz.kong.model.plugin.authentication.oauth2;

import com.kalix.fabric8.kongclient.biz.kong.model.common.AbstractEntityList;

import java.util.List;

//import lombok.Data;

/**
 * Created by fanhua on 2017-08-07.
 */
//@Data
public class TokenList extends AbstractEntityList {

	Long total;

	List<Token> data;

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public List<Token> getData() {
		return data;
	}

	public void setData(List<Token> data) {
		this.data = data;
	}
}
