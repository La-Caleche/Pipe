/* SeedRanks

You can write your SQL migration here.
Please be sure that your migration work before pushing them to git.
You can run your migration by using DatabaseMigration artifact and running
`java -jar DatabaseMigration.jar migrate`
*/

INSERT INTO ranks (slug, color_code, is_default, created_at, updated_at) VALUES ('player', '#f4f2e5', true, DEFAULT, DEFAULT);

INSERT INTO ranks (slug, color_code, created_at, updated_at) VALUES ('admin', '#f05454', DEFAULT, DEFAULT);

INSERT INTO ranks (slug, color_code, created_at, updated_at) VALUES ('vip', '#f1c40f', DEFAULT, DEFAULT);

INSERT INTO ranks (slug, color_code, created_at, updated_at) VALUES ('viip', '#9b59b6', DEFAULT, DEFAULT);

INSERT INTO ranks (slug, color_code, created_at, updated_at) VALUES ('staff', '#1abc9c', DEFAULT, DEFAULT);
