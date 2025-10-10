# Setup alias for MinIO
mc alias set myminio http://localhost:9000 <MINIO_ROOT_USERNAME> <MINIO_ROOT_PASSWORD>

# Create a bucket
mc mb myminio/<MINIO_BUCKET>

# Create Service user
mc admin user add myminio <MINIO_USER> <MINIO_PASSWORD>

# Assign Admin policy to Admin user
mc admin policy attach myminio readwrite --user <MINIO_USER>