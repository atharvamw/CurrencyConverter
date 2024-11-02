import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import java.util.*;
import java.text.*;
import java.util.stream.Collectors;

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
		
		StockMarket stockmarket = new StockMarket("Stock Market Exchange");
		stockmarket.setBackground(new Color(48, 48, 48));
		
		
		homepage.ice.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent ae)
			{
				remove(homepage);
				add(international);
				validate();
				repaint();
			}
		});
		
		homepage.sme.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent ae)
			{
				remove(homepage);
				add(stockmarket);
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
		
		stockmarket.home.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				remove(stockmarket);
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
		mainBtnFormat(new AbstractButton[]{ice,sme},true);
		
		
		add(ice);
		add(sme);
		add(homeTitle);
		add(homeImage);
		
	}
	
	public static void mainBtnFormat(AbstractButton[] cmpArray, boolean extra)
	{
		for(AbstractButton cmp : cmpArray)
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
	static JLabel date = new JLabel("",JLabel.RIGHT);
	JLabel exIcon = new JLabel(new ImageIcon("exchange.png"));
	
	JButton home = new JButton(new ImageIcon("home.png"));

	Choice to = new Choice();
	Choice from = new Choice();
	JLabel Lfrom = new JLabel("From");
	JLabel Lamt = new JLabel("Amount");
	JLabel Lto = new JLabel("To");
	JLabel Lcon = new JLabel("Converted");
	JToggleButton Fcrypto = new JToggleButton("International");
	JToggleButton Tcrypto = new JToggleButton("International");
	
	JTextField Tfrom = new JTextField();
	JTextField Tto = new JTextField();
	
	JSlider decimalDigits = new JSlider(0,5,4);
	
	final static Color darkGreen = new Color(50, 120, 70);
	final static Color dark = new Color(48, 48, 48); 
	final static Color lime = new Color(180, 230, 100); 
	final static Font large = new Font("Garamond",Font.BOLD, 48);
	final static Font medium = new Font("Garamond",Font.BOLD, 38);
	final static Font small = new Font("Garamond",Font.BOLD, 28);
	final static Font verysmall = new Font("Garamond",Font.BOLD, 24);
	final static Font tiny = new Font("Garamond",Font.BOLD, 14);
	
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
			to.add(code);
		}
		
		from.select("USD");
		to.select("INR");
		
		String today = new Date().toString();
		date.setText(today.substring(0,10) + " " + today.substring(24));

		Homepage.mainBtnFormat(new AbstractButton[]{home,Fcrypto, Tcrypto},false);

		setLayout(null);
		
		giveForeground(new Component[]{exchange,date,Lfrom,Lto,Lamt,Lcon}, darkGreen);
		giveForeground(new Component[]{heading,from,to,Tfrom,Tto,decimalDigits,Fcrypto,Tcrypto}, lime);
		giveBackground(new Component[]{from,to,Tfrom,Tto,decimalDigits,home}, dark);
		giveBackground(new Component[]{heading,Fcrypto,Tcrypto}, darkGreen);
		
		Lfrom.setFont(verysmall); Lto.setFont(verysmall);
		Lamt.setFont(verysmall); Lcon.setFont(verysmall);
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
					Tcrypto.setFont(tiny);
					Fcrypto.setFont(tiny);
				}
				
				heading.setBounds(0,20,getWidth(),70);
				exchange.setBounds(0,200,getWidth(),50);
				date.setBounds(0,100,getWidth()-15,50);
				exIcon.setBounds(getWidth()/2-110,280,200,200);
				Lfrom.setBounds((getWidth()/2)-400,300,200,30);
				from.setBounds((getWidth()/2)-400,340,200,30);
				Fcrypto.setBounds((getWidth()/2)-530,342,120,30);
				Lamt.setBounds((getWidth()/2)-400,395,200,30);
				Tfrom.setBounds((getWidth()/2)-400,430,200,30);
				Lto.setBounds((getWidth()/2) + 150,300,200,30);
				to.setBounds((getWidth()/2) + 150,340,200,30);
				Tcrypto.setBounds((getWidth()/2)+359,342,120,30);
				Lcon.setBounds((getWidth()/2) + 150,395,200,30);
				Tto.setBounds((getWidth()/2) + 150,430,200,30);
				decimalDigits.setBounds((getWidth()/2) + 150,470,200,50);
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
		add(Lamt);
		add(Lcon);
		
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
	
	public static void giveForeground(Component[] arr, Color c)
	{
		for(Component element : arr)
		{
			element.setForeground(c);
		}
	}
	
	public static void giveBackground(Component[] arr, Color c)
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


class StockMarket extends JPanel
{	
	final String[] stockIndices = {"NIFTY 50", "NIFTY NEXT 50", "NIFTY 100", "NIFTY MIDCAP 100", "NIFTY MIDCAP 150", "NIFTY 200", "NIFTY 500", "NIFTY IT", "NIFTY BANK" };
	Choice stockOptions = new Choice();
	JLabel date = new JLabel(Screen.date.getText(),JLabel.RIGHT);
	JLabel heading;
	
	JButton home = new JButton(new ImageIcon("home.png"));
	
