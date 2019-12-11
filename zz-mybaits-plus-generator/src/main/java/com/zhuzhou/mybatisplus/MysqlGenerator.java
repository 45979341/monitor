package com.zhuzhou.mybatisplus;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.zhuzhou.mybatisplus.config.MybatisPackageConfig;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @Author: chenzeting
 * Date:     2018/12/18
 * Description:
 */
public class MysqlGenerator {

    /**
     * <p>
     * 读取控制台内容
     * </p>
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    /**
     * RUN THIS
     */
    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/zz-mybatis-plus-generator/src/main/java");
        gc.setAuthor("chenzeting");
        gc.setDateType(DateType.SQL_PACK);
        gc.setOpen(false);
        String moduleName = scanner("模块名");
        String tableName = scanner("表名");
        int i = tableName.indexOf("_");
        if (i > 0) {
            String[] split = tableName.split("_");
            String temp = "";
            for (int k = 0;k < split.length;k++) {
                temp += (split[k].substring(0,1).toUpperCase() + split[k].substring(1));
            }
            gc.setServiceName(temp + "Service");
        } else {
            gc.setServiceName(tableName.substring(0,1).toUpperCase() + tableName.substring(1) + "Service");
        }
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://192.168.10.94:3306/haulage_motor?useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&useSSL=false");
        // dsc.setSchemaName("public");
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("bYb123456.");
        mpg.setDataSource(dsc);

        // 包配置
        MybatisPackageConfig pc = new MybatisPackageConfig();
//        pc.setModuleName(moduleName);
        pc.setParent("com.zhuzhou");
        pc.setService("spi." + moduleName);
        pc.setServiceImpl("impl." + moduleName);
        pc.setController("manager.api." + moduleName);
        pc.setMapper("dao." + moduleName);
        pc.setEntity("entity." + moduleName);
        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        List<FileOutConfig> focList = new ArrayList<>();
        focList.add(new FileOutConfig("/mapper/mapper.xml.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输入文件名称
                return projectPath + "/zz-railway-web/railway-dao/src/main/resources/mapper/" + moduleName
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        focList.add(new FileOutConfig("/mapper/service.java.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输入文件名称
                return projectPath + "/zz-railway-web/railway-spi/src/main/java/com/zhuzhou/spi/" + moduleName
                        + "/" + tableInfo.getServiceName() + StringPool.DOT_JAVA;
            }
        });
        focList.add(new FileOutConfig("/mapper/serviceImpl.java.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输入文件名称
                return projectPath + "/zz-railway-web/railway-impl/src/main/java/com/zhuzhou/impl/" + moduleName
                        + "/" + tableInfo.getServiceImplName() + StringPool.DOT_JAVA;
            }
        });
        focList.add(new FileOutConfig("/mapper/controller.java.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输入文件名称
                return projectPath + "/zz-railway-web/railway-controller/src/main/java/com/zhuzhou/manager/api/" + moduleName
                        + "/" + tableInfo.getControllerName() + StringPool.DOT_JAVA;
            }
        });
        focList.add(new FileOutConfig("/mapper/mapper.java.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输入文件名称
                return projectPath + "/zz-railway-web/railway-dao/src/main/java/com/zhuzhou/dao/" + moduleName
                        + "/" + tableInfo.getMapperName() + StringPool.DOT_JAVA;
            }
        });
        focList.add(new FileOutConfig("/mapper/entity.java.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输入文件名称
                return projectPath + "/zz-railway-web/railway-model/src/main/java/com/zhuzhou/entity/" + moduleName
                        + "/" + tableInfo.getEntityName() + StringPool.DOT_JAVA;
            }
        });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);
        // 关闭默认 xml 生成，调整生成 至 根目录
        mpg.setTemplate(new TemplateConfig().setXml(null)
                // 自定义模板配置，模板可以参考源码 /mybatis-plus/src/main/resources/template 使用 copy
                // 至您项目 src/main/resources/template 目录下，模板名称也可自定义如下配置：
                .setController(null)
                .setEntity(null)
                .setMapper(null)
                .setXml(null)
                .setService(null)
                .setServiceImpl(null)
        );

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
//        strategy.setSuperEntityClass("com.zhuzhou.mybatisplus.BaseEntity");
        strategy.setEntityLombokModel(true);
//        strategy.setSuperControllerClass("com.zhuzhou.mybatisplus.BaseController");
        strategy.setInclude(tableName);
        strategy.setSuperServiceClass("com.zhuzhou.spi.BaseService");
        strategy.setSuperServiceImplClass("com.zhuzhou.impl.BaseServiceImpl");
//        strategy.setSuperEntityColumns("id");
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(pc.getModuleName() + "_");
        mpg.setStrategy(strategy);
        // 选择 freemarker 引擎需要指定如下加，注意 pom 依赖必须有！
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }

}