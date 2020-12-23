//package com.zkwg.modelmanager.config;
//
//import com.alibaba.druid.pool.DruidDataSource;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.mybatis.spring.SqlSessionFactoryBean;
//import org.mybatis.spring.annotation.MapperScan;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
//import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//
//import javax.sql.DataSource;
//
//@Configuration
//// 扫描 Mapper 接口并容器管理
////@MapperScan(basePackages = DataSetDataSourceConfig.PACKAGE, sqlSessionFactoryRef = "datasetSqlSessionFactory")
//public class DataSetDataSourceConfig {
//
//    // 精确到 cluster 目录，以便跟其他数据源隔离
//    static final String PACKAGE = "com.springboot.dao.second";
//    static final String MAPPER_LOCATION = "classpath:mapper/second/*.xml";
//
//    @Value("${dataset.datasource.url}")
//    private String url;
//
//    @Value("${dataset.datasource.username}")
//    private String user;
//
//    @Value("${dataset.datasource.password}")
//    private String password;
//
//    @Value("${dataset.datasource.driverClassName}")
//    private String driverClass;
//
//    @Bean(name = "dataSetDataSource")
//    public DataSource clusterDataSource() {
//        DruidDataSource dataSource = new DruidDataSource();
//        dataSource.setDriverClassName(driverClass);
//        dataSource.setUrl(url);
//        dataSource.setUsername(user);
//        dataSource.setPassword(password);
//        return dataSource;
//    }
//
//    @Bean(name = "datasetTransactionManager")
//    public DataSourceTransactionManager clusterTransactionManager() {
//        return new DataSourceTransactionManager(clusterDataSource());
//    }
//
//    @Bean(name = "datasetSqlSessionFactory")
//    public SqlSessionFactory clusterSqlSessionFactory(@Qualifier("dataSetDataSource") DataSource clusterDataSource)
//            throws Exception {
//        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
//        sessionFactory.setDataSource(clusterDataSource);
//        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
//                .getResources(DataSetDataSourceConfig.MAPPER_LOCATION));
//        return sessionFactory.getObject();
//    }
//}