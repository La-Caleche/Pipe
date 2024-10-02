/* AddPermLevelToRank

You can write your SQL migration here.
Please be sure that your migration work before pushing them to git.
You can run your migration by using DatabaseMigration artifact and running
`java -jar DatabaseMigration.jar migrate`
*/

alter table ranks
    add perm_level int default 0 null after is_default;
