DROP TEMPORARY TABLE IF EXISTS trans;

CREATE TEMPORARY TABLE trans (
  code     VARCHAR(50) COLLATE utf8mb4_0900_ai_ci PRIMARY KEY,
  yes_code VARCHAR(50) COLLATE utf8mb4_0900_ai_ci,
  no_code  VARCHAR(50) COLLATE utf8mb4_0900_ai_ci
) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

INSERT INTO trans (code, yes_code, no_code) VALUES
('Q_WORKING','Q_BLACK_FEEL','Q_STUDENT'),
('Q_BLACK_FEEL','Q_OVERTIME','Q_OT_PAY'),
('Q_OVERTIME','Q_SUICIDAL','Q_BOSS_MOOD'),
('Q_BOSS_MOOD','Q_CHANT','Q_PAY_MATCH'),
('Q_CHANT','Q_PAY_MATCH','Q_PAY_MATCH'),
('Q_PAY_MATCH','Q_PAID_LEAVE','Q_SLEEP'),
('Q_OT_PAY','Q_PAID_LEAVE','Q_SLEEP'),
('Q_PAID_LEAVE','FINISH','Q_SLEEP'),
('Q_SLEEP','Q_SUICIDAL','FINISH'),
('Q_SUICIDAL','FINISH','FINISH'),
('Q_STUDENT','FINISH','Q_MINOR'),
('Q_MINOR','Q_CARE','Q_CARE'),
('Q_CARE','Q_DISABLED','Q_DISABLED'),
('Q_DISABLED','Q_NEET','Q_NEET'),
('Q_NEET','FINISH','FINISH');

INSERT INTO questions (code, qtext, category, yes_point, no_point, active, is_start)
VALUES
('Q_WORKING','現在、働いていますか？','workstyle',50,-100,1,1),
('Q_BLACK_FEEL','あなたは自身の職場をブラックだと感じたことはありますか？','workstyle',100,0,1,0),
('Q_OVERTIME','残業は月に80時間を超えることがありますか？','workstyle',100,0,1,0),
('Q_BOSS_MOOD','上司の機嫌で評価が決まりますか？','workstyle',100,0,1,0),
('Q_CHANT','社訓を毎朝大声で唱和しますか？','workstyle',50,0,1,0),
('Q_PAY_MATCH','労働に対して見合った給料を受け取っていますか？','money',100,0,1,0),
('Q_SLEEP','仕事が原因で睡眠時間は3時間未満ですか？','lifestyle',100,0,1,0),
('Q_OT_PAY','残業代は支給されますか？','money',100,0,1,0),
('Q_PAID_LEAVE','有給は使うことはできますか？','money',100,0,1,0),
('Q_SUICIDAL','仕事のことで死にたくなることがありますか？','lifestyle',200,0,1,0),
('Q_MINOR','あなたは未成年ですか？','profile',-10,0,1,0),
('Q_STUDENT','あなたは学生ですか？','profile',90,-10,1,0),
('Q_CARE','身内の介護をしていますか？','profile',-10,0,1,0),
('Q_DISABLED','身体的、精神的要因によって働くことが困難ですか？（公的機関で認められたもの）','profile',-10,0,1,0),
('Q_NEET','あなたはニートですね？','profile',-960,0,1,0),
('FINISH','質問終了','finish',0,0,1,0)
As new
ON DUPLICATE KEY UPDATE
  qtext=new.qtext, category=new.category, yes_point=new.yes_point, no_point=new.no_point,
  active=new.active, is_start=new.is_start;

UPDATE questions q
LEFT JOIN trans t      ON t.code = q.code
LEFT JOIN questions qy ON qy.code = t.yes_code
LEFT JOIN questions qn ON qn.code = t.no_code
SET q.next_yes_id = qy.id,
    q.next_no_id  = qn.id;

SELECT q.code, qy.code AS yes_to, qn.code AS no_to
FROM questions q
LEFT JOIN questions qy ON qy.id = q.next_yes_id
LEFT JOIN questions qn ON qn.id = q.next_no_id
ORDER BY q.id;

DROP TEMPORARY TABLE trans;
