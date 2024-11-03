# 关于junit

JUnit Jupiter 是 JUnit 5 的一部分,JUnit 5 是 JUnit 测试框架的最新版本.
具体来说,JUnit 5 包含三个主要模块：
JUnit Platform：提供运行测试的基础设施.它可以运行 JUnit 4、JUnit 5 和其他测试框架的测试.
JUnit Jupiter：这是 JUnit 5 的新编程模型和扩展模型,提供了新的注解和功能,比如 @Test、@BeforeEach、@AfterEach 等.
JUnit Vintage：允许运行 JUnit 3 和 JUnit 4 的测试,确保旧版测试的兼容性.


Spring Boot 2 默认使用的是 JUnit 5.在 Spring Boot 2.x 版本中,测试模块已经迁移到 JUnit 5,尤其是 JUnit Jupiter API.这意味着你可以使用 JUnit 5 的新特性和注解.
虽然 Spring Boot 2 默认使用 JUnit 5,但它仍然支持 JUnit 4 测试.如果你需要继续使用 JUnit 4,可以在依赖中添加 junit:junit