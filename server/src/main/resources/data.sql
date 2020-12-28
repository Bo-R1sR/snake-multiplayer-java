insert into user(id, username, password)
values (1L, 'admin', 'admin');
insert into user(id, username, password)
values (2L, 'testuser1', 'testuser1');
insert into user(id, username, password)
values (3L, 'testuser2', 'testuser2');
insert into user(id, username, password)
values (4L, 'testuser3', 'testuser3');
insert into user(id, username, password)
values (5L, 'testuser4', 'testuser4');
insert into user(id, username, password)
values (6L, 'testuser5', 'testuser5');

insert into game_history(id, points_user1, points_user2, username1, username2)
values (1L, 5, 0, 'admin', 'testuser1');
insert into game_history(id, points_user1, points_user2, username1, username2)
values (2L, 2, 3, 'testuser2', 'testuser3');
insert into game_history(id, points_user1, points_user2, username1, username2)
values (3L, 4, 1, 'testuser3', 'testuser1');
insert into game_history(id, points_user1, points_user2, username1, username2)
values (4L, 0, 5, 'testuser4', 'testuser2');
insert into game_history(id, points_user1, points_user2, username1, username2)
values (5L, 3, 2, 'testuser5', 'admin');






