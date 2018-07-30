package com.kalix.fabric8.user.entities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * @类描述：用户实体实现类
 * @创建人：sunlf
 * @创建时间：2014-04-26 上午11:10
 * @修改人：
 * @修改时间：
 * @修改备注：
 */
@Entity
@Table(name = "sys_user_ms")
@ApiModel("用户<br>UserBean")
public class UserBean extends UserEntity {
    @ApiModelProperty(value = "用户类型", example = "0")
    private Long userType;
    @ApiModelProperty(value = "工号", example = "0")
    private String code;
    @ApiModelProperty(value = "岗位", example = "0")
    private Integer position;

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getUserType() { return userType;}

    public void setUserType(Long userType) {this.userType = userType;}

}
