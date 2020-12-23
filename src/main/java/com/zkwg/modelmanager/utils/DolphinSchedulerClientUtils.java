package com.zkwg.modelmanager.utils;

import cn.hutool.core.lang.Assert;
import com.google.common.collect.ImmutableMap;
import com.zkwg.modelmanager.response.DSResponse;
import io.fabric8.kubernetes.client.utils.Serialization;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

public class DolphinSchedulerClientUtils {

    private static Logger logger = LoggerFactory.getLogger(DolphinSchedulerClientUtils.class);

    private static String baseUrl;

    private static String projectName;

    private static DolphinSchedulerClientUtils dolphinSchedulerClientUtils;

    private static DolphinSchedulerProperties dolphinSchedulerProperties;

    private DolphinSchedulerClientUtils() {
    }

    private static Map<String,String> tokenHeaderMap;

    private static OkHttpClient okHttpClient = new OkHttpClient();

    static {
        dolphinSchedulerProperties = SpringUtils.getApplicationContext().getBean(DolphinSchedulerProperties.class);
        baseUrl = dolphinSchedulerProperties.getBaseUrl();
        projectName = dolphinSchedulerProperties.getProjectName();
        tokenHeaderMap = ImmutableMap.of("token", dolphinSchedulerProperties.getToken());// "2934cc986db1104f79f3bae4a40f8307"

//        baseUrl = "http://116.63.172.190:31205";
//        projectName = "AI";
//        tokenHeaderMap = ImmutableMap.of("token","1a44aa9cad8adbf817afd21f1f6a729c");
    }

    public static DolphinSchedulerClientUtils getInstance() {
        if(dolphinSchedulerClientUtils == null){
            synchronized (DolphinSchedulerClientUtils.class){
                if(dolphinSchedulerClientUtils == null){
                    return dolphinSchedulerClientUtils = new DolphinSchedulerClientUtils();
                }
            }
        }
        return dolphinSchedulerClientUtils;
    }

    /**
     * get请求
     * @param url
     * @param params
     * @param headers
     * @return
     */
    public static Response get(String url, Map<String,String> params, Map<String,String> headers) throws IOException {
        //URL带的参数
//        HashMap<String,String> params = new HashMap<>();
        //GET 请求带的Header
//        HashMap<String,String> headers = new HashMap<>();
        //HttpUrl.Builder构造带参数url
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        if (params != null) {
            for (String key : params.keySet()) {
                urlBuilder.setQueryParameter(key, params.get(key));
            }
        }
        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .headers(headers == null ? new Headers.Builder().build() : Headers.of(headers))
                .get()
                .build();

        Call call = okHttpClient.newCall(request);

        return call.execute();
    }

    /**
     * post form 请求
     * @param url
     * @param params
     * @param headers
     * @return
     */
    public static Response post(String url, Map<String,String> params, Map<String,String> headers) throws IOException {
        //POST参数构造MultipartBody.Builder，表单提交
//        HashMap<String,String> params = new HashMap<>();
        MultipartBody.Builder urlBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (params != null) {
            for (String key : params.keySet()) {
                if (params.get(key)!=null){
                    urlBuilder.addFormDataPart(key, params.get(key));
                }
                //urlBuilder.addFormDataPart(key, params.get(key));
            }
        }
        // 构造Request->call->执行
        Request request = new Request.Builder()
                .headers(headers == null ? new Headers.Builder().build() : Headers.of(headers))//extraHeaders 是用户添加头
                .url(url)
                .post(urlBuilder.build())//参数放在body体里
                .build();

        Call call = okHttpClient.newCall(request);

        return call.execute();
    }

    /**
     * 解析返回结果
     * @param response
     * @return
     * @throws IOException
     */
    private static DSResponse handlerResponse(Response response) throws IOException {
        if(response.isSuccessful()) {
            String bodyString = response.body().string();
            logger.info("ds response string is {} ", bodyString);
            DSResponse dsResponse = Serialization.unmarshal(bodyString, DSResponse.class);
            Assert.state( dsResponse.getCode() == 0 , "调用dolphinscheduler返回异常");
            return dsResponse;
        }
        throw new RuntimeException("调用dolphinscheduler异常 ");
    }

