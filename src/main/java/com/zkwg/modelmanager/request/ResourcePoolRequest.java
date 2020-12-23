package com.zkwg.modelmanager.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zkwg.modelmanager.constant.Constant;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.PodSpec;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ResourcePoolRequest extends PageInfoRequest {

    public interface SaveResourcePool {}

    public interface ListResourcePool extends PageInfo{}

    /* Pod 配置文件数据 */
    private final String apiVersion = Constant.Api_Version_V1;

    private final String kind = Constant.Kind_Pod;

    @NotNull(message = "不能为空",groups = {SaveResourcePool.class})
    private ObjectMeta metadata;

    @NotNull(message = "不能为空",groups = {SaveResourcePool.class})
    private PodSpec spec;

    private Map<String, Object> additionalProperties = new HashMap();

    /* Pod 环境数据 */
    @NotBlank(message = "不能为空",groups = {SaveResourcePool.class})
    @NotNull(message = "不能为NULL",groups = {ListResourcePool.class})
    private String name;

    private Date createTime;

    @NotBlank(message = "不能为空",groups = {SaveResourcePool.class})
    private String creator;

//    private int status;

    public ResourcePoolRequest() {
    }

    public ObjectMeta getMetadata() {
        return metadata;
    }

    public void setMetadata(ObjectMeta metadata) {
        this.metadata = metadata;
    }

    public PodSpec getSpec() {
        return spec;
    }

    public void setSpec(PodSpec spec) {
        this.spec = spec;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public String getKind() {
        return kind;
    }
//    public int getStatus() {
//        return status;
//    }
//
//    public void setStatus(int status) {
//        this.status = status;
//    }
}
