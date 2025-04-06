package DAO;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;
import java.util.*;

import Model.Account;
import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO 
{
    /*
     * addMessage: Adds a new message to table
     * @param postedBy Id of user who posted
     * @param message Message the user posted
     * @param timePosted When a user posed a message
     * @returns Message if was added, null otherwise
     */
    public Message addMessage(int postedBy, String message, long timePosted)
    {
        //Try to add the message
            //First, try to connect to database
        Connection connection = ConnectionUtil.getConnection();
            //Then, avoid any SQL errors
        try
        {
            //Now, create an SQL to add the message
            String SQL = "INSERT INTO message (`posted_by`, `message_text`, `time_posted_epoch`) VALUES (?, ?, ?);";
            //And create a prepared statement based on it
            PreparedStatement statement = connection.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
            //Then, pass the postedBy, message, and timePosted to the prepared statement
            statement.setInt(1, postedBy);
            statement.setString(2, message);
            statement.setLong(3, timePosted);
            //Now, execute the insert
            statement.executeUpdate();
            //And try to get the generated codes
            ResultSet generated = statement.getGeneratedKeys();
            //Then check if there is a generated code
            if(generated.next())
            {
                //There is, get the generated ID
                int messageId = generated.getInt(1);
                //And return everything with the generated message ID in message instance
                return new Message(messageId, postedBy, message, timePosted);
            }
        }
        catch(SQLException exception)
        {
            //Error encountered
            exception.printStackTrace();
        }
            //Return nothing by default
        return null;
    }

    /*
     * getMessagesByAccountID: Looks for message by account ID
     * @param accountID Identifier of whomever posted a message
     * @returns Message if no error is encountered
     */
    public List<Message> getMessagesByAccountID(int postedBy)
    {
        //Try to get the message by ID
            //Create a connection to our database
        Connection connection = ConnectionUtil.getConnection();
            //List of messages
        List<Message> messages = new ArrayList<>();
            //Now try to avoid SQL errors
        try
        {
            //First, create an SQL for getting message
            String SQL = "SELECT * FROM message WHERE `posted_by` = ?";
            //Then, create a prepared statement using the SQL
            PreparedStatement statement = connection.prepareStatement(SQL);
            //And push the message id onto it
            statement.setInt(1, postedBy);
            //Now execute the select query and get the results
            ResultSet results = statement.executeQuery();
            //And only continue if a result is received
            while(results.next())
            {
                //Result was received, get all message components
                int messageId = results.getInt("message_id");
                String messageText = results.getString("message_text");
                long timePostedEpoch = results.getLong("time_posted_epoch");
                //And return message components in a Message instance
                messages.add(new Message(messageId, postedBy, messageText, timePostedEpoch));
            }
        }
        catch (SQLException exception)
        {
            //We encountered an error
            exception.printStackTrace();
        }
            //Return messages by default
        return messages;
    }

    
    /*
     * getMessagesByAccountID: Looks for message by account ID
     * @param accountID Identifier of whomever posted a message
     * @returns Message if no error is encountered
     */
    public List<Message> getMessages()
    {
        //Try to get the message by ID
            //Create a connection to our database
        Connection connection = ConnectionUtil.getConnection();
            //Now try to avoid SQL errors
        try
        {
            //List of messages
            List<Message> messages = new ArrayList<>();
            //First, create an SQL for getting message
            String SQL = "SELECT * FROM message";
            //Then, create a prepared statement using the SQL
            PreparedStatement statement = connection.prepareStatement(SQL);
            //Now execute the select query and get the results
            ResultSet results = statement.executeQuery();
            //And only continue if a result is received
            while(results.next())
            {
                //Result was received, get all message components
                int messageId = results.getInt("message_id");
                int postedBy = results.getInt("posted_by");
                String messageText = results.getString("message_text");
                long timePostedEpoch = results.getLong("time_posted_epoch");
                //And return message components in a Message instance
                messages.add(new Message(messageId, postedBy, messageText, timePostedEpoch));
            }

            //And return the messages
            return messages;
        }
        catch (SQLException exception)
        {
            //We encountered an error
            exception.printStackTrace();
        }
            //Return null by default
        return null;
    }

    /*
     * getMessageByMessageID: Gets message by its ID
     * @param messageID Identifier of potential message
     * @returns Message if no error is encountered
     */
    public Message getMessageByMessageID(int messageID)
    {
        //Try to get the message by ID
            //Create a connection to our database
        Connection connection = ConnectionUtil.getConnection();
            //Now try to avoid SQL errors
        try
        {
            //First, create an SQL for getting message
            String SQL = "SELECT * FROM message WHERE `message_id` = ?";
            //Then, create a prepared statement using the SQL
            PreparedStatement statement = connection.prepareStatement(SQL);
            //And push the message id onto it
            statement.setInt(1, messageID);
            //Now execute the select query and get the results
            ResultSet results = statement.executeQuery();
            //And only continue if a result is received
            if(results.next())
            {
                //Result was received, get all message components
                int messageId = results.getInt("message_id");
                int postedBy = results.getInt("posted_by");
                String messageText = results.getString("message_text");
                long timePostedEpoch = results.getLong("time_posted_epoch");
                //And return message components in a Message instance
                return new Message(messageId, postedBy, messageText, timePostedEpoch);
            }
        }
        catch (SQLException exception)
        {
            //We encountered an error
            exception.printStackTrace();
        }
            //Return null by default
        return null;
    }

    /*
     * deleteMessage: removes message from table given message id
     * @param messageID: ID of message to be deleted
     * @returns Deleted Message if successful, null otherwise
     */
    public Message deleteMessage(int messageID)
    {
        //Try to delete the message by its message ID
            //Create a connection to our database
        Connection connection = ConnectionUtil.getConnection();
            //Now avoid SQL errors
        try
        {
            //First, create an SQL to delete the message
            String SQL = "DELETE FROM message WHERE `message_id` = ?";
            //Then, create a prepared statement
            PreparedStatement statement = connection.prepareStatement(SQL);
            //After that, push the message ID onto the statement
            statement.setInt(1, messageID);
            //Then, get the message by its message ID
            Message message = this.getMessageByMessageID(messageID);
            //With that, execute the prepared statement
            int rowsAffected = statement.executeUpdate();
            //Now check if rows affected > 0
            if(rowsAffected > 0)
            {
                //And return the message
                return message;
            }
        }
        catch(SQLException exception)
        {
            //Encountered an error
            exception.printStackTrace();
        }
            //Return null by default
        return null;
    }

    /*
     * updateMessage: changes message data based on its ID
     * @param messageID: ID of message to be changed
     * @param message: new message data
    message_id int primary key auto_increment,
    posted_by int,
    message_text varchar(255),
    time_posted_epoch bigint,
    foreign key (posted_by) references  account(account_id)
     * @returns Message with updated data if it is updated, null otherwise
     */
    public Message updateMessage(int messageID, String message)
    {
        //Try to delete the message by its message ID
            //Create a connection to our database
        Connection connection = ConnectionUtil.getConnection();
            //Now avoid SQL errors
        try
        {
            //First, create an SQL to delete the message
            String SQL = "UPDATE message SET `message_text` = ? WHERE `message_id` = ?";
            //Then, create a prepared statement
            PreparedStatement statement = connection.prepareStatement(SQL);
            //After that, push the message ID onto the statement
            statement.setString(1, message);
            statement.setInt(2, messageID);
            //With that, execute the prepared statement
            int rowsAffected = statement.executeUpdate();
            //Then, get the message by its message ID
            Message storedMessage = this.getMessageByMessageID(messageID);
            //Now check if rows affected > 0
            if(rowsAffected > 0)
            {
                //And return the message
                return storedMessage;
            }
        }
        catch(SQLException exception)
        {
            //Encountered an error
            exception.printStackTrace();
        }
            //Return null by default
        return null;
    }
}