    /**
     * database 数据库名      必须
     * host  IP主机名         必须
     * name 数据源名称        必须
     * port 数据源端口        必须
     * principal principal   必须
     * type                  必须
     * userName 用户名       必须
     * note 数据源描述
     * other jdbc连接参数，格式为:{"key1":"value1",...}
     * password
     */
    // 创建数据源 POST
    public static void createDatasources(Map<String,String> params) throws IOException {

        Response response = post(baseUrl + "/dolphinscheduler/datasources/create",params, tokenHeaderMap );

        handlerResponse(response);
    }

    /**
     * type 数据源类型,可用值:MYSQL,POSTGRESQL,HIVE,SPARK,CLICKHOUSE,ORACLE,SQLSERVER,DB2 true
     */
    // 查询数据源列表通过数据源类型 GET /dolphinscheduler/datasources/list
    public static List<Map<String,String>> queryDataSourceList(String type) throws IOException {

        Response response = get(baseUrl + "/dolphinscheduler/datasources/list?type="+type,null, tokenHeaderMap );

        DSResponse<List<Map<String,String>>> dsResponse = handlerResponse(response);

        return dsResponse.getData();
    }

    /**
     * pageNo 页码号 false
     * pageSize 页大小 false
     * searchVal 搜索值 false
     */
    // 分页查询数据源列表 GET /dolphinscheduler/datasources/list-paging
    public static int queryDataSourceListPaging(String dataSourceName) throws IOException {

        Map<String,String> params = ImmutableMap.of("pageNo", "1", "pageSize", "10", "searchVal", dataSourceName);

        Response response = get(baseUrl + "/dolphinscheduler/datasources/list-paging",params, tokenHeaderMap );

        DSResponse<Map<String,Object>> dsResponse = handlerResponse(response);

        if((int) dsResponse.getData().get("total") > 0) {
            List<Map<String, Object>> mapList = (List<Map<String, Object>>) dsResponse.getData().get("totalList");
            Optional<Map<String,Object>> optional = mapList.stream().findFirst();
            return optional.isPresent() ?  (Integer) optional.get().get("id")  : -1;
        }

        return -1;
    }


    /**
     * pageNo 页码号 false
     * pageSize 页大小 false
     * searchVal 搜索值 false
     */
    // 分页查询数据源列表 GET /dolphinscheduler/projects/{projectName}/process/list-paging
    public static int queryProcessDefinitionListPaging(String proDefName) throws IOException {

        Map<String,String> params = ImmutableMap.of("pageNo", "1", "pageSize", "10", "searchVal", proDefName);

        Response response = get(baseUrl + "/dolphinscheduler/projects/"+ projectName +"/process/list-paging",params, tokenHeaderMap );

        DSResponse<Map<String,Object>> dsResponse = handlerResponse(response);

        if((int) dsResponse.getData().get("total") > 0) {
            List<Map<String, Object>> mapList = (List<Map<String, Object>>) dsResponse.getData().get("totalList");
            Optional<Map<String,Object>> optional = mapList.stream().findFirst();
            return optional.isPresent() ?  (Integer) optional.get().get("id")  : -1;
        }

        return -1;
    }


    /**
     * pageNo 页码号 false
     * pageSize 页大小 false
     * processDefinitionId 搜索值 false
     */
    // 分页查询数据源列表 GET /dolphinscheduler/projects/{projectName}/instance/list-paging
    public static int queryProcessInstanceList(int  processDefinitionId) throws IOException {

        Map<String,String> params = ImmutableMap.of("pageNo", "1", "pageSize", "10", " processDefinitionId",  processDefinitionId+"");

        Response response = get(baseUrl + "/dolphinscheduler/projects/"+ projectName +"/instance/list-paging",params, tokenHeaderMap );

        DSResponse<Map<String,Object>> dsResponse = handlerResponse(response);

        if((int) dsResponse.getData().get("total") > 0) {
            List<Map<String, Object>> mapList = (List<Map<String, Object>>) dsResponse.getData().get("totalList");
            Optional<Map<String,Object>> optional = mapList.stream().findFirst();
            return optional.isPresent() ?  (Integer) optional.get().get("id")  : -1;
        }

        return -1;
    }

