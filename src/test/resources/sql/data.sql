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

-- INSERT INTO notes (id,list_id, title, content) VALUES (2,2, 'Note1', 'Content1');
-- INSERT INTO notes (id,list_id, title, content) VALUES (7,3 'Note2', 'Content2');
-- INSERT INTO notes (id,list_id, title, content) VALUES (3,3,'Note3', 'Content3');
-- INSERT INTO notes (id,list_id, title, content) VALUES (4,4, 'Note4', 'Content4');
-- INSERT INTO notes (id,list_id, title, content) VALUES (5,5, 'Note5', 'Content5');
-- INSERT INTO notes (id,list_id, title, content) VALUES (6,6, 'Note5', 'Content5');