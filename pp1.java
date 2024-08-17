package a5;

import java.sql.*;
import java.io.*;

public class pp1 {
    public static void main(String args[]) {
        Connection con = null;
        Statement stmt = null;
        try {
            String conurl = "jdbc:oracle:thin:@10.190.34.184:1521:xe";
            con = DriverManager.getConnection(conurl, "sys as sysdba", "123456");
            stmt = con.createStatement();
            // creationOrDrop(stmt);

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            int choice_variable;
            // String sqlCmd = "insert into depositor values('C0016', 'A0005')";
            // stmt.executeQuery(sqlCmd);
            do {
                System.out.println("***** Banking Management System *****");
                System.out.println("1. Show Customer Records");
                System.out.println("2. Add Customer Record");
                System.out.println("3. Delete Customer Record");
                System.out.println("4. Update Customer Information");
                System.out.println("5. Show Account Details of a Customer");
                System.out.println("6. Show Loan Details of a Customer");
                System.out.println("7. Deposit Money to an Account");
                System.out.println("8. Withdraw Money from an Account");
                System.out.println("9. Exit the Program");
                System.out.println("Enter your choice(1-9):");

                choice_variable = Integer.parseInt(reader.readLine());

                switch (choice_variable) {
                    case 1:
                        showCustomerRecords(stmt);
                        break;
                    case 2:
                        addCustomerRecord(con, stmt, reader);
                        break;
                    case 3:
                        deleteCustomerRecord(con, stmt, reader);
                        break;
                    case 4:
                        updateCustomerInformation(con, stmt, reader);
                        break;
                    case 5:
                        showAccountDetails(con, stmt, reader);
                        break;
                    case 6:
                        showLoanDetails(con, stmt, reader);
                        break;
                    case 7:
                        depositMoney(con, stmt, reader);
                        break;
                    case 8:
                        withdrawMoney(con, stmt, reader);
                        break;
                    case 9:
                        System.out.println("Exiting the program...");
                        break;
                    default:
                        System.out.println("Invalid choice! Please enter a number between 1 and 9.");
                }
            } while (choice_variable != 9);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
                if (con != null)
                    con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    static void creationOrDrop(Statement stmt) throws SQLException{
        String sqlCust = "create table customer(cust_no varchar(10) primary key,name varchar(30) not null, phone_no int,city varchar(30) not null)";
        String sqlAcc = "create table account(account_no varchar(10) primary key,type varchar(30) check(type in ('SB','CA','FD')), balance int check(balance < 10000000))";
        String sqlDepo = "create table depositor(cust_no varchar(10) references customer(cust_no),account_no varchar(9) references account(account_no))";
        String sqlLoan = "create table loan(loan_no varchar(10) primary key,cust_no varchar(10) references customer(cust_no),amount int check(amount > 1000))";
        stmt.executeUpdate(sqlCust);
        stmt.executeUpdate(sqlAcc);
        stmt.executeUpdate(sqlDepo);
        stmt.executeUpdate(sqlLoan);
        drop(stmt, "EMPLOYEE");
    }
    static void drop(Statement stmt,String tableName) throws SQLException{
        String sqlDrop = "drop table "+tableName;
        stmt.executeQuery(sqlDrop);
    }
    private static void showCustomerRecords(Statement stmt) throws SQLException {
        String QUERY = "SELECT * FROM Customer";
        ResultSet rs = stmt.executeQuery(QUERY);
        while(rs.next()){
            System.out.print("Customer Number: " + rs.getString("cust_no"));
            System.out.print(", Name: " + rs.getString("name"));
            System.out.print(", Phone Number: " + rs.getLong("phone_no"));
            System.out.println(", City: " + rs.getString("city"));
        }
    }
    

    private static void addCustomerRecord(Connection con, Statement stmt, BufferedReader reader) throws IOException, SQLException {
        System.out.println("Enter the Customer Numer/Id: ");
        String cust_no = String.valueOf(reader.readLine());
        System.out.println("Enter the Customer Name: ");
        String name = String.valueOf(reader.readLine());
        System.out.println("Enter the Customer Phone Number: ");
        String phone_no = String.valueOf(reader.readLine());
        System.out.println("Enter the Customer City: ");
        String city = String.valueOf(reader.readLine());
        // System.out.println(name);
        String sqlIns = "INSERT INTO Customer VALUES ('"+cust_no+"', '"+name+"',"+phone_no+", '"+city+"')";
        stmt.executeUpdate(sqlIns);
        System.out.println("Inserted records into the table...");
    }

    private static void deleteCustomerRecord(Connection con, Statement stmt, BufferedReader reader) throws IOException, SQLException {
        System.out.println("Enter the Customer Number/Id to be deleted: ");
        String del = String.valueOf(reader.readLine());
        String sqlDepo = "Delete from Depositor where cust_no ='"+del+"'";
        String sqlDel = "Delete from Customer where cust_no ='"+del+"'";
        stmt.executeQuery(sqlDepo);
        stmt.executeQuery(sqlDel);
        System.out.println("Record deleted of Customer Numer/Id: "+del);
    }

    private static void updateCustomerInformation(Connection con, Statement stmt, BufferedReader reader) throws IOException, SQLException {
        System.out.println(("Enter the Customer Number/Id to be updated: "));
        String upd = String.valueOf(reader.readLine());
        System.out.println("Enter the Choice to be updated(1 -> Name, 2 -> Phone Number, 3 -> City): ");
        int choice = Integer.parseInt(reader.readLine());
        switch(choice){
            case 1:
                System.out.println("Enter the New Name: ");
                String name = String.valueOf(reader.readLine());
                String sqlUpd = "Update Customer set name = '"+name+"' where cust_no = '"+upd+"'";
                stmt.executeQuery(sqlUpd);
                System.out.println("Updated Name of Customer Numer/Id: "+upd);
                break;
            case 2:
                System.out.println("Enter the New Phone Number: ");
                String phone_no = String.valueOf(reader.readLine());
                String sqlUpd2 = "Update Customer set phone_no = '"+phone_no+"' where cust_no = '"+upd+"'";
                stmt.executeQuery(sqlUpd2);
                System.out.println("Updated Phone Number of Customer Numer/Id: "+upd);
                break;
            case 3:
                System.out.println("Enter the New City: ");
                String city = String.valueOf(reader.readLine());
                String sqlUpd3 = "Update Customer set city = '"+city+"' where cust_no = '"+upd+"'";
                stmt.executeQuery(sqlUpd3);
                System.out.println("Updated City of Customer Number/Id: "+upd);
                break;
            default:
                System.out.println("Try other option: ");
                System.out.println("Enter the Choice to be updated(1 -> Name, 2 -> Phone Number, 3 -> City): ");
                choice = Integer.parseInt(reader.readLine());
                break;
        }
    }

    private static void showAccountDetails(Connection con, Statement stmt, BufferedReader reader) throws IOException, SQLException {
        System.out.println(("Enter the Customer Number/Id to fetch account details: "));
        String upd = String.valueOf(reader.readLine());
        String QUERY = "SELECT * FROM account a JOIN depositor d ON a.account_no = d.account_no JOIN customer c ON d.cust_no = c.cust_no WHERE c.cust_no = '"+upd+"'";
        ResultSet rs = stmt.executeQuery(QUERY);
        while(rs.next()){
            System.out.print("Account Number: " + rs.getString("account_no"));
            System.out.print(", Type: " + rs.getString("type"));
            System.out.println(", Balance: " + rs.getLong("balance"));
        }
    }

    private static void showLoanDetails(Connection con, Statement stmt, BufferedReader reader) throws IOException, SQLException {
        System.out.println(("Enter the Customer Number/Id to fetch account details: "));
        String upd = String.valueOf(reader.readLine());
        String sqlLoan = "select * from Loan where cust_no ='"+upd+"'";
        ResultSet rs = stmt.executeQuery(sqlLoan);
        while(rs.next()){
            System.out.print("Loan Number: " + rs.getString("loan_no"));
            System.out.println(", Amount: " + rs.getString("amount"));
        }
    }

    private static void depositMoney(Connection con, Statement stmt, BufferedReader reader) throws IOException, SQLException {
        System.out.println(("Enter the Account Number/Id to depositMoney: "));
        String upd = String.valueOf(reader.readLine());
        String sqlLastBalance = "select balance from account where account_no = '"+upd+"'";
        ResultSet rs = stmt.executeQuery(sqlLastBalance);
        long lastBalance = 0;
        while(rs.next())
            lastBalance = rs.getLong("balance");
        System.out.println("Enter the deposit money: ");
        int money = Integer.parseInt(reader.readLine());
        lastBalance += money;
        String sqlDepositMoney = "update account set balance = "+lastBalance;
        stmt.executeQuery(sqlDepositMoney);
        System.out.println("Money Deposited.");
    }

    private static void withdrawMoney(Connection con, Statement stmt, BufferedReader reader) throws IOException, SQLException {
        System.out.println(("Enter the Account Number/Id to depositMoney: "));
        String upd = String.valueOf(reader.readLine());
        String sqlLastBalance = "select balance from account where account_no = '"+upd+"'";
        ResultSet rs = stmt.executeQuery(sqlLastBalance);
        long lastBalance = 0;
        while(rs.next())
            lastBalance = rs.getLong("balance");
        System.out.println("Enter the Withdraw money: ");
        int money = Integer.parseInt(reader.readLine());
        lastBalance -= money;
        String sqlDepositMoney = "update account set balance = "+lastBalance;
        stmt.executeQuery(sqlDepositMoney);
        System.out.println("Money Withdrawn.");
    }
}