    /**
     * pageNo 页码号 false
     * pageSize 页大小 false
     * processInstanceId 流程实例ID false
     */
    // 分页查询数据源列表 GET /dolphinscheduler/projects/{projectName}/task-instance/list-paging
    public static int queryTaskListPaging(int processInstanceId) throws IOException {

        Map<String,String> params = ImmutableMap.of("pageNo", "1", "pageSize", "100", "processInstanceId", processInstanceId+"");

        Response response = get(baseUrl + "/dolphinscheduler/projects/"+ projectName +"/task-instance/list-paging",params, tokenHeaderMap );

        DSResponse<Map<String,Object>> dsResponse = handlerResponse(response);

        if((int) dsResponse.getData().get("total") > 0) {
            List<Map<String, Object>> mapList = (List<Map<String, Object>>) dsResponse.getData().get("totalList");
            Optional<Map<String,Object>> optional = mapList.stream().findFirst();
            return optional.isPresent() ?  (Integer) optional.get().get("id")  : -1;
        }

        return -1;
    }

    /**
     * 获取流程实例下的第一个任务实例
     */
    // 分页查询数据源列表 GET /dolphinscheduler/projects/{projectName}/task-instance/list-paging
    public static Map<String,Object> queryFirstTaskForProInst(int processInstanceId) throws IOException {

        Map<String,String> params = ImmutableMap.of("pageNo", "1", "pageSize", "100", "processInstanceId", processInstanceId+"");

        Response response = get(baseUrl + "/dolphinscheduler/projects/"+ projectName +"/task-instance/list-paging",params, tokenHeaderMap );

        DSResponse<Map<String,Object>> dsResponse = handlerResponse(response);

        if((int) dsResponse.getData().get("total") > 0) {
            List<Map<String, Object>> mapList = (List<Map<String, Object>>) dsResponse.getData().get("totalList");
            Optional<Map<String,Object>> optional = mapList.stream().findFirst();
            return optional.isPresent() ?  optional.get()  : Collections.emptyMap();
        }
        return Collections.emptyMap();
    }

    // 删除数据源 GET
    public static void deleteDatasources(int id) throws IOException {

        Response response = get(baseUrl + "/dolphinscheduler/datasources/delete?id="+id,null, tokenHeaderMap );

        handlerResponse(response);
    }

    /**
     * limit 显示多少条 false
     * skipLineNum 忽略行数 false
     * taskInstId 任务实例ID false
     */
    // 查询任务实例日志 GET
    public static String queryLog(Integer taskInstanceId) throws IOException {

        Map<String,String> params = ImmutableMap.of("skipLineNum","0", "limit", "1000");

        Response response = get( baseUrl + "/dolphinscheduler/log/detail?taskInstanceId="+taskInstanceId ,params, tokenHeaderMap );

        DSResponse dsResponse = handlerResponse(response);

        return dsResponse.getData().toString();
    }

    /**
     *  processInstanceId 流程实例ID
     */
    // 删除流程实例 GET /dolphinscheduler/projects/{projectName}/instance/delete
    public static void deleteProcessInstanceById(Integer processInstanceId) throws IOException {

        Response response = get( baseUrl + "/dolphinscheduler/projects/"+ projectName +"/instance/delete?processInstanceId="+processInstanceId ,null, tokenHeaderMap );

        handlerResponse(response);

    }

    /**
     *  processDefinitionId 流程定义ID
     */
    // 删除流程定义通过流程定义ID GET /dolphinscheduler/projects/{projectName}/process/delete
    public static void deleteProcessDefinitionById(Integer processDefinitionId) throws IOException {

        Response response = get( baseUrl + "/dolphinscheduler/projects/"+ projectName +"/process/delete?processDefinitionId="+processDefinitionId ,null, tokenHeaderMap );

        handlerResponse(response);

    }

