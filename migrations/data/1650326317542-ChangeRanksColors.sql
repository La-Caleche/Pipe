/* ChangeRanksColors

You can write your SQL migration here.
Please be sure that your migration work before pushing them to git.
You can run your migration by using DatabaseMigration artifact and running
`java -jar DatabaseMigration.jar migrate`
*/

updatePlayers {
    UPDATE ranks SET color_code = '<color:#f4f2e5>' WHERE id = 1;
}

updateAdmin {
    UPDATE ranks SET color_code = '<color:#f05454>' WHERE id = 2;
}

updateVip {
    UPDATE ranks SET color_code = '<color:#f1c40f>' WHERE id = 3;
}

updateViip {
    UPDATE ranks SET color_code = '<color:#9b59b6>' WHERE id = 4;
}

updateStaff {
    UPDATE ranks SET color_code = '<color:#e91e63>' WHERE id = 5;
}

updateMember {
    UPDATE ranks SET color_code = '<color:#1abc9c>' WHERE id = 6;
}

