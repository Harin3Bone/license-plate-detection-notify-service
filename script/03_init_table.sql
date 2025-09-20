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
    license_plate          VARCHAR(20)                                        NOT NULL,
    notify_message         TEXT                                               NOT NULL,
    image_id               uuid,
    remark                 TEXT,
    status                 VARCHAR(15)                                        NOT NULL,
    province               VARCHAR(30)                                        NOT NULL,
    vehicle_type           VARCHAR(30)                                        NOT NULL,
    created_timestamp      TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_updated_timestamp TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

-- Init Image Table
DROP TABLE IF EXISTS image_evidence;
CREATE TABLE image_evidence
(
    image_id               uuid                     DEFAULT uuid_generate_v4() PRIMARY KEY,
    image_path             TEXT                                               NOT NULL,
    created_timestamp      TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_updated_timestamp TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
)