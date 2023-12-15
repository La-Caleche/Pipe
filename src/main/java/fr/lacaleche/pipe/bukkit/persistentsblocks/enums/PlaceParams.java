package fr.lacaleche.pipe.bukkit.persistentsblocks.enums;

public enum PlaceParams {

    TOP_BLOCK(false);

    private final Object defaultValue;

    PlaceParams(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

}
