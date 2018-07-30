package com.kalix.fabric8.kongclient.biz.kong.model.plugin.security.acl;

import com.kalix.fabric8.kongclient.biz.kong.model.common.AbstractEntityList;

import java.util.List;

//import lombok.Data;

/**
 * Created by dvilela on 10/22/17.
 */
//@Data
public class AclList extends AbstractEntityList {
    Long total;
    List<Acl> data;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<Acl> getData() {
        return data;
    }

    public void setData(List<Acl> data) {
        this.data = data;
    }
}
