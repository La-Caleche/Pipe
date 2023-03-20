/* AddGlobalToFeatures

You can write your SQL migration here.
Please be sure that your migration work before pushing them to git.
You can run your migration by using DatabaseMigration artifact and running
`java -jar DatabaseMigration.jar migrate`
*/

addGlobalToFeatures {
    alter table features
        add host varchar(255) null after name;
}