################### MinIO ###################
# Execute login to minio container
docker exec -it dl_minio /bin/bash

# Execute initialization script
/var/lib/scripts/run.sh

# Exit minio container
exit

################### RabbitMQ ###################
# Execute login to rabbit container
docker exec -it dl_rabbitmq /bin/bash

# Execute initialization script
/var/lib/scripts/run.sh

# Exit rabbitmq container
exit

################### PostgreSQL ###################
# Execute login to postgres container
docker exec -it dl_postgres /bin/bash

# Execute initialization script
/var/lib/scripts/run.sh

# Exit postgres container
exit