package fr.lacaleche.pipe;

import fr.lacaleche.core.utils.logger.Logger;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.IntStream;

public class Tests {

    public static void main(String[] args) throws IOException, SQLException {
        Tests test = new Tests();
        test.start();
        test.run();
        test.stop();
    }

    private void run() {
        Collection<String> ints = new ArrayList<>();

        ints.addAll(IntStream.rangeClosed(0, 24000 / 1000)
                .boxed()
                .map(i -> i * 1000)
                .map(Object::toString)
                .toList());

        // Preprocess list to store numeric values as BigDecimal
        Map<String, BigDecimal> numericValues = new HashMap<>();
        for (String s : ints) {
            if (NumberUtils.isCreatable(s)) {
                numericValues.put(s, new BigDecimal(s));
            }
        }

        ints = ints.stream().sorted(Comparator.comparing(
                (String s) -> NumberUtils.isCreatable(s) ? numericValues.get(s) : new BigDecimal(Integer.MAX_VALUE),
                BigDecimal::compareTo
        ).thenComparing(String::toString)).toList();

        ints = ints.stream().map(s -> s.replace("%", "")).toList();

        ints.forEach(Logger::info);
    }

    private void start() {}

    private void stop() {}
}
