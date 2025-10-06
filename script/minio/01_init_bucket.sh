''# Execute login to minio container
docker exec -it dl_minio /bin/bash

# Setup alias for MinIO
mc alias set myminio http://localhost:9000 <MINIO_ROOT_USERNAME> <MINIO_ROOT_PASSWORD>

# Create a bucket
mc mb myminio/detection-notification

# Exit container
exit