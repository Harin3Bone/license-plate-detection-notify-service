''# Execute login to rabbitmq container
docker exec -it dl_rabbitmq /bin/bash

# Create Admin user
rabbitmqctl add_user detection_svc <YOUR_PASSWORD_HERE>
rabbitmqctl set_user_tags detection_svc detection_svc
rabbitmqctl set_permissions -p / detection_admin ".*" ".*" ".*"

# Exit container
exit