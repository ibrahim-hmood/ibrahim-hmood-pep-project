package Service;

import java.util.*;

import Model.Account;
import Model.Message;

import DAO.AccountDAO;
import DAO.MessageDAO;

public class MessageService
{
    //Data access object for account
    private AccountDAO accountDAO;
    //Data access object for message
    private MessageDAO dao;

    /*
     * MessageService: creates a new data access object
     */
    public MessageService()
    {
        //Create a new DAO
        this.accountDAO = new AccountDAO();
        this.dao = new MessageDAO();
    }

    /*
     * MessageService: creates message service, sets dao to parameter dao
     * @param dao Data access object passed to service
     */
    public MessageService(AccountDAO accountDao, MessageDAO dao)
    {
        //Set the dao
        this.accountDAO = accountDao;
        this.dao = dao;
    }

    /*
     * createMessage: checks if the message is too long, the message is empty, or the user does not exist. If these are the case, nothing is returned.
     * Otherwise, a message is added and a new message instance is returned.
     * @param postedBy Whomever posted this message
     * @param message Message being posted
     * @param timePosted whenever the message was posted
     * @returns Message instance if it was added, none otherwise
     */
    public Message createMessage(int postedBy, String message, long timePosted)
    {
        //Try to create a new message
            //Create a boolean for checking if the message is too long
        boolean messageTooLong = message.length() > 255;
            //And one for checking if the message is empty
        boolean messageIsEmpty = message.isEmpty();
            //Then make one for if the user doesnt exist
        boolean userDoesntExist = this.accountDAO.getAccountByUserID(postedBy) == null;
            //If neither conditions are true, add message to database
        if((!messageTooLong) && (!messageIsEmpty) && (!userDoesntExist))
        {
                //Neither are true, add the message and return the message instance
            return this.dao.addMessage(postedBy, message, timePosted);
        }
            //They were true, return nothing
        return null;
    }

    /*
     * createMessage: checks if the message is too long, the message is empty, or the user does not exist. If these are the case, nothing is returned.
     * Otherwise, a message is added and a new message instance is returned.
     * @param message Message being possibly recorded
     * @returns Message instance if it was added, none otherwise
     */
    public Message createMessage(Message message)
    {
        //Try to register the message
        return this.createMessage(message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
    }

    /*
     * getByMessageID: Gets message by its message ID
     * @param messageID ID of message we want to get
     * @returns Message if ID is in table, null otherwise
     */
    public Message getByMessageID(int messageId)
    {
        //Try to get the message by its ID
        return this.dao.getMessageByMessageID(messageId);
    }

    /*
     * deleteByMessageID: Deletes message by its ID. Message is returned if it was deleted, null otherwise.
     * @param messageID ID of message to be deleted
     * @returns Message if message was deleted, null otherwise
     */
    public Message deleteByMessageID(int messageId)
    {
        //Try to delete the message by its ID
        return this.dao.deleteMessage(messageId);
    }

    /*
     * getByAccountID: Gets message by account ID
     * @param accountID ID of account whose message we want
     * @returns Messages if a message was found, null otherwise
     */
    public List<Message> getByAccountID(int accountID)
    {
        //Try to get a message by accountID
        return this.dao.getMessagesByAccountID(accountID);
    }

    /*
     * getMessages: Gets all messages
     * @returns Gets all messages
     */
    public List<Message> getMessages()
    {
        //Try to get all messages
        return this.dao.getMessages();
    }

    /*
     * updateMessage: changes message based on message ID
     * @param messageID: ID of message being changed
     * @param message: New message text
     * @returns Message if message was updated, null otherwise
     */
    public Message updateMessage(int messageID, String message)
    {
        //Update the message
            //First, create a boolean if message is too long
        boolean isTooLong = message.length() > 255;
            //And a boolean for checking if message doesnt exist
        boolean messageDoesntExist = this.dao.getMessageByMessageID(messageID) == null;
            //And a boolean for checking if updated message is empty
        boolean emptyUpdate = message.isEmpty() || message.isBlank();
            //Now check if all parameters were not met
        if((!isTooLong) && (!messageDoesntExist) && (!emptyUpdate))
        {
                //They were not, update the message and return update results
            return this.dao.updateMessage(messageID, message);
        }
            //Then, return nothing
        return null;
    }
}