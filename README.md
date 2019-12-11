## 采用技术框架
项目架构以Spring Boot为核心技术框架, 主要集成了以下框架
- Spring Boot
- Spring Cloud
- Spring Session
- Spring Cache
- Spring Validation
- MybatisPlus
- Lombok
- Redis
- MySql
- Apache Atlas  http://atlas.apache.org/
- RocketMQ

------------

## Spring Boot 优势
- 遵循“习惯优于配置”的原则，使用Spring Boot只需要很少的配置，大部分的时候我们直接使用默认的配置即可；
- 项目快速搭建，可以无需配置的自动整合第三方的框架；
- 可以完全不使用XML配置文件，只需要自动配置和Java Config；
- 内嵌Servlet容器，降低了对环境的要求，可以使用命令直接执行项目，应用可用jar包执行：java -jar；
- 提供了starter POM, 能够非常方便的进行包管理, 很大程度上减少了jar hell或者dependency hell；
- 运行中应用状态的监控；
- 对主流开发框架的无配置集成；
- 与云计算的天然继承；

------------


## 项目架构Controller设计
### 统一返回结果
整个项目完全采用动静分离的结构, 后台只负责提供接口服务, 提供给客户端Json格式化的数据, 后端不负责视图的渲染, 页面路由.
Controller类中方法, 全部采用如下结构:
*public Result methodName(Form form);*

### 分页查询，单表查询。自定义注解提升开发sql编写
所有分页查询Form表单全部继承PageForm
对于查询条件参数可以加上@Condition注解, 根据封装的SpecUtils类，对Service自动会进行动态查询
对于需要需要排序的查询加上@Order注解, 会自定按对应的属性进行排序

### 动态JSON视图
Json动态视图直接采用注解@Json
在控制器方法上采用@Json注解, Json注解中include 指定包含哪些字段, exclude指定排除哪些字段 如下
   
    @OAuth
    @Json(type = SysPost.class, exclude = "createdTime,updatedTime", include="name")
    @GetMapping("/sys/post/find")
    public Result page (PostPageForm form) {
        IPage<SysPost> page = sysPostService.pageForm(form);
        return Result.success().setData(page);
    }

### 采用JSR 303标准进行参数校验 
JSR 303 – Bean Validation 是一个数据验证的规范，2009 年 11 月确定最终方案。2009 年 12 月 Java EE 6 发布，
Bean Validation 作为一个重要特性被包含其中。本文将对 Bean Validation 的主要功能进行介绍，
并通过一些示例来演示如何在 Java 开发过程正确的使用 Bean Validation。
> Bean Validation 中的 constraint
@Null	被注释的元素必须为 null
@NotNull	被注释的元素必须不为 null
@AssertTrue	被注释的元素必须为 true
@AssertFalse	被注释的元素必须为 false
@Min(value)	被注释的元素必须是一个数字，其值必须大于等于指定的最小值
@Max(value)	被注释的元素必须是一个数字，其值必须小于等于指定的最大值
@DecimalMin(value)	被注释的元素必须是一个数字，其值必须大于等于指定的最小值
@DecimalMax(value)	被注释的元素必须是一个数字，其值必须小于等于指定的最大值
@Size(max, min)	被注释的元素的大小必须在指定的范围内
@Digits (integer, fraction)	被注释的元素必须是一个数字，其值必须在可接受的范围内
@Past	被注释的元素必须是一个过去的日期
@Future	被注释的元素必须是一个将来的日期
@Pattern(value)	被注释的元素必须符合指定的正则表达式
@Email	被注释的元素必须是电子邮箱地址
@Length	被注释的字符串的大小必须在指定的范围内
@NotEmpty	被注释的字符串的必须非空
@Range	被注释的元素必须在合适的范围内
@Phone  被注释的元素必须是正则表达式的手机号

