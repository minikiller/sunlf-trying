package com.kalix.fabric8.kongclient.biz.kong.model.plugin.authentication.jwt;

import com.kalix.fabric8.kongclient.biz.kong.model.common.AbstractEntityList;

import java.util.List;

//import lombok.Data;

/**
 * Created by vaibhav on 16/06/17.
 */
//@Data
public class JwtCredentialList extends AbstractEntityList {
    Long total;
    List<JwtCredential> data;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<JwtCredential> getData() {
        return data;
    }

    public void setData(List<JwtCredential> data) {
        this.data = data;
    }
}
