import java.math.BigInteger;
import java.util.*;
import java.lang.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

public class Temp {

	// function to return prime number p annd q 	
    static BigInteger big_primes(int bitLength){
        Random rnd = new Random();
        return BigInteger.probablePrime(bitLength, rnd);
    }
    
	// for primes p and q , phi =(p-1)*(q-1)
    static BigInteger euler_phi(BigInteger p, BigInteger q){
	   return p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
    }
    
	// function to find gcd of two numbers( BigIntegers)
    static BigInteger big_gcd(BigInteger a, BigInteger b){
	   return a.gcd(b);
    }    

    static String ascii_string(String input_string_block){
        String temp = "";
        for(int i=0;i<input_string_block.length();i++){
            temp += (int)input_string_block.charAt(i);
        }
        return temp;
    }
 /* static BigInteger calculate_e( BigInteger phi){
    	// Find a random number between 1 and phi such that gcd(e,phi)=1
        BigInteger e;
    	for( BigInteger i=BigInteger.ONE; i.compareTo(phi) < 0; i.add(BigInteger.ONE) ){
    	        if(big_gcd(i,phi).compareTo(BigInteger.ONE) == 0){
                     e = BigInteger(i);
                    break;
            }
     	}
        return e;
    }
*/
	
    static BigInteger encrypt(BigInteger message_i,BigInteger e, BigInteger n){
	   //System.out.println(" Message is : "+message_i);
       return message_i.modPow(e,n);
    }

    static BigInteger decrypt(BigInteger cipher_i, BigInteger d, BigInteger n){
	   return cipher_i.modPow(d,n);
    }
    static String repeat_string_n_times(String str,int n){
        String temp="";
        if(n>0){
            for(int i=0;i<n;i++){
                temp+=str;
            }
            return temp;
        }
        else{
            return "";
        }  
    }

    static String string_to_ascii(String str){
        String temp="";
        for(int i = 0; i<str.length();i++){
            temp += (int)(str.charAt(i));
        }
        return temp;
    }


















  public static void main(String[] args) {
       //Creating the Frame
        JFrame frame = new JFrame("RSA ");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700,900);

        //Creating the panel at bottom and adding components
        JPanel panel = new JPanel(); // the panel is not visible in output


        TitledBorder border = new TitledBorder("RSA Algorithm");
        border.setTitleJustification(TitledBorder.CENTER);
        border.setTitlePosition(TitledBorder.TOP);
        panel.setBorder(border);

        JLabel no_of_bits = new JLabel("Enter number of bits for the  RSA public key ");
        JLabel p = new JLabel();
        JLabel q = new JLabel();
        JLabel phi = new JLabel();
        JLabel e_RSA = new JLabel();
        JTextField no_bits = new JTextField(10); // accepts upto 10 characters
        JButton send = new JButton("Start RSA execution");
        JLabel message_label = new JLabel("Enter the message for RSA algorithm");
        JTextArea p_q_phi_textArea = new JTextArea ("",30,60);
        p_q_phi_textArea.setLineWrap(true);
        p_q_phi_textArea.setWrapStyleWord(true);
      //  JTextArea cipher_textArea = new JTextArea ("",10,30);
      //  JTextArea plain_textArea = new JTextArea ("",10,30);
        JTextArea message_textArea = new JTextArea("",5,50);
        message_textArea.setLineWrap(true);
        message_textArea.setWrapStyleWord(true);
              


        


        JScrollPane scroll_p = new JScrollPane (p_q_phi_textArea);
        scroll_p.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll_p.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

       JScrollPane scroll_q = new JScrollPane (message_textArea);
        scroll_q.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll_p.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
/*
        JScrollPane scroll_phi = new JScrollPane (plain_textArea);
        scroll_phi.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll_phi.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
*/