代码示例
   
	public class OrderAddForm implement Form {
		// 必须不为 null, 大小是 10
		@NotNull
		@Size(min = 10, max = 10)
		private String orderId;
		// 必须不为空
		@NotEmpty
		private String customer;
		// 必须是一个电子信箱地址
		@Email
		private String email;
		@NotNull
		private String status;
		// 必须不为 null
		@NotNull
		private Date createDate;
		// 嵌套验证
		@Valid
		private Product product;
		//必须是type字段为1，2的值
        @ValueSet({"1","2"})
        private Integer type;
	}


### RequestMapping注解
#最好在@RequestMapping注解的name属性赋值, 方便后面自动生成接口文档, 自动抓取API接口信息写入数据库Resource表中
#后期手动添加相当麻烦, 也容易出错,或者漏掉.  ** 将来要整合yapi自动生成接口文档，待优化中。

### Spring Session
pom文件引入spring-session, 集群环境保证用户登录session共享问题，若系统设计为指定hash，可另行设计。

### Spring Cache
Spring 3.1 引入了激动人心的基于注释（annotation）的缓存（cache）技术，它本质上不是一个具体的缓存实现方案
（例如 EHCache），而是一个对缓存使用的抽象，通过在既有代码中添加少量它定义的各种 annotation，
即能够达到缓存方法的返回对象的效果。

例如如下对活动的分页结果进行缓存,只需要在方法上加@Cacheable注解
即以id为RedisKey.IDX_ID + key为key, value为list 放入redis
如： 

    @Override
    @Cacheable(value = RedisKey.IDX_ID, key = "#id")
    public List<Idx> detail(String id) {
        //获取idx数据
        ResponseWrap idxWrap = HttpUtils.getInstance().get(IDX_URL)
                .addParameter("url", idx.getDir() + idx.getFileName()).execute();
        IdxAnalyzeResult idxResult = idxWrap.getJson(IdxAnalyzeResult.class);
        List<Idx> list = idxResult.getData();
        return list;
    }
	
------------


