CREATE TYPE entity_type AS ENUM ('CNR', 'CEE', 'TRN');
CREATE TABLE company (
  id SERIAL PRIMARY KEY,
  cid VARCHAR(40) UNIQUE NOT NULL,
  short_code VARCHAR(50),
  name VARCHAR(128),
  gstin VARCHAR(50),
  tin VARCHAR(50),
  tan VARCHAR(50),
  cin VARCHAR(50),
  pan VARCHAR(50),
  types entity_type ARRAY NOT NULL,
  contact_number VARCHAR(10),
  contact_user_id VARCHAR(40),
  place_id VARCHAR(40),
  head_office_id VARCHAR(40),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_active BOOLEAN DEFAULT TRUE NOT NULL,
  is_deleted BOOLEAN DEFAULT FALSE NOT NULL,
  deleted_at TIMESTAMP DEFAULT NULL,
  created_by VARCHAR(40) DEFAULT NULL,
  updated_by VARCHAR(40) DEFAULT NULL
);

ALTER TABLE company
ADD CONSTRAINT fk_contact_user FOREIGN KEY(contact_user_id) REFERENCES users (uid),
ADD CONSTRAINT fk_created_by_user FOREIGN KEY(created_by) REFERENCES users (uid),
ADD CONSTRAINT fk_updated_by_user FOREIGN KEY(updated_by) REFERENCES users (uid);

ALTER TABLE company ALTER COLUMN cid drop not null;