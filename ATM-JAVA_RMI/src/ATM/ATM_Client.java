package ATM;
import java.io.*;

import java.rmi.*;
import java.net.*;
import java.util.Scanner;
//import java.sql.Date;
import java.text.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ATM_Client
{
    public static void main(String args[])
    {
        try
            {
               // while(true)
               // {
                	Scanner s=new Scanner(System.in);
                    String url="rmi://localhost:5000/atm";
                    ATM_Interface intf=(ATM_Interface)Naming.lookup(url);
                    int amount;
                    boolean verified=true;
                    String acc_no="";
                   while(verified) {
                	   System.out.println("Enter Account Number: ");
                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                     acc_no=br.readLine();
                     System.out.println("Enter PIN: ");
                    int pin = Integer.parseInt(s.next());
                    boolean check = intf.login(acc_no, pin);
                    if (check == true) {
                    	verified = false;
                    	System.out.println("Logged in! ");
                    }
 
                   }
                   while(true)
                   {
                    
                    System.out.println("1. : Deposit Money"+"\n"+
                        "2. : Withdraw Money"+"\n"+
                        "3. : Check Balance"+"\n"+
                        "4. : Check Transaction History"+"\n"+
                        "5. : To Quit"+"\n"+
                        "6. : To change pin \n");
                   
                    int no=Integer.parseInt(s.next());
                    //Integer res;
                    switch(no){
                        case 1:
                            System.out.println("Enter Amount to be deposited :");                            
                            int num=s.nextInt();
                            
                            intf.deposit(num,acc_no);
                           
                        break;

                        case 2:
                            System.out.println("Enter Amount to be withdrawn :");                            
                            BufferedReader get_amount=new BufferedReader(new InputStreamReader(System.in));
                            amount=Integer.parseInt(get_amount.readLine());
                            int bal_left2 = intf.display_balance(acc_no);
                            
                           
                            if(bal_left2>= amount)
                            {
                                intf.withdraw(amount,acc_no);
                                System.out.println("Withdrawn completed"+"\n");
                              
                            }
                            else 
                            {
                                System.out.println("Insufficient balance."+"\n");
                            }
                           
                

                        break;
                        
                        case 3:
                            Integer bal_left = intf.display_balance(acc_no);
                            if(bal_left>-1)
                            {
                                System.out.println("Balance left in account number "+acc_no+" is : "+bal_left+"\n");
                            }
                            else
                            {
                                System.out.println("Account does not exist."+"\n");
                            }
                        break;

                        case 4:
                            System.out.println("Enter 1 to view all transactions and 2 to view transactions within a range :");                            
                            BufferedReader get_choice=new BufferedReader(new InputStreamReader(System.in));
                            int choice=Integer.parseInt(get_choice.readLine());
                            DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
                            String output ="";
                            if(choice == 1){
                                output = intf.transaction_details1(acc_no);
                            }
                            else if(choice==2){
                                System.out.println("Enter Start Date (Eg. 2020-12-26): ");
                                s=new Scanner(System.in);
                                String start_date=s.next();
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                Date parsed = format.parse(start_date);
                                java.sql.Date sqldate = new java.sql.Date(parsed.getTime());
                                
                                
                                System.out.println("Enter End Date: ");
                                s=new Scanner(System.in);
                                String end_date=s.next();
                                 parsed = format.parse(end_date);
                                java.sql.Date sqldate2 = new java.sql.Date(parsed.getTime());
                               
                                
                                output = intf.transaction_details2(acc_no,sqldate,sqldate2);    
                            }
                            System.out.println(output);
                            break;

                        case 5:
                            return;
                        case 6:
                        	boolean same= true;
                        	while(same) {
                        	 System.out.println("Enter New pin: ");
                        	Integer new_pin = Integer.parseInt(s.next()) ;
                        	
                        	String checkdigits = new_pin.toString();
                        	//System.out.println(checkdigits.length() == 4);
                        	if(checkdigits.length()== 4) {
                        		System.out.println("Re-enter New pin: ");
                        		Integer renew_pin = Integer.parseInt(s.next()) ;
                        		String checkdigits2 = renew_pin.toString();
                        		
                        		if (checkdigits.equals(checkdigits2)) {
                        			same = false;
                        			intf.change_pin(acc_no, new_pin);
                        			System.out.println("Pin changed!");
                        		}
                        	}
                        	}
                        	
                        	break;
                        default:
                            System.out.println("Entered option does not exist!");
                            break;
                    }
                }
            }
            catch(Exception ex)
            {
                System.out.println("Error: "+ex);
            }
    }
}
