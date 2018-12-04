/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;


import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 *
 * @author viktitors
 */
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "clientId", propertyValue = "jms/Noticias")
    ,
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/Noticias")
    ,
        @ActivationConfigProperty(propertyName = "subscriptionName", propertyValue = "jms/Noticias")
    ,
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic")
})
public class EjbMessageBean implements MessageListener {

    @EJB
    private EjBSessionBeanLocal ejBSessionBean;
    
    public EjbMessageBean() {
    }
    
    @Override
    public void onMessage(Message message) {

        FileWriter fw = null;
        try{
            fw = new FileWriter("C:\\Users\\usuario\\Documents\\NetBeansProjects\\ProyectoEJB\\fichero.txt", true);//modo append
            TextMessage textMessage = (TextMessage) message;        //transformar a string
            String me = textMessage.getText(); 
            
            PrintWriter msg = new PrintWriter(fw);
            msg.print(me);
            msg.println();
            ejBSessionBean.addToList(me);//añadirlo a la lista
            fw.close();
       
    } catch (IOException ex)
        {
            System.out.println("Se produjo un error");
        } catch (JMSException ex) {
            System.out.println("Se produjo un error JMSException");
        }
        
    }
    
}