## 项目Service 层设计
所有的Service接口都要继承BaseService接口, BaseService定义基本的增删改查, 还有按不同的条件, 排序, 分页查询接口,
在BaseServiceImpl已经全部实现 所有其他的具体的Service都不需要写了,


    public interface BaseService<T> {

    /**
     * <p>
     * 插入一条记录（选择字段，策略插入）
     * </p>
     *
     * @param entity 实体对象
     */
    boolean save(T entity);

    /**
     * <p>
     * 插入（批量）
     * </p>
     *
     * @param entityList 实体对象集合
     */
    default boolean saveBatch(Collection<T> entityList) {
        return saveBatch(entityList, 1000);
    }

    /**
     * <p>
     * 插入（批量）
     * </p>
     *
     * @param entityList 实体对象集合
     * @param batchSize  插入批次数量
     */
    boolean saveBatch(Collection<T> entityList, int batchSize);

    /**
     * <p>
     * 批量修改插入
     * </p>
     *
     * @param entityList 实体对象集合
     */
    default boolean saveOrUpdateBatch(Collection<T> entityList) {
        return saveOrUpdateBatch(entityList, 1000);
    }

    /**
     * <p>
     * 批量修改插入
     * </p>
     *
     * @param entityList 实体对象集合
     * @param batchSize  每次的数量
     */
    boolean saveOrUpdateBatch(Collection<T> entityList, int batchSize);

    /**
     * <p>
     * 根据 ID 删除
     * </p>
     *
     * @param id 主键ID
     */
    boolean removeById(Serializable id);

    /**
     * <p>
     * 根据 columnMap 条件，删除记录
     * </p>
     *
     * @param columnMap 表字段 map 对象
     */
    boolean removeByMap(Map<String, Object> columnMap);

    /**
     * <p>
     * 根据 entity 条件，删除记录
     * </p>
     *
     * @param queryWrapper 实体包装类 {@link QueryWrapper}
     */
    boolean remove(Wrapper<T> queryWrapper);

    /**
     * <p>
     * 根据 entity 条件，删除记录
     * </p>
     *
     * @param entity 实体包装类
     */
    boolean remove(T entity);

    /**
     * <p>
     * 删除（根据ID 批量删除）
     * </p>
     *
     * @param idList 主键ID列表
     */
    boolean removeByIds(Collection<? extends Serializable> idList);

    /**
     * <p>
     * 根据 ID 选择修改
     * </p>
     *
     * @param entity 实体对象
     */
    boolean updateById(T entity);

    /**
     * <p>
     * 根据 whereEntity 条件，更新记录
     * </p>
     *
     * @param entity        实体对象
     * @param updateWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper}
     */
    boolean update(T entity, Wrapper<T> updateWrapper);

    /**
     * <p>
     * 根据ID 批量更新
     * </p>
     *
     * @param entityList 实体对象集合
     */
    default boolean updateBatchById(Collection<T> entityList) {
        return updateBatchById(entityList, 1000);
    }

    /**
     * <p>
     * 根据ID 批量更新
     * </p>
     *
     * @param entityList 实体对象集合
     * @param batchSize  更新批次数量
     */
    boolean updateBatchById(Collection<T> entityList, int batchSize);

    /**
     * <p>
     * TableId 注解存在更新记录，否插入一条记录
     * </p>
     *
     * @param entity 实体对象
     */
    boolean saveOrUpdate(T entity);

    /**
     * <p>
     * 根据 ID 查询
     * </p>
     *
     * @param id 主键ID
     */
    T getById(Serializable id);

    /**
     * <p>
     * 查询（根据ID 批量查询）
     * </p>
     *
     * @param idList 主键ID列表
     */
    Collection<T> listByIds(Collection<? extends Serializable> idList);

    /**
     * <p>
     * 查询（根据 columnMap 条件）
     * </p>
     *
     * @param columnMap 表字段 map 对象
     */
    Collection<T> listByMap(Map<String, Object> columnMap);

    /**
     * <p>
     * 根据 Wrapper，查询一条记录
     * </p>
     *
     * @param queryWrapper 实体对象封装操作类 {@link QueryWrapper}
     */
    default T getOne(Wrapper<T> queryWrapper) {
        return getOne(queryWrapper, false);
    }

    /**
     * <p>
     * 根据 entity，查询一条记录
     * </p>
     *
     * @param entity 实体对象封装操作类
     */
    default T getOne(T entity) {
        Wrapper<T> queryWrapper = new QueryWrapper<>(entity);
        return getOne(queryWrapper, false);
    }

    /**
     * <p>
     * 根据 Wrapper，查询一条记录
     * </p>
     *
     * @param queryWrapper 实体对象封装操作类 {@link QueryWrapper}
     * @param throwEx      有多个 result 是否抛出异常
     */
    T getOne(Wrapper<T> queryWrapper, boolean throwEx);

    /**
     * <p>
     * 根据 Wrapper，查询一条记录
     * </p>
     *
     * @param queryWrapper 实体对象封装操作类 {@link QueryWrapper}
     */
    Map<String, Object> getMap(Wrapper<T> queryWrapper);

    /**
     * <p>
     * 根据 entity，查询一条记录
     * </p>
     *
     * @param entity 实体对象封装操作类
     */
    Map<String, Object> getMap(T entity);

    /**
     * <p>
     * 根据 Wrapper，查询一条记录
     * </p>
     *
     * @param queryWrapper 实体对象封装操作类 {@link QueryWrapper}
     */
    default Object getObj(Wrapper<T> queryWrapper) {
        return SqlHelper.getObject(listObjs(queryWrapper));
    }

    /**
     * <p>
     * 根据 Wrapper 条件，查询总记录数
     * </p>
     *
     * @param queryWrapper 实体对象封装操作类 {@link QueryWrapper}
     */
    int count(Wrapper<T> queryWrapper);

    /**
     * <p>
     * 根据 entity 条件，查询总记录数
     * </p>
     *
     * @param entity 实体对象封装操作类
     */
    int count(T entity);

    /**
     * <p>
     * 查询总记录数
     * </p>
     *
     * @see Wrappers#emptyWrapper()
     */
    default int count() {
        return count(Wrappers.<T>emptyWrapper());
    }

    /**
     * <p>
     * 查询列表
     * </p>
     *
     * @param queryWrapper 实体对象封装操作类 {@link QueryWrapper}
     */
    List<T> list(Wrapper<T> queryWrapper);

    /**
     * <p>
     * 查询列表
     * </p>
     *
     * @param entity 实体对象封装操作类
     */
    List<T> list(T entity);

    /**
     * <p>
     * 查询所有
     * </p>
     *
     * @see Wrappers#emptyWrapper()
     */
    default List<T> list() {
        return list(Wrappers.<T>emptyWrapper());
    }

    /**
     * list 参数校验
     * @param form
     * @return
     */
    List<T> list(Form<T> form);

    /**
     * <p>
     * 翻页查询
     * </p>
     *
     * @param page         翻页对象
     * @param queryWrapper 实体对象封装操作类 {@link QueryWrapper}
     */
    IPage<T> page(IPage<T> page, Wrapper<T> queryWrapper);

    /**
     * <p>
     * 翻页查询
     * </p>
     *
     * @param page         翻页对象
     * @param entity 实体对象封装操作类
     */
    IPage<T> page(IPage<T> page, T entity);

    /**
     * <p>
     * 无条件翻页查询
     * </p>
     *
     * @param page 翻页对象
     * @see Wrappers#emptyWrapper()
     */
    default IPage<T> page(IPage<T> page) {
        return page(page, Wrappers.<T>emptyWrapper());
    }

    /**
     * <p>
     * form 自定义条件分页查询
     * 翻页查询
     * </p>
     * @param form 翻页对象
     */
    IPage<T> pageForm(Form<T> form);

    /**
     * <p>
     * 查询列表
     * </p>
     *
     * @param queryWrapper 实体对象封装操作类 {@link QueryWrapper}
     */
    List<Map<String, Object>> listMaps(Wrapper<T> queryWrapper);

    /**
     * <p>
     * 查询列表
     * </p>
     *
     * @param entity 实体对象封装操作类
     */
    List<Map<String, Object>> listMaps(T entity);

    /**
     * <p>
     * 查询所有列表
     * </p>
     *
     * @see Wrappers#emptyWrapper()
     */
    default List<Map<String, Object>> listMaps() {
        return listMaps(Wrappers.<T>emptyWrapper());
    }

    /**
     * <p>
     * 根据 Wrapper 条件，查询全部记录
     * </p>
     *
     * @param queryWrapper 实体对象封装操作类 {@link QueryWrapper}
     */
    List<Object> listObjs(Wrapper<T> queryWrapper);

    /**
     * <p>
     * 查询全部记录
     * </p>
     *
     * @see Wrappers#emptyWrapper()
     */
    default List<Object> listObjs() {
        return listObjs(Wrappers.<T>emptyWrapper());
    }

    /**
     * <p>
     * 翻页查询
     * </p>
     *
     * @param page         翻页对象
     * @param queryWrapper 实体对象封装操作类 {@link QueryWrapper}
     */
    IPage<Map<String, Object>> pageMaps(IPage<T> page, Wrapper<T> queryWrapper);

    /**
     * <p>
     * 无条件翻页查询
     * </p>
     *
     * @param page 翻页对象
     * @see Wrappers#emptyWrapper()
     */
    default IPage<Map<String, Object>> pageMaps(IPage<T> page) {
        return pageMaps(page, Wrappers.<T>emptyWrapper());
    }
}

