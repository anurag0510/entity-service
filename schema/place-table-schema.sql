CREATE EXTENSION postgis;

CREATE TABLE places (
  id SERIAL PRIMARY KEY,
  pid VARCHAR(40) UNIQUE NOT NULL,
  short_code VARCHAR(50),
  label VARCHAR(128),
  address VARCHAR(1024) DEFAULT NULL,
  latitude NUMERIC(7,5) DEFAULT NULL,
  longitude NUMERIC(8,5) DEFAULT NULL,
  parent_id VARCHAR(40) NOT NULL,
  google_place_id VARCHAR(128) UNIQUE NOT NULL,
  geometry geometry(POINT, 4326) DEFAULT NULL,
  shape geometry DEFAULT NULL,
  attributes JSONB DEFAULT NULL,
  formatted_address VARCHAR(1024) DEFAULT NULL,
  region VARCHAR(50),
  locality VARCHAR(50),
  country VARCHAR(50),
  postal_code VARCHAR(50),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_active BOOLEAN DEFAULT TRUE NOT NULL,
  is_deleted BOOLEAN DEFAULT FALSE NOT NULL,
  deleted_at TIMESTAMP DEFAULT NULL,
  created_by VARCHAR(40) DEFAULT NULL,
  updated_by VARCHAR(40) DEFAULT NULL
);

ALTER TABLE places
ADD CONSTRAINT fk_created_by_user FOREIGN KEY(created_by) REFERENCES users (uid),
ADD CONSTRAINT fk_updated_by_user FOREIGN KEY(updated_by) REFERENCES users (uid);

ALTER TABLE places
ADD CONSTRAINT unique_short_code UNIQUE(short_code);

ALTER TABLE places DROP CONSTRAINT places_google_place_id_key;

ALTER TABLE places ALTER COLUMN parent_id drop not null;