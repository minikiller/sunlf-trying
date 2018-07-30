package com.kalix.fabric8.kongclient.biz.kong.model.plugin.authentication.oauth2;

import com.kalix.fabric8.kongclient.biz.kong.model.common.AbstractEntityList;

import java.util.List;

//import lombok.Data;

/**
 * Created by vaibhav on 15/06/17.
 */
//@Data
public class ApplicationList extends AbstractEntityList {

    Long total;

    List<Application> data;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<Application> getData() {
        return data;
    }

    public void setData(List<Application> data) {
        this.data = data;
    }
}
