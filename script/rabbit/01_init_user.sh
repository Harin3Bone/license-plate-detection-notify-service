# Execute login to rabbitmq container
docker exec -it dl_rabbitmq /bin/bash

# Create user
rabbitmqctl add_user <RABBIT_USER> <RABBIT_PASSWORD>
rabbitmqctl set_user_tags <RABBIT_USER> administrator
rabbitmqctl set_permissions -p / <RABBIT_USER> ".*" ".*" ".*"

# Exit container
exit
