ALTER TABLE questions
  ADD COLUMN finish TINYINT(1) NOT NULL DEFAULT 0 AFTER is_start;

CREATE INDEX idx_questions_finish ON questions(finish);