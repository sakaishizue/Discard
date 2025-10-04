INSERT INTO article (discarded_date,title,imagefilename,trashtype,effort,created_at,updated_at)
VALUES
('2025-10-10','タイトル１','sampleimage1.png',0,0,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);

INSERT INTO article (discarded_date,title,imagefilename,trashtype,effort,created_at,updated_at)
VALUES
('2025-10-18','タイトル２','sampleimage2.png',1,1,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);

INSERT INTO article (discarded_date,title,imagefilename,trashtype,effort,created_at,updated_at)
VALUES
('2025-10-20','タイトル３','sampleimage1.png',1,1,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);

INSERT INTO t_type (id,name)
VALUES
(0,'可燃ゴミ');

INSERT INTO t_type (id,name)
VALUES
(1,'不燃ゴミ');

INSERT INTO t_type (id,name)
VALUES
(2,'粗大ゴミ');

INSERT INTO t_type (id,name)
VALUES
(3,'その他');

INSERT INTO t_effort (id,name)
VALUES
(0,'普通');

INSERT INTO t_effort (id,name)
VALUES
(1,'少し厄介');

INSERT INTO t_effort (id,name)
VALUES
(2,'めんどくさい');

INSERT INTO t_effort (id,name)
VALUES
(3,'大変');
