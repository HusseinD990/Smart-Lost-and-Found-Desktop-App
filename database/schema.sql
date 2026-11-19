DROP DATABASE IF EXISTS lost_and_found;
CREATE DATABASE lost_and_found CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE lost_and_found;

CREATE TABLE users (
    user_id        INT AUTO_INCREMENT PRIMARY KEY,
    username       VARCHAR(50)  NOT NULL UNIQUE,
    email          VARCHAR(100) NOT NULL UNIQUE,
    password_hash  VARCHAR(100) NOT NULL,
    full_name      VARCHAR(100) NOT NULL,
    role           ENUM('USER','ADMIN') NOT NULL DEFAULT 'USER',
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE categories (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(60) NOT NULL UNIQUE,
    description VARCHAR(255)
);

CREATE TABLE item_reports (
    report_id        INT AUTO_INCREMENT PRIMARY KEY,
    report_type      ENUM('LOST','FOUND') NOT NULL,
    user_id          INT NOT NULL,
    category_id      INT NOT NULL,
    title            VARCHAR(120) NOT NULL,
    description      TEXT,
    color            VARCHAR(40),
    brand            VARCHAR(60),
    distinguishing_features TEXT,
    event_date       DATE NOT NULL,
    location         VARCHAR(150),
    image_path       VARCHAR(255),
    status           ENUM('OPEN','RESOLVED','RETURNED','EXPIRED','CLOSED') NOT NULL DEFAULT 'OPEN',
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id)     REFERENCES users(user_id)           ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories(category_id)
);

CREATE TABLE claims (
    claim_id           INT AUTO_INCREMENT PRIMARY KEY,
    found_report_id    INT NOT NULL,
    claimant_id        INT NOT NULL,
    ownership_details  TEXT NOT NULL,
    status             ENUM('PENDING','UNDER_REVIEW','APPROVED','REJECTED','RETURNED') NOT NULL DEFAULT 'PENDING',
    reviewer_id        INT NULL,
    decision_reason    VARCHAR(500),
    decision_date      TIMESTAMP NULL,
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (found_report_id) REFERENCES item_reports(report_id) ON DELETE CASCADE,
    FOREIGN KEY (claimant_id)     REFERENCES users(user_id)          ON DELETE CASCADE,
    FOREIGN KEY (reviewer_id)     REFERENCES users(user_id)          ON DELETE SET NULL
);

CREATE TABLE notifications (
    notification_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id         INT NOT NULL,
    type            ENUM('POSSIBLE_MATCH','CLAIM_UPDATE','REPORT_UPDATE') NOT NULL,
    message         VARCHAR(255) NOT NULL,
    related_id      INT NULL,
    is_read         BOOLEAN NOT NULL DEFAULT FALSE,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE activity_history (
    activity_id  INT AUTO_INCREMENT PRIMARY KEY,
    user_id      INT NOT NULL,
    action       VARCHAR(60) NOT NULL,
    details      VARCHAR(255),
    report_id    INT NULL,
    claim_id     INT NULL,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id)   REFERENCES users(user_id)           ON DELETE CASCADE,
    FOREIGN KEY (report_id) REFERENCES item_reports(report_id)  ON DELETE SET NULL,
    FOREIGN KEY (claim_id)  REFERENCES claims(claim_id)         ON DELETE SET NULL
);

CREATE TABLE match_dismissals (
    dismissal_id    INT AUTO_INCREMENT PRIMARY KEY,
    user_id         INT NOT NULL,
    lost_report_id  INT NOT NULL,
    found_report_id INT NOT NULL,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (lost_report_id, found_report_id, user_id),
    FOREIGN KEY (user_id)         REFERENCES users(user_id)          ON DELETE CASCADE,
    FOREIGN KEY (lost_report_id)  REFERENCES item_reports(report_id) ON DELETE CASCADE,
    FOREIGN KEY (found_report_id) REFERENCES item_reports(report_id) ON DELETE CASCADE
);

CREATE INDEX idx_reports_type_status ON item_reports(report_type, status);
CREATE INDEX idx_reports_category    ON item_reports(category_id);
CREATE INDEX idx_notif_user_read     ON notifications(user_id, is_read);
