-- Add schedule_id column to the availability table
ALTER TABLE availability
ADD COLUMN schedule_id BIGINT NOT NULL;

-- Add foreign key constraint
ALTER TABLE availability
ADD CONSTRAINT fk_schedule FOREIGN KEY (schedule_id) REFERENCES schedule(schedule_id);