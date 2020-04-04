import org.junit.Test;
import parser.Converter;

import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void simpleTest() {
        String sql = "SELECT * FROM sales LIMIT 10";
        String mongoDB = "db.sales.find({}).limit(10)";
        try {
            Converter converter = new Converter(sql);
            assertEquals(mongoDB, converter.convertToMongoDB());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        sql = "SELECT name, surname FROM [collection]";
        mongoDB = "db.collection.find({}, {name: 1, surname: 1})";
        try {
            Converter converter = new Converter(sql);
            assertEquals(mongoDB, converter.convertToMongoDB());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        sql = "SELECT * FROM customers WHERE age > 22 AND name = 'Vasya'";
        mongoDB = "db.customers.find({age: {$gt: 22}, name: {$eq: 'Vasya'}})";
        try {
            Converter converter = new Converter(sql);
            assertEquals(mongoDB, converter.convertToMongoDB());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        sql = "SELECT * FROM collection  LIMIT 10 OFFSET 5";
        mongoDB = "db.collection.find({}).skip(5).limit(10)";
        try {
            Converter converter = new Converter(sql);
            assertEquals(mongoDB, converter.convertToMongoDB());
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
            Converter converter = new Converter(sql);
            assertEquals(mongoDB, converter.convertToMongoDB());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        sql = "SELECT name, \"FROM\" FROM collection";
        mongoDB = "db.collection.find({}, {name: 1, FROM: 1})";
        try {
            Converter converter = new Converter(sql);
            assertEquals(mongoDB, converter.convertToMongoDB());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        sql = "SELECT name, 'FROM' FROM collection";
        mongoDB = "db.collection.find({}, {name: 1, FROM: 1})";
        try {
            Converter converter = new Converter(sql);
            assertEquals(mongoDB, converter.convertToMongoDB());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }


        sql = "SELECT name_surname_2 FROM collection";
        mongoDB = "db.collection.find({}, {name_surname_2: 1})";
        try {
            Converter converter = new Converter(sql);
            assertEquals(mongoDB, converter.convertToMongoDB());
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
            Converter converter = new Converter(sql);
            converter.convertToMongoDB();
        } catch (Exception ignored) {
            wasException = true;
        }
        if (!wasException) {
            fail();
        }

        sql = "SELECT name,  FROM collection";
        wasException = false;
        try {
            Converter converter = new Converter(sql);
            converter.convertToMongoDB();
        } catch (Exception ignored) {
            wasException = true;
        }
        if (!wasException) {
            fail();
        }

        sql = "SELECT name  FROM collection WHERE age > 2 AND age > 3 AND LIMIT 10";
        wasException = false;
        try {
            Converter converter = new Converter(sql);
            converter.convertToMongoDB();
        } catch (Exception ignored) {
            wasException = true;
        }
        if (!wasException) {
            fail();
        }


        sql = "SELECT name  FROM collection WHERE age > 2 AND age > 3 LIMIT 1a0";
        wasException = false;
        try {
            Converter converter = new Converter(sql);
            converter.convertToMongoDB();
        } catch (Exception ignored) {
            wasException = true;
        }
        if (!wasException) {
            fail();
        }
    }

    @Test
    public void wrapperTest(){
        String sql = "SELECT [SELECT], [from], 'FROM' ,'select', \"SELECT\", \"FROM\"   FROM [from]";
        String mongoDB = "db.from.find({}, {SELECT: 1, from: 1, FROM: 1, select: 1, SELECT: 1, FROM: 1})";
        try {
            Converter converter = new Converter(sql);
            assertEquals(mongoDB, converter.convertToMongoDB());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        sql = "SELECT [SELECT], name, 'FROM' ,'LIMIT', \"SELECT\", \"OFFSET\"   FROM [from] WHERE time > 20 LIMIT 10 OFFSET 2";
        mongoDB = "db.from.find({time: {$gt: 20}}, {SELECT: 1, name: 1, FROM: 1, LIMIT: 1, SELECT: 1, OFFSET: 1}).skip(2).limit(10)";
        try {
            Converter converter = new Converter(sql);
            assertEquals(mongoDB, converter.convertToMongoDB());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void  whereOperationTest(){
        String sql = "SELECT name FROM collection WHERE age > 22 AND sex <> MEN AND weight = 80 AND height < 200 AND name = 'Vasya' LIMIT 10 OFFSET 5";
        String mongoDB = "db.collection.find({age: {$gt: 22}, sex: {$ne: MEN}, weight: {$eq: 80}, height: {$lt: 200}, name: {$eq: 'Vasya'}}, {name: 1}).skip(5).limit(10)";
        try {
            Converter converter = new Converter(sql);
            assertEquals(mongoDB, converter.convertToMongoDB());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

    }

    @Test
    public  void selectOperationTest(){
        String sql = "SELECT name, name2, nick_name_2, surname FROM collection WHERE age > 22 AND sex <> MEN  LIMIT 10 OFFSET 5";
        String mongoDB = "db.collection.find({age: {$gt: 22}, sex: {$ne: MEN}}, {name: 1, name2: 1, nick_name_2: 1, surname: 1}).skip(5).limit(10)";
        try {
            Converter converter = new Converter(sql);
            assertEquals(mongoDB, converter.convertToMongoDB());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }




}
