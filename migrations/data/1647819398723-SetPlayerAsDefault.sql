/* SetPlayerAsDefault

You can write your SQL migration here.
Please be sure that your migration work before pushing them to git.
You can run your migration by using DatabaseMigration artifact and running
`java -jar DatabaseMigration.jar migrate`
*/
setPlayerAsDefault {
    UPDATE ranks SET is_default = true, updated_at = CURRENT_TIMESTAMP WHERE slug = 'player';
}