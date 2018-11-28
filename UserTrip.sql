CREATE TABLE UserTrip
(
    id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT NOT NULL,
    date_entered DATE NOT NULL,
    trip MEDIUMTEXT NOT NULL,
    user_id INT UNSIGNED,
    FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE
);