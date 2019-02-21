package com.kalix.fabric8.kongclient.biz.kong.model.admin.aim;

import com.kalix.fabric8.kongclient.biz.kong.model.common.AbstractEntityList;

import java.util.List;

//import lombok.Data;

/**
 * Created by vaibhav on 13/06/17.
 */
//@Data
public class TargetList extends AbstractEntityList {
    Long total;
    String next;
    List<Target> data;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    @Override
    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public List<Target> getData() {
        return data;
    }

    public void setData(List<Target> data) {
        this.data = data;
    }
}
