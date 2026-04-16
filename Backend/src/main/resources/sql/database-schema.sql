-- Create pets table
create table if not exists pets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    owner VARCHAR(255) NOT NULL,
    type VARCHAR(255) NOT NULL,
    race VARCHAR(255) NOT NULL,
    realAge INT NOT NULL,
    humanAge INT NOT NULL
);