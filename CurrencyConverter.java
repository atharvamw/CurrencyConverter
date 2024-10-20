import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import java.util.*;
import java.text.*;

class CurrencyConverter extends JFrame
{	
	CurrencyConverter()
	{
		setTitle("Currency Convertor");
		setSize(1100,700);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Homepage homepage = new Homepage();
		homepage.setBackground(new Color(48, 48, 48));
		
		
		String internationalURL = "https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@latest/v1/currencies/";
		
		Screen international = new Screen(internationalURL, "International Currency Exchange");
		international.setBackground(new Color(48, 48, 48));
		
		
		homepage.ice.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent ae)
			{
				remove(homepage);
				add(international);
				validate();
				repaint();
			}
		});
		
		international.home.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				remove(international);
				add(homepage);
				validate();
				repaint();
			}
		});

		
		add(homepage,BorderLayout.CENTER);
		setVisible(true);
	}

	public static void main(String[] args)
	{
		new CurrencyConverter();
	}
	
}

class Homepage extends JPanel
{
	JButton ice = new JButton("International Currency Exchange");
	JButton sme = new JButton("Stock Market Exchange");
	
	JLabel homeImage = new JLabel(new ImageIcon("homepage.png"));
	JLabel homeTitle = new JLabel("<html><h1 style='color: green; font-size: 55px; font-family: Garamond'>Currency <br> Converter</h1></html>");
	
	Homepage()
	{
		setLayout(null);
		
		ice.setBounds(40,500,400,30);
		sme.setBounds(40,550,400,30);
		homeTitle.setBounds(80,50,300,300);
		homeImage.setBounds(500,50,500,500);
		mainBtnFormat(new JButton[]{ice,sme},true);
		
		
		add(ice);
		add(sme);
		add(homeTitle);
		add(homeImage);
		
	}
	
	public static void mainBtnFormat(JButton[] cmpArray, boolean extra)
	{
		for(JButton cmp : cmpArray)
		{
			cmp.setFocusPainted(false);
			cmp.setBorderPainted(false);
			cmp.setContentAreaFilled(false);
			cmp.setOpaque(true);
			cmp.setFont(new Font("Garamond",Font.BOLD,24));
			
			if(extra)
			{
				cmp.setBackground(new Color(28, 99, 47));
				cmp.setForeground(new Color(255, 255, 255));
				cmp.addMouseListener(new MouseAdapter(){
					
					public void mouseEntered(MouseEvent me)
					{
						cmp.setBackground(new Color(18, 84, 36));
					}
					public void mouseExited(MouseEvent me)
					{
						cmp.setBackground(new Color(28, 99, 47));
					}
					public void mousePressed(MouseEvent me)
					{
						cmp.setBackground(new Color(45, 95, 50));
					}	
					public void mouseReleased(MouseEvent me)
					{
						cmp.setBackground(new Color(18, 84, 36));
					}	
				});
			}
		}
	}
}

class Screen extends JPanel
{
	JLabel heading;
	JLabel exchange = new JLabel("1 USD = 83 INR",JLabel.CENTER);
	JLabel date = new JLabel("1st October 2024",JLabel.RIGHT);
	JLabel exIcon = new JLabel(new ImageIcon("exchange.png"));
	
	JButton home = new JButton(new ImageIcon("home.png"));

	Choice to = new Choice();
	Choice from = new Choice();
	JLabel Lfrom = new JLabel("From");
	JLabel Lto = new JLabel("To");
	JToggleButton Fcrypto = new JToggleButton("International");
	JToggleButton Tcrypto = new JToggleButton("International");
	
	JTextField Tfrom = new JTextField();
	JTextField Tto = new JTextField();
	
	JSlider decimalDigits = new JSlider(0,5,4);
	
	final Color darkGreen = new Color(28, 99, 47);
	final Color dark = new Color(48, 48, 48); 
	final Color lime = new Color(180, 230, 100); 
	final Font large = new Font("Garamond",Font.BOLD, 48);
	final Font medium = new Font("Garamond",Font.BOLD, 38);
	final Font small = new Font("Garamond",Font.BOLD, 28);
	final Font verysmall = new Font("Garamond",Font.BOLD, 24);
	
