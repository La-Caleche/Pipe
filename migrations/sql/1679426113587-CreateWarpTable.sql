/* CreateWarpTable

You can write your SQL migration here.
Please be sure that your migration work before pushing them to git.
You can run your migration by using DatabaseMigration artifact and running
`java -jar DatabaseMigration.jar migrate`
*/
createWarpTable {
    create table warps
    (
        id         int auto_increment,
        name       varchar(255)                        not null,
        location   text                                not null,
        host       varchar(255)                        not null,
        created_at timestamp default current_timestamp null,
        updated_at timestamp default current_timestamp null,
        constraint warps_pk
            primary key (id)
    );
}