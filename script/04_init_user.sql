-- Create super user for administrator
CREATE USER detection_admin WITH PASSWORD 'q*KLJqLYp9k1d4Z';

-- Grant superuser to the administrator user
ALTER USER detection_admin WITH SUPERUSER;

-- ##############################################################

-- Create user for microservices
CREATE USER detection_svc WITH PASSWORD 'n3#s7!77hQ2v3MW';

-- ##############################################################

-- Create user for read-only user
CREATE USER detection_ro WITH PASSWORD '1YM9Z0f*V993*GJ';

-- ##############################################################

-- Verify users exists
SELECT usr.usename
FROM pg_catalog.pg_user usr
WHERE usr.usename IN ('detection_admin', 'detection_svc', 'detection_ro');