    /**
     * connects 流程定义节点图标连接信息(json格式) true
     * locations 流程定义节点坐标位置信息(json格式) true
     * name 流程定义名称 true
     * processDefinitionJson 流程定义详细信息(json格式) true
     * projectName 项目名称 true
     * description 流程定义描述信息 false
     */
    // 创建流程定义 dolphinscheduler/projects/{projectName}/process/save
    public static void saveProcess(Map<String,String> params) throws IOException {

        Response response = post(baseUrl + "/dolphinscheduler/projects/"+ projectName +"/process/save",params, tokenHeaderMap );

        handlerResponse(response);
    }

    /**
     * name 流程定义名称 true
     * processId 流程定义ID true
     * projectName 项目名称 true
     * releaseState 流程定义节点图标连接信息(json格式) true
     */
    // 发布流程定义  releaseState: 0 上线  下线
    public static void releaseProccessDefinition(Integer processId, Integer releaseState) throws IOException {

        Map<String, String> params = ImmutableMap.of("processId", processId+"", "releaseState",releaseState+"");

        Response response = post(baseUrl + "/dolphinscheduler/projects/"+ projectName +"/process/release", params, tokenHeaderMap );

        handlerResponse(response);
    }

    /**
     * processId 流程定义ID true
     * projectName 项目名称 true
     */
    // 查询流程定义通过流程定义ID   /dolphinscheduler/projects/{projectName}/process/select-by-id
    public static void queryProccessDefinitionById() {

    }

    /**
     * failureStrategy 失败策略,可用值:END,CONTINUE true
     * processDefinitionId 流程定义ID true
     * processInstancePriority 流程实例优先级,可用值:HIGHEST,HIGH,MEDIUM,LOW,LOWEST true
     * projectName 项目名称 true
     * scheduleTime 定时时间 true
     * warningGroupId 发送组ID true
     * warningType 发送策略,可用值:NONE,SUCCESS,FAILURE,ALL true
     * execType 指令类型,可用值:START_PROCESS,START_CURRENT_TASK_PROCESS,RECOVER_TOLERANCE_FAULT_PROCESS,RECOVER_SUSPENDED_PROCESS,START_FAILURE_TASK_PROCESS,COMPLEMENT_DATA,SCHEDULER,REPEAT_RUNNING,PAUSE,STOP,RECOVER_WAITTING_THREAD false
     * receivers 收件人 false
     * receiversCc 收件人(抄送) false
     * runMode 运行模式,可用值:RUN_MODE_SERIAL,RUN_MODE_PARALLEL false
     * startNodeList 开始节点列表(节点name) false
     * taskDependType 任务依赖类型,可用值:TASK_ONLY,TASK_PRE,TASK_POST false
     * timeout 超时时间 false
     * workerGroupId Worker Server分组ID false
     */
    // 运行流程实例 POST   /dolphinscheduler/projects/{projectName}/executors/start-process-instance
    public static void startProcessInstance(Map<String,String> params) throws IOException {

        Response response = post(baseUrl + "/dolphinscheduler/projects/"+ projectName +"/executors/start-process-instance",params, tokenHeaderMap );

        handlerResponse(response);
    }

    // 执行流程实例的各种操作(暂停、停止、重跑、恢复等)
    // /dolphinscheduler/projects/{projectName}/executors/execute
    public static void execute(int processInstanceId) throws IOException {

        Map<String,String> params = ImmutableMap.of("processInstanceId", processInstanceId+"");

        Response response = post(baseUrl + "/dolphinscheduler/projects/"+ projectName +"/executors/execute",params, tokenHeaderMap );

        handlerResponse(response);
    }

    /**
     * taskInstId 任务实例ID false
     */
    // 下载任务实例日志   /dolphinscheduler/log/download-log
    public static void downloadTaskLog() {

    }
}
