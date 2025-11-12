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
  id           BIGINT AUTO_INCREMENT PRIMARY KEY,
  code         VARCHAR(50)   NOT NULL,
  qtext        VARCHAR(500)  NOT NULL,

  yes_point    INT NOT NULL DEFAULT 0,
  no_point     INT NOT NULL DEFAULT 0,

  next_yes_id  BIGINT NULL,
  next_no_id   BIGINT NULL,

  category     VARCHAR(50) NULL,
  active       TINYINT(1) NOT NULL DEFAULT 1,
  is_start     TINYINT(1) NOT NULL DEFAULT 0,
  finish       TINYINT(1) NOT NULL DEFAULT 0,

  CONSTRAINT uq_questions_code UNIQUE (code),
  CONSTRAINT fk_questions_yes FOREIGN KEY (next_yes_id)
    REFERENCES questions(id) ON DELETE SET NULL,
  CONSTRAINT fk_questions_no  FOREIGN KEY (next_no_id)
    REFERENCES questions(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_questions_next_yes_id ON questions(next_yes_id);
CREATE INDEX idx_questions_next_no_id  ON questions(next_no_id);
CREATE INDEX idx_questions_is_start    ON questions(is_start);
CREATE INDEX idx_questions_active      ON questions(active);

CREATE TABLE IF NOT EXISTS scores (												
id BIGINT AUTO_INCREMENT PRIMARY KEY,												
user_id BIGINT NOT NULL,												
score INT NOT NULL,												
created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,												
CONSTRAINT fk_scores_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE												
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;		

CREATE INDEX idx_questions_next_yes ON questions (next_yes_id);
CREATE INDEX idx_questions_next_no  ON questions (next_no_id);
CREATE INDEX idx_questions_active_cat ON questions (active, category);
CREATE INDEX idx_scores_user_created ON scores (user_id, created_at);

CREATE TABLE IF NOT EXISTS answers (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  run_id VARCHAR(36) NOT NULL,
  question_id BIGINT NOT NULL,
  answer TINYINT(1) NOT NULL,
  point_granted INT NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT uk_answers_run_q UNIQUE (run_id, question_id),
  CONSTRAINT fk_answers_question FOREIGN KEY (question_id) REFERENCES questions(id)
)  ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_answers_run_created ON answers(run_id, created_at);
CREATE INDEX idx_answers_question ON answers (question_id);	

CREATE TABLE results (
    id INT AUTO_INCREMENT PRIMARY KEY,
    min_score INT NOT NULL,
    max_score INT NOT NULL,
    message VARCHAR(255) NOT NULL
);					

ALTER TABLE results ADD CONSTRAINT uq_results_range UNIQUE (min_score, max_score);														
												
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

CREATE INDEX idx_user_items_user  ON user_items (user_id);
CREATE INDEX idx_user_items_item  ON user_items (item_id);
CREATE INDEX idx_user_items_score ON user_items (score_id);											
												
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

CREATE INDEX idx_user_trophies_user   ON user_trophies (user_id);
CREATE INDEX idx_user_trophies_trophy ON user_trophies (trophy_id);
CREATE INDEX idx_user_trophies_score  ON user_trophies (score_id);				

ALTER TABLE results
  ADD CONSTRAINT chk_results_min_le_max CHECK (min_score <= max_score);

ALTER TABLE answers   ADD CONSTRAINT chk_answers_answer CHECK (answer IN (0,1));
ALTER TABLE questions ADD CONSTRAINT chk_questions_active CHECK (active IN (0,1));
ALTER TABLE bosses    ADD CONSTRAINT chk_bosses_trait   CHECK (trait  IN (0,1));						