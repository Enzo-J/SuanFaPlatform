package com.zkwg.modelmanager.k8s_client;

import com.zkwg.modelmanager.entity.seldon.SeldonDeployment;
import com.zkwg.modelmanager.utils.K8sClientUtils;
import com.zkwg.modelmanager.utils.K8sProperties;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import io.fabric8.kubernetes.api.model.apps.DeploymentSpec;
import io.fabric8.kubernetes.client.*;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.dsl.base.CustomResourceDefinitionContext;
import io.fabric8.kubernetes.client.dsl.internal.RawCustomResourceOperationsImpl;
import io.fabric8.kubernetes.client.utils.Serialization;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class SeldonDeploymentExamples {

  private static final Logger logger = LoggerFactory.getLogger(SeldonDeploymentExamples.class);

  private static final String SELDON_API_VERSION_V1 = "machinelearning.seldon.io/v1";

  private static final String KIND = "SeldonDeployment";

  private static final String PREDICTORS_IMLEMENTATION = "SKLEARN_SERVER";

  public static void main(String[] args) throws Exception {
    deploySklearnModelByLoadFile();
//    deploySklearnModel();

//    InputStream is = new FileInputStream(new File("E:\\项目\\github\\model-manager\\src\\main\\resources\\minio\\sklearn.yml"));
//    SeldonDeployment seldonDeployment = Serialization.unmarshal(is, SeldonDeployment.class);
//    log(seldonDeployment.toString());
  }

  public static void deploySklearnModelByLoadFile() {

//      String master = "https://172.20.10.14:6443/";
//      Config config = new ConfigBuilder().withMasterUrl(master).build();
//      KubernetesClient client = new DefaultKubernetesClient(config);

      KubernetesClient client = null;
      try {

        String is = IOUtils.toString(new FileInputStream(new File("E:\\项目\\github\\model-manager\\src\\main\\resources\\k8s\\admin.conf")));
        Config config = Config.fromKubeconfig(is);
        client = new DefaultKubernetesClient(config);


        CustomResourceDefinitionContext crdContext = new CustomResourceDefinitionContext.Builder()
                .withGroup("machinelearning.seldon.io")
                .withPlural("seldondeployments")
                .withScope("Namespaced")
                .withVersion("v1")
                .build();



        InputStream inputStream = SeldonDeploymentExamples.class.getResourceAsStream("/minio/test.yml");
        SeldonDeployment seldonDeployment = Serialization.unmarshal(inputStream,SeldonDeployment.class);
        String namespace = seldonDeployment.getMetadata().getNamespace();//"sklearn-model";

        RawCustomResourceOperationsImpl rawCustomResourceOperationsImpl = client.customResource(crdContext);
        rawCustomResourceOperationsImpl.create(namespace,Serialization.asJson(seldonDeployment));
//        client.customResource(crdContext).create(namespace,Serialization.asJson(seldonDeployment));

//        client.events().inNamespace(namespace).watch(new Watcher<Event>() {
//          @Override
//          public void eventReceived(Action action, Event resource) {
//            System.out.println("event " + action.name() + " " + resource.toString());
//          }
//          @Override
//          public void onClose(KubernetesClientException cause) {
//            System.out.println("Watcher close due to " + cause);
//          }
//        });

//        log("Watching custom resources now");
        final CountDownLatch closeLatch = new CountDownLatch(1);
        Watch watch = rawCustomResourceOperationsImpl.watch(namespace,null,null,null, new Watcher<String>() {
          @Override
          public void eventReceived(Watcher.Action action, String resource) {
            logger.info("{}: {}", action, resource);
            Map<String, Object> map = Serialization.unmarshal(resource,Map.class);
            Map<String, Object> status = (Map<String, Object>) map.get("status");
            if(status != null && "Available".equals(status.get("state"))){
              closeLatch.countDown();
            }
          }
          @Override
          public void onClose(KubernetesClientException e) {
            logger.debug("Watcher onClose");
//            closeLatch.countDown();
            if (e != null) {
              logger.error(e.getMessage(), e);
            }
          }
        });

        closeLatch.await(10, TimeUnit.MINUTES);
//        watch.close();
        log("Done...");

      } catch (Exception e){
        e.printStackTrace();
      } finally {
//        client.namespaces().withName("thisisatest").delete();
        client.close();
      }

  }


  public static void deploySklearnModel(){
//    String master = "https://172.20.10.14:6443/";
//    Config config = new ConfigBuilder().withMasterUrl(master).build();
//    KubernetesClient client = new DefaultKubernetesClient(config);

    KubernetesClient client = null;
    try {

      String is = IOUtils.toString(new FileInputStream(new File("E:\\项目\\github\\model-manager\\src\\main\\resources\\k8s\\admin.conf")));
      Config config = Config.fromKubeconfig(is);
      client = new DefaultKubernetesClient(config);

      Map<String,String> annotationsMap = new HashMap<String,String>();
      annotationsMap.put("seldon.io/executor","true");

      Map<String,Object> graphMap = new HashMap<String,Object>();
      graphMap.put("implementation",PREDICTORS_IMLEMENTATION);
      graphMap.put("modelUri","s3://iris");
      graphMap.put("envSecretRefName","seldon-init-container-secret");
      graphMap.put("name","classifier");

      Map<String,Object> componentSpecsMap = new HashMap<String,Object>();
      componentSpecsMap.put("graph",graphMap);
      componentSpecsMap.put("name","default");
      componentSpecsMap.put("replicas",2);
      componentSpecsMap.put("componentSpecs",null);

      List<Map<String,Object>> list = new ArrayList<>();
      list.add(componentSpecsMap);

      DeploymentSpec deploymentSpec = new DeploymentSpec();
      deploymentSpec.setAdditionalProperty("annotations",annotationsMap);
      deploymentSpec.setAdditionalProperty("name","iris");
      deploymentSpec.setAdditionalProperty("predictors",list);

      log(deploymentSpec.toString());

      System.out.println();

      Deployment seldonDeployment = new DeploymentBuilder()
              .withApiVersion(SELDON_API_VERSION_V1)
              .withKind(KIND)
              .withNewMetadata()
              .withName("sklearn-iris")
              .withNamespace("sklearn-model")
              .endMetadata()
              .build();

      seldonDeployment.setSpec(deploymentSpec);

      log(Serialization.asJson(seldonDeployment));

      CustomResourceDefinitionContext crdContext = new CustomResourceDefinitionContext.Builder()
              .withGroup("machinelearning.seldon.io")
              .withPlural("seldondeployments")
              .withScope("Namespaced")
              .withVersion("v1")
              .build();

//      Map<String, Object> resultMap = client.customResource(crdContext).create("sklearn-model",Serialization.asJson(seldonDeployment));

      String name = "sklearn-iris";
      String namespace = "sklearn-model";
      final Map<String, Object> resultStatusMap = new HashMap<>();
//      status.put("state","TimeOut");
      final CountDownLatch latch = new CountDownLatch(1);

      K8sProperties k8sProperties = new K8sProperties();
      k8sProperties.setAdminConf(new FileSystemResource("E:\\项目\\github\\model-manager\\src\\main\\resources\\k8s\\admin.conf"));

      Watch watch = K8sClientUtils.getInstance(k8sProperties).createOrReplaceCRD(namespace,name,
              Serialization.asJson(seldonDeployment), new Watcher<String>() {
                @Override
                public void eventReceived(Watcher.Action action, String resource) {
                  logger.info("{}: {}", action, resource);
                  Map<String, Object> map = Serialization.unmarshal(resource,Map.class);
                  Map<String, Object> status = (Map<String, Object>) map.get("status");
                  if(status != null && "Available".equals(status.get("state"))){
                      resultStatusMap.putAll(status);
                      latch.countDown();
                  }
                }

                @Override
                public void onClose(KubernetesClientException e) {
                  logger.debug("Watcher onClose");
                  if (e != null) {
                    logger.error(e.getMessage(), e);
                  }
                }
              });

      latch.await(3, TimeUnit.MINUTES);

      watch.close();

//      seldonDeployment = client.apps().deployments().inNamespace("sklearn-model").create(seldonDeployment);

      log("Done.");

    } catch (Exception e) {
      e.printStackTrace();
    }   finally {
//      client.namespaces().withName("thisisatest").delete();
      client.close();
    }
  }


  private static void log(String action, Object obj) {
    logger.info("{}: {}", action, obj);
  }

  private static void log(String action) {
    logger.info(action);
  }
}