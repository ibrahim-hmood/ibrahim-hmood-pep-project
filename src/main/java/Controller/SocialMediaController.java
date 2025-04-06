package Controller;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.*;

import Model.*;
import Service.AccountService;
import Service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.delete("messages/{message_id}", this::deleteMessage);
        app.get("messages/{message_id}", this::messageByMessageID);
        app.patch("messages/{message_id}", this::patchMessage);
        app.post("login", this::login);
        app.post("messages", this::messages);
        app.post("register", this::register);
        app.get("accounts/{account_id}/messages", this::messagesByAccountID);
        app.get("messages", this::allMessages);
        return app;
    }

    /*
     * patchMessage: updates a message given ID and new message
     * @param context Javalin context
     */
    private void patchMessage(Context context)
    {
        //Try to get all messages
            //Avoid any Json processing errors
        try
        {
            //First, create an object mapper
            ObjectMapper mapper = new ObjectMapper();
            //Then, create a message service
            MessageService service = new MessageService();
            //After that, update the message
            Integer messageID = Integer.parseInt(context.pathParam("message_id"));
            Message update = mapper.readValue(context.body(), Message.class);
            Message message = service.updateMessage(messageID, update.getMessage_text());
            //Then, check if message was updated
            if(message != null)
            {
                //It was, send it back
                context.json(mapper.writeValueAsString(message));
            }
            else
            {
                //It wasnt, send 400 back
                context.status(400);
            }
        }
        catch (JsonProcessingException jpe)
        {
            //Error encountered
            jpe.printStackTrace();
        }
    }

    /*
     * allMessages: Returns a list of all messages
     * @param context Javalin Context containing url, post, and get data
     */
    private void allMessages(Context context)
    {
        //Try to get all messages
            //Avoid any Json processing errors
        try
        {
            //First, create an object mapper
            ObjectMapper mapper = new ObjectMapper();
            //Then, create a message service
            MessageService service = new MessageService();
            //After that, get a list of all messages
            List<Message> messages = service.getMessages();
            //Then, check if a list was received
            if(messages != null)
            {
                //A list was, send it back
                context.json(mapper.writeValueAsString(messages));
            }
            else
            {
                //A list wasnt, send 200 back
                context.status(200);
            }
        }
        catch (JsonProcessingException jpe)
        {
            //Error encountered
            jpe.printStackTrace();
        }

    }

    /*
     * messageByMessageID: Gets message by message ID
     * @param context Javalin Context containing data
     */
    private void messageByMessageID(Context context)
    {
        //Try to get the message by its ID
            //Avoid any json processing errors
        try
        {
            //First, make an object mapper
            ObjectMapper mapper = new ObjectMapper();
            //Then create a message service
            MessageService service = new MessageService();
            //And use the message service to get message
            int messageID = Integer.parseInt(context.pathParam("message_id"));
            Message responseMessage = service.getByMessageID(messageID);
            //Now check if the message was found
            if(responseMessage != null)
            {
                //It was, return the message as a response
                context.json(mapper.writeValueAsString(responseMessage));
            }
            else
            {
                //It was not, return a 200
                context.status(200);
            }
        }
        catch(JsonProcessingException jpe)
        {
            //Error encountered
            jpe.printStackTrace();
        }
    }

    /*
     * messagesByAccountID: Gets message by message ID
     * @param context Javalin Context containing data
     */
    private void messagesByAccountID(Context context)
    {
        //Try to get the message by its account's ID
            //Avoid any json processing errors
        try
        {
            //First, make an object mapper
            ObjectMapper mapper = new ObjectMapper();
            //Then create a message service
            MessageService service = new MessageService();
            //And use the message service to get message
            int messageID = Integer.parseInt(context.pathParam("account_id"));
            List<Message> responseMessage = service.getByAccountID(messageID);
            //Now check if the message was found
            if(responseMessage != null)
            {
                //It was, return the message as a response
                context.json(mapper.writeValueAsString(responseMessage));
            }
            else
            {
                //It was not, return a 200
                context.status(200);
            }
        }
        catch(JsonProcessingException jpe)
        {
            //Error encountered
            jpe.printStackTrace();
        }
    }

    /*
     * deleteMessage: deletes a message by its message ID
     * @param context The Javalin Context containing JSON body
     */
    private void deleteMessage(Context context)
    {
        //Try to delete a message by given ID
            //Avoid any json processing errors
        try
        {
            //First, make an object mapper
            ObjectMapper mapper = new ObjectMapper();
            //Then create a message service
            MessageService service = new MessageService();
            //And use the message service to delete message
            int messageID = Integer.parseInt(context.pathParam("message_id"));
            Message responseMessage = service.deleteByMessageID(messageID);
            //Now check if the message was deleted
            if(responseMessage != null)
            {
                //It was, return the message as a response
                context.json(mapper.writeValueAsString(responseMessage));
            }
            else
            {
                //It was not, return a 200
                context.status(200);
            }
        }
        catch(JsonProcessingException jpe)
        {
            //Error encountered
            jpe.printStackTrace();
        }
    }

    /**
     * register: registers a new user if they havent been
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void register(Context context) {
        try
        {
            //Add the username and password using account service
                //Create an object mapper
            ObjectMapper mapper = new ObjectMapper();
                //And make the account service
            AccountService service = new AccountService();
                //Now read the body from the context
            String body = context.body();
                //And use mapper to read the data from the body as Account.class
            Account account = mapper.readValue(body, Account.class);
                //Now use the account service to create the account
            Account newAccount = service.register(account);
                //And check if account was not added
            if(newAccount == null)
                //It was not, return status 400
                context.status(400);
            else
                context.json(mapper.writeValueAsString(newAccount));
            
        }
        catch(JsonProcessingException e)
        {
            e.printStackTrace();
        }
    }

    /*
     * messages: creates a new message
     * @param context Javalin Context object for managing information
     */
    public void messages(Context context)
    {
        //Try to add the message that was received
            //Avoid any json processing errors
        try
        {
            //Create an object mapper
            ObjectMapper mapper = new ObjectMapper();
            //And create a message service
            MessageService service = new MessageService();
            //Now read the body
            String body = context.body();
            //And use mapper to read data from the body as Message.class
            Message receivedMessageData = mapper.readValue(body, Message.class);
            //Now use message service to create the message
            Message registeredMessage = service.createMessage(receivedMessageData);
            //And check if the message wasnt registered
            if(registeredMessage == null)
                //It wasnt, return status 400
                context.status(400);
            else
                //It was registered, return the message
                context.json(mapper.writeValueAsString(registeredMessage));
        }
        catch (JsonProcessingException jpe)
        {
            //Error encountered
            jpe.printStackTrace();
        }
    }

    /*
     * login: handler for logging in
     * @param context The Javalin Context object that manages information about HTTP request and response
     */
    private void login(Context context)
    {
        try
        {
            //Try to login using account service
                //Create an object mapper
            ObjectMapper mapper = new ObjectMapper();
                //And create account service
            AccountService service = new AccountService();
                //Now read the body from the context
            String body = context.body();
                //And use mapper to read the data from the body as Account.class
            Account account = mapper.readValue(body, Account.class);
                //Now try to login using account service
            Account registeredData = service.login(account);
                //Then, check if data was returned
            if(registeredData != null)
            {
                //Data was, return it as json
                context.json(mapper.writeValueAsString(registeredData));
            }
            else
            {
                //Error encountered, 401
                context.status(401);
            }
        }
        catch(JsonProcessingException jpe)
        {
            jpe.printStackTrace();
        }
    }
}