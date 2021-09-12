package org.infodavid.util;

import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Objects;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * The Class ObjectUtils.
 */
public final class ObjectUtils {

    /** The Constant SEPARATOR_CHAR. */
    private static final char SEPARATOR_CHAR = ',';

    /** The Constant IS_NOT_A_BOOLEAN_VALUE. */
    private static final String IS_NOT_A_BOOLEAN_VALUE = " is not a boolean value";

    /** The Constant IS_NOT_A_BYTE_VALUE. */
    private static final String IS_NOT_A_BYTE_VALUE = " is not a byte value";

    /** The Constant IS_NOT_AN_INTEGER_VALUE. */
    private static final String IS_NOT_AN_INTEGER_VALUE = " is not an integer value";

    /** The Constant IS_NOT_A_LONG_INTEGER_VALUE. */
    private static final String IS_NOT_A_LONG_INTEGER_VALUE = " is not a long integer value";

    /** The Constant IS_NOT_A_SHORT_INTEGER_VALUE. */
    private static final String IS_NOT_A_SHORT_INTEGER_VALUE = " is not a short integer value";

    /** The Constant MUST_BE_SPECIFIED. */
    private static final String MUST_BE_SPECIFIED = " must be specified";

    /** The singleton. */
    private static WeakReference<ObjectUtils> instance = null;

    /**
     * returns the singleton.
     * @return the singleton
     */
    public static synchronized ObjectUtils getInstance() {
        if (instance == null || instance.get() == null) {
            instance = new WeakReference<>(new ObjectUtils());
        }

        return instance.get();
    }

    /**
     * Instantiates a new util.
     */
    private ObjectUtils() {
        super();
    }

    /**
     * Equals method taking into account:<br/>
     * BigInteger vs Number like Integer, Long, etc,
     * BigDecimal vs Number like Double, Float,
     * String vs Character,
     * @param left the left
     * @param right the right
     * @return true, if value are equal
     */
    public boolean equals(final Object left, final Object right) {
        if (left instanceof BigDecimal && !(right instanceof BigDecimal) && right instanceof Number) {
            return Objects.equals(left, BigDecimal.valueOf(((Number)right).doubleValue()));
        }
        else if (left instanceof BigInteger && !(right instanceof BigInteger) && right instanceof Number) {
            return Objects.equals(left, BigInteger.valueOf(((Number)right).longValue()));
        }
        else if (right instanceof BigDecimal && !(left instanceof BigDecimal) && left instanceof Number) {
            return Objects.equals(BigDecimal.valueOf(((Number)left).doubleValue()), right);
        }
        else if (right instanceof BigInteger && !(left instanceof BigInteger) && left instanceof Number) {
            return Objects.equals(BigInteger.valueOf(((Number)left).longValue()), right);
        }
        else if (left instanceof String && right instanceof Character) {
            return Objects.equals(left, ((Character)right).toString());
        }
        else if (right instanceof String && left instanceof Character) {
            return Objects.equals(((Character)left).toString(), right);
        }

        return Objects.equals(left, right);
    }

    /**
     * To boolean.
     * @param object the value
     * @return true, if successful
     */
    public boolean toBoolean(final Object object) {
        return toBoolean(null, object);
    }

    /**
     * To boolean.
     * @param label the label, assuming the value is required
     * @param object the value
     * @return true, if successful
     */
    public boolean toBoolean(final String label, final Object object) {
        if (object == null) {
            if (label != null) {
                throw new IllegalArgumentException(label + MUST_BE_SPECIFIED);
            }

            return false;
        }

        if (object instanceof Boolean) {
            return BooleanUtils.toBoolean((Boolean)object);
        }

        if (object instanceof Number) {
            return BooleanUtils.toBoolean(((Number)object).intValue());
        }

        if (object instanceof String) {
            return BooleanUtils.toBoolean(StringUtils.trim((String)object));
        }

        if (label == null) {
            return false;
        }

        throw new IllegalArgumentException(label + IS_NOT_A_BOOLEAN_VALUE);
    }

