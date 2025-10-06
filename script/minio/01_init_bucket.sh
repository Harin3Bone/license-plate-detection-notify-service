''# Execute login to minio container
docker exec -it dl_minio /bin/bash

# Setup alias for MinIO
mc alias set myminio http://localhost:9000 <ROOT_MINIO_USERNAME> <ROOT_MINIO_PASSWORD>

# Create a bucket
mc mb myminio/detection-notification

# Exit container
exit