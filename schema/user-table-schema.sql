CREATE TABLE users (
  id SERIAL PRIMARY KEY,
  uid VARCHAR(40) UNIQUE NOT NULL,
  user_name VARCHAR(50),
  first_name VARCHAR(128),
  last_name VARCHAR(128),
  email_address VARCHAR(128),
  country_code VARCHAR(3) NOT NULL DEFAULT '91',
  mobile_number VARCHAR(10),
  password VARCHAR(128) DEFAULT NULL,
  salt VARCHAR(32) DEFAULT NULL,
  address VARCHAR(255) DEFAULT NULL,
  country VARCHAR(100) DEFAULT NULL,
  city VARCHAR(100) DEFAULT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_active BOOLEAN DEFAULT TRUE NOT NULL,
  is_deleted BOOLEAN DEFAULT FALSE NOT NULL,
  deleted_at TIMESTAMP DEFAULT NULL
);