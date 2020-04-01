import Solution.Tree;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ParserTest {

    @Test
    public void simpleTest(){
        String sql = "SELECT * FROM sales LIMIT 10";
        String mongoDB = "db.sales.find({}).limit(10)";
        try {
            Tree tree = new Tree();
            String str = new String(tree.sol(sql));
            assertEquals(mongoDB, tree.sol(sql));
        } catch (Exception  e){
            e.printStackTrace();
        }

        sql = "SELECT name, surname FROM collection";
        mongoDB = "db.collection.find({}, {name: 1, surname: 1})";
        try {
            Tree tree = new Tree();
            String str = new String(tree.sol(sql));
            assertEquals(mongoDB, tree.sol(sql));
        } catch (Exception  e){
            e.printStackTrace();
        }

        sql = "SELECT * FROM customers WHERE age > 22 AND name = 'Vasya'";
        mongoDB = "db.customers.find({age: {$gt: 22}, name: 'Vasya'})";
        try {
            Tree tree = new Tree();
            String str = tree.sol(sql);
            System.out.println(str);
            assertEquals(mongoDB, tree.sol(sql));
        } catch (Exception  e){
            e.printStackTrace();
        }

        sql = "SELECT * FROM collection SKIP 5 LIMIT 10";
        mongoDB = "db.collection.find({}).skip(5).limit(10)";
        try {
            Tree tree = new Tree();
            String str = new String(tree.sol(sql));
            assertEquals(mongoDB, tree.sol(sql));
        } catch (Exception  e){
            e.printStackTrace();
        }
    }

}