    /**
     * Gets the boolean.
     * @param object the value
     * @return the boolean
     */
    public Boolean toBooleanObject(final Object object) {
        return toBooleanObject(null, object);
    }

    /**
     * Gets the boolean.
     * @param label the label, assuming the value is required
     * @param object the value
     * @return the boolean
     */
    public Boolean toBooleanObject(final String label, final Object object) {
        if (object == null) {
            if (label != null) {
                throw new IllegalArgumentException(label + MUST_BE_SPECIFIED);
            }

            return null; // NOSONAR Null is allowed
        }

        if (object instanceof Boolean) {
            return (Boolean)object;
        }

        if (object instanceof String) {
            return BooleanUtils.toBooleanObject(StringUtils.trim((String)object));
        }

        if (object instanceof Number) {
            return BooleanUtils.toBooleanObject(((Number)object).intValue());
        }

        if (label == null) {
            return null; // NOSONAR Null is allowed
        }

        throw new IllegalArgumentException(label + IS_NOT_A_BOOLEAN_VALUE);
    }

    /**
     * Gets the booleans.
     * @param value the value
     * @return the booleans
     */
    @SuppressWarnings("rawtypes")
    public boolean[] toBooleans(final Object value) {
        if (value == null) {
            return null; // NOSONAR Must return null
        }

        boolean[] result = null;

        if (value instanceof Boolean) {
            result = new boolean[] {
                    ((Boolean)value).booleanValue()
            };
        }
        else if (value instanceof Collection) {
            final Collection collection = (Collection)value;
            result = new boolean[collection.size()];
            int i = 0;

            for (final Object item : collection) {
                result[i++] = toBoolean(item);
            }
        }
        else if (value.getClass().isArray() && value.getClass().getComponentType().equals(Boolean.class)) {
            result = new boolean[Array.getLength(value)];

            for (int i = 0; i < result.length; i++) {
                result[i] = toBoolean(Array.get(value, i));
            }
        }
        else if (value instanceof String) {
            final String[] items = StringUtils.split((String)value, SEPARATOR_CHAR);
            result = new boolean[items.length];

            for (int i = 0; i < items.length; i++) {
                result[i] = toBoolean(items[i]);
            }
        }

        return result;
    }

    /**
     * To byte.
     * @param object the value
     * @return true, if successful
     */
    public byte toByte(final Object object) {
        return toByte(null, object);
    }

    /**
     * To byte.
     * @param label the label, assuming the value is required
     * @param object the value
     * @return true, if successful
     */
    public byte toByte(final String label, final Object object) {
        if (object == null) {
            if (label != null) {
                throw new IllegalArgumentException(label + MUST_BE_SPECIFIED);
            }

            return 0;
        }

        if (object instanceof Byte) {
            return ((Byte)object).byteValue();
        }

        if (object instanceof String) {
            return Byte.parseByte(StringUtils.trim((String)object));
        }

        if (label == null) {
            return 0;
        }

        throw new IllegalArgumentException(label + IS_NOT_A_BYTE_VALUE);
    }

    /**
     * Gets the byte.
     * @param object the value
     * @return the byte
     */
    public Byte toByteObject(final Object object) {
        return toByteObject(null, object);
    }

    /**
     * Gets the byte.
     * @param label the label, assuming the value is required
     * @param object the value
     * @return the byte
     */
    public Byte toByteObject(final String label, final Object object) {
        if (object == null) {
            if (label != null) {
                throw new IllegalArgumentException(label + MUST_BE_SPECIFIED);
            }

            return null; // NOSONAR Null is allowed
        }

        if (object instanceof Byte) {
            return (Byte)object;
        }

        if (object instanceof String) {
            return Byte.valueOf(StringUtils.trim((String)object));
        }

        if (object instanceof Number) {
            return Byte.valueOf(((Number)object).byteValue());
        }

        if (label == null) {
            return null; // NOSONAR Null is allowed
        }

        throw new IllegalArgumentException(label + IS_NOT_A_BYTE_VALUE);
    }