	String apiURL;
	URL url;
	HttpURLConnection con;
	BufferedReader bf;
	Double rate;
	StringBuilder data = new StringBuilder();
	String usedCurrency = "USD";
	int usedIndex=0;
	String currentSelection = "";
	
	String[] currencyCodes = {  "AFN", "ALL", "AOA", "ARS", "AMD", "AWG", "AUD", "AZN", "BAM", "BBD", "BDT", "BGN", "BHD", "BIF", "BMD", 
												"BND", "BOB", "BRL", "BSD", "BTN", "BWP", "BYN", "BZD", "CAD", "CDF", "CHF", "CLP", "CNY", "COP", "CRC", "CUP", 
												"CZK", "DJF", "DKK", "DOP", "DZD", "EUR", "EGP", "ERN", "ETB", "FJD", "FKP", "GBP", "GEL", "GHS", "GMD", "GNF", "GTQ", 
												"GYD", "HKD", "HNL", "HRK", "HTG", "HUF", "IDR", "ILS", "IMP", "INR", "IQD", "IRR", "ISK", "JEP", "JPY", "KES", 
												"KGS", "KHR", "KPW", "KRW", "KWD", "KYD", "KZT", "LAK", "LBP", "LKR", "LRD", "LSL", "LYD", "MAD", "MDL", "MGA", 
												"MKD", "MMK", "MNT", "MOP", "MRU", "MUR", "MVR", "MWK", "MXN", "MYR", "MZN", "NAD", "NGN", "NIO", "NOK", "NPR", 
												"NZD", "OMR", "PAB", "PEN", "PGK", "PHP", "PKR", "PLN", "PYG", "QAR", "RON", "RSD", "RUB", "RWF", "SAR", "SBD", 
												"SCR", "SDG", "SEK", "SGD", "SHP", "SLL", "SOS", "SRD", "SSP", "STN", "SYP", "SZL", "THB", "TJS", "TMT", "TND", 
												"TOP", "TRY", "TTD", "TWD", "TZS", "UAH", "UGX", "USD", "UYU", "UZS", "VES", "VND", "VUV", "WST", "XAF", "XAG", 
												"XAU", "XCD", "XDR", "XOF", "XPF", "YER", "ZAR", "ZMW", "ZWL" };
												
