DELETE FROM results;

INSERT INTO results (min_score, max_score, message) VALUES
(0, 0, 'ホワイト'),
(1, 100, 'まあ普通'),
(101, 200, '普通に忙しい'),
(201, 300, 'ハラスメントする上司いるかな'),
(301, 400, 'エブリデイ残業'),
(401, 500, '休日返上'),
(501, 600, '心も体も限界寸前'),
(601, 700, 'もはや正気では働けない'),
(701, 800, '無法地帯'),
(801, 900, '人のやることではない'),
(901, 1000, '死寄りの生と死のはざま')
ON DUPLICATE KEY UPDATE
  message = VALUES(message);