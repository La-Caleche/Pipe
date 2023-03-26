/* CreateBanTable

You can write your SQL migration here.
Please be sure that your migration work before pushing them to git.
You can run your migration by using DatabaseMigration artifact and running
`java -jar DatabaseMigration.jar migrate`
*/

CreateBanTable {
    create table bans
    (
        id         int auto_increment,
        author     varchar(255)                        not null,
        target     varchar(255)                        not null,
        reason     varchar(255)                        not null,
        end_at     timestamp                           null,
        created_at timestamp default CURRENT_TIMESTAMP null,
        updated_at timestamp default CURRENT_TIMESTAMP null,
        constraint ban_pk
            primary key (id)
    );
}