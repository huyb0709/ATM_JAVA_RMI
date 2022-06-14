package ATM;
import java.rmi.*;
import java.sql.SQLException;
import java.sql.Date;

public interface ATM_Interface extends Remote
{
    public void deposit(int x,String y) throws RemoteException, SQLException;
    public void withdraw(int x,String y) throws RemoteException, SQLException;
    public int display_balance(String y) throws RemoteException, SQLException;
    public String transaction_details1( String y) throws RemoteException, SQLException;
    public String transaction_details2(String y, Date start_date, Date end_date) throws RemoteException, SQLException;
    public boolean login(String acc,int pin) throws RemoteException,SQLException;
    public void change_pin(String acc,int pin) throws RemoteException,SQLException;
}