# Eureka Server
實現CAP中的AP，在集群部屬中，其中一台若出現故障，Eureka會進入自我保護機制，待恢復後再自動同步資訊，固可保證服務的可用性，但在同步資訊前仍會提供服務，無法保持一致性。

Eureka cluster沒有master/slave機制，採用peer to peer方式對等通訊，因此每個node皆需註冊其他node的url，亦提供API供跨網域註冊服務。

## 集群
Server需新增eureka.client.serviceUrl.defaultZone設定，定義cluster的server路徑，並註冊hostname
        
C:\Windows\System32\drivers\etc\hosts新增

        127.0.0.1 eureka.cluster1
        127.0.0.1 eureka.cluster2
        127.0.0.1 eureka.cluster3

        

## 自我保護
client默認30秒發送一次heart break(或透過actuator)，server 90秒未收到heartbreak就會將該服務剃除，15分鐘內heartbreak fail>15%就會觸發自我保護，避免consumer取得無用的服務。 

        eureka.server.enableSelfPreservation=true #開啟自我保護
## region & zone (未實現)
Eureka提供region & zone設定，當consumer發出請求時，搭配Robbin時，Robbin會優先提供同region/zone的producer。

        eureka.client.serviceUrl.defaultZone=defaultZone #設定預設zone
        eureka.client.region=myregion
        eureka.client.availabilityZones.myregion=myzone
## 安全驗證 (未實現)
Eureka可整合Spring Security在登入網址加上登入資訊

        eureka.client.serviceUrl.defaultZone=http://<username>:<password>@${eureka.instance.hostname}:${server.port}/eureka/