## 项目DAO层设计

### Dao设计采用 Mybatis-Plus
特性
无侵入：只做增强不做改变，引入它不会对现有工程产生影响，如丝般顺滑
损耗小：启动即会自动注入基本 CURD，性能基本无损耗，直接面向对象操作
强大的 CRUD 操作：内置通用 Mapper、通用 Service，仅仅通过少量配置即可实现单表大部分 CRUD 操作，更有强大的条件构造器，满足各类使用需求
支持 Lambda 形式调用：通过 Lambda 表达式，方便的编写各类查询条件，无需再担心字段写错
支持多种数据库：支持 MySQL、MariaDB、Oracle、DB2、H2、HSQL、SQLite、Postgre、SQLServer2005、SQLServer 等多种数据库
支持主键自动生成：支持多达 4 种主键策略（内含分布式唯一 ID 生成器 - Sequence），可自由配置，完美解决主键问题
支持 XML 热加载：Mapper 对应的 XML 支持热加载，对于简单的 CRUD 操作，甚至可以无 XML 启动
支持 ActiveRecord 模式：支持 ActiveRecord 形式调用，实体类只需继承 Model 类即可进行强大的 CRUD 操作
支持自定义全局通用操作：支持全局通用方法注入（ Write once, use anywhere ）
支持关键词自动转义：支持数据库关键词（order、key......）自动转义，还可自定义关键词
内置代码生成器：采用代码或者 Maven 插件可快速生成 Mapper 、 Model 、 Service 、 Controller 层代码，支持模板引擎，更有超多自定义配置等您来使用
内置分页插件：基于 MyBatis 物理分页，开发者无需关心具体操作，配置好插件之后，写分页等同于普通 List 查询
内置性能分析插件：可输出 Sql 语句以及其执行时间，建议开发测试时启用该功能，能快速揪出慢查询
内置全局拦截插件：提供全表 delete 、 update 操作智能分析阻断，也可自定义拦截规则，预防误操作
内置 Sql 注入剥离器：支持 Sql 注入剥离，有效预防 Sql 注入攻击

