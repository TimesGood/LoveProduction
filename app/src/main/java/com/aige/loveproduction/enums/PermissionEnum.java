package com.aige.loveproduction.enums;

/**
 * 权限枚举
 */
public enum PermissionEnum {
    Administrator("Administrator"),
    Distributor("经销商"),
    Workshop("车间"),
    Storage("入库出库发货");
    private final String message;

    PermissionEnum(String message) {
        this.message = message;
    }


    public String getMessage() {
        return message;
    }

}
