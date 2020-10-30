package com.jnu.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.SqlExplainInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.jnu.handler.MyMetaObjectHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author ：Killer
 * @date ：Created in 20-10-21 下午5:37
 * @description：${description}
 * @modified By：
 * @version: version
 */
@Configuration
@MapperScan(basePackages = {"com.jnu.dao"}, sqlSessionTemplateRef = "sqlSessionTemplate")
@EnableTransactionManagement(proxyTargetClass = true)
public class MybatisPlusConfig {


    @Autowired
    private GlobalConfig globalConfig;
    @Autowired
    private PaginationInterceptor paginationInterceptor;
    @Autowired
    private SqlExplainInterceptor sqlExplainInterceptor;

    @Bean(name = "dataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    @Primary
    public DataSource dataSource() {
        //坑...,这里的属性名是user,而不是username
        return new DruidDataSource();
    }

    @Bean(name = "sqlSessionFactory")
    @Primary
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource")DataSource dataSource) throws Exception {
        //坑.....,这里必须用MybatisSqlSessionFactoryBean 而不能用SqlSessionFactoryBean,否则访问baseMapper
        //方法时会报not binding错误
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        //设置数据源
        bean.setDataSource(dataSource);
        //设置mapper扫描路径
        //这里注意:扫描的xml文件中每个方法会生成一个MapperStatemnt,放入Map<String, MappedStatement> mappedStatements中
        //每个文件只会扫描一次,多个数据源之间如果出现重复扫描,下一个数据源将扫描不到,当调用时就会报Invalid bound statement (not found)
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:com/jnu/mapper/*.xml"));
        //设置全局配置
        bean.setGlobalConfig(globalConfig);
        bean.setPlugins(new Interceptor[]{
                //添加分页功能
                paginationInterceptor,
                //添加分析插件
                sqlExplainInterceptor
        });
        return bean.getObject();
    }

    @Bean(name = {"sqlSessionTemplate"})
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
    /**
     * 实体类需要加@TableName注解指定数据库表名，
     * 通过@TableId注解指定id的增长策略。
     * 实体类少倒也无所谓，实体类一多的话也麻烦。
     * 所以可以进行全局策略配置
     */
    @Bean
    public GlobalConfig globalConfig(MetaObjectHandler metaObjectHandler) {
        GlobalConfig.DbConfig dbConfig = new GlobalConfig.DbConfig();
        // 主键类型  AUTO:"数据库ID自增", INPUT:"用户输入ID",ID_WORKER:"全局唯一ID (数字类型唯一ID)", UUID:"全局唯一ID UUID";
        dbConfig.setIdType(IdType.AUTO);
        //设置表明映射前缀
        // dbConfig.setTablePrefix("t_");
        // 字段策略 IGNORED:"忽略判断",NOT_NULL:"非 NULL 判断"),NOT_EMPTY:"非空判断"
        dbConfig.setFieldStrategy(FieldStrategy.NOT_NULL);
//        // 数据库大写下划线转换
        dbConfig.setCapitalMode(true);
//        // 逻辑删除配置
        dbConfig.setLogicDeleteValue("true");
        dbConfig.setLogicNotDeleteValue("false");
        dbConfig.setDbType(DbType.MYSQL);


        GlobalConfig globalConfig = new GlobalConfig();
        //设置逻辑删除注入   以后的删除操作实际是update is_delete=1,查询时候也不会将is_delete为1的记录查出来
        globalConfig.setSqlInjector(new LogicSqlInjector());
        //#刷新mapper 调试神器
        //globalConfig.setRefresh(true);
        globalConfig.setDbConfig(dbConfig);
        //MetaDataObjectHandler  自动填充全局字段
        globalConfig.setMetaObjectHandler(metaObjectHandler);
        return globalConfig;
    }

    /**
     * mybatis-plus分页插件
     */
    @Bean("paginationInterceptor")
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    /**
     * 分析插件,当执行全表删除操作时会阻止执行
     */
    @Bean("sqlExplainInterceptor")
    public SqlExplainInterceptor sqlExplainInterceptor() {
        SqlExplainInterceptor sqlExplainInterceptor = new SqlExplainInterceptor();
        Properties properties = new Properties();
        properties.put("stopProceed", true);
        sqlExplainInterceptor.setProperties(properties);
        return sqlExplainInterceptor;
    }

    /**
     * 构建填充器
     *
     * @return 填充器
     */
    @Bean("metaObjectHandler")
    public MetaObjectHandler metaObjectHandler() {
        return new MyMetaObjectHandler();
    }


}