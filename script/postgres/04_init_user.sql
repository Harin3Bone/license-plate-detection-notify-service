-- Create user for microservices
CREATE USER <POSTGRES_USER> WITH PASSWORD '<POSTGRES_PASSWORD>';
-- ALTER USER detection_svc WITH PASSWORD 'YOUR_RESET_PASSWORD_HERE';

-- Grant connect on the database to the service user
GRANT CONNECT ON DATABASE <POSTGRES_DB> TO <POSTGRES_USER>;

-- Grant all privileges on the public schema and tables to the service user
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO <POSTGRES_USER>;

-- ##############################################################

-- Verify users exists
SELECT usr.usename
FROM pg_catalog.pg_user usr
WHERE usr.usename = '<POSTGRES_USER>';

-- Verify privileges for each user on the tables in the public schema
SELECT tb.grantee        AS user_name
     , tb.table_name     AS table_name
     , tb.privilege_type AS permssion
     , tb.is_grantable   AS grant_option
FROM information_schema.table_privileges tb
WHERE tb.table_catalog = '<POSTGRES_DB>'
  AND tb.table_schema = 'public'
  AND tb.grantee = '<POSTGRES_USER>';
