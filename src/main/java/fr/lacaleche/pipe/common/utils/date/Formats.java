package fr.lacaleche.pipe.common.utils.date;

public enum Formats {
    SECONDS("?s"),
    MINUTES("?m"),
    HOURS("?h"),
    DAYS("?d"),
    WEEKS("?w"),
    MONTHS("?mo"),
    YEARS("?y"),
    DATE("dd:MM:yyyy"),
    DEF("def");

    private final String completer;
    private final boolean combinable;

    Formats(String completer) {
        this.completer = completer;
        this.combinable = completer.contains("?");
    }

    public String getCompleter() {
        return completer;
    }

    public String parser() {
        return completer.replace("?", "");
    }

    public static Formats match(String value) {
        for (Formats formats : values()) {
            if (DateParser.getParser(formats).match(value)) return formats;
        }
        return null;
    }
}
