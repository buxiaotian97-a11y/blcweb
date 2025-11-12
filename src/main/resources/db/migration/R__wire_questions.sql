UPDATE questions SET finish = 1 WHERE code IN ('FINISH');

UPDATE questions q SET
  next_yes_id = (SELECT id FROM (SELECT id, code FROM questions) t WHERE t.code='Q_BLACK_FEEL'),
  next_no_id  = (SELECT id FROM (SELECT id, code FROM questions) t WHERE t.code='Q_STUDENT')
WHERE q.code='Q_WORKING';

UPDATE questions q SET
  next_yes_id = (SELECT id FROM (SELECT id, code FROM questions) t WHERE t.code='Q_OVERTIME'),
  next_no_id  = (SELECT id FROM (SELECT id, code FROM questions) t WHERE t.code='Q_OT_PAY')
WHERE q.code='Q_BLACK_FEEL';

UPDATE questions q SET
  next_yes_id = (SELECT id FROM (SELECT id, code FROM questions) t WHERE t.code='Q_SUICIDAL'),
  next_no_id  = (SELECT id FROM (SELECT id, code FROM questions) t WHERE t.code='Q_BOSS_MOOD')
WHERE q.code='Q_OVERTIME';

UPDATE questions q SET
  next_yes_id = (SELECT id FROM (SELECT id, code FROM questions) t WHERE t.code='Q_CHANT'),
  next_no_id  = (SELECT id FROM (SELECT id, code FROM questions) t WHERE t.code='Q_PAY_MATCH')
WHERE q.code='Q_BOSS_MOOD';

UPDATE questions q SET
  next_yes_id = (SELECT id FROM (SELECT id, code FROM questions) t WHERE t.code='Q_PAY_MATCH'),
  next_no_id  = (SELECT id FROM (SELECT id, code FROM questions) t WHERE t.code='Q_PAY_MATCH')
WHERE q.code='Q_CHANT';

UPDATE questions q SET
  next_yes_id = (SELECT id FROM (SELECT id, code FROM questions) t WHERE t.code='Q_PAID_LEAVE'),
  next_no_id  = (SELECT id FROM (SELECT id, code FROM questions) t WHERE t.code='Q_SLEEP')
WHERE q.code='Q_PAY_MATCH';

UPDATE questions q SET
  next_yes_id = (SELECT id FROM (SELECT id, code FROM questions) t WHERE t.code='Q_PAID_LEAVE'),
  next_no_id  = (SELECT id FROM (SELECT id, code FROM questions) t WHERE t.code='Q_SLEEP')
WHERE q.code='Q_OT_PAY';

UPDATE questions q SET
  next_yes_id = (SELECT id FROM (SELECT id, code FROM questions) t WHERE t.code='FINISH'),
  next_no_id  = (SELECT id FROM (SELECT id, code FROM questions) t WHERE t.code='Q_SLEEP')
WHERE q.code='Q_PAID_LEAVE';

UPDATE questions q SET
  next_yes_id = (SELECT id FROM (SELECT id, code FROM questions) t WHERE t.code='Q_SUICIDAL'),
  next_no_id  = (SELECT id FROM (SELECT id, code FROM questions) t WHERE t.code='FINISH')
WHERE q.code='Q_SLEEP';

UPDATE questions q SET
  next_yes_id = (SELECT id FROM (SELECT id, code FROM questions) t WHERE t.code='FINISH'),
  next_no_id  = (SELECT id FROM (SELECT id, code FROM questions) t WHERE t.code='FINISH')
WHERE q.code='Q_SUICIDAL';

UPDATE questions q SET
  next_yes_id = (SELECT id FROM (SELECT id, code FROM questions) t WHERE t.code='FINISH'),
  next_no_id  = (SELECT id FROM (SELECT id, code FROM questions) t WHERE t.code='Q_MINOR')
WHERE q.code='Q_STUDENT';

UPDATE questions q SET
  next_yes_id = (SELECT id FROM (SELECT id, code FROM questions) t WHERE t.code='Q_CARE'),
  next_no_id  = (SELECT id FROM (SELECT id, code FROM questions) t WHERE t.code='Q_CARE')
WHERE q.code='Q_MINOR';

UPDATE questions q SET
  next_yes_id = (SELECT id FROM (SELECT id, code FROM questions) t WHERE t.code='Q_DISABLED'),
  next_no_id  = (SELECT id FROM (SELECT id, code FROM questions) t WHERE t.code='Q_DISABLED')
WHERE q.code='Q_CARE';

UPDATE questions q SET
  next_yes_id = (SELECT id FROM (SELECT id, code FROM questions) t WHERE t.code='Q_NEET'),
  next_no_id  = (SELECT id FROM (SELECT id, code FROM questions) t WHERE t.code='Q_NEET')
WHERE q.code='Q_DISABLED';

UPDATE questions q SET
  next_yes_id = (SELECT id FROM (SELECT id, code FROM questions) t WHERE t.code='FINISH'),
  next_no_id  = (SELECT id FROM (SELECT id, code FROM questions) t WHERE t.code='FINISH')
WHERE q.code='Q_NEET';
