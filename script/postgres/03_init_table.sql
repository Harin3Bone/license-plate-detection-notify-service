-- Init User Table
DROP TABLE IF EXISTS users;
CREATE TABLE users
(
    user_id                uuid                     DEFAULT uuid_generate_v4() PRIMARY KEY,
    username               VARCHAR(50)                                        NOT NULL UNIQUE,
    password               TEXT                                               NOT NULL,
    first_name             VARCHAR(100)                                       NOT NULL,
    last_name              VARCHAR(100)                                       NOT NULL,
    email                  VARCHAR(100)                                       NOT NULL UNIQUE,
    phone_number           VARCHAR(15)                                        NOT NULL UNIQUE,
    role                   VARCHAR(20)                                        NOT NULL,
    created_timestamp      TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by             uuid                                               NOT NULL,
    last_updated_timestamp TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_updated_by        uuid                                               NOT NULL
);

-- Init History Table
DROP TABLE IF EXISTS notify_history;
CREATE TABLE notify_history
(
    history_id             uuid                     DEFAULT uuid_generate_v4() PRIMARY KEY,
    license_plate          VARCHAR(100)                                       NOT NULL,
    notify_message         TEXT                                               NOT NULL,
    upload_id              uuid,
    remark                 TEXT,
    status                 VARCHAR(15)                                        NOT NULL,
    vehicle_type           VARCHAR(30)                                        NOT NULL,
    created_timestamp      TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_updated_timestamp TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

-- Init Image Table
DROP TABLE IF EXISTS media_evidence;
CREATE TABLE media_evidence
(
    upload_id              uuid                     DEFAULT uuid_generate_v4() NOT NULL,
    file_id                uuid                     DEFAULT uuid_generate_v4() NOT NULL,
    file_path              TEXT                                                NOT NULL,
    content_type           VARCHAR(100)                                        NOT NULL,
    created_timestamp      TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP  NOT NULL,
    last_updated_timestamp TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP  NOT NULL,
    CONSTRAINT media_evidence_pkey PRIMARY KEY (upload_id, file_id)
);
