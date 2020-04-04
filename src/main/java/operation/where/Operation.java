package operation.where;

/**
 * Enum  Operation
 * <p>
 * Author: Sharipov Samariddin
 * <p>
 * <p>
 * Purpose: Класс перечисление, типов сравнение
 */

public enum Operation {
    GREAT_THAN {
        @Override
        public String toMongoDB() {
            return "$gt";
        }

        @Override
        public String toString() {
            return ">";
        }
    },

    LESS_THAN {
        @Override
        public String toMongoDB() {
            return "$lt";
        }

        @Override
        public String toString() {
            return "<";
        }
    },

    NOT_EQUAL {
        @Override
        public String toMongoDB() {
            return "$ne";
        }

        @Override
        public String toString() {
            return "<>";
        }
    },

    EQUAL {
        @Override
        public String toMongoDB() {
            return "$eq";
        }

        @Override
        public String toString() {
            return "=";
        }
    };

    public abstract String toMongoDB();

}
