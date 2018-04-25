import java.math.BigInteger;
import java.util.*;
import java.lang.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

/*
------------__RSA__------------------

This RSA implementation uses block size as 6, that is it converts the text message into blocks with
6 characters and pads zeroes when necessary.

e is set as 65537

For number of bits in the JFrame add integers values only such as 1024,3072.
The code has been successfully tested on 4096 bits.

Enter message and click on button to see RSA work

*/

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

    // The  encrypt() of the RSA algorithm, cipher =(message^e)%n
    static BigInteger encrypt(BigInteger message_i,BigInteger e, BigInteger n){
	   //System.out.println(" Message is : "+message_i);
       return message_i.modPow(e,n);
    }

    // The  decrypt of the RSA algorithm, message =(cipher^d)%n
    static BigInteger decrypt(BigInteger cipher_i, BigInteger d, BigInteger n){
	   return cipher_i.modPow(d,n);
    }

    // a simple function that returns a given string 'n' time
    // Ex --  repear_string_n_times("0",3) gives "000"
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

    // function that converts  a given string to its corresponding ascii representation
    static String string_to_ascii(String str){
        String temp="";
        for(int i = 0; i<str.length();i++){
            temp += (int)(str.charAt(i));
        }
        return temp;
    }

  public static void main(String[] args) {

       // ----------------GUI code starts----------------
       //Creating the Frame
        JFrame frame = new JFrame("RSA ");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700,900);

        //Creating the panel for  adding components
        JPanel panel = new JPanel();

        // adding title to the JPanel
        TitledBorder border = new TitledBorder("RSA Algorithm");
        border.setTitleJustification(TitledBorder.CENTER);
        border.setTitlePosition(TitledBorder.TOP);
        panel.setBorder(border);

        JLabel no_of_bits = new JLabel("Enter number of bits for the  RSA public key ");
        JTextField no_bits = new JTextField(10); // accepts upto 10 characters
        JButton send = new JButton("Start RSA execution");
        JLabel message_label = new JLabel("Enter the message for RSA algorithm");
        JTextArea p_q_phi_textArea = new JTextArea ("",30,60);
        p_q_phi_textArea.setLineWrap(true);
        p_q_phi_textArea.setWrapStyleWord(true);
        JTextArea message_textArea = new JTextArea("",5,50);
        message_textArea.setLineWrap(true);
        message_textArea.setWrapStyleWord(true);
              

        JScrollPane scroll_p = new JScrollPane (p_q_phi_textArea);
        scroll_p.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll_p.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        JScrollPane scroll_q = new JScrollPane (message_textArea);
        scroll_q.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll_p.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        
        // when the 'Start RSA execution ' button is clicked
        send.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){  
                int bitLength = Integer.parseInt(no_bits.getText());
                p_q_phi_textArea.append(" Starting RSA with public key bit length as "+bitLength+" and block size as 6.\n\n");
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

                p_q_phi_textArea.append(" message after padding "+number_of_zeros_appended+" zeroes is "+message+"\n\n");
                p_q_phi_textArea.append(" Number of blocks is : "+ message.length()/6+"\n\n");

                // BigIntger[] for storing encrypted and decrypted message blocks
                // block size is 6
                BigInteger[] cipher_i = new BigInteger[message.length()/6];
                BigInteger[] message_i = new BigInteger[message.length()/6];
                String[] mess = new String[message.length()/6];
                String[] temp_ascii_string = new String[message.length()/6];
                String[] message_array = message.split("(?<=\\G.{6})");         // for splitting string into equal blocks of size 6

                

                for( int i=0; i<message_array.length; i++){
                    temp_ascii_string[i] = string_to_ascii(message_array[i]);
                    p_q_phi_textArea.append(" ASCII equivalent of the block["+Integer.toString(i+1)+"] is : "+temp_ascii_string[i]+"\n\n");
                    //cipher_i = encrypt(new BigInteger(string_to_ascii(temp_ascii_string)),e,n);
                    cipher_i[i] = encrypt(new BigInteger(message_array[i].getBytes()),e_R,n);
                    p_q_phi_textArea.append(" Encrypted text of block["+Integer.toString(i+1)+"] is : "+cipher_i[i]+"\n\n" );
                    message_i[i] = decrypt( cipher_i[i], d, n);
                    //p_q_phi_textArea.append("\n\n Decrypted text is : "+message_i[i] +"\n\n");
                   // message_textArea.append("\n\n Original text is : "+ascii_to_string(new decrypt( cipher_i,d,n)));g
                    mess[i] = new String(message_i[i].toByteArray());
                    p_q_phi_textArea.append(" Decrypted block["+Integer.toString(i+1)+"] text is : "+mess[i]+"\n\n");
                } 

                p_q_phi_textArea.append(" Decrypted Text message in ASCII with padding is "+String.join("",temp_ascii_string)+"\n\n");
                
                // showing the initial message( obtained by joining all mess[] elements)
                String tttt = String.join("",mess);
                p_q_phi_textArea.append("Original Text message after removing the padded zeroes is "+tttt.substring(0,message.length()-number_of_zeros_appended)+"\n\n");

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
            }  
        });
        panel.add(no_of_bits); // Components Added using Flow Layout
        panel.add(no_bits);
        panel.add(message_label);
        panel.add(message_textArea);  
        panel.add(send); 
        panel.add(scroll_p);  
        
        //Adding Components to the frame.
        frame.getContentPane().add(panel);
        // frame.getContentPane().add(BorderLayout.CENTER, ta);
        frame.setVisible(true);

  }
}
