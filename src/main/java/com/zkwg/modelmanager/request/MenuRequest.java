package com.zkwg.modelmanager.request;


public class MenuRequest extends PageInfoRequest {

    /**
     * 权限编码
     */
    private String permsCode;

    /**
     * 权限名称
     */
    private String name;

    /**
     * 类型，-1系统 0:目录 1：菜单，2：按钮，3：其他
     */
    private Byte type;

    /**
     * 父权限ID
     */
    private Integer parentId;

    /**
     * 权限层级
     */
    private Byte level;

    /**
     * 请求url
     */
    private String path;

    /**
     * 菜单组件
     */
    private String component;

    /**
     * 图标
     */
    private String icon;

    /**
     * 权限在当前模块下的顺序，由小到大
     */
    private Byte sort;

    /**
     * 是否为外链
     */
    private Byte isFrame;

    public MenuRequest() {
    }

    public String getPermsCode() {
        return permsCode;
    }

    public void setPermsCode(String permsCode) {
        this.permsCode = permsCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Byte getLevel() {
        return level;
    }

    public void setLevel(Byte level) {
        this.level = level;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Byte getSort() {
        return sort;
    }

    public void setSort(Byte sort) {
        this.sort = sort;
    }

    public Byte getIsFrame() {
        return isFrame;
    }

    public void setIsFrame(Byte isFrame) {
        this.isFrame = isFrame;
    }
}
