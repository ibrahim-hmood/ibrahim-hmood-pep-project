package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService 
{
    //Account data access object
    private AccountDAO dao;

    /*
    AccountService: creates account data access object
    */
    public AccountService()
    {
        this.dao = new AccountDAO();
    }

    /*
     * AccountService: sets data access object to a determined one
     * @param dao New data access object for account
     */
    public AccountService(AccountDAO dao)
    {
        this.dao = dao;
    }

    /*
     * login: it checks if the username is not valid (exists on database) and if the password is not valid (is the password associated with a username).
     * If both conditions are met, nothing is returned. Otherwise, the account is returned.
     * @param username Name of user possibly logging in
     * @param password Password associated with username
     * @returns Account if username exists and password is its password, null otherwise
     */
    public Account login(String username, String password)
    {
        //Try to get the user's information using username and password
            //Try to get account information via username
        Account account = this.dao.getAccountByUsername(username);
            //Now create a boolean for checking if the username wasnt legitimate
        boolean illlegitimateUsername = account == null;
            //Check if the username was legitimate
        if((!illlegitimateUsername))
        {
                //And create a boolean for checking if the password wasnt legitimate
            boolean illlegitimatePassword = !account.getPassword().equals(password);
                //Avoid illegitimate passwords
            if(!illlegitimatePassword)
            {
                //Account data was found
                return account;
            }
        }
            //Return nothing
        return null;
    }

    /*
     * login: it checks if the username is not valid (exists on database) and if the password is not valid (is the password associated with a username).
     * If both conditions are met, nothing is returned. Otherwise, the account is returned.
     * @param account Account data being checked
     * @returns Account if username was found and password equals account password, null otherwise
     */
    public Account login(Account account)
    {
        //Try to get the user's information using username and password in account data
        return this.login(account.getUsername(), account.getPassword());
    }

    /*
     * register: it checks if username was added, is empty, or if the password length < 4. If either are true, nothing is returned.
     * Otherwise, just adds user to account table, and the account's data is returned
     * @param username Name of user being added
     * @param password Password to associate with user
     * @returns Account if user was added, nothing otherwise
     */
    public Account register(String username, String password)
    {
        //Try to sign the user up
            //Create a boolean for checking if the username was added
        boolean userAdded = this.dao.getAccountByUsername(username) != null;
            //And one for if the username is empty
        boolean usernameEmpty = username.isEmpty();
            //And one for if the password length < 4
        boolean passwordSmall = password.length() < 4;
            //Now check if none of the conditions were met
        if((!userAdded) && (!usernameEmpty) && (!passwordSmall))
        {
                //None were, register the user and return the Account instance
            return this.dao.addAccount(username, password);
        }
            //Return nothing by default
        return null;
    }

    /*
     * register: it checks if username was added, is empty, or if the password length < 4. If either are true, nothing is returned.
     * Otherwise, just adds user to account table.
     * @param account Account data to be added
     * @returns new Account if it was added, null otherwise
     */
    public Account register(Account account)
    {
        //Try to register using above register function
        return this.register(account.getUsername(), account.getPassword());
    }
}
