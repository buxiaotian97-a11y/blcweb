CREATE TABLE answers (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  run_id VARCHAR(36) NOT NULL,
  question_id BIGINT NOT NULL,
  answer TINYINT(1) NOT NULL,
  point_granted INT NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT uk_answers_run_q UNIQUE (run_id, question_id),
  CONSTRAINT fk_answers_question FOREIGN KEY (question_id) REFERENCES questions(id)
);
CREATE INDEX idx_answers_run_created ON answers(run_id, created_at);