	JLabel Lindices = new JLabel("Select a Index",JLabel.CENTER);
	JLabel Lcurrent = new JLabel("Current");
	JLabel currentChange = new JLabel("");
	JTextField current = new JTextField();
	ImageIcon up = new ImageIcon("up.png");
	ImageIcon down = new ImageIcon("down.png");
	JLabel currentIcon = new JLabel("",JLabel.RIGHT);
	JLabel graphPanel = new JLabel();
	
	String Chartjson = "";
	
	
	HashMap<String, String> stockMap = new HashMap<>();
	
	public String quote(String str)
	{
			return "\"" + str + "\"";
	}
	
	StockMarket(String title)
	{
		setLayout(null);
		
		heading = new JLabel(title,JLabel.CENTER);
		heading.setFont(Screen.large);
		heading.setOpaque(true);
		heading.setBounds(0,20,1000,50);
		
		Homepage.mainBtnFormat(new AbstractButton[]{home},false);
		
		current.setEditable(false);
		
		stockOptions.setFocusable(false); home.setFocusable(false);
		Screen.giveForeground(new Component[]{date,Lindices,Lcurrent,currentChange}, Screen.darkGreen);
		Screen.giveBackground(new Component[]{heading}, Screen.darkGreen);
		Screen.giveForeground(new Component[]{heading,stockOptions, current}, Screen.lime);
		Screen.giveBackground(new Component[]{stockOptions,current,home}, Screen.dark);
		
		stockMap.put("NIFTY 50", ".NSEI");
		stockMap.put("NIFTY NEXT 50", ".NN50");
		stockMap.put("NIFTY 100", ".NIFTY100");
		stockMap.put("NIFTY MIDCAP 100", ".NIFMDCP100");
		stockMap.put("NIFTY MIDCAP 150", ".NIMI150");
		stockMap.put("NIFTY 200", ".NIFTY200");
		stockMap.put("NIFTY 500", ".NIFTY500");
		stockMap.put("NIFTY IT", ".NIFTYIT");
		stockMap.put("NIFTY BANK", ".NSEBANK");
		
		addComponentListener(new ComponentAdapter(){
			
			public void componentResized(ComponentEvent ce)
			{	
				if(getWidth()<750)
				{
					heading.setFont(Screen.medium);
					Lindices.setFont(Screen.verysmall);
					stockOptions.setFont(Screen.verysmall);
					Lcurrent.setFont(Screen.verysmall);
					current.setFont(Screen.verysmall);
					date.setFont(Screen.verysmall);
					currentChange.setFont(Screen.verysmall);
				}
				else if(getWidth()>=750)
				{
					heading.setFont(Screen.large);
					Lindices.setFont(Screen.medium);
					Lcurrent.setFont(Screen.verysmall);
					current.setFont(Screen.verysmall);
					stockOptions.setFont(Screen.medium);
					date.setFont(Screen.small);
					currentChange.setFont(Screen.verysmall);
				}
				
				heading.setBounds(0,15,getWidth(),70);
				date.setBounds(0,80,getWidth()-15,50);
				Lindices.setBounds(0,100,getWidth(),50);
				stockOptions.setBounds(getWidth()/4,150,getWidth()/2,50);
				graphPanel.setBounds(getWidth()/4,220,(int) (getWidth()/1.3) ,300);
				Lcurrent.setBounds(getWidth()/4,540,getWidth()/4,30);
				current.setBounds((int)(getWidth()/2.15),540,getWidth()/4,30);
				currentIcon.setBounds((int)(getWidth()/1.385),540,24,24);
				currentChange.setBounds((int)(getWidth()/2.15),570,getWidth()/4,30);
				home.setBounds(15,80,24,50);
				
			}
		});
		
		stockOptions.addItemListener(new ItemListener(){
			
			public void itemStateChanged(ItemEvent ie)
			{
				updateStockDashboard();
			}
		});
		
		for(String option : stockIndices)
		{
			stockOptions.add(option);
		}
		
		add(heading);
		add(date);
		add(Lindices);
		add(stockOptions);
		add(Lcurrent);
		add(current);
		add(currentIcon);
		add(currentChange);
		add(home);
		add(graphPanel);
		
		updateStockDashboard();	
	}
	
