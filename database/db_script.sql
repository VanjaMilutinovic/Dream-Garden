-- Create the database
CREATE DATABASE dream_garden;
USE dream_garden;

-- Table: user_type
CREATE TABLE user_type (
    user_type_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL
);

-- Table: user_status
CREATE TABLE user_status (
    user_status_id INT PRIMARY KEY AUTO_INCREMENT,
    status VARCHAR(50) NOT NULL
);

-- Table: photo
CREATE TABLE photo (
    photo_id INT PRIMARY KEY AUTO_INCREMENT,
    path VARCHAR(255) NOT NULL
);

-- Table: user
CREATE TABLE user (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    user_type_id INT,
    username VARCHAR(50) NOT NULL UNIQUE,
    hashed_password VARCHAR(255) NOT NULL,
    name VARCHAR(50) NOT NULL,
    lastname VARCHAR(50) NOT NULL,
    gender ENUM('M', 'F') NOT NULL,
    address TEXT NOT NULL,
    contact_number VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    photo_id INT,
    credit_card_number VARCHAR(20),
    user_status_id INT,
    FOREIGN KEY (user_type_id) REFERENCES user_type(user_type_id),
    FOREIGN KEY (photo_id) REFERENCES photo(photo_id),
    FOREIGN KEY (user_status_id) REFERENCES user_status(user_status_id)
);

-- Table: company
CREATE TABLE company (
    company_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    address TEXT NOT NULL,
    latitude DECIMAL(9,6),
    longitude DECIMAL(9,6),
    contact_number VARCHAR(20) NOT NULL,
    contact_person VARCHAR(100) NOT NULL
);

-- Table: company_holiday
CREATE TABLE company_holiday (
    company_holiday_id INT PRIMARY KEY AUTO_INCREMENT,
    company_id INT,
    start_date_time DATETIME NOT NULL,
    end_date_time DATETIME NOT NULL,
    FOREIGN KEY (company_id) REFERENCES company(company_id)
);

-- Table: job_status
CREATE TABLE job_status (
    job_status_id INT PRIMARY KEY AUTO_INCREMENT,
    status VARCHAR(50) NOT NULL
);

-- Table: garden_type
CREATE TABLE garden_type (
    garden_type_id INT PRIMARY KEY AUTO_INCREMENT,
    type VARCHAR(50) NOT NULL
);

-- Table: job
CREATE TABLE job (
    job_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    company_id INT,
    worker_id INT,
    job_status_id INT,
    request_date_time DATETIME NOT NULL,
    start_date_time DATETIME,
    end_date_time DATETIME,
    garden_size DECIMAL(10,2) NOT NULL,
    garden_type_id INT,
    description TEXT,
    rejected_description TEXT,
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    FOREIGN KEY (company_id) REFERENCES company(company_id),
    FOREIGN KEY (worker_id) REFERENCES user(user_id),
    FOREIGN KEY (job_status_id) REFERENCES job_status(job_status_id),
    FOREIGN KEY (garden_type_id) REFERENCES garden_type(garden_type_id)
);

-- Table: private_garden
CREATE TABLE private_garden (
    job_id INT PRIMARY KEY,
    pool_size DECIMAL(10,2),
    number_of_pools INT,
    grass_size DECIMAL(10,2),
    paved_size DECIMAL(10,2),
    FOREIGN KEY (job_id) REFERENCES job(job_id)
);

-- Table: restaurant_garden
CREATE TABLE restaurant_garden (
    job_id INT PRIMARY KEY,
    fountain_size DECIMAL(10,2),
    number_of_fountains INT,
    grass_size DECIMAL(10,2),
    number_of_tables INT,
    number_of_seats INT,
    FOREIGN KEY (job_id) REFERENCES job(job_id)
);

-- Table: service
CREATE TABLE service (
    service_id INT PRIMARY KEY AUTO_INCREMENT,
    company_id INT,
    price DECIMAL(10,2) NOT NULL,
    service_name VARCHAR(100) NOT NULL,
    service_description TEXT,
    FOREIGN KEY (company_id) REFERENCES company(company_id)
);

-- Table: worker
CREATE TABLE worker (
    user_id INT PRIMARY KEY,
    company_id INT,
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    FOREIGN KEY (company_id) REFERENCES company(company_id)
);

-- Table: job_review
CREATE TABLE job_review (
    job_review_id INT PRIMARY KEY AUTO_INCREMENT,
    job_id INT,
    comment TEXT,
    grade INT CHECK (grade >= 1 AND grade <= 5),
    FOREIGN KEY (job_id) REFERENCES job(job_id)
);

-- Table: job_service
CREATE TABLE job_service (
    job_service_id INT PRIMARY KEY AUTO_INCREMENT,
    job_id INT,
    service_id INT,
    FOREIGN KEY (job_id) REFERENCES job(job_id),
    FOREIGN KEY (service_id) REFERENCES service(service_id)
);

-- Table: job_photo
CREATE TABLE job_photo (
    job_photo_id INT PRIMARY KEY AUTO_INCREMENT,
    job_id INT,
    photo_id INT,
    FOREIGN KEY (job_id) REFERENCES job(job_id),
    FOREIGN KEY (photo_id) REFERENCES photo(photo_id)
);

-- Table: maintainance
CREATE TABLE maintainance (
    maintainance_id INT PRIMARY KEY AUTO_INCREMENT,
    job_id INT,
    job_status_id INT,
    request_date_time DATETIME NOT NULL,
    start_date_time DATETIME,
    estimated_end_date_time DATETIME,
    worker_id INT,
    FOREIGN KEY (job_id) REFERENCES job(job_id),
    FOREIGN KEY (job_status_id) REFERENCES job_status(job_status_id),
    FOREIGN KEY (worker_id) REFERENCES user(user_id)
);
