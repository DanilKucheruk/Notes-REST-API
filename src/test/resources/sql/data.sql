INSERT INTO users (id,username, password) VALUES (2,'test@gmail.com', 'test');
INSERT INTO users (id,username, password) VALUES (3,'user2', 'pass2');
INSERT INTO users (id,username, password) VALUES (4,'user3', 'pass3');
INSERT INTO users (id,username, password) VALUES (5,'user4', 'pass4');
INSERT INTO users (id,username, password) VALUES (6,'user5', 'pass5');

INSERT INTO lists (id,user_id, title, description) VALUES (2,2, 'list1', 'description1');
INSERT INTO lists (id,user_id, title, description) VALUES (7,2, 'list2forUser1', 'description1');
INSERT INTO lists (id,user_id, title, description) VALUES (3,3, 'list2', 'description2');
INSERT INTO lists (id,user_id, title, description) VALUES (4,4, 'list3', 'description3');
INSERT INTO lists (id,user_id, title, description) VALUES (5,5, 'list4', 'description4');
INSERT INTO lists (id,user_id, title, description) VALUES (6,6, 'list5', 'description5');

INSERT INTO notes (id,list_id, title, content) VALUES (2,2, 'note1', 'content1');
INSERT INTO notes (id,list_id, title, content) VALUES (7,2,'note2forList2', 'content2');
INSERT INTO notes (id,list_id, title, content) VALUES (3,3,'note3', 'content3');
INSERT INTO notes (id,list_id, title, content) VALUES (4,4, 'note4', 'content4');
INSERT INTO notes (id,list_id, title, content) VALUES (5,5, 'note5', 'content5');
INSERT INTO notes (id,list_id, title, content) VALUES (6,6, 'note5', 'content5');