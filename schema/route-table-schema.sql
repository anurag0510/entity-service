CREATE TABLE routes (
  id SERIAL PRIMARY KEY,
  rid VARCHAR(40) UNIQUE NOT NULL,
  label VARCHAR(256) NOT NULL,
  short_code VARCHAR(32) NULL DEFAULT NULL,
  origin_pid VARCHAR(40) NOT NULL,
  destination_pid VARCHAR(40) NOT NULL,
  parent_id VARCHAR(40) NOT NULL DEFAULT NULL,
  attributes JSONB NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_active BOOLEAN DEFAULT TRUE NOT NULL,
  is_deleted BOOLEAN DEFAULT FALSE NOT NULL,
  deleted_at TIMESTAMP DEFAULT NULL,
  created_by VARCHAR(40) DEFAULT NULL,
  updated_by VARCHAR(40) DEFAULT NULL
);

CREATE UNIQUE INDEX route_owner_code_uidx ON routes USING btree (parent_id, short_code) WHERE (deleted_at IS NULL);

ALTER TABLE routes ADD CONSTRAINT route_origin_pid_fkey FOREIGN KEY (origin_pid) REFERENCES places(pid) ON DELETE SET NULL;
ALTER TABLE routes ADD CONSTRAINT route_destination_pid_fkey FOREIGN KEY (destination_pid) REFERENCES places(pid) ON DELETE SET NULL;