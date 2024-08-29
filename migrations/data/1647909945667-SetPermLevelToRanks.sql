/* SetPermLevelToRanks

You can write your SQL migration here.
Please be sure that your migration work before pushing them to git.
You can run your migration by using DatabaseMigration artifact and running
`java -jar DatabaseMigration.jar migrate`
*/

UPDATE ranks SET perm_level = 100, updated_at = current_timestamp WHERE id = 2;

UPDATE ranks SET perm_level = 1, updated_at = current_timestamp WHERE id = 3;

UPDATE ranks SET perm_level = 2, updated_at = current_timestamp WHERE id = 4;

UPDATE ranks SET perm_level = 20, updated_at = current_timestamp WHERE id = 5;

UPDATE ranks SET perm_level = 10, updated_at = current_timestamp WHERE id = 6;
