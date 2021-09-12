package org.infodavid.util;

import java.lang.ref.WeakReference;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.commons.lang3.ArrayUtils;

/**
 * The Class StringUtils.
 */
public final class StringUtils {

    /** The Constant CR. */
    public static final char CR = '\n';

    /** The Constant CR. */
    public static final String CRLF = "\r\n";

    /** The Constant DOT. */
    public static final String DOT = ".";

    /** The Constant DOT_CHAR. */
    public static final char DOT_CHAR = '.';

    /** The Constant EQ. */
    public static final char EQ = '=';

    /** The Constant SPACE_CHAR. */
    public static final char SPACE_CHAR = ' ';

    /** The Constant TAB. */
    public static final char TAB = '\t';

    /** The singleton. */
    private static WeakReference<StringUtils> instance = null;

    /** The rand. */
    private static Random rand = new Random(System.currentTimeMillis());

    /**
     * returns the singleton.
     * @return the singleton
     */
    public static synchronized StringUtils getInstance() {
        if (instance == null || instance.get() == null) {
            instance = new WeakReference<>(new StringUtils());
        }

        return instance.get();
    }

    /** The source. */
    private final byte[] source = " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".getBytes(StandardCharsets.UTF_8); // NOSONAR Allow unallocation

    /** The target. */
    private final byte[] target = "Qa5Ab8zWZcS0XtdEsDyCr6eRFqViT f9hGpBoYg4jnHkmNlU3Ju2MIv1KO7LwPx".getBytes(StandardCharsets.UTF_8); // NOSONAR Allow unallocation

    /**
     * Instantiates a new util.
     */
    private StringUtils() {
        super();
    }

    /**
     * Compare version.
     * @param v1 the v 1
     * @param v2 the v 2
     * @return the int
     */
    public int compareVersion(final String v1, final String v2) {
        if (v1 == v2) { // NOSONAR
            return 0;
        }
        if (v1 == null) {
            return -1;
        }

        if (v2 == null) {
            return 1;
        }

        if (v1.equals(v2)) {
            return 0;
        }

        String[] parts1 = v1.split("\\.");
        String[] parts2 = v2.split("\\.");

        if (parts1.length < parts2.length) {
            parts1 = ArrayUtils.add(parts1, "0");
        }
        else if (parts2.length < parts1.length) {
            parts2 = ArrayUtils.add(parts2, "0");
        }

        for (int i = 0; i < parts1.length; i++) {
            final int part1 = Integer.parseInt(parts1[i]);
            final int part2 = Integer.parseInt(parts2[i]);

            if (part1 < part2) {
                return -1;
            }

            if (part1 > part2) {
                return 1;
            }
        }
        return 0;
    }

    /**
     * Decode.
     * @param bytes the bytes
     * @return the string
     */
    public String decode(final byte[] bytes) {
        if (bytes == null) {
            return null;
        }

        if (bytes.length == 0) {
            return "";
        }

        final byte[] result = new byte[bytes.length];

        for (int i = 0; i < bytes.length; i++) {
            final int index = ArrayUtils.indexOf(target, bytes[i]);

            if (index >= 0) {
                result[i] = source[index];
            }
            else {
                result[i] = bytes[i];
            }
        }

        return new String(result, StandardCharsets.UTF_8);
    }

    /**
     * Decode.
     * @param text the text to decode
     * @return the string
     */
    public String decode(final String text) {
        if (text == null) {
            return null;
        }

        if (text.length() == 0) {
            return "";
        }

        final byte[] result = text.getBytes(StandardCharsets.UTF_8);

        for (int i = 0; i < result.length; i++) {
            final int index = ArrayUtils.indexOf(target, result[i]);

            if (index >= 0) {
                result[i] = source[index];
            }
        }

        return new String(result, StandardCharsets.UTF_8);
    }

    /**
     * Encode.
     * @param text the text to encode
     * @return the byte[]
     */
    public byte[] encode(final String text) {
        if (text == null || text.length() == 0) {
            return new byte[0];
        }

        final byte[] result = text.getBytes(StandardCharsets.UTF_8);

        for (int i = 0; i < result.length; i++) {
            final int index = ArrayUtils.indexOf(source, result[i]);

            if (index >= 0) {
                result[i] = target[index];
            }
        }

        return result;
    }

