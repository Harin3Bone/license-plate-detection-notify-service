-- Grant all on the database to the administrator user
GRANT ALL PRIVILEGES ON DATABASE detection_notification TO detection_admin;

-- Grant all privileges on the public schema and tables to the service user
GRANT ALL PRIVILEGES ON SCHEMA public TO detection_admin;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO detection_admin;

-- ##############################################################

-- Grant connect on the database to the service user
GRANT CONNECT ON DATABASE detection_notification TO detection_svc;

-- Grant all privileges on the public schema and tables to the service user
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO detection_svc;

-- ##############################################################

-- Grant connect on the database to the read-only user
GRANT CONNECT ON DATABASE detection_notification TO detection_ro;

-- Grant read-only on the public schema and tables to the read-only user
GRANT SELECT ON ALL TABLES IN SCHEMA public TO detection_ro;

-- ##############################################################

-- Verify privileges for each user on the tables in the public schema
SELECT tb.grantee        AS user_name
     , tb.table_name     AS table_name
     , tb.privilege_type AS permssion
     , tb.is_grantable   AS grant_option
FROM information_schema.table_privileges tb
WHERE tb.table_catalog = 'detection_notification'
  AND tb.table_schema = 'public'
  AND tb.grantee IN ('detection_admin', 'detection_svc', 'detection_ro');