package com.zkwg.modelmanager.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @Classname MenuMetaVo
 */
@Data
@AllArgsConstructor
public class MenuMetaVo implements Serializable {

    private String title;
    private String icon;
}
