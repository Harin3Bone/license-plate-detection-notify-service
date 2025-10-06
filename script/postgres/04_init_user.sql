-- Create user for microservices
CREATE USER detection_svc WITH PASSWORD '<YOUR_POSTGRES_PASSWORD_HERE>';
-- ALTER USER detection_svc WITH PASSWORD 'YOUR_RESET_PASSWORD_HERE';

-- Grant connect on the database to the service user
GRANT CONNECT ON DATABASE detection_notification TO detection_svc;

-- Grant all privileges on the public schema and tables to the service user
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO detection_svc;

-- ##############################################################

-- Verify users exists
SELECT usr.usename
FROM pg_catalog.pg_user usr
WHERE usr.usename = 'detection_svc';

-- Verify privileges for each user on the tables in the public schema
SELECT tb.grantee        AS user_name
     , tb.table_name     AS table_name
     , tb.privilege_type AS permssion
     , tb.is_grantable   AS grant_option
FROM information_schema.table_privileges tb
WHERE tb.table_catalog = 'detection_notification'
  AND tb.table_schema = 'public'
  AND tb.grantee = 'detection_svc';
