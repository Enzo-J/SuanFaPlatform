package com.zkwg.modelmanager.config;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.google.common.collect.ImmutableList;
import com.zkwg.modelmanager.core.BaseContextHandler;
import com.zkwg.modelmanager.security.AISecurityUser;
import com.zkwg.modelmanager.security.dataScope.DataScopeInterceptor;
import com.zkwg.modelmanager.service.IUserService;
import com.zkwg.modelmanager.utils.SecurityUtil;
import com.zkwg.modelmanager.utils.SpringUtils;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @author miemie
 * @since 2018-08-10
 */
@Configuration
@MapperScan("com.zkwg.modelmanager.mapper")
public class MybatisPlusConfig {

    private Logger logger = LoggerFactory.getLogger(MybatisPlusConfig.class);

    /**
     * 新多租户插件配置,一缓和二缓遵循mybatis的规则,需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存万一出现问题
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new MyTenantLineInnerInterceptor(new TenantLineHandler() {
            @Override
            public Expression getTenantId() {
                Integer tenantId = BaseContextHandler.getTenant();
                Assert.notNull(tenantId, "租户id为空");
                logger.info("租户id为 {}", tenantId);
                return new LongValue(tenantId);
            }

            // 这是 default 方法,默认返回 false 表示所有表都需要拼多租户条件
            @Override
            public boolean ignoreTable(String tableName) {
//                return !"user".equalsIgnoreCase(tableName);
//                return false;
                ImmutableList<String> igoreTableList =  ImmutableList.of("permission","tenant","role_permission","user_role","role_org","training_model","dict","dict_item","algorithm_type","parameter","test_case");
                return igoreTableList.contains(tableName);
            }
        }));
        // 如果用了分页插件注意先 add TenantLineInnerInterceptor 再 add PaginationInnerInterceptor
        // 用了分页插件必须设置 MybatisConfiguration#useDeprecatedExecutor = false
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return interceptor;
    }

    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return configuration -> configuration.setUseDeprecatedExecutor(false);
    }

    /**
     * 数据权限插件
     *
     * @return DataScopeInterceptor
     */
    @Order(10)
    @Bean
//    @ConditionalOnProperty(prefix = DatabaseProperties.PREFIX, name = "isDataScope", havingValue = "true", matchIfMissing = true)
    public DataScopeInterceptor dataScopeInterceptor() {
        return new DataScopeInterceptor((userId) -> SpringUtils.getBean(IUserService.class).getDataScopeById(userId));
    }

}