    /**
     * Gets the bytes.
     * @param value the value
     * @return the bytes
     */
    @SuppressWarnings("rawtypes")
    public byte[] toBytes(final Object value) {
        if (value == null) {
            return null; // NOSONAR Must return null
        }

        byte[] result = null;

        if (value instanceof Number) {
            result = new byte[] {
                    ((Number)value).byteValue()
            };
        }
        else if (value instanceof Collection) {
            final Collection collection = (Collection)value;
            result = new byte[collection.size()];
            int i = 0;

            for (final Object item : collection) {
                result[i++] = toByte(item);
            }
        }
        else if (value.getClass().isArray() && (value.getClass().getComponentType().equals(Number.class) || value.getClass().getComponentType().equals(Long.class) || value.getClass().getComponentType().equals(Integer.class) || value.getClass().getComponentType().equals(Byte.class) || value.getClass().getComponentType().equals(Short.class) || value.getClass().getComponentType().equals(Float.class) || value.getClass().getComponentType().equals(Double.class))) {
            result = new byte[Array.getLength(value)];

            for (int i = 0; i < result.length; i++) {
                result[i] = toByte(Array.get(value, i));
            }
        }
        else if (value instanceof String) {
            final String[] items = StringUtils.split((String)value, SEPARATOR_CHAR);
            result = new byte[items.length];

            for (int i = 0; i < items.length; i++) {
                result[i] = toByte(items[i]);
            }
        }

        return result;
    }

    /**
     * Gets the integer.
     * @param object the value
     * @return the integer
     */
    public int toInteger(final Object object) {
        return toInteger(null, object);
    }

    /**
     * Gets the integer.
     * @param label the label, assuming the value is required
     * @param object the value
     * @return the integer
     */
    public int toInteger(final String label, final Object object) {
        if (object == null) {
            if (label != null) {
                throw new IllegalArgumentException(label + MUST_BE_SPECIFIED);
            }

            return 0;
        }

        if (object instanceof Integer) {
            return ((Integer)object).intValue();
        }

        if (object instanceof Number) {
            return ((Number)object).intValue();
        }

        if (object instanceof String && StringUtils.isNumeric(StringUtils.trim((String)object))) {
            return Integer.parseInt(StringUtils.trim((String)object));
        }

        if (label == null) {
            return 0;
        }

        throw new IllegalArgumentException(label + IS_NOT_AN_INTEGER_VALUE);
    }

    /**
     * Gets the integer.
     * @param object the value
     * @return the integer
     */
    public Integer toIntegerObject(final Object object) {
        return toIntegerObject(null, object);
    }

    /**
     * Gets the integer.
     * @param label the label, assuming the value is required
     * @param object the value
     * @return the integer
     */
    public Integer toIntegerObject(final String label, final Object object) {
        if (object == null) {
            if (label != null) {
                throw new IllegalArgumentException(label + MUST_BE_SPECIFIED);
            }

            return null; // NOSONAR Null is allowed
        }

        if (object instanceof Integer) {
            return (Integer)object;
        }

        if (object instanceof Number) {
            return Integer.valueOf(((Number)object).intValue());
        }

        if (object instanceof String && StringUtils.isNumeric(StringUtils.trim((String)object))) {
            return Integer.valueOf(StringUtils.trim((String)object));
        }

        if (label == null) {
            return null; // NOSONAR Null is allowed
        }

        throw new IllegalArgumentException(label + IS_NOT_AN_INTEGER_VALUE);
    }

