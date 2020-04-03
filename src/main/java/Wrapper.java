public enum Wrapper {
    SQUARE_BRACKETS {
        @Override
        public char openWrapper() {
            return '[';
        }

        @Override
        public char closeWrapper() {
            return ']';
        }
    },

    DOUBLE_QUOTES {
        @Override
        public char openWrapper() {
            return '\"';
        }

        @Override
        public char closeWrapper() {
            return '\"';
        }
    },
    SINGLE_QUOTES {
        @Override
        public char openWrapper() {
            return '\'';
        }

        @Override
        public char closeWrapper() {
            return '\'';
        }
    };

    public abstract char openWrapper();

    public abstract char closeWrapper();
}
