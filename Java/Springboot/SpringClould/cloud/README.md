# Spring Cloud
此專案將實現各項Spring Clould Framework功能，藉此了解Spring Clould生態圈
* spring-cloud-config-server: Spring配置中心，統一管理各Server使用的application.yml資訊，各Server只需設定好連線資訊，即可透過@ConfigurationProperties將線上的yml設定檔注入pojo bean中
* spring-cloud-config-client: 從spring-cloud-config-server讀取設定檔的範例程式，可依需求配置到其他project，或藉此了解如何配置