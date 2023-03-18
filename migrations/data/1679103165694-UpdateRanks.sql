/* UpdateRanks

You can write your SQL migration here.
Please be sure that your migration work before pushing them to git.
You can run your migration by using DatabaseMigration artifact and running
`java -jar DatabaseMigration.jar migrate`
*/

createGameDesignerRank {
    INSERT INTO ranks (slug, color_code, perm_level) VALUES ('game-designer', '<color:#f0941f>', 30);
}

createBuilderRank {
    INSERT INTO ranks (slug, color_code, perm_level) VALUES ('builder', '<color:#90a19d>', 30);
}

createModeratorRank {
    INSERT INTO ranks (slug, color_code, perm_level) VALUES ('moderator', '<color:#e67e22>', 50);
}

createResponsableRank {
    INSERT INTO ranks (slug, color_code, perm_level) VALUES ('responsable', '<color:#3498db>', 70);
}

createDeveloperRank {
    INSERT INTO ranks (slug, color_code, perm_level) VALUES ('developer', '<color:#196774>', 90);
}