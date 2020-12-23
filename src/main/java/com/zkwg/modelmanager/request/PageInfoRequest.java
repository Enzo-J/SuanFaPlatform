package com.zkwg.modelmanager.request;

//import com.github.pagehelper.IPage;

import javax.validation.constraints.NotNull;

public class PageInfoRequest {

    public interface PageInfo{}

    //当前页
    @NotNull(message = "当前页不能为空",groups = {PageInfo.class})
    private Integer pageNum;
    //每页的数量
    @NotNull(message = "每页的数量不能为空",groups = {PageInfo.class})
    private Integer pageSize;
    //当前页的数量
//    @NotNull(message = "当前页的数量不能为空",groups = {PageInfo.class})
//    private Integer size;

    private String orderBy;

    public PageInfoRequest() {
    }


    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }


    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }


    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
//    public int getSize() {
//        return size;
//    }
//
//    public void setSize(int size) {
//        this.size = size;
//    }
}
