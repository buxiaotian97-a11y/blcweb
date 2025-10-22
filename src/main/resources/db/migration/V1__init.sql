CREATE DATABASE IF NOT EXISTS blc DEFAULT CHARACTER SET utf8mb4;												
USE blc;												
												
SET NAMES utf8mb4;												
SET time_zone = '+09:00';												
												
CREATE TABLE IF NOT EXISTS users (												
id BIGINT AUTO_INCREMENT PRIMARY KEY,												
name VARCHAR(100) NOT NULL,
department_name VARCHAR(100) NOT NULL,												
created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP												
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;												
												
CREATE TABLE IF NOT EXISTS characters (												
id BIGINT AUTO_INCREMENT PRIMARY KEY,												
code VARCHAR(20) NOT NULL UNIQUE,												
name VARCHAR(20) NOT NULL												
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;												
												
CREATE TABLE IF NOT EXISTS questions (												
id BIGINT AUTO_INCREMENT PRIMARY KEY,												
qtext VARCHAR(100) NOT NULL,												
point INT NOT NULL,												
category VARCHAR(10) NULL,												
active TINYINT(1) NOT NULL DEFAULT 1												
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;												
												
CREATE TABLE IF NOT EXISTS trophies (												
id BIGINT AUTO_INCREMENT PRIMARY KEY,												
name VARCHAR(20) NOT NULL,												
condition_type INT UNSIGNED NOT NULL,												
condition_value VARCHAR(30) NULL												
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;												
												
CREATE TABLE IF NOT EXISTS items (												
id BIGINT AUTO_INCREMENT PRIMARY KEY,												
name VARCHAR(20) NOT NULL,												
condition_type INT UNSIGNED NOT NULL,												
condition_value VARCHAR(30) NULL												
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;												
												
CREATE TABLE IF NOT EXISTS bosses (												
id BIGINT AUTO_INCREMENT PRIMARY KEY,												
name VARCHAR(10) NOT NULL,												
trait TINYINT(1) NOT NULL DEFAULT 0												
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;												
												
CREATE TABLE IF NOT EXISTS scores (												
id BIGINT AUTO_INCREMENT PRIMARY KEY,												
user_id BIGINT NOT NULL,												
score INT NOT NULL,												
created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,												
CONSTRAINT fk_scores_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE												
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;												
												
CREATE TABLE IF NOT EXISTS user_items (												
id BIGINT AUTO_INCREMENT PRIMARY KEY,												
user_id BIGINT NOT NULL,												
item_id BIGINT NOT NULL,												
score_id BIGINT NULL,												
earned_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,												
CONSTRAINT fk_ui_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,												
CONSTRAINT fk_ui_item FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE,												
CONSTRAINT fk_ui_score FOREIGN KEY (score_id) REFERENCES scores(id) ON DELETE SET NULL												
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;												
												
CREATE TABLE IF NOT EXISTS user_trophies (												
id BIGINT AUTO_INCREMENT PRIMARY KEY,												
user_id BIGINT NOT NULL,												
trophy_id BIGINT NOT NULL,												
score_id BIGINT NULL,												
earned_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,												
CONSTRAINT fk_ut_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,												
CONSTRAINT fk_ut_trophy FOREIGN KEY (trophy_id) REFERENCES trophies(id) ON DELETE CASCADE,												
CONSTRAINT fk_ut_score FOREIGN KEY (score_id) REFERENCES scores(id) ON DELETE SET NULL												
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;												