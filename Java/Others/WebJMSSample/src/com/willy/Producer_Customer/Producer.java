package com.willy.Producer_Customer;

import java.io.IOException;
import java.io.PrintWriter;

import javax.jms.DeliveryMode;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class JMSTest
 */
@WebServlet("/Producer")
public class Producer extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Producer() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
    	System.out.println("Producer Start");
        PrintWriter out = response.getWriter();

        try {
            // get the initial context
            InitialContext context = new InitialContext();

            // lookup the queue object
            Queue queue = (Queue) context.lookup("java:comp/env/queue/queue0");

            // lookup the queue connection factory
            QueueConnectionFactory conFactory = (QueueConnectionFactory) context
                    .lookup("java:comp/env/queue/connectionFactory");

            // create a queue connection
            QueueConnection queConn = conFactory.createQueueConnection();

            // create a queue session
            QueueSession queSession = queConn.createQueueSession(false,
                    Session.DUPS_OK_ACKNOWLEDGE);

            // create a queue sender
            QueueSender queSender = queSession.createSender(queue);
            queSender.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            // create a simple message to say "Hello World"
            TextMessage message = queSession.createTextMessage("Hello World");

            // send the message
            queSender.send(message);

            // print what we did
            out.write("Message Sent: " + message.getText());

            // close the queue connection
            queConn.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("Producer End");
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
    }

}