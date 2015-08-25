package com.cloudyear.caesarcipher;



import java.math.BigInteger;

import com.kloudier.CipherYourself.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	
	public String Plain_text;
	public String Cipher_text;
	int cipher,cipher2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        
        
      

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            }
            
        }
        
        
        
        
        
        
    }
    
    public void swaptext(View view)
    {
    	EditText pt=(EditText)findViewById(R.id.Inputtext);
    	   	
    	EditText ct=(EditText)findViewById(R.id.Outputtext);
    	
    	String temp=ct.getText().toString();
    	ct.setText(pt.getText());
    	pt.setText(temp);
    	
    	
    	
    	
    	
    }
    
    
    void handleSendText(Intent intent)
    {
    	  String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
    	    if (sharedText != null) {
    	    	Plain_text=sharedText;
    	    	Cipher_text=sharedText;
    	    	EditText textview=(EditText)findViewById(R.id.Inputtext);
    	    	textview.setText(Plain_text);
    	    	textview=(EditText)findViewById(R.id.Outputtext);
    	    	textview.setText(Cipher_text);
    	        // Update UI to reflect text being shared
    	    }
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	switch(item.getItemId())
    	{
    	case R.id.action_settings : opensettings();return true;
    	case R.id.share_settings : openshare();return true;
    	default : return super.onOptionsItemSelected(item);
    	}
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }
    /*
     * 
     * 
     * 
     * HILL CIPHER
     * 
     * 
     */
    public int[][] hill_key(int row,int column)
    {
    	int [][] key=new int[3][3];
    	SharedPreferences hill_pref=PreferenceManager.getDefaultSharedPreferences(this);
    	String h_pref="hkey_";
    	int p_count=0;
    	
    	for(int i=0;i<row;i++)
    	{
    		for(int j=0;j<column;j++)
    		{
    			h_pref+=p_count;
    			key[i][j]=hill_pref.getInt(h_pref, 0);
    			
    			p_count++;
    			h_pref="hkey_";
    			
    		}
    	}
    	
    	return key;
    }
    
    
    public void hill_cipher()
    {
    	
    	Cipher_text="";
    	int[][] i_key=new int[3][3];
    	
    	i_key=hill_key(3,3);
    	
    	int [] i_pt=new int[100];
    	int [] i_ct=new int[100];
    	
    	
    	String pt=Plain_text;
    	
    	
    	
    	int count=0;
    	for(int i=0;i<Plain_text.length();i++)
    	{
    		if(Character.isLetter(Plain_text.charAt(i))==true)
    		{
    			i_pt[count++]=Plain_text.charAt(i)-'a';
    			
    		}
    	}
    	
    	
    	
    	if((count%3)!=0)
    	{
    		for(int i=1;i<=count%3;i++)
    		{
    			i_pt[count++]=0;
    		}
    	}
    	
    	
    	int temp_pt[] = new int[3];
    	int temp_ct[] = new int[3];
    	
    	for(int i=0;i<count;i+=3)
    	{
    		for(int k=0;k<3;k++)
    		{
    			temp_pt[k]=i_pt[i+k];
    		}
    		
    		temp_ct=Matrix_Manipulation.multiply(i_key, temp_pt, 3);
    		for(int k=0;k<3;k++)
    		{
    			i_ct[i+k]=(temp_ct[k])%26+'a';
    			//Cipher_text+="debug1:"+temp_ct[k];
    		}
    	}
    	
    	int inc=0;
    	for(int i=0;i<Plain_text.length();i++)
    	{
    		if(Character.isLetter(Plain_text.charAt(i))==false)
    		{
    			Cipher_text+=Plain_text.charAt(i);
    		}
    		else
    		{
    			Cipher_text+=(char)i_ct[inc];
    			inc++;
    		}
    	}
    	
    
    	
    	
    	

    			
    
    }
 /*
  * 
  *    AFFINE CIPHER
  *    
  */
    public int affine_cipher()
    {
    	SharedPreferences affine_pref=PreferenceManager.getDefaultSharedPreferences(this);
    	String skey_a=affine_pref.getString("affine_a_l", "1");
    	int ikey_a=Integer.parseInt(skey_a);
    	BigInteger a=new BigInteger(""+ikey_a);
    	BigInteger b=new BigInteger(""+affine_pref.getInt("affine_b", 0));
    	BigInteger gcd=a.gcd(new BigInteger("26"));
    	
    	if((gcd.intValue())!=1)
    	{
    		Context context = getApplicationContext();
    		CharSequence text = "First Key and 26 not coprimes!!";
    		int duration = Toast.LENGTH_SHORT;

    		Toast toast = Toast.makeText(context, text, duration);
    		toast.show();
    		return 1;
    	}
    	
    	Cipher_text="";
    	for(int i=0;i<Plain_text.length();i++)
    	{
    		if(Character.isLetter(Plain_text.charAt(i))==true)
    		{
    		int ct=((Plain_text.charAt(i)-'a')*a.intValue()+b.intValue())%26+'a';
    		Cipher_text+=(char)ct;
    		}
    		else
    		{
    			Cipher_text+=Plain_text.charAt(i);
    		}
    	}
    	return 0;
    }
    public int affine_decipher()
    {
    	SharedPreferences affine_pref=PreferenceManager.getDefaultSharedPreferences(this);
    	String skey_a=affine_pref.getString("affine_a_l", "1");
    	int ikey_a=Integer.parseInt(skey_a);
    	BigInteger a=new BigInteger(""+ikey_a);
    	BigInteger b=new BigInteger(""+affine_pref.getInt("affine_b", 0));
    	BigInteger gcd=a.gcd(new BigInteger("26"));
    	
    	if((gcd.intValue())!=1)
    	{
    		Context context = getApplicationContext();
    		CharSequence text = "First Key and 26 not coprimes!!";
    		int duration = Toast.LENGTH_SHORT;

    		Toast toast = Toast.makeText(context, text, duration);
    		toast.show();
    		return 1;
    	}
    	
    	Plain_text="";
    	for(int i=0;i<Cipher_text.length();i++)
    	{
    		if(Character.isLetter(Cipher_text.charAt(i))==true)
    		{    			
    		int pt=((Cipher_text.charAt(i)-'a')+(26-b.intValue()));
    		BigInteger big_pt=a.modInverse(new BigInteger(""+26));
    		pt=(((pt%26)*big_pt.intValue()))%26+'a';
    		Plain_text+=(char)pt;
    		}
    		else
    		{
    			Plain_text+=Cipher_text.charAt(i);
    		}
    		
    		
    	}
    	return 0;
    }
    
 /*
  * 
  * VIGENERE CIPHER
  * 
  */
    
    public String vigenere_key()
    {
    	String key;
    	SharedPreferences key_pref=PreferenceManager.getDefaultSharedPreferences(this);
    	key=key_pref.getString("vigenere_key", "JOE");
    	key=key.toLowerCase();
    	return key;
    }
    
    public void vigenere_cipher(View view)
    {
    	Cipher_text="";
    	String key=vigenere_key();
    	String pt=Plain_text;
    	while(pt.length()>key.length())
       	{
    		for(int i=0;i<key.length();i++)
    		{
    			if(Character.isLetter(pt.charAt(i))==true)
    			{	
    			int a =((pt.charAt(i)-'a')+(key.charAt(i)-'a'))%26+'a';
    			char x=(char)a;
    			Cipher_text+=x;
    			}
    			else
    			{
    				Cipher_text+=pt.charAt(i);
    			}
    		}
    		pt=pt.substring(key.length(),pt.length());
    	}
    	if(pt.length()>0)
    	{
    		for(int i=0;i<pt.length();i++)
    		{
    			if(Character.isLetter(pt.charAt(i))==true)
    			{	
    			int a =((pt.charAt(i)-'a')+(key.charAt(i)-'a'))%26+'a';
    			char x=(char)a;
    			Cipher_text+=x;
    			}
    			else
    			{
    				Cipher_text+=pt.charAt(i);
    			}
    		}
    	}
    	
    }
    
    public void vigenere_decipher(View view)
    {
    	Plain_text="";
    	String key=vigenere_key();
    	String ct=Cipher_text;
    	while(ct.length()>key.length())
       	{
    		for(int i=0;i<key.length();i++)
    		{
    			if(Character.isLetter(ct.charAt(i))==true)
    			{	
    			int a =((ct.charAt(i)-'a')+(26-(key.charAt(i)-'a')))%26+'a';
    			char x=(char)a;
    			Plain_text+=x;
    			}
    			else
    			{
    				Plain_text+=ct.charAt(i);
    			}
    		}
    		ct=ct.substring(key.length(),ct.length());
    	}
    	if(ct.length()>0)
    	{
    		for(int i=0;i<ct.length();i++)
    		{
    			if(Character.isLetter(ct.charAt(i))==true)
    			{	
    			int a =((ct.charAt(i)-'a')+(26-(key.charAt(i)-'a')))%26+'a';
    			char x=(char)a;
    			Plain_text+=x;
    			}
    			else
    			{
    				Plain_text+=ct.charAt(i);
    			}
    			
    		}
    	}
    	
    }
    /*   
     * 
     * Shift Cipher
     * 
     */
    public int shift_key()
    {
    	int key=0;
    	SharedPreferences key_pref=PreferenceManager.getDefaultSharedPreferences(this);
    	key=key_pref.getInt("shift_key", 3);
    	

    	return key;
    	
    }
    public void shift_cipher(View view)
    {
    	int key=shift_key();
    	for(int i=0;i<Plain_text.length();i++)
    	{
    		if(Character.isLetter(Plain_text.charAt(i))==true)
    		{int a=(((Plain_text.charAt(i))-'a')+key)%26+'a';
    		char x=(char)a;
    		Cipher_text+=x;
    		}
    		else
    		{
    			Cipher_text+=Plain_text.charAt(i);
    		}
    	}
    	
   
    	
    	
    }
    public int get_cipher(int i)
    {
    	
    	SharedPreferences cipher_pref=PreferenceManager.getDefaultSharedPreferences(this);
    	int c=0;
    	String pcipher=new String();
    	switch(i)
    	{
    	case 1:
    		pcipher=cipher_pref.getString("cipher_list", "1");
        	c=Integer.parseInt(pcipher);
    		break;
    	case 2:
    		pcipher=cipher_pref.getString("second_list", "0");
        	c=Integer.parseInt(pcipher);
    		break;
    		default:
    			
    	}
    	return c;
    }
    public void shift_decipher(View view)
    {
    	int key=shift_key();
    	
    	for(int i=0;i<Cipher_text.length();i++)
    	{
    		if(Character.isLetter(Cipher_text.charAt(i))==true)
    		{
    		int a=(((Cipher_text.charAt(i))-'a')+(26-key))%26+'a';
    		char x=(char)a;
    		Plain_text+=x;
    		}
    		else
    		{
    			Plain_text+=Cipher_text.charAt(i);
    		}
    		
    			
    	}
    		
    	
    	
    }
    
    
    /*
     * 
     * On button Click events
     * 
     */
    public void encrypt(View view)
    {

    	
    	Context context=getApplicationContext();
    	CharSequence under_construction="Sorry, this feature is still under development!";
    	int duration=Toast.LENGTH_SHORT;
    	Toast toast=Toast.makeText(context, under_construction, duration);
    	
    	
    	Cipher_text="";
    	EditText editText=(EditText) findViewById(R.id.Inputtext);
    	Plain_text=editText.getText().toString();
    	Plain_text=Plain_text.toLowerCase();
    
    	
    	cipher=get_cipher(1);
    	switch(cipher)
    	{
    	case 1:
    		shift_cipher(view);
    		
    		
    		break;
    	case 2:
    		vigenere_cipher(view);
    		
    		
    		break;
    	case 3:
    		//toast.show();
    		affine_cipher();
    		
    		
    		break;
    	case 4:
    		toast.show();
    		hill_cipher();
    		
    		
    		break;
    	default:
    		break;
    	}

    	EditText textview=(EditText) findViewById(R.id.Outputtext);
    	textview.setText(Cipher_text);
    	
    	
    }
    public void decrypt(View view)
    {

    	Context context=getApplicationContext();
    	CharSequence under_construction="Sorry, this cipher is still under development!";
    	int duration=Toast.LENGTH_SHORT;
    	Toast toast=Toast.makeText(context, under_construction, duration);
    	
    	
    	
    	Plain_text="";
    	EditText editText=(EditText)findViewById(R.id.Outputtext);
    	Cipher_text=editText.getText().toString();
    	Cipher_text=Cipher_text.toLowerCase();
    	cipher=get_cipher(1);
    	switch(cipher)
    	{
    	case 1:
    		shift_decipher(view);
    	
    		
    		break;
    	case 2:
    		vigenere_decipher(view);
    	
    		break;
    	case 3:
    		//toast.show();
    		affine_decipher();
    	
    		
    		break;
		case 4:
			toast.show();
    		break;
    	default:
    		break;
    	}
    	EditText textview=(EditText) findViewById(R.id.Inputtext);
    	textview.setText(Plain_text);
    }
    
    public void opensettings()
    {
    	Intent intent=new Intent(this,Display_Settings.class);
    	startActivity(intent);
    }
    public void openshare()
    {
    	Intent intent=new Intent();
    	intent.setAction(Intent.ACTION_SEND);
    	intent.putExtra(Intent.EXTRA_TEXT, Cipher_text);
    	intent.setType("text/plain");
    	startActivity(Intent.createChooser(intent, getResources().getText(R.string.share_title)));
    	
    	
    }
    
   
    
}

    