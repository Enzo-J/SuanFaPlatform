package com.zkwg.modelmanager.entity.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * @Classname menuVo
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MenuVo {

    private String name;
    private String path;
    private String redirect;
    private String component;
    private Boolean alwaysShow;
    private MenuMetaVo meta;
    private List<MenuVo> children;

    public static void main(String[] args) {
//        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
//        textEncryptor.setPassword("EbfYkitulv73I2p0mXI50JMXoaxZTKJ1");
//        String password = textEncryptor.encrypt("101.132.64.80");
//        System.out.println(password);

    }
}
