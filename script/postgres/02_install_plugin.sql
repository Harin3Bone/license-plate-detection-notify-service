-- Enable the "uuid-ossp" extension for generating UUIDs
CREATE EXTENSION "uuid-ossp";

-- Verify the extension is created
SELECT uuid_generate_v4();
