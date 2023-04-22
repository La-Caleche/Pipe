package fr.pipe;

import fr.lacaleche.core.Core;
import fr.lacaleche.core.databases.mysql.SqlDatabase;
import fr.lacaleche.core.databases.mysql.SqlDatabaseImpl;
import fr.lacaleche.core.utils.logger.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Tests {

    public static void main(String[] args) throws IOException, SQLException {
        Tests test = new Tests();
        test.start();
        test.run();
        test.stop();
    }

    private void run() {
        Map<Integer, String> uselessMap = new HashMap<>();
        uselessMap.put(2, "test2");
        uselessMap.put(-1, "minus1");
        uselessMap.put(0, "zero");
        uselessMap.put(-10, "-10");
        uselessMap.put(10, "10");
        uselessMap.put(8, "8");

        int[] ints = uselessMap.keySet().stream().mapToInt(Integer::intValue).sorted().toArray();
        System.out.println(String.join(", ", Arrays.toString(ints)));
    }

    private void start() {}

    private void stop() {}
}
