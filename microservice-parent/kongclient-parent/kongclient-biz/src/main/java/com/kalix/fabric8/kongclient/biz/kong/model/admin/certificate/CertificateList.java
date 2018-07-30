package com.kalix.fabric8.kongclient.biz.kong.model.admin.certificate;

import com.kalix.fabric8.kongclient.biz.kong.model.common.AbstractEntityList;

import java.util.List;

//import lombok.Data;

/**
 * Created by vaibhav on 13/06/17.
 */
//@Data
public class CertificateList extends AbstractEntityList {
    Long total;
    List<Certificate> data;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<Certificate> getData() {
        return data;
    }

    public void setData(List<Certificate> data) {
        this.data = data;
    }
}
