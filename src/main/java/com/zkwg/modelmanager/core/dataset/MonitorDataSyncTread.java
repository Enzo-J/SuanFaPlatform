package com.zkwg.modelmanager.core.dataset;

import com.zkwg.modelmanager.core.BaseContextHandler;
import com.zkwg.modelmanager.entity.DataSet;
import com.zkwg.modelmanager.entity.DatabaseTypeEnum;
import com.zkwg.modelmanager.entity.DsSyncTask;
import com.zkwg.modelmanager.service.IDataSetService;
import com.zkwg.modelmanager.service.IDsSyncTaskService;
import com.zkwg.modelmanager.utils.DolphinSchedulerClientUtils;

import java.util.Map;
import java.util.concurrent.Executors;

public class MonitorDataSyncTread extends Thread {

    private Integer tenantId;
    private DsSyncTask dsSyncTask;
    private DataSet dataSet;
    private IDsSyncTaskService dsSyncTaskService;
    private IDataSetService dataSetService;


    public MonitorDataSyncTread(Integer tenantId, IDataSetService dataSetService, DataSet dataSet, IDsSyncTaskService dsSyncTaskService, DsSyncTask dsSyncTask) {
        this.tenantId = tenantId;
        this.dsSyncTask = dsSyncTask;
        this.dataSet = dataSet;
        this.dsSyncTaskService = dsSyncTaskService;
        this.dataSetService = dataSetService;
    }

    /**
     * Status: 0 commit succeeded, 1 running, 2 prepare to pause, 3 pause, 4 prepare to stop, 5 stop, 6 fail,
     *         7 succeed, 8 need fault tolerance, 9 kill, 10 wait for thread, 11 wait for dependency to complete
     */
    @Override
    public void run() {
        try {
            BaseContextHandler.setTenant(tenantId);
            Map<String, Object> reusltMap = null;
            do {
                reusltMap = DolphinSchedulerClientUtils.queryFirstTaskForProInst(dsSyncTask.getProcessInstanceId());
                Integer status = SyncStatusEnum.getCode(reusltMap.get("state") == null ? "" : reusltMap.get("state").toString());
                if (status != dataSet.getStatus() && status != dsSyncTask.getStatus()) {
                    dsSyncTask.setStatus(status);
                    dataSet.setStatus(status);
                    dsSyncTaskService.updateById(dsSyncTask);
                    dataSetService.updateById(dataSet);
                    if (status == 5 || status == 6 || status == 7) { // stop(5,"STOP"), fail(6,"FAIL"), succeed(7,"SUCCEED"),
                        break;
                    }
                }

                Thread.sleep(1000);
            } while (reusltMap != null && !reusltMap.isEmpty());

        } catch (Exception e) {
            throw new RuntimeException("监控数据同步状态进程出现异常",e);
        }
    }

    private enum SyncStatusEnum {

        commit_succeeded(0,"COMMIT_SUCCEEDED"),
        running(1,"RUNNING"),
        prepare_to_pause(2,"PREPARETOPAUSE"),
        pause(3,"COMMIT_SUCCEEDED"),
        prepare_to_stop(4,"PREPARE_TO_STOP"),
        stop(5,"STOP"),
        fail(6,"FAIL"),
        succeed(7,"SUCCESS"),
        need_fault_tolerance(8,"NEED_FAULT_TOLERANCE"),
        kill(9,"KILL"),
        wait_for_thread(10,"WAIT_FOR_THREAD"),
        wait_for_dependency_to_complete(11,"WAIT_FOR_DEPENDENCY_TO_COMPLETE");

        private int code;
        private String text;

        private SyncStatusEnum(int code, String text) {
            this.code = code;
            this.text = text;
        }

        public static Integer getCode(String text) {
            for(SyncStatusEnum syncStatusEnum : SyncStatusEnum.values()) {
                if(syncStatusEnum.text.equals(text)) {
                    return syncStatusEnum.code;
                }
            }
            return -1;
        }
    }

}
