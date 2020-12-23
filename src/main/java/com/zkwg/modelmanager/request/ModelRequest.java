package com.zkwg.modelmanager.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
@Data
public class ModelRequest extends PageInfoRequest {

    public interface SaveModel {}

    public interface ListModel extends PageInfo{}

    private Integer userId;

//    @NotNull(message = "不能为空",groups = {SaveModel.class})
    private Integer trainId;

    /**
     * 模型名称
     */
    @NotBlank(message = "不能为空",groups = {SaveModel.class})
    private String name;

    private String nameEn;

    @NotBlank(message = "不能为空",groups = {SaveModel.class})
    private String version;

    @NotBlank(message = "不能为空",groups = {SaveModel.class})
    private Byte type;

    private Integer businessType;

//    @NotBlank(message = "不能为空",groups = {SaveModel.class})
    private String creator;

    @NotBlank(message = "不能为空",groups = {SaveModel.class})
    private String department;

    private String remark;

    private String implementation;

    private String deployJson;

    private String path;

    private String image;
}
