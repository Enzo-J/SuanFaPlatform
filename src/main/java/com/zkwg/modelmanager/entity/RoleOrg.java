package com.zkwg.modelmanager.entity;

//import com.baomidou.mybatisplus.samples.generator.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author jobob
 * @since 2020-09-20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
//@EqualsAndHashCode(callSuper = true)
public class RoleOrg  {

    private static final long serialVersionUID = 1L;

    private Integer roleId;

    private Integer orgId;


}
