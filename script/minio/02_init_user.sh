# Execute login to minio container
docker exec -it dl_minio /bin/bash

# Create Service user
mc admin user add myminio <MINIO_USER> <MINIO_PASSWORD>

# Assign Admin policy to Admin user
mc admin policy attach myminio readwrite --user <MINIO_USER>

# Exit container
exit