所有的Dao接口可以继承BaseDao接口, BaseDao定义基本的增删改查, 还有按不同的条件, 排序, 分页查询接口,还有其他各种复杂的查询接口,  在BaseDaoImpl已经全部实现
所有其他Dao接口只需要继承BaseDao ,便可以继承其全部的功能, 基本不要写其他任何代码.

*代码生成器*
方便开发生成自动化代码，对应有dao，spi，impl，model，controller层相关代码，减少单表查询crud代码操作。
极大提升后端开发效率。相关链接：
https://mp.baomidou.com/guide/generator.html#%E4%BD%BF%E7%94%A8%E6%95%99%E7%A8%8B

------------


##项目model层设计
所有实体类全部实现Serializable类,根据数据库可自动生成对应数据库实体类
@TableName代表对应数据库名称
>代码示例

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_user")
public class SysUser implements Serializable {

    /**
     * 用户ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * 部门ID
     */
    private String deptId;

    /**
     * 职务ID
     */
    private String postId;

    /**
     * 专属角色IDS
     */
    private String roleIds;

    /**
     * 工号
     */
    private String jobNumber;

    /**
     * 登录账号
     */
    private String loginName;

    /**
     * 姓名
     */
    private String userName;

    /**
     * 用户性别:0男,1女
     */
    private Integer sex;

    /**
     * 生日
     */
    private String birthday;

    /**
     * 入职日期
     */
    private String joinDay;

    /**
     * 密码
     */
    @JsonIgnore
    private String password;

    /**
     * 盐值
     */
    @JsonIgnore
    private String salt;

    /**
     * 帐号状态:0正常,1禁用
     */
    private Integer status;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Timestamp updateTime;

    /**
     * 备注
     */
    private String remark;
}

### Lombok
作用：
1 提高编码效率；
2 使代码更简洁；
3 消除冗长代码；
4 避免修改字段名字时忘记修改方法名；
5 注意：IDEA上必须要支持Lombok插件，否则会报错；
官网有视频教程
https://projectlombok.org/

### Apache Atlas
主要运用在mysql中间件，实现数据库读写分离。
与程序完全隔离，完美整合mybatis-plus使用，只需通过事务追踪到主库或者从库，关注点在中间件上。
只需搭建atlas,程序稍微配置事务即可
不仅仅只提供读写分离，还有其他更丰富资源。
相关altas学习：
http://atlas.apache.org/