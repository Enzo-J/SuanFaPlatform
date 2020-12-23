package com.zkwg.modelmanager.k8s_client;

import io.fabric8.kubernetes.client.*;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class KubernetesVersionTest {

  private static Logger logger = LoggerFactory.getLogger(KubernetesVersionTest.class);

  public static void main(String args[]) {
//    String master = "https://172.20.10.14:6443/";
//    if (args.length == 1) {
//      master = args[0];
//    }
//
//    Config config = new ConfigBuilder().withMasterUrl(master).build();

    try{

      String is = IOUtils.toString(new FileInputStream(new File("E:\\项目\\github\\model-manager\\src\\main\\resources\\k8s\\admin.conf")));
      Config config = Config.fromKubeconfig(is);
      final KubernetesClient client = new DefaultKubernetesClient(config);

      VersionInfo versionInfo = client.getVersion();
      System.out.println(client.namespaces().list());
      log("Version details of this Kubernetes cluster :-");
      log("Major        : ", versionInfo.getMajor());
      log("Minor        : ", versionInfo.getMinor());
      log("GitVersion   : ", versionInfo.getGitVersion());
      log("BuildDate    : ", versionInfo.getBuildDate());
      log("GitTreeState : ", versionInfo.getGitTreeState());
      log("Platform     : ", versionInfo.getPlatform());
      log("GitVersion   : ", versionInfo.getGitVersion());
      log("GoVersion    : ", versionInfo.getGoVersion());
      log("GitCommit    : ", versionInfo.getGitCommit());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void log(String action, Object obj) {
    logger.info("{}: {}", action, obj);
  }

  private static void log(String action) {
    logger.info(action);
  }
}
