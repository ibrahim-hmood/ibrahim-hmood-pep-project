package DAO;

import java.sql.*;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO 
{
    /*
     * getAccountByUserID: Gets account data by user ID
     * @param userID User ID we are looking for
     * @returns Account if user ID was found, null otherwise 
     */
    public Account getAccountByUserID(int userId)
    {
        //Try to get user account by username
            //First, connect to the database
        Connection connection = ConnectionUtil.getConnection();
            //Avoid any SQL errors
        try
        {
            //Then, create an SQL for finding the user data
            String SQL = "SELECT * FROM account WHERE `account_id` = ?";
            //Now pass the SQL into a prepared statement
            PreparedStatement statement = connection.prepareStatement(SQL);
            //And pass the username to it
            statement.setInt(1, userId);
            //Then execute the query
            ResultSet result = statement.executeQuery();
            //And get the first result
            while(result.next())
            {
                //Now, get the account ID, and password
                int accountID = result.getInt("account_id");
                String username = result.getString("username");
                String password = result.getString("password");
                //And pass them to a new account
                return new Account(accountID, username, password);
            }
        }
        catch(SQLException error)
        {
            //An error was encountered
            error.printStackTrace();
        }
            //Return nothing by default
        return null;
    }

    /*
     * getAccountByUsername: Gets account data by username
     * @param username Username we are looking for
     * @returns Account if username was found, null otherwise
     */
    public Account getAccountByUsername(String username)
    {
        //Try to get user account by username
            //First, connect to the database
        Connection connection = ConnectionUtil.getConnection();
            //Avoid any SQL errors
        try
        {
            //Then, create an SQL for finding the user data
            String SQL = "SELECT * FROM account WHERE `username` = ?";
            //Now pass the SQL into a prepared statement
            PreparedStatement statement = connection.prepareStatement(SQL);
            //And pass the username to it
            statement.setString(1, username);
            //Then execute the query
            ResultSet result = statement.executeQuery();
            //And get the first result
            while(result.next())
            {
                //Now, get the account ID and password
                int accountID = result.getInt("account_id");
                String password = result.getString("password");
                //And pass them to a new account
                return new Account(accountID, username, password);
            }
        }
        catch(SQLException error)
        {
            //An error was encountered
            error.printStackTrace();
        }
            //Return nothing by default
        return null;
    }

    /*
     * addAccount: adds an account to account table
     * @param username Username that we want to add
     * @param password Password of new account
     * @returns Account based on username and password
     */
    public Account addAccount(String username, String password)
    {
        //Try to add the user
            //First, connect to the database
        Connection connection = ConnectionUtil.getConnection();
            //Avoid any SQL errors
        try
        {
            //Then, create an SQL string for inserting the username and password
            String SQL = "INSERT INTO account (`username`, `password`) VALUES(?, ?);";
            //Now pass the sql to the prepared statement
            PreparedStatement statement = connection.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
            //And pass the username and password to the prepared statement
            statement.setString(1, username);
            statement.setString(2, password);
            //After that, execute the update
            statement.executeUpdate();
            //Now try to get the generated account ID
            ResultSet generatedData = statement.getGeneratedKeys();
            //Check if one was generated
            if(generatedData.next())
            {
                //Get the account ID
                int accountID = generatedData.getInt(1);
                //And return the new account
                return new Account(accountID, username, password);
            }
        }
        catch(SQLException exception)
        {
            //SQL error was encountered
            exception.printStackTrace();
        }

        //Return nothing
        return null;
    }
}
