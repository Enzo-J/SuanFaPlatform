package com.zkwg.modelmanager.k8s_client;

import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.NamespaceBuilder;
import io.fabric8.kubernetes.api.model.ServiceAccount;
import io.fabric8.kubernetes.api.model.ServiceAccountBuilder;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import io.fabric8.kubernetes.api.model.apps.DeploymentSpec;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeploymentExamples {

  private static final Logger logger = LoggerFactory.getLogger(DeploymentExamples.class);

  private static final String SELDON_API_VERSION_V1 = "machinelearning.seldon.io/v1";

  private static final String KIND = "SeldonDeployment";

  private static final String PREDICTORS_IMLEMENTATION = "SKLEARN_SERVER";

  public static void main(String[] args) throws InterruptedException {

    String master = "https://172.20.10.14:6443/";
    Config config = new ConfigBuilder().withMasterUrl(master).build();
    KubernetesClient client = new DefaultKubernetesClient(config);

    try {

//      String namespace = client.getNamespace();
//      System.out.println(namespace);

      // Create a namespace for all our stuff
      Namespace ns = new NamespaceBuilder().withNewMetadata().withName("sklearn-model").addToLabels("this", "rocks").endMetadata().build();
      log("Created namespace", client.namespaces().createOrReplace(ns));

//      ServiceAccount fabric8 = new ServiceAccountBuilder().withNewMetadata().withName("fabric8").endMetadata().build();
//
//      client.serviceAccounts().inNamespace("sklearn-model").createOrReplace(fabric8);
//      for (int i = 0; i < 2; i++) {
//        System.err.println("Iteration:" + (i+1));
        DeploymentSpec deploymentSpec = new DeploymentSpec();
//        deploymentSpec.setAdditionalProperty();
        Deployment deployment = new DeploymentBuilder()
//                .withApiVersion("machinelearning.seldon.io/v1")
          .withNewMetadata()
          .withName("nginx")
          .endMetadata()
          .withNewSpec()
          .withReplicas(1)
          .withNewTemplate()
          .withNewMetadata()
          .addToLabels("app", "nginx")
          .endMetadata()
          .withNewSpec()
          .addNewContainer()
          .withName("nginx")
          .withImage("nginx")
          .addNewPort()
          .withContainerPort(80)
          .endPort()
          .endContainer()
          .endSpec()
          .endTemplate()
          .withNewSelector()
          .addToMatchLabels("app", "nginx")
          .endSelector()
          .endSpec()
          .build();

        client.apps().deployments().inNamespace("sklearn-model").withName("").get();
        deployment = client.apps().deployments().inNamespace("sklearn-model").create(deployment);
        log("Created deployment", deployment.toString());

//        System.err.println("Scaling up:" + deployment.getMetadata().getName());
//        client.apps().deployments().inNamespace("thisisatest").withName("nginx").scale(2, true);
//        log("Created replica sets:", client.apps().replicaSets().inNamespace("thisisatest").list().getItems());
//        System.err.println("Deleting:" + deployment.getMetadata().getName());
//        client.resource(deployment).delete();
//      }
      log("Done.");

    }finally {
      client.namespaces().withName("thisisatest").delete();
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