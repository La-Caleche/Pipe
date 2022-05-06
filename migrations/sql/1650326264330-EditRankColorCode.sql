/* EditRankColorCode

You can write your SQL migration here.
Please be sure that your migration work before pushing them to git.
You can run your migration by using DatabaseMigration artifact and running
`java -jar DatabaseMigration.jar migrate`
*/

changeColorCodeLength {
    alter table ranks
        modify color_code varchar(255) null;
}