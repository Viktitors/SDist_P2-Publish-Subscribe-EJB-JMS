/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.annotation.Resource;
import javax.jms.TopicConnection;
import javax.jms.TopicPublisher;

/**
 *
 * @author viktitors
 */
@Singleton
public class EjBSessionBean implements EjBSessionBeanLocal {

    private int count_msg;
    private LinkedList<String> list_msg;
    
    private TopicConnection conexion;
    private TopicSession sesion;
    private TopicPublisher publisher;
    
    private TextMessage msg;
    
    @Resource(mappedName="jms/FactoriaConexiones")
    private TopicConnectionFactory tcf;
    @Resource(mappedName="jms/Noticias")
    private Topic t;
            
    @PostConstruct void inicializacion(){
        
        count_msg=0;
        list_msg = new LinkedList<String>();
        FileReader fr = null;
        BufferedReader br=null;           
            
            try {
                File archivo = new File("C:\\Users\\usuario\\Documents\\NetBeansProjects\\ProyectoEJB\\fichero.txt"); 
                fr = new FileReader(archivo);
                br = new BufferedReader(fr);
                String linea=null;
            try {
                linea = br.readLine();
                while(linea != null){
                    addToList(linea);  
                    linea=br.readLine();
                }
                fr.close();
            } catch (IOException ex) {
                Logger.getLogger(EjBSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        } 
        }   catch (FileNotFoundException ex) {
            Logger.getLogger(EjBSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    
    
    } 
    @Override
    public void addMsg(String m) {
        try
        {  
            conexion = tcf.createTopicConnection();
            sesion = conexion.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
            publisher = sesion.createPublisher(t);
            conexion.start();
            msg = sesion.createTextMessage();
            msg.setText(m);
            publisher.publish(msg);
            publisher.close();
        
        }catch(JMSException ex)
        {
            System.out.println("Se produjo un error");     
        }                       
    }

    @Override
    public int getNumber() {
        return count_msg;
    }

    @Override
    public LinkedList<String> getList() {
        return list_msg;
    }

    @Override
    public void addToList(String m) {
        list_msg.add(m);
        count_msg++;
    }

     
    
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
