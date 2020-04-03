package where;

public enum Operation {
    GREAT_THAN{
        @Override
        public String toString() {
            return ">";
        }
    },

    LESS_THAN{
        @Override
        public String toString() {
            return "<";
        }
    },

    NOT_EQUAL{
        @Override
        public String toString() {
            return "<>";
        }
    },

    EQUAL{
        @Override
        public String toString() {
            return "=";
        }
    }






}