    /**
     * Equals removing white spaces.
     * @param s1 the s 1
     * @param s2 the s 2
     * @return true, if successful
     */
    public boolean equalsRemovingWhiteSpaces(final String s1, final String s2) {
        if (s1 == null && s2 == null) {
            return true;
        }

        if (s1 == null || s2 == null) {
            return false;
        }

        return removeWhiteSpaces(s1).equals(removeWhiteSpaces(s2));
    }

    /**
     * Generate random string.
     * @param characters the characters
     * @param length the length
     * @return the string
     */
    public String generateRandomString(final String characters, final int length) {
        final StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            final int pos = rand.nextInt(characters.length());
            sb.append(characters.charAt(pos));
        }

        return sb.toString();
    }

    /**
     * Gets the UUID.
     * @return the UUID
     */
    public String generateUuid() {
        return UUID.nameUUIDFromBytes((NetUtils.getInstance().getComputerName() + '-' + String.valueOf(System.nanoTime())).getBytes()).toString();
    }

    /**
     * To class.
     * @param value the value
     * @return the class
     */
    @SuppressWarnings("rawtypes")
    public Class getClassQuietly(final String value) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(value)) {
            return null;
        }

        try {
            return Class.forName(value);
        }
        catch (final ClassNotFoundException e) { // NOSONAR Quietly
            return null;
        }
    }

    /**
     * Gets the lines.
     * @param s the s
     * @return the lines
     */
    public String[] getLines(final String s) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(s)) {
            return new String[0];
        }

        return s.split("\\r?\\n");
    }

    /**
     * Left susp.
     * @param s the s
     * @param i the i
     * @return the string
     */
    public String leftSusp(final String s, final int i) {
        if (s == null) {
            return null;
        }

        if (i > s.length()) {
            return s;
        }

        return s.substring(0, i - 3) + "...";
    }

    /**
     * Matches.
     * @param regex the regex
     * @param value the value
     * @return true, if successful
     */
    public boolean matches(final Collection<String> regex, final String value) {
        if (regex == null || regex.isEmpty()) {
            return false;
        }

        for (final String item : regex) {
            if (matches(item, value)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Matches.
     * @param regex the regex
     * @param value the value
     * @return true, if successful
     */
    public boolean matches(final String regex, final String value) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(regex) && org.apache.commons.lang3.StringUtils.isEmpty(value)) {
            return true;
        }

        if (org.apache.commons.lang3.StringUtils.isEmpty(regex) || org.apache.commons.lang3.StringUtils.isEmpty(value)) {
            return false;
        }

        try {
            if (Pattern.matches(regex, value)) {
                return true;
            }
        }
        catch (final PatternSyntaxException e) {
            if (regex.equals(value)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Null less.
     * @param s the s
     * @return the string
     */
    public String nullLess(final String s) {
        return s == null ? "" : s;
    }

    /**
     * Removes the white spaces.
     * @param s the s
     * @return the string
     */
    public String removeWhiteSpaces(final String s) {
        return s.replaceAll("\\s+", "");
    }

    /**
     * Replace all no regex.
     * @param s the s
     * @param oldValue the old value
     * @param newValue the new value
     * @return the string
     */
    public String replaceAllNoRegex(final String s, final String oldValue, final String newValue) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(s)) {
            return s;
        }

        if (org.apache.commons.lang3.StringUtils.isEmpty(oldValue)) {
            return s;
        }

        if (newValue == null) {
            return s;
        }

        String result = s;

        while (result.indexOf(oldValue) >= 0) {
            result = result.replace(oldValue, newValue);
        }

        return result;
    }

    /**
     * Replace empty string by null.
     * @param s the s
     * @return the string
     */
    public String replaceEmptyStringByNull(final String s) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(s)) {
            return null;
        }

        return s;
    }

    /**
     * Replace the string at the specified position.
     * @param value the value
     * @param replacement the replacement
     * @param index the index
     * @return the string
     */
    public String setAt(final String value, final String replacement, final int index) {
        if (value == null || index < 0 || index > value.length() - 1 || replacement == null || replacement.length() == 0) {
            return value;
        }

        final char[] array1 = value.toCharArray();
        final char[] array2 = replacement.toCharArray();
        int length = array2.length;

        if (index + length > array1.length) {
            length = array1.length - index;
        }

        System.arraycopy(array2, 0, array1, index, length);

        return new String(array1);
    }

    /**
     * Sets the char at the specified position.
     * @param value the value
     * @param replacement the replacement
     * @param index the index
     * @return the string
     */
    public String setCharAt(final String value, final char replacement, final int index) {
        if (value == null || index < 0 || index > value.length() - 1) {
            return value;
        }

        final char[] array = value.toCharArray();
        array[index] = replacement;

        return new String(array);
    }

    /**
     * Boolean to string.
     * @param active the active
     * @return the string
     */
    public String toBooleanString(final int active) {
        if (active > 0) {
            return "true";
        }

        return "false";
    }

    /**
     * To java script html string.
     * @param s the s
     * @return the string
     */
    public String toJavaScriptHtmlString(final String s) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(s)) {
            return "";
        }

        String result = s;

        result = result.replaceAll("[\r\n]", "");
        result = result.replaceAll("\\u0022", "\\\\\"");
        result = result.replaceAll("\\u0027", "\\\\\'");

        return result;
    }

    /**
     * To string.
     * @param value the value
     * @return the string
     */
    @SuppressWarnings("rawtypes")
    public String toString(final Class value) {
        return value == null ? null : value.getName();
    }

    /**
     * To string.
     * @param collection the value
     * @param buffer the buffer
     */
    @SuppressWarnings({
            "rawtypes"
    })
    public void toString(final Collection collection, final StringBuilder buffer) {
        final Iterator ite = collection.iterator();

        buffer.append('[');

        if (ite.hasNext()) {
            toString(ite.next(), buffer);

            while (ite.hasNext()) {
                buffer.append(',');
                toString(ite.next(), buffer);
            }
        }

        buffer.append(']');
    }

    /**
     * To string.
     * @param collection the collection
     * @return the string
     */
    public String toString(final Collection<Object> collection) {
        return org.apache.commons.lang3.StringUtils.join(collection, ",");
    }

    /**
     * To string.
     * @param map the value
     * @param buffer the buffer
     */
    @SuppressWarnings({
            "rawtypes", "unchecked"
    })
    public void toString(final Map map, final StringBuilder buffer) {
        final Iterator<Entry> ite = map.entrySet().iterator();

        buffer.append('[');

        if (ite.hasNext()) {
            Map.Entry entry = ite.next();

            buffer.append(entry.getKey());
            buffer.append('=');
            toString(entry.getValue(), buffer);

            while (ite.hasNext()) {
                entry = ite.next();

                buffer.append(',');
                buffer.append(entry.getKey());
                buffer.append('=');
                toString(entry.getValue(), buffer);
            }
        }

        buffer.append(']');
    }

    /**
     * To string.
     * @param object the object
     * @param buffer the buffer
     */
    @SuppressWarnings({
            "rawtypes"
    })
    public void toString(final Object object, final StringBuilder buffer) {
        if (object == null) {
            buffer.append("null");
        }
        else if (object.getClass().isArray()) {
            if (object.getClass().getComponentType() == byte.class) {
                buffer.append(Arrays.toString((byte[])object));
            }
            else if (object.getClass().getComponentType() == short.class) {
                buffer.append(Arrays.toString((short[])object));
            }
            else if (object.getClass().getComponentType() == int.class) {
                buffer.append(Arrays.toString((int[])object));
            }
            else if (object.getClass().getComponentType() == long.class) {
                buffer.append(Arrays.toString((long[])object));
            }
            else if (object.getClass().getComponentType() == float.class) {
                buffer.append(Arrays.toString((float[])object));
            }
            else if (object.getClass().getComponentType() == double.class) {
                buffer.append(Arrays.toString((double[])object));
            }
            else {
                toString((Object[])object, buffer);
            }
        }
        else if (object instanceof Collection) {
            toString((Collection)object, buffer);
        }
        else if (object instanceof Map) {
            toString((Map)object, buffer);
        }
        else {
            buffer.append(object);
        }
    }

    /**
     * To string.
     * @param array the array
     * @param buffer the buffer
     */
    public void toString(final Object[] array, final StringBuilder buffer) {
        buffer.append(Arrays.toString(array));
    }
}
