/* AddWorldToWarps

You can write your SQL migration here.
Please be sure that your migration work before pushing them to git.
You can run your migration by using DatabaseMigration artifact and running
`java -jar DatabaseMigration.jar migrate`
*/

addWorldToWarps {
    ALTER TABLE `warps` ADD `world` VARCHAR(255) NOT NULL DEFAULT 'world' AFTER `name`;
}