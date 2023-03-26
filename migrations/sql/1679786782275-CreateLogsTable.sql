/* CreateLogsTable

You can write your SQL migration here.
Please be sure that your migration work before pushing them to git.
You can run your migration by using DatabaseMigration artifact and running
`java -jar DatabaseMigration.jar migrate`
*/

CreateLogsTable {
    create table logs
    (
        id         int auto_increment,
        source     varchar(255)                        not null,
        data       text                                not null,
        created_at timestamp default CURRENT_TIMESTAMP null,
        updated_at timestamp default CURRENT_TIMESTAMP null,
        constraint logs_pk
            primary key (id)
    );
}