    /**
     * Gets the integers.
     * @param value the value
     * @return the integers
     */
    @SuppressWarnings("rawtypes")
    public int[] toIntegers(final Object value) {
        if (value == null) {
            return null; // NOSONAR Must return null
        }

        int[] result = null;

        if (value instanceof Number) {
            result = new int[] {
                    ((Number)value).intValue()
            };
        }
        else if (value instanceof Collection) {
            final Collection collection = (Collection)value;
            result = new int[collection.size()];
            int i = 0;

            for (final Object item : collection) {
                result[i++] = toInteger(item);
            }
        }
        else if (value.getClass().isArray() && (value.getClass().getComponentType().equals(Number.class) || value.getClass().getComponentType().equals(Long.class) || value.getClass().getComponentType().equals(Integer.class) || value.getClass().getComponentType().equals(Byte.class) || value.getClass().getComponentType().equals(Short.class) || value.getClass().getComponentType().equals(Float.class) || value.getClass().getComponentType().equals(Double.class))) {
            result = new int[Array.getLength(value)];

            for (int i = 0; i < result.length; i++) {
                result[i] = toInteger(Array.get(value, i));
            }
        }
        else if (value instanceof String) {
            final String[] items = StringUtils.split((String)value, SEPARATOR_CHAR);
            result = new int[items.length];

            for (int i = 0; i < items.length; i++) {
                result[i] = toInteger(items[i]);
            }
        }

        return result;
    }

    /**
     * To long object.
     * @param object the value
     * @return true, if successful
     */
    public long toLong(final Object object) {
        return toLong(null, object);
    }

    /**
     * To long object.
     * @param label the label, assuming the value is required
     * @param object the value
     * @return true, if successful
     */
    public long toLong(final String label, final Object object) {
        if (object == null) {
            if (label != null) {
                throw new IllegalArgumentException(label + MUST_BE_SPECIFIED);
            }

            return 0;
        }

        if (object instanceof Number) {
            return ((Number)object).longValue();
        }

        if (object instanceof String) {
            return Long.parseLong(StringUtils.trim((String)object));
        }

        if (label == null) {
            return 0;
        }

        throw new IllegalArgumentException(label + IS_NOT_A_LONG_INTEGER_VALUE);
    }

    /**
     * To long object.
     * @param object the object
     * @return true, if successful
     */
    public Long toLongObject(final Object object) {
        return toLongObject(null, object);
    }

    /**
     * To long object.
     * @param label the label, assuming the value is required
     * @param object the value
     * @return true, if successful
     */
    public Long toLongObject(final String label, final Object object) {
        if (object == null) {
            if (label != null) {
                throw new IllegalArgumentException(label + MUST_BE_SPECIFIED);
            }

            return null; // NOSONAR Null is allowed
        }

        if (object instanceof Long) {
            return (Long)object;
        }

        if (object instanceof Number) {
            return Long.valueOf(((Number)object).longValue());
        }

        if (object instanceof String) {
            return Long.valueOf(StringUtils.trim((String)object));
        }

        if (label == null) {
            return null; // NOSONAR Null is allowed
        }

        throw new IllegalArgumentException(label + IS_NOT_A_LONG_INTEGER_VALUE);
    }

    /**
     * Gets the longs.
     * @param value the value
     * @return the longs
     */
    @SuppressWarnings("rawtypes")
    public long[] toLongs(final Object value) {
        if (value == null) {
            return null; // NOSONAR Must return null
        }

        long[] result = null;

        if (value instanceof Number) {
            result = new long[] {
                    ((Number)value).longValue()
            };
        }
        else if (value instanceof Collection) {
            final Collection collection = (Collection)value;
            result = new long[collection.size()];
            int i = 0;

            for (final Object item : collection) {
                result[i++] = toLong(item);
            }
        }
        else if (value.getClass().isArray() && (value.getClass().getComponentType().equals(Number.class) || value.getClass().getComponentType().equals(Long.class) || value.getClass().getComponentType().equals(Integer.class) || value.getClass().getComponentType().equals(Byte.class) || value.getClass().getComponentType().equals(Short.class) || value.getClass().getComponentType().equals(Float.class) || value.getClass().getComponentType().equals(Double.class))) {
            result = new long[Array.getLength(value)];

            for (int i = 0; i < result.length; i++) {
                result[i] = toLong(Array.get(value, i));
            }
        }
        else if (value instanceof String) {
            final String[] items = StringUtils.split((String)value, SEPARATOR_CHAR);
            result = new long[items.length];

            for (int i = 0; i < items.length; i++) {
                result[i] = toLong(items[i]);
            }
        }

        return result;
    }

