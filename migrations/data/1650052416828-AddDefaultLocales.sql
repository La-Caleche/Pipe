/* AddDefaultLocales

You can write your SQL migration here.
Please be sure that your migration work before pushing them to git.
You can run your migration by using DatabaseMigration artifact and running
`java -jar DatabaseMigration.jar migrate`
*/

INSERT INTO locales (slug, is_default, created_at, updated_at) VALUES ('fr', 1, DEFAULT, DEFAULT);

INSERT INTO locales (slug, is_default, created_at, updated_at) VALUES ('en', 0, DEFAULT, DEFAULT);

UPDATE clients SET locale_id = (SELECT id FROM locales WHERE is_default = 1);
