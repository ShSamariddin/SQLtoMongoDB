import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void simpleTest() {
        String sql = "SELECT * FROM sales LIMIT 10";
        String mongoDB = "db.sales.find({}).limit(10)";
        try {
            Converter converter = new Converter();
            assertEquals(mongoDB, converter.convertToMongoDB(sql));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        sql = "SELECT name, surname FROM [collection]";
        mongoDB = "db.collection.find({}, {name: 1, surname: 1})";
        try {
            Converter converter = new Converter();
            assertEquals(mongoDB, converter.convertToMongoDB(sql));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        sql = "SELECT * FROM customers WHERE age > 22 AND name = 'Vasya'";
        mongoDB = "db.customers.find({age: {$gt: 22}, name: 'Vasya'})";
        try {
            Converter converter = new Converter();
            assertEquals(mongoDB, converter.convertToMongoDB(sql));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        sql = "SELECT * FROM collection SKIP 5 LIMIT 10";
        mongoDB = "db.collection.find({}).skip(5).limit(10)";
        try {
            Converter converter = new Converter();
            assertEquals(mongoDB, converter.convertToMongoDB(sql));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }


    @Test
    public void hardTest() {
        String sql = "SELECT name, [FROM] FROM collection";
        String mongoDB = "db.collection.find({}, {name: 1, FROM: 1})";
        try {
            Converter converter = new Converter();
            assertEquals(mongoDB, converter.convertToMongoDB(sql));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        sql = "SELECT name, \"FROM\" FROM collection";
        mongoDB = "db.collection.find({}, {name: 1, FROM: 1})";
        try {
            Converter converter = new Converter();
            assertEquals(mongoDB, converter.convertToMongoDB(sql));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        sql = "SELECT name, 'FROM' FROM collection";
        mongoDB = "db.collection.find({}, {name: 1, FROM: 1})";
        try {
            Converter converter = new Converter();
            assertEquals(mongoDB, converter.convertToMongoDB(sql));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }


        sql = "SELECT name_surname_2 FROM collection";
        mongoDB = "db.collection.find({}, {name_surname_2: 1})";
        try {
            Converter converter = new Converter();
            assertEquals(mongoDB, converter.convertToMongoDB(sql));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }


    }

    @Test
    public void exceptionTest() {
        String sql = "SELECT * ? FROM collection";
        boolean wasException = false;
        try {
            Converter converter = new Converter();
            converter.convertToMongoDB(sql);
        } catch (Exception ignored) {
            wasException = true;
        }
        if (!wasException) {
            fail();
        }

        sql = "SELECT name,  FROM collection";
        wasException = false;
        try {
            Converter converter = new Converter();
            converter.convertToMongoDB(sql);
        } catch (Exception ignored) {
            wasException = true;
        }
        if (!wasException) {
            fail();
        }

        sql = "SELECT name  FROM collection WHERE age > 2 AND age > 3 AND LIMIT 10";
        wasException = false;
        try {
            Converter converter = new Converter();
            converter.convertToMongoDB(sql);
        } catch (Exception ignored) {
            wasException = true;
        }
        if (!wasException) {
            fail();
        }


        sql = "SELECT name  FROM collection WHERE age > 2 AND age > 3 LIMIT 1a0";
        wasException = false;
        try {
            Converter converter = new Converter();
            converter.convertToMongoDB(sql);
        } catch (Exception ignored) {
            wasException = true;
        }
        if (!wasException) {
            fail();
        }
    }


}
