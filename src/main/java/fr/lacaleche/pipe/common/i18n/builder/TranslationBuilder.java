package fr.lacaleche.pipe.common.i18n.builder;

import net.kyori.adventure.text.Component;

public interface TranslationBuilder {

    TranslationBuilder arg(String key, String value);

    TranslationBuilder arg(String key, int value);

    TranslationBuilder arg(String key, double value);

    TranslationBuilder arg(String key, boolean value);

    TranslationBuilder arg(String key, long value);

    TranslationBuilder arg(String key, float value);

    TranslationBuilder arg(String key, short value);

    TranslationBuilder arg(String key, byte value);

    TranslationBuilder arg(String key, char value);

    TranslationBuilder arg(String key, Component value);

    TranslationBuilder arg(String key, Object value);

    TranslationBuilder arg(TArg argument);

    <R> TranslationBuilder ph(String key, R r);

    <R, S> TranslationBuilder ph(String key, R r, S s);

    <R, S, T> TranslationBuilder ph(String key, R r, S s, T t);

    <R, S, T, U> TranslationBuilder ph(String key, R r, S s, T t, U u);

    TranslationBuilder from(String from);

    String t();

    Component ct();

}
