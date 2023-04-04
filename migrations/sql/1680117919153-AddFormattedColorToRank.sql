/* AddFormattedColorCodeToRank

You can write your SQL migration here.
Please be sure that your migration work before pushing them to git.
You can run your migration by using DatabaseMigration artifact and running
`java -jar DatabaseMigration.jar migrate`
*/

addNewColumn {
    alter table ranks
        add formatted_color varchar(255) null after color_code;
}