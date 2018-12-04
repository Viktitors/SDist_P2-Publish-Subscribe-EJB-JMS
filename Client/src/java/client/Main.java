/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.util.Scanner;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.*;


public class Main {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws JMSException,NamingException, InterruptedException{
       
            // TODO code application logic here
            
              
            
            	
                InitialContext iniCtx = new InitialContext();
                Object tmp = iniCtx.lookup("jms/FactoriaConexiones");
                TopicConnectionFactory tcf=(TopicConnectionFactory)tmp;
                Topic t=(Topic)iniCtx.lookup("jms/Noticias");
                

                TopicConnection connection = tcf.createTopicConnection();
                TopicSession session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE); 
                TopicPublisher publisher = session.createPublisher(t);
                TopicSubscriber subscriber = session.createSubscriber(t);
                 
                subscriber.setMessageListener(new MsgListener());              
                connection.start(); 
                TextMessage mensaje=session.createTextMessage();
                String noticia=null;
                
                while(noticia!="") {
                    System.out.println("Escribir noticia:");
                    Scanner scan=new Scanner(System.in);
                    noticia=scan.nextLine();
                    mensaje.setText("Noticia " + noticia);
                    publisher.publish(mensaje);
                    Thread.sleep(100);
                    publisher.close();
                }
                
                

            
    }

    private static class MsgListener implements MessageListener {

        public MsgListener() {
        }
        
        public void onMessage(Message message) {
            TextMessage textMessage = (TextMessage)message;
        
            try {
                System.out.println("Recibo: " + textMessage.getText());
            } 
            catch (JMSException ex) {
                System.err.println("Se produjo un error");
            } 
        }
    } 
}