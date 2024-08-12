-- Insert values into the user_type table
INSERT INTO user_type (name) VALUES ('owner');
INSERT INTO user_type (name) VALUES ('decorator');
INSERT INTO user_type (name) VALUES ('admin');

-- Insert values into the user_status table
INSERT INTO user_status (status) VALUES ('pending');
INSERT INTO user_status (status) VALUES ('active');
INSERT INTO user_status (status) VALUES ('denied');
INSERT INTO user_status (status) VALUES ('blocked');

-- Insert values into the garden_type table
INSERT INTO garden_type (type) VALUES ('private');
INSERT INTO garden_type (type) VALUES ('restaurant');

-- Insert values into the job_status table
INSERT INTO job_status (status) VALUES ('pending');
INSERT INTO job_status (status) VALUES ('rejected');
INSERT INTO job_status (status) VALUES ('accepted');
INSERT INTO job_status (status) VALUES ('in progress');
INSERT INTO job_status (status) VALUES ('done');
INSERT INTO job_status (status) VALUES ('cancelled');
