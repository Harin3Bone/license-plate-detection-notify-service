# Create user
rabbitmqctl add_user <RABBIT_USER> <RABBIT_PASSWORD>

# Grant permissions to user
rabbitmqctl set_user_tags <RABBIT_USER> administrator
rabbitmqctl set_permissions -p / <RABBIT_USER> ".*" ".*" ".*"
