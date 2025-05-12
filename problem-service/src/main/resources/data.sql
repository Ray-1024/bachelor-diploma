insert into tags ("id","name") values (1,'GRAPHS') on conflict do nothing;
insert into tags ("id","name") values (2,'BINARY_SEARCH') on conflict do nothing;
insert into tags ("id","name") values (3,'MATH') on conflict do nothing;
insert into tags ("id","name") values (4,'TREES') on conflict do nothing;
insert into tags ("id","name") values (5,'SORTINGS') on conflict do nothing;
insert into tags ("id","name") values (6,'BITMASKS') on conflict do nothing;
insert into tags ("id","name") values (7,'DYNAMIC_PROGRAMMING') on conflict do nothing;
insert into tags ("id","name") values (8,'DATA_STRUCTURES') on conflict do nothing;
insert into tags ("id","name") values (9,'DISJOINT_SET_UNION') on conflict do nothing;
insert into tags ("id","name") values (10,'BRUTE_FORCE') on conflict do nothing;
insert into tags ("id","name") values (11,'TWO_POINTERS') on conflict do nothing;
insert into tags ("id","name") values (12,'FLOWS') on conflict do nothing;
insert into tags ("id","name") values (13,'STRINGS') on conflict do nothing;
insert into tags ("id","name") values (14,'GEOMETRY') on conflict do nothing;
insert into tags ("id","name") values (15,'SPECIAL') on conflict do nothing;


insert into programminglanguages ("name") values ('CPP') on conflict do nothing;
insert into programminglanguages ("name") values ('JAVA') on conflict do nothing;


insert into submissionstatuses ("name") values ('NEW') on conflict do nothing;
insert into submissionstatuses ("name") values ('TESTING') on conflict do nothing;
insert into submissionstatuses ("name") values ('OK') on conflict do nothing;
insert into submissionstatuses ("name") values ('RUNTIME_ERROR') on conflict do nothing;
insert into submissionstatuses ("name") values ('COMPILATION_ERROR') on conflict do nothing;
insert into submissionstatuses ("name") values ('WRONG_ANSWER') on conflict do nothing;


insert into testcases ("id","input","output") values (1,'2', '4') on conflict do nothing;
insert into testcases ("id","input","output") values (2,'1', '1') on conflict do nothing;
insert into testcases ("id","input","output") values (3,'-5', '25') on conflict do nothing;
insert into testcases ("id","input","output") values (4,'', '') on conflict do nothing;
insert into testcases ("id","input","output") values (5,'', '') on conflict do nothing;


INSERT INTO problems (id, author_id, creation_date, title, description, input, output, memory_limit_bytes, time_limit_milliseconds)
VALUES (
            1,
           1,
           NOW(),
           'Square it',
           'Calculate square integer number.',
           '|A| < 100.',
           'Write A^2 value to output stream or output.txt file.',
           256000000,
           1000
       ) on conflict do nothing;
INSERT INTO problems_samples (problem_id, samples_id) VALUES (1, 1);
INSERT INTO problems_samples (problem_id, samples_id) VALUES (1, 2);
INSERT INTO problems_tests (problem_id, tests_id) VALUES (1, 3);
INSERT INTO problems_tests (problem_id, tests_id) VALUES (1, 4);
INSERT INTO problems_tests (problem_id, tests_id) VALUES (1, 5);
INSERT INTO problems_tags (problem_id, tags_id) VALUES (1, 1);
INSERT INTO problems_tags (problem_id, tags_id) VALUES (1, 2);
