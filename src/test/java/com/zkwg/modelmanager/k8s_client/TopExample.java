package com.zkwg.modelmanager.k8s_client;

import io.fabric8.kubernetes.api.model.NodeList;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.api.model.metrics.v1beta1.NodeMetrics;
import io.fabric8.kubernetes.api.model.metrics.v1beta1.NodeMetricsList;
import io.fabric8.kubernetes.client.*;
import io.fabric8.kubernetes.client.dsl.Resource;
import net.bytebuddy.dynamic.scaffold.MethodGraph;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

public class TopExample {

  private static final Logger logger = LoggerFactory.getLogger(TopExample.class);

  public static void main(String[] args) throws Exception {

//    String is = IOUtils.toString(new FileInputStream(new File("E:\\project\\github\\model-manager\\src\\main\\resources\\k8s\\admin.conf")));
//    Config config = Config.fromKubeconfig(is);

    String ca = IOUtils.toString(new FileInputStream(new File("E:\\project\\github\\model-manager\\src\\main\\resources\\k8s\\ca.crt")));

    String akc = Base64.getEncoder().encodeToString(IOUtils.toByteArray(new FileInputStream(new File("E:\\project\\github\\model-manager\\src\\main\\resources\\k8s\\apiserver-kubelet-client.crt"))));

    String akck = IOUtils.toString(new FileInputStream(new File("E:\\project\\github\\model-manager\\src\\main\\resources\\k8s\\apiserver-kubelet-client.key")));

//    Config config = new ConfigBuilder()
//            .withMasterUrl("https://116.63.172.190:6443")
//            .withCaCertData(ca)
//            .withClientCertData(akc)
//            .withClientKeyData(akck)
//            .build();

    Config config = new ConfigBuilder().withMasterUrl("https://116.63.172.190:6443")
            .withTrustCerts(true)
            .withCaCertData(ca)
            .withClientCertData(akc)
            .withClientKeyData(akck)
            // 需将 Namespace 初始化为 null
            .withNamespace(null)
            .build();


//    final KubernetesClient client = new DefaultKubernetesClient(config);
    try (KubernetesClient client = new DefaultKubernetesClient()) {
//    try (KubernetesClient client = new DefaultKubernetesClient(config)) {
//      NodeMetricsList nodeMetricList = client.top().nodes().metrics();
//
//      logger.info("==== Node Metrics  ====");
//      nodeMetricList.getItems().forEach(nodeMetrics ->
//        logger.info("{}\tCPU: {}{}\tMemory: {}{}",
//          nodeMetrics.getMetadata().getName(),
//          nodeMetrics.getUsage().get("cpu").getAmount(), nodeMetrics.getUsage().get("cpu").getFormat(),
//          nodeMetrics.getUsage().get("memory").getAmount(), nodeMetrics.getUsage().get("memory").getFormat()
//        ));

//      NodeMetrics nodeMetrics = client.top().nodes().metrics("node");
//      nodeMetrics.getUsage();


//      NodeList nodeList = client.nodes().withLabelNotIn("node-role.kubernetes.io/master").list();
//
//      nodeList.getKind();
//
//      int j = 0;

//      Node selectedNode = client.nodes().withLabelNotIn("node-role.kubernetes.io/master").get();

//      NodeStatus nodeStatus = selectedNode.getStatus();

      PodList podList = client.pods().inAnyNamespace().list();

      List<Pod> pods = podList.getItems().stream().filter(pod -> !"master".equals(pod.getSpec().getNodeName())).collect(Collectors.toList());

//      for(Pod pod : podList.getItems()){
//        pod.getMetadata();
//        int i = 9;
//      }

      int i = 0;

//      logger.info("==== Pod Metrics ====");
//      client.top().pods().metrics("default").getItems().forEach(podMetrics ->
//        podMetrics.getContainers().forEach(containerMetrics ->
//          logger.info("{}\t{}\tCPU: {}{}\tMemory: {}{}",
//            podMetrics.getMetadata().getName(), containerMetrics.getName(),
//            containerMetrics.getUsage().get("cpu").getAmount(), containerMetrics.getUsage().get("cpu").getFormat(),
//            containerMetrics.getUsage().get("memory").getAmount(), containerMetrics.getUsage().get("memory").getFormat()
//          ))
//      );

//      final String defaultNamespace = "default";
//      client.pods().inNamespace(defaultNamespace).list().getItems().stream().findFirst().map(pod -> {
//        logger.info("==== Individual Pod Metrics ({}) ====", pod.getMetadata().getName());
//        return client.top().pods().metrics(defaultNamespace, pod.getMetadata().getName());
//      }).ifPresent(podMetrics ->
//        podMetrics.getContainers().forEach(containerMetrics ->
//          logger.info("{}\t{}\tCPU: {}{}\tMemory: {}{}",
//            podMetrics.getMetadata().getName(), containerMetrics.getName(),
//            containerMetrics.getUsage().get("cpu").getAmount(), containerMetrics.getUsage().get("cpu").getFormat(),
//            containerMetrics.getUsage().get("memory").getAmount(), containerMetrics.getUsage().get("memory").getFormat()
//          ))
//      );

    } catch (KubernetesClientException e) {
      logger.error(e.getMessage(), e);
    }
  }
}
