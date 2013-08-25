package ru.albemuth.frontend.metadata;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleMacros extends Macros {

    private Pattern pattern;
    private MessageFormat replacement;

    public SimpleMacros(String pattern, String replacementPattern) {
        this.pattern = Pattern.compile(pattern, Pattern.DOTALL);
        this.replacement = new MessageFormat(replacementPattern);
    }

    public StringBuffer apply(StringBuffer source) {
        Object[] args = null;
        int from = 0;
        for (Matcher m = pattern.matcher(source); m.find(from); ) {
            if (args == null) {
                args = new Object[m.groupCount()];
            }
            for (int i = 0; i < m.groupCount(); i++) {
                args[i] = m.group(i + 1);
            }
            source.replace(m.start(), m.end(), replacement.format(args, new StringBuffer(), null).toString());
        }
        return source;
    }

}
