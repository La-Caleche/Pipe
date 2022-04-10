/* AddRankToClient

You can write your SQL migration here.
Please be sure that your migration work before pushing them to git.
You can run your migration by using DatabaseMigration artifact and running
`java -jar DatabaseMigration.jar migrate`
*/

addRankId {
    alter table clients
        add rank_id int default 1 null;
}

addCreatedAt {
    alter table clients
        add created_at timestamp default CURRENT_TIMESTAMP null;
}

addUpdatedAt {
    alter table clients
        add updated_at timestamp default CURRENT_TIMESTAMP null;
}

addFkToRank {
    alter table clients
        add constraint clients_ranks_id_fk
            foreign key (rank_id) references ranks (id)
                on update cascade on delete set null;
}

