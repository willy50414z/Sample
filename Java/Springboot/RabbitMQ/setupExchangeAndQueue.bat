REM creat exchange
docker exec -it rabbitmq rabbitmqadmin declare exchange name=directExchange type=direct
docker exec -it rabbitmq rabbitmqadmin declare exchange name=topicExchange type=topic
docker exec -it rabbitmq rabbitmqadmin declare exchange name=headersExchange type=headers
docker exec -it rabbitmq rabbitmqadmin declare exchange name=fanoutExchange type=fanout

REM creat queue
docker exec -it rabbitmq rabbitmqadmin declare queue name=queue11
docker exec -it rabbitmq rabbitmqadmin declare queue name=queue21
docker exec -it rabbitmq rabbitmqadmin declare queue name=queue32
docker exec -it rabbitmq rabbitmqadmin declare queue name=queue42
docker exec -it rabbitmq rabbitmqadmin declare queue name=userMessageQueue

REM creat bind
docker exec -it rabbitmq rabbitmqadmin declare binding source=topicExchange destination=queue21 routing_key=topic.route.key

REM creat user and grant vhost permission
docker exec -it rabbitmq rabbitmqctl add_user admin 123456
docker exec -it rabbitmq rabbitmqctl set_user_tags admin management
docker exec -it rabbitmq rabbitmqctl set_permissions -p "/" admin .* .* .*