        send.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){  

                int bitLength = Integer.parseInt(no_bits.getText());
                BigInteger p,q,n,phi,e_R;
                
                p = big_primes(bitLength/2);
                p_q_phi_textArea.append(" PUBLIC KEY 'p' is : "+p+"\n\n");
                q = big_primes(bitLength/2);
                p_q_phi_textArea.append(" PRIVATE KEY 'q' is : "+q+"\n\n");

                n = p.multiply(q);
                phi = euler_phi(p, q);
                p_q_phi_textArea.append(" phi is : "+phi+"\n\n");
        
                
                e_R = new BigInteger("65537");   // or 65537 
                p_q_phi_textArea.append(" e is : "+e_R+"\n\n");
            
                BigInteger d = e_R.modInverse(phi);
                p_q_phi_textArea.append(" d is : "+d+"\n\n");
            
                String message = message_textArea.getText();
                int number_of_zeros_appended=0;
                if(message.length()%6 != 0){
                    number_of_zeros_appended =6- (message.length() % 6);
                    message += repeat_string_n_times("0",number_of_zeros_appended);
                }
                
                p_q_phi_textArea.append(" Number of blocks is : "+ message.length()/6+"\n\n");

                BigInteger[] cipher_i = new BigInteger[message.length()/6];
                BigInteger[] message_i = new BigInteger[message.length()/6];
                String[] mess = new String[message.length()/6];
                String[] temp_ascii_string = new String[message.length()/6];
                String[] message_array = message.split("(?<=\\G.{6})");
                for( int i=0; i<message_array.length; i++){
                    temp_ascii_string[i] = string_to_ascii(message_array[i]);
                    p_q_phi_textArea.append(string_to_ascii(temp_ascii_string[i]));
                    //cipher_i = encrypt(new BigInteger(string_to_ascii(temp_ascii_string)),e,n);
                    cipher_i[i] = encrypt(new BigInteger(message_array[i].getBytes()),e_R,n);
                    p_q_phi_textArea.append("\n\n Encrypted text of block["+Integer.toString(i)+"] is : "+cipher_i[i]+"\n\n" );
                    message_i[i] = decrypt( cipher_i[i], d, n);
                    p_q_phi_textArea.append("\n\n Decrypted text is : "+message_i[i] +"\n\n");
                   // message_textArea.append("\n\n Original text is : "+ascii_to_string(new decrypt( cipher_i,d,n)));g
                   mess[i] = new String(message_i[i].toByteArray());
                    p_q_phi_textArea.append("\n\n Original text is : "+mess[i]+"\n\n");
                }
                String tttt = String.join("",mess);
                p_q_phi_textArea.append(" Text message in ASCII with padding is "+String.join("",temp_ascii_string)+"\n\n");
                p_q_phi_textArea.append("Original Text message is "+tttt.substring(0,message.length()-number_of_zeros_appended)+"\n\n");

                String text = new String();
                if(no_bits.getText().length() < 1){
                    JOptionPane.showMessageDialog(null, "Only numbers are allowed!");
                }
                else{
                    
                    text = no_bits.getText();
                }
                
                JLabel temp = new JLabel();
                temp.setText(text);
                panel.add(temp);
                no_of_bits.setText("RSA using bits : "); 
            /*    p_q_phi_textArea.append(text+"\n");
                p_q_phi_textArea.append("p"+"\n"); 
                p_q_phi_textArea.append(" phi = (p-1)*(q-1) is : 47483780563576844554455710381837"+"\n");
                p_q_phi_textArea.append(" d value is : 47483780563576844554455710381837"+"\n"); 

                p_q_phi_textArea.append(" message in ASCII is : "+"\n");

                p_q_phi_textArea.append(" encrypted message text is : "+"\n");

                p_q_phi_textArea.append(" decrypted cipher text is : "+"\n");
                e_RSA.setText("e value is : 65537");

                */
                //JTextArea textArea = new JTextArea ("",10,30);
                //textArea.append("47483780563576844554455710381837");
                //panel.add(textArea);
               // panel.add(scroll_q);
                //panel.add(scroll_phi);
                
            }  
        });
        panel.add(no_of_bits); // Components Added using Flow Layout
        panel.add(no_bits);
        panel.add(message_label);
        panel.add(message_textArea);  
        panel.add(send); 
       // panel.add(scroll_q);
        panel.add(scroll_p);  
        panel.add(p); // Components Added using Flow Layout
        panel.add(q);
        panel.add(phi);
        panel.add(e_RSA);
        

        
        

        //Adding Components to the frame.
        frame.getContentPane().add( panel);
       // frame.getContentPane().add(BorderLayout.CENTER, ta);
        frame.setVisible(true);

       

  }
}