    /**
     * To short.
     * @param object the value
     * @return true, if successful
     */
    public short toShort(final Object object) {
        return toShort(null, object);
    }

    /**
     * To short.
     * @param label the label, assuming the value is required
     * @param object the value
     * @return true, if successful
     */
    public short toShort(final String label, final Object object) {
        if (object == null) {
            if (label != null) {
                throw new IllegalArgumentException(label + MUST_BE_SPECIFIED);
            }

            return 0;
        }

        if (object instanceof Short) {
            return ((Short)object).byteValue();
        }

        if (object instanceof String) {
            return Short.parseShort(StringUtils.trim((String)object));
        }

        if (label == null) {
            return 0;
        }

        throw new IllegalArgumentException(label + IS_NOT_A_SHORT_INTEGER_VALUE);
    }

    /**
     * Gets the short.
     * @param object the value
     * @return the short
     */
    public Short toShortObject(final Object object) {
        return toShortObject(null, object);
    }

    /**
     * Gets the short.
     * @param label the label, assuming the value is required
     * @param object the value
     * @return the short
     */
    public Short toShortObject(final String label, final Object object) {
        if (object == null) {
            if (label != null) {
                throw new IllegalArgumentException(label + MUST_BE_SPECIFIED);
            }

            return null; // NOSONAR Null is allowed
        }

        if (object instanceof Short) {
            return (Short)object;
        }

        if (object instanceof String) {
            return Short.valueOf(StringUtils.trim((String)object));
        }

        if (object instanceof Number) {
            return Short.valueOf(((Number)object).shortValue());
        }

        if (label == null) {
            return null; // NOSONAR Null is allowed
        }

        throw new IllegalArgumentException(label + IS_NOT_A_SHORT_INTEGER_VALUE);
    }

    /**
     * Gets the shorts.
     * @param value the value
     * @return the shorts
     */
    @SuppressWarnings("rawtypes")
    public short[] toShorts(final Object value) {
        if (value == null) {
            return null; // NOSONAR Must return null
        }

        short[] result = null;

        if (value instanceof Number) {
            result = new short[] {
                    ((Number)value).shortValue()
            };
        }
        else if (value instanceof Collection) {
            final Collection collection = (Collection)value;
            result = new short[collection.size()];
            int i = 0;

            for (final Object item : collection) {
                result[i++] = toShort(item);
            }
        }
        else if (value.getClass().isArray() && (value.getClass().getComponentType().equals(Number.class) || value.getClass().getComponentType().equals(Long.class) || value.getClass().getComponentType().equals(Integer.class) || value.getClass().getComponentType().equals(Byte.class) || value.getClass().getComponentType().equals(Short.class) || value.getClass().getComponentType().equals(Float.class) || value.getClass().getComponentType().equals(Double.class))) {
            result = new short[Array.getLength(value)];

            for (int i = 0; i < result.length; i++) {
                result[i] = toShort(Array.get(value, i));
            }
        }
        else if (value instanceof String) {
            final String[] items = StringUtils.split((String)value, SEPARATOR_CHAR);
            result = new short[items.length];

            for (int i = 0; i < items.length; i++) {
                result[i] = toShort(items[i]);
            }
        }

        return result;
    }

    /**
     * Gets the string.
     * @param object the value
     * @return the string
     */
    public String toString(final Object object) {
        return toString(null, object);
    }

    /**
     * Gets the string.
     * @param label the label, assuming the value is required
     * @param object the value
     * @return the string
     */
    public String toString(final String label, final Object object) {
        if (object == null) {
            if (label != null) {
                throw new IllegalArgumentException(label + MUST_BE_SPECIFIED);
            }

            return null; // NOSONAR Null is allowed
        }

        if (object instanceof String) {
            return (String)object;
        }
        else {
            final StringBuilder buffer = new StringBuilder();

            org.infodavid.util.StringUtils.getInstance().toString(object, buffer);

            return buffer.toString();
        }
    }
}