	public void updateStockDashboard()
	{
		String Chartjson = fetchJSON("https://api.tickertape.in/stocks/charts/inter/<<?>>?duration=1mo", stockOptions.getSelectedItem());
		String Stockjson = fetchJSON("https://quotes-api.tickertape.in/quotes?sids=<<?>>", stockOptions.getSelectedItem());

		StockValues indexValues = fetchStockValues(Stockjson, stockOptions.getSelectedItem());
		
		current.setText(indexValues.lastPrice + "");
		currentChange.setText(indexValues.change + " ( " + indexValues.pChange + "% )");
		
		Chartjson = fetch(Chartjson, quote("points"));
		
		ArrayList<String> chartData = new ArrayList<>( Arrays.asList( Chartjson.split(quote("v"))) );
		chartData.remove(chartData.size()-1);
		
		StringBuilder graphLabels = new StringBuilder();
		StringBuilder graphData = new StringBuilder();
		double minvalue = Integer.MAX_VALUE;
		
		for(String item : chartData)
		{
			graphLabels.append("%27" + fetch(item,quote("ts"),10) + "%27,");
			graphData.append(fetch(item,quote("lp")) + ",");
			
			double value = Double.parseDouble(fetch(item,quote("lp")));
			
			if(value < minvalue)
				minvalue=value;
		}
		
		String graphURL = "https://quickchart.io/chart?c={type:%27line%27,data:{labels:[<<labs?>>],%20datasets:[{label:%27<<name?>>%27,data:[<<data?>>]}]},options:{scales:{yAxes:[{ticks:{beginAtZero:true,min:<<minvalue?>>}}]}}}&backgroundColor=white&width=500&height=300&devicePixelRatio=1.2&format=png&version=2.9.3";
		
		graphURL = graphURL.replace("<<name?>>",stockOptions.getSelectedItem().replace(" ", "%20"));
		graphURL = graphURL.replace("<<labs?>>",graphLabels.toString());
		graphURL = graphURL.replace("<<data?>>",graphData.toString());
		graphURL = graphURL.replace("<<minvalue?>>",Double.toString(minvalue * 0.96) );
		
		try{
			
			URL url = new URL(graphURL);
			ImageIcon imageIcon = new ImageIcon(url);
			graphPanel.setIcon(imageIcon);
			
		}catch(Exception e){}

				
		if(indexValues.change < 0.0)
		{
			currentIcon.setIcon(down);
			currentChange.setForeground(Color.red);
		}
		else
		{
			currentIcon.setIcon(up);
			currentChange.setForeground(Color.green);
		}
		
	}
	
	public void paintComponent(Graphics g)
	{
		g.setColor(Screen.dark);
		g.fillRect(0,0,getWidth(),getHeight());
	}
	
	public String fetchJSON(String url , String stockSymbol)
	{	
		StringBuilder data = new StringBuilder();
		String line;
		
		try
		{
			URL apiURL = new URL(url.replace("<<?>>", stockMap.get(stockSymbol)));
			
			HttpURLConnection con = (HttpURLConnection) apiURL.openConnection();

			con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.61 Safari/537.36");
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			
			BufferedReader bf = new BufferedReader(new InputStreamReader(con.getInputStream()));
		
			while((line = bf.readLine())!=null)
			{
				data.append(line + "\n");
			}
			bf.close();

			return data.toString();
			
		}catch(Exception e){System.out.println("Exception in fetchData(): " + e);}
		
		return null;
	}
	
	public String fetch(String jsonString, String key, int... limit)
	{
		int index = jsonString.indexOf(key) + key.length() + 1;
		String s;
		s = jsonString.substring(index);

		if(jsonString.charAt(index) == '"')
		{
			if(limit.length<1)
				return jsonString.substring(index+1, index+1 + s.substring(1).indexOf('"')).trim();
			else
				return jsonString.substring(index+1, index+1 + limit[0]);
		}
		
		else if(jsonString.charAt(index) == '{')
		{
			StringBuilder res = new StringBuilder();
			int noOfBrackets = 1;
			int i = 1;
			while(noOfBrackets>0)
			{
					char c = jsonString.charAt(index+i);
					
					if(c == '{')
						noOfBrackets++;
					else if(c == '}')
					{
						if(--noOfBrackets <= 0)
							break;
					}
					
					res.append(c);
					i++;
					
			}
			return res.toString().trim();
			
		}
		
		else if(jsonString.charAt(index) == '[')
		{
			StringBuilder res = new StringBuilder();
			int noOfBrackets = 1;
			int i = 1;
			while(noOfBrackets>0)
			{
					char c = jsonString.charAt(index+i);
					
					if(c == '[')
					{
						noOfBrackets++;
					}
						
					else if(c == ']')
					{
						if(--noOfBrackets <= 0)
							break;
					}
					
					res.append(c);
					i++;
						
			}
			return res.toString().trim();
		}
		return jsonString.substring(index, index + s.indexOf(',')).trim();
	}
	
	public StockValues fetchStockValues(String json, String stockName)
	{	
		StockValues vals = new StockValues(fetch(json, quote("o")), fetch(json, quote("h")), fetch(json, quote("l")), fetch(json, quote("price")), fetch(json, quote("change")), fetch(json, quote("dyChange")));
		return vals;
	}
}

class StockValues 
{
	double open, dayHigh, dayLow, lastPrice,change, pChange;
	
	StockValues(String o, String dH, String dL, String lP,String ch, String pCh)
	{
		DecimalFormat df = new DecimalFormat("#.####");
		open = Double.parseDouble(df.format(Double.parseDouble(o)));
		dayHigh = Double.parseDouble(df.format(Double.parseDouble(dH)));
		dayLow = Double.parseDouble(df.format(Double.parseDouble(dL)));
		lastPrice = Double.parseDouble(df.format(Double.parseDouble(lP)));
		change = Double.parseDouble(df.format(Double.parseDouble(ch)));
		pChange = Double.parseDouble(df.format(Double.parseDouble(pCh)));
		
	}
}


