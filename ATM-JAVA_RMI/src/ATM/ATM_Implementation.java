package ATM;
import java.rmi.*;

import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;




public class ATM_Implementation extends UnicastRemoteObject implements ATM_Interface
{
	
	private static final long serialVersionUID = 1L;
	// JDBC driver name and database URL 
    String JDBC_DRIVER = "com.mysql.jdbc.Driver";   
    String DB_URL = "jdbc:mysql://localhost:3306/atm_db";  
    // Database credentials
    String USER = "root"; 
    String PASS = ""; 
    
    
    Connection conn = null; 
    Statement stmt = null;  
    
    
    public Connection getConnection() throws  SQLException, ClassNotFoundException {
    	Class.forName("com.mysql.jdbc.Driver");
    	conn = DriverManager.getConnection(DB_URL, USER, PASS);
        return conn;
     }

	protected ATM_Implementation() throws RemoteException {
	 
	}

	
    
  //Register JDBC driver 
    ;

    public void deposit(int x,String y) throws RemoteException, SQLException
    {
    	
    	 try {
			conn = getConnection();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	PreparedStatement prepstmt =conn.prepareStatement("UPDATE account_info SET balance= balance + ? WHERE account_no =? ");
    	prepstmt.setInt(1,x);
    	prepstmt.setString(2,y);
    	prepstmt.executeUpdate();
    	
    	LocalDate localDate = LocalDate.now();
    	java.util.Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    	 java.sql.Date sqlDate = new java.sql.Date(date.getTime());
    	 String query = " insert into transactions (account_no,type,amount,date)"
 		        + " values (?,'deposit',?,?)";
    	PreparedStatement prepstmt2 =conn.prepareStatement(query);
    	prepstmt2.setString(1, y);
    	prepstmt2.setInt(2, x);
    	prepstmt2.setDate(3, sqlDate);
    	prepstmt2.executeUpdate();
    	

    }

    public void withdraw(int x,String y) throws RemoteException, SQLException
    {
    	try {
			conn = getConnection();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	PreparedStatement prepstmt =conn.prepareStatement("UPDATE account_info SET balance= balance - ? WHERE account_no =? ");
    	prepstmt.setInt(1,x);
    	prepstmt.setString(2,y);
    	prepstmt.executeUpdate();
    	
    	LocalDate localDate = LocalDate.now();
    	java.util.Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    	 java.sql.Date sqlDate = new java.sql.Date(date.getTime());
    	
    	 String query = " insert into transactions (account_no,type,amount,date)"
    		        + " values (?,'withdraw',?,?)";
    	PreparedStatement prepstmt2 =conn.prepareStatement(query);
    	prepstmt2.setString(1, y);
    	prepstmt2.setInt(2, x);
    	prepstmt2.setDate(3, sqlDate);
    	prepstmt2.executeUpdate();
    	
    }

    public int display_balance(String y) throws RemoteException, SQLException
    {
    	 try {
			conn = getConnection();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	int balance=0;
    	PreparedStatement prepstmt =conn.prepareStatement("SELECT balance FROM account_info WHERE account_no=? ");
    	prepstmt.setString(1,y);
    	ResultSet rs = prepstmt.executeQuery();  
    	 while(rs.next()) { 
             // Retrieve by column name 
             balance  = rs.getInt("balance");
            		 }
    	 rs.close();
    	 return balance;
    }
    	

    public String transaction_details1( String y) throws RemoteException, SQLException
    {
    	 try {
			conn = getConnection();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	String result="";
    	String type=null;
    	int amount=0;
    	Date dt=null;
    	PreparedStatement prepstmt =conn.prepareStatement("SELECT * FROM transactions WHERE account_no=? ");
    	prepstmt.setString(1,y); 
        ResultSet rs = prepstmt.executeQuery();  
        while(rs.next()) { 
            // Retrieve by column name 
            type  = rs.getString("type");
            amount = rs.getInt("amount");
            dt = rs.getDate("date");
            result += type + "\t" + amount + "\t" + dt.toString() + "\n" ;
           		 }
   	 rs.close();
   	 
      return result;
    }

	public String transaction_details2(String y, Date start_date, Date end_date)
			throws RemoteException, SQLException{
		 try {
			conn = getConnection();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String result="";
    	String type=null;
    	int amount=0;
    	Date dt=null;
    	PreparedStatement prepstmt =conn.prepareStatement("SELECT * FROM transactions WHERE account_no=? AND date>=? AND date<=? ");
    	prepstmt.setString(1,y);
    	prepstmt.setDate(2,(Date) start_date); 
    	prepstmt.setDate(3,(Date) end_date); 
        ResultSet rs = prepstmt.executeQuery();  
        while(rs.next()) { 
            // Retrieve by column name 
            type  = rs.getString("type");
            amount = rs.getInt("amount");
            dt = rs.getDate("date");
            result += type + "\t" + amount + "\t" + dt.toString() + "\n" ;
           		 }
   	 rs.close();
   	 
      return result;
	}

	public boolean login(String acc,int pin) throws RemoteException,SQLException{
		 int stored_pin= 00000000000000000000000;
		try {
				conn = getConnection();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 PreparedStatement prepstmt =conn.prepareStatement("SELECT * FROM users WHERE account_no= ?");
		 prepstmt.setString(1,acc);
		 ResultSet rs = prepstmt.executeQuery(); 
		 while(rs.next()) { 
	            // Retrieve by column name 
	            stored_pin = rs.getInt("pin");
	           
	           		 }
	   	 rs.close();
	   	if(stored_pin != 00000000000000000000000) { 
		if (stored_pin == pin) {
			return true;
		}}
		
		return false;
	}
	
	public void change_pin(String acc,int pin) throws RemoteException,SQLException{
		try {
			conn = getConnection();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PreparedStatement prepstmt =conn.prepareStatement("UPDATE users SET pin= ? WHERE account_no =? ");
    	prepstmt.setInt(1,pin);
    	prepstmt.setString(2,acc);
    	prepstmt.executeUpdate();
		
		
	}



}