	String[] cryptoCodes = { "APE", "BAKE", "BAT", "BCH", "BNB", "BSV", "BTC", "BTCB", "BTG", "CRO", "CRV", "DOGE", "DOT", "ENJ", "ETC", "ETH", "FIL", "GMX", "GT", "HT", 
							"KCS", "LDO", "LEO", "LTC", "MANA", "MBX", "RVN", "SHIB", "SOL", "TRX", "TWT", "USDP", "USDT", "XAUT", "XEC" };


	
	Screen(String url, String title)
	{						
		apiURL = url;
		heading = new JLabel(title,JLabel.CENTER);
		
		for(String code : currencyCodes)
		{
			from.add(code);
			if(!code.equals("USD"))
			to.add(code);
		}
		
		from.select("USD");
		to.select("INR");
		
		String today = new Date().toString();
		date.setText(today.substring(0,10) + " " + today.substring(24));

		Homepage.mainBtnFormat(new JButton[]{home},false);
		Fcrypto.setFocusPainted(false);
		Fcrypto.setContentAreaFilled(false);
        Fcrypto.setOpaque(true);
		
		Tcrypto.setFocusPainted(false);
		Tcrypto.setContentAreaFilled(false);
        Tcrypto.setOpaque(true);

		setLayout(null);
		
		giveForeground(new Component[]{exchange,date,Lfrom,Lto}, darkGreen);
		giveForeground(new Component[]{heading,from,to,Tfrom,Tto,decimalDigits,Fcrypto,Tcrypto}, lime);
		giveBackground(new Component[]{from,to,Tfrom,Tto,decimalDigits,home}, dark);
		giveBackground(new Component[]{heading,Fcrypto,Tcrypto}, darkGreen);
		
		Lfrom.setFont(verysmall); Lto.setFont(verysmall);
		Tfrom.setFont(verysmall); Tto.setFont(verysmall);
		from.setFont(verysmall); to.setFont(verysmall);
		from.setFocusable(false); to.setFocusable(false);
		Tfrom.setMargin(new Insets(0, 3, 0, 0));
		Tto.setMargin(new Insets(0, 3, 0, 0));
		Tto.setEditable(false);
		decimalDigits.setMajorTickSpacing(1);
		decimalDigits.setPaintLabels(true);
		decimalDigits.setPaintTicks(true);
		decimalDigits.setPaintTrack(true);
		
		heading.setOpaque(true);
		
		addComponentListener(new ComponentAdapter(){
			
			public void componentResized(ComponentEvent ce)
			{	
				if(getWidth()<750)
				{
					heading.setFont(medium);
					exchange.setFont(verysmall);
					date.setFont(verysmall);
				}
				else if(getWidth()>=750)
				{
					heading.setFont(large);
					exchange.setFont(medium);
					date.setFont(small);
				}
				
				heading.setBounds(0,20,getWidth(),70);
				exchange.setBounds(0,200,getWidth(),50);
				date.setBounds(0,100,getWidth()-15,50);
				exIcon.setBounds(getWidth()/2-110,280,200,200);
				Lfrom.setBounds((getWidth()/2)-400,300,200,30);
				from.setBounds((getWidth()/2)-400,340,200,30);
				Fcrypto.setBounds((getWidth()/2)-520,342,110,30);
				Tfrom.setBounds((getWidth()/2)-400,390,200,30);
				Lto.setBounds((getWidth()/2) + 150,300,200,30);
				to.setBounds((getWidth()/2) + 150,340,200,30);
				Tcrypto.setBounds((getWidth()/2)+359,342,110,30);
				Tto.setBounds((getWidth()/2) + 150,390,200,30);
				decimalDigits.setBounds((getWidth()/2) + 150,430,200,50);
				home.setBounds(15,100,24,50);
			}
		});
		
		add(heading);
		add(exchange);
		add(date);
		add(exIcon);
		add(Lfrom); add(Tfrom); add(from);
		add(Lto); add(Tto); add(to);
		add(decimalDigits);
		add(home);
		add(Fcrypto);
		add(Tcrypto);
		
		Tfrom.addKeyListener(new AllowOnlyDigits());
		Tto.addKeyListener(new AllowOnlyDigits());
		
		from.addItemListener(new ItemListener(){
			
			public void itemStateChanged(ItemEvent ie){fromUpdate(ie);}
		});
		
		to.addItemListener(new ItemListener(){
			
			public void itemStateChanged(ItemEvent ie){  toUpdate(ie);  }
			
		});
		
		Tfrom.getDocument().addDocumentListener(new DocumentListener(){
			
			public void changedUpdate(DocumentEvent te){}
			public void insertUpdate(DocumentEvent te)
			{
				String destCurrency = to.getSelectedItem().toString().toLowerCase();
				String amtStr = Tfrom.getText();
				try
				{	
					if(!amtStr.equals(""))
					{
						double amount = Double.parseDouble(amtStr);
						double ans = amount * rate;
						DecimalFormat df = new DecimalFormat("#.#####");
						String[] result = df.format(ans).split("[.]");
						
						if(decimalDigits.getValue()!=0 && result.length>1)
						{
							int end = result[1].length()<decimalDigits.getValue()?result[1].length():decimalDigits.getValue();
							Tto.setText(result[0] + "." + result[1].substring(0,end));
						}
						else
						Tto.setText(result[0]);
					}
					else
					{
						Tto.setText("");
					}
				}
				catch(Exception e){System.out.println(e);}
			}
			public void removeUpdate(DocumentEvent te)
			{
				insertUpdate(te);
			}
		});
		
		fromUpdate(new ItemEvent(from, ItemEvent.ITEM_STATE_CHANGED, from.getSelectedItem(), ItemEvent.SELECTED));
		
		Fcrypto.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent ae)
			{
				if(Fcrypto.isSelected())
				{
					Fcrypto.setText("Crypto");
					from.removeAll();
					
					for(int i=0;i<cryptoCodes.length;i++)
					{	
						from.add(cryptoCodes[i]);
						if(cryptoCodes[i].equals(from.getSelectedItem()))
						{
							usedCurrency = cryptoCodes[i];
							usedIndex = i;
						}
					}
					try{to.remove(usedCurrency);}catch(Exception e){}

				}
				else
				{
					Fcrypto.setText("International");
					from.removeAll();
					for(int i=0;i<currencyCodes.length;i++)
					{	
						from.add(currencyCodes[i]);
						if(currencyCodes[i].equals(from.getSelectedItem()))
						{
							usedCurrency = currencyCodes[i];
							usedIndex = i;
						}
					}
					try{to.remove(usedCurrency);}catch(Exception e){}
					
				}
				fromUpdate(new ItemEvent(from, ItemEvent.ITEM_STATE_CHANGED, from.getSelectedItem(), ItemEvent.SELECTED));
			}
		});
		
		Tcrypto.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent ae)
			{
				if(Tcrypto.isSelected())
				{
					Tcrypto.setText("Crypto");
					to.removeAll();
					for(int i=0;i<cryptoCodes.length;i++)
					{	
						to.add(cryptoCodes[i]);
						if(cryptoCodes[i].equals(from.getSelectedItem()))
						{
							to.remove(cryptoCodes[i]);
							usedCurrency = cryptoCodes[i];
							usedIndex = i;
						}
					}
						
							
				}
				else
				{
					Tcrypto.setText("International");
					to.removeAll();
					for(int i=0;i<currencyCodes.length;i++)
					{	
						to.add(currencyCodes[i]);
						if(currencyCodes[i].equals(from.getSelectedItem()))
						{
							to.remove(currencyCodes[i]);
							usedCurrency = currencyCodes[i];
							usedIndex = i;
						}
					}
				}
				toUpdate(new ItemEvent(to, ItemEvent.ITEM_STATE_CHANGED, to.getSelectedItem(), ItemEvent.SELECTED));
			}
		});
	}
	
	public void toUpdate(ItemEvent ie)
	{
		int index = data.indexOf(ie.getItem().toString().toLowerCase());
		rate = Double.parseDouble(data.toString().substring(index+6,index+14));
		System.out.println(rate);
		Tto.setText("");
		Tfrom.setText("");
		exchange.setText("1 " + from.getSelectedItem() + " = " + rate + " " + to.getSelectedItem());
	}
	
	public void fromUpdate(ItemEvent ie)
	{
		data.setLength(0);
		currentSelection = to.getSelectedItem();  
												 
		if(Fcrypto.isSelected() == Tcrypto.isSelected())
		{
			to.insert(usedCurrency,usedIndex);   
			usedCurrency = from.getSelectedItem();
			usedIndex = from.getSelectedIndex();
			to.remove(usedCurrency);
			
		}
			
		to.select(currentSelection);
		
		try
		{
			url = new URL(apiURL+from.getSelectedItem().toLowerCase()+".json");
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			bf = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line = "";
			while((line=bf.readLine())!=null)
			{
				data.append(line+"\n");
			}	
		}
		catch(Exception e){System.out.println(e);}
				
		exchange.setText("1 " + from.getSelectedItem() + "= " + rate + " " + to.getSelectedItem());
		toUpdate(new ItemEvent(to, ItemEvent.ITEM_STATE_CHANGED, to.getSelectedItem(), ItemEvent.SELECTED));
		
	}
	
	public void giveForeground(Component[] arr, Color c)
	{
		for(Component element : arr)
		{
			element.setForeground(c);
		}
	}
	
	public void giveBackground(Component[] arr, Color c)
	{
		for(Component element : arr)
		{
			element.setBackground(c);
		}
	}
	
}

class AllowOnlyDigits extends KeyAdapter
{
	@Override
	public void keyTyped(KeyEvent ke)
	{
		if(!Character.isDigit(ke.getKeyChar()) && ke.getKeyChar()!='.')
		{
			ke.consume();
		}
	}
}

