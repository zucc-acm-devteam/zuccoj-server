package top.kealine.zuccoj.constant;

import com.google.common.collect.ImmutableSet;

import java.util.Set;

public class SupportedLanguage {
    public static final int C = 1;
    public static final int CPP = 2;
    public static final int JAVA = 3;

    public static final Set<Integer> SUPPORTED_LANGUAGE_LIST = ImmutableSet.of(C, CPP, JAVA);

    public static boolean isLanguageSupported(int lang) {
        return SUPPORTED_LANGUAGE_LIST.contains(lang);
    }
}
