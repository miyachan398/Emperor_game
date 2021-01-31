import java.net.*;
import java.io.*;
import javax.swing.*;
import java.lang.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.awt.image.*;//画像処理に必要
import java.awt.geom.*;//画像処理に必要
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import java.awt.Container;
import java.awt.BorderLayout;
import java.applet.*;//wavファイルの再生に使用

public class MyGame extends JFrame implements MouseListener,MouseMotionListener {
	private JButton buttonArray[], title;//ボタン用の配列
	private Container c;
	private AudioClip bgm, se;
	private JLabel titleLabel, winLabel, loseLabel;//勝利判定と、タイトルを表示するためのJLabel
	
	
	private int myStatus;//皇帝側か奴隷側かの判別
	
	private int slaveNum = 11;
	private int emperorNum = 11;
	
	private ImageIcon titleIcon, citizenIcon, emperorIcon, slaveIcon, playIcon, cardIcon, backIcon, winIcon, loseIcon;
	
	PrintWriter out;//出力用のライタ
	
	public int theBNum = 12;
	public int theENum = 12;
	public int theSNum = 12;
	
	private int emperorTurn = 1;//皇帝側のターン数
	private int slaveTurn = 1;//奴隷側のターン数
	
	private int emperorSelect, slaveSelect, judgeE, judgeS;
	
	boolean isTitle,isStart, isCheck, myPlay, yourPlay, emperorPlay, slavePlay, numberHold;
	
	String[] str = {"5","6","7","8","9","10","11","12","13"};//文字列

	public MyGame() {
		
		emperorSelect = slaveSelect = 12;
		
		judgeE = judgeS = 0;
		
		myPlay = yourPlay = false;
		emperorPlay = slavePlay = false;
		isTitle = isStart = isCheck = numberHold = true;
		
		
		
		
		//名前の入力ダイアログを開く
		String myName = JOptionPane.showInputDialog(null,"名前を入力してください","名前の入力",JOptionPane.QUESTION_MESSAGE);
		if(myName.equals("")){
			myName = "No name";//名前がないときは，"No name"とする
		}
		
		//IPアドレスの入力ダイアログを開く
		String myAdress = JOptionPane.showInputDialog(null,"IPアドレスを入力してください","IPアドレスの入力",JOptionPane.QUESTION_MESSAGE);

		//ウィンドウを作成する
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//ウィンドウを閉じるときに，正しく閉じるように設定する
		setTitle("MyGame");//ウィンドウのタイトルを設定する
		setSize(1200,900);//ウィンドウのサイズを設定する
		c = getContentPane();//フレームのペインを取得する

		//アイコンの設定
		titleIcon = new ImageIcon("Title.png");
		slaveIcon = new ImageIcon("slave_card.png");
		emperorIcon = new ImageIcon("emperor.png");
		citizenIcon = new ImageIcon("citizen.png");
		cardIcon = new ImageIcon("card.png");
		playIcon = new ImageIcon("play.png");
		backIcon = new ImageIcon("table.png");
		winIcon = new ImageIcon("Win.png");
		loseIcon = new ImageIcon("Lose.png");
		
		//JLabelno設定（勝敗時のポップアップ）
		winLabel = new JLabel(winIcon);
		loseLabel = new JLabel(loseIcon);
		
		//勝利時のポップアップの画像を表示する準備
		c.add(winLabel);
		winLabel.setBounds(570,380,800,300);
		winLabel.setVisible(false);
		
		//敗北時のポップアップの画像を表示する準備
		c.add(loseLabel);
		loseLabel.setBounds(570,380,800,300);
		loseLabel.setVisible(false);
		
		
		c.setLayout(null);//自動レイアウトの設定を行わない
		
		//ボタンの生成
		buttonArray = new JButton[12];//ボタンの配列を５個作成する[0]から[10]まで使える
		for(int i=0;i<12;i++){
			
			//ボタンにアイコンを設定する
			buttonArray[i] = new JButton(citizenIcon);
			c.add(buttonArray[i]);//ペインに貼り付ける
			
			
			buttonArray[i].setBounds(i*200 + 450,760,180,230);//ボタンの大きさと位置を設定する．(x座標，y座標,xの幅,yの幅）
			
			//Playボタンの設定
			if(i==5){
				buttonArray[i].setBounds(1650,425,180,130);//ボタンの大きさと位置を設定する．(x座標，y座標,xの幅,yの幅）
				buttonArray[i].setIcon(playIcon);
			}
			
			//相手側の手札のボタン設定
			if(i > 5){
				buttonArray[i].setBounds((i%6)*200 + 450,10,180,230);//ボタンの大きさと位置を設定する．(x座標，y座標,xの幅,yの幅）
				buttonArray[i].setIcon(cardIcon);
			}
			
			if(i == 11){
				buttonArray[i].setBounds(0, 0, 1950, 1000);//ボタンの大きさと位置を設定する．(x座標，y座標,xの幅,yの幅）
				buttonArray[i].setIcon(titleIcon);
			}
			
			buttonArray[i].addMouseListener(this);//ボタンをマウスでさわったときに反応するようにする
			buttonArray[i].addMouseMotionListener(this);//ボタンをマウスで動かそうとしたときに反応するようにする
			buttonArray[i].setActionCommand(Integer.toString(i));
			buttonArray[i].setVisible(false);
		}
		
		
		
		JLabel backLabel = new JLabel(backIcon); //背景画像の準備
		c.add(backLabel, BorderLayout.NORTH); //パスボタンをコンテナに追加
		backLabel.setBounds(0,0, 1950, 1000);//背景画像の置き場所を決定(x座標，y座標,xの幅,yの幅)
		
		//音源の読み込み
        bgm = Applet.newAudioClip(getClass().getResource("energy.wav"));
        se = Applet.newAudioClip(getClass().getResource("button.wav"));
		
		
		bgm.loop();	//音源の再生
		
		
		
		//サーバに接続する
		Socket socket = null;
		try {
			
			//"localhost"は，自分内部への接続．localhostを接続先のIP Address（"133.42.155.201"形式）に設定すると他のPCのサーバと通信できる
			//10000はポート番号．IP Addressで接続するPCを決めて，ポート番号でそのPC上動作するプログラムを特定する
			socket = new Socket("localhost", 10000);
			
		} catch (UnknownHostException e) {
			
			System.err.println("ホストの IP アドレスが判定できません: " + e);
			
		} catch (IOException e) {
			
			System.err.println("エラーが発生しました: " + e);
			
		}
		
		MesgRecvThread mrt = new MesgRecvThread(socket, myName);//受信用のスレッドを作成する
		mrt.start();//スレッドを動かす（Runが動く）
		
	}
		
	//メッセージ受信のためのスレッド
	public class MesgRecvThread extends Thread {
		
		Socket socket;
		String myName;
		
		public MesgRecvThread(Socket s, String n){
			
			socket = s;
			myName = n;
			
		}
		
		//通信状況を監視し，受信データによって動作する
		public void run() {
			
			try{
				
				System.out.println(theBNum);
				
				
				
				InputStreamReader sisr = new InputStreamReader(socket.getInputStream());
				BufferedReader br = new BufferedReader(sisr);
				out = new PrintWriter(socket.getOutputStream(), true);
				out.println(myName);//接続の最初に名前を送る
				
				String myNumberStr = br.readLine();//サーバが送った番号を受け取れる
				System.out.println(myNumberStr);
				int myNumberInt = Integer.parseInt(myNumberStr);
				
				//皇帝か奴隷か決める
				if(myNumberInt % 2 == 0){
					
					myStatus = 0;//皇帝側
					
				}else{
					
					myStatus = 1;//奴隷側
					
				}
				
				if(myStatus == 0){//皇帝側のカードの絵柄の設定
				
					for(int a=0;a<5;a++){
						
						buttonArray[a].setIcon(citizenIcon);
						
					}
					for(int a=6;a<11;a++){
						
						buttonArray[a].setIcon(cardIcon);
						
					}
					
					buttonArray[0].setIcon(emperorIcon);
					buttonArray[5].setIcon(playIcon);//Playボタン
					
				}else{//奴隷側のカードの絵柄の設定
				
					for(int a=0;a<5;a++){
						
						buttonArray[a].setIcon(citizenIcon);
						
					}
					for(int a=6;a<11;a++){
						
						buttonArray[a].setIcon(cardIcon);
						
					}
					
					buttonArray[0].setIcon(slaveIcon);
					buttonArray[5].setIcon(playIcon);//Playボタン
					
				}
				
				while(true) {
					
					if(isStart){
						
						if(isTitle){
							
							buttonArray[11].setVisible(true);//タイトルを表示
							
						}else{
							for(int a = 0; a < 11; a++){
								
								buttonArray[a].setVisible(true);//タイトルが消えてから手札を表示
								isStart = false;
							}
						}
					}
						
					
					//勝利判定に移行させる
					if(judgeE != 0 && judgeS != 0){
						
						if(judgeE == judgeS){
							
							isCheck = false;
							judge(emperorSelect, slaveSelect);
							
							isCheck = true;
							judgeE = judgeS = 0;
							
						}
					
					}
					
					String inputLine = br.readLine();//データを一行分だけ読み込んでみる
					
					if (inputLine != null) {//読み込んだときにデータが読み込まれたかどうかをチェックする
					
						System.out.println(inputLine);//デバッグ（動作確認用）にコンソールに出力する
						String[] inputTokens = inputLine.split(" ");	//入力データを解析するために、スペースで切り分ける
						String cmd = inputTokens[0];//コマンドの取り出し．１つ目の要素を取り出す
						
						if(cmd.equals("MOVE")){//cmdの文字と"MOVE"が同じか調べる．同じ時にtrueとなる
						
							isCheck = true;
							//MOVEの時の処理(コマの移動の処理)
							String theBName = inputTokens[1];//ボタンの名前（番号）の取得
							
							//持ち手札の中から場に出すカードを選んでいるかどうかの検知
							if(Arrays.asList(str).contains(theBName)){
							}else{
								
								theBNum = Integer.parseInt(theBName);//選んだカードの配列番号
								
							}
							
						}
						
						//皇帝側に何のカードを出したかを送る（自分のカードは場に出ているが、対戦相手の画面にはまだ表示されていない為）
						if(cmd.equals("EMPERORRECEIVE")){//cmdの文字と"EMPERORRECEIVE"が同じか調べる．同じ時にtrueとなる
							
							emperorPlay = true;
							
							
							String theEName = inputTokens[1];//ボタンの名前（番号）の取得
							theENum = Integer.parseInt(theEName);
							
							slaveSelect = Integer.parseInt(inputTokens[2]);//奴隷側のプレイヤーが選んだカードの配列番号
							
							if(theENum == 0){//奴隷側が奴隷を出した時
								slaveNum = theENum;
							}else{
								slaveNum = theENum + 6;
							}
							
							
							slavePlay = false;
							
							receive();
							
							judgeE = 1;
							
						}
						
						//奴隷側に何のカードを場に出したかを送る（自分のカードは場に出ているが、対戦相手の画面にはまだ表示されていない為）
						if(cmd.equals("SLAVERECEIVE")){//cmdの文字と"SLAVERECEIVE"が同じか調べる．同じ時にtrueとなる
							
							slavePlay = true;
							
							String theSName = inputTokens[1];//ボタンの名前（番号）の取得
							theSNum = Integer.parseInt(theSName);
							
							emperorSelect = Integer.parseInt(inputTokens[2]);//皇帝側のプレイヤーが選んだカードの配列番号
							
							if(theSNum == 0){//皇帝側が皇帝を出した時
								emperorNum = theSNum;
							}else{
								emperorNum = theSNum + 6;
							}
							emperorPlay = false;
							
							
							receive();
							
							judgeS = 1;
							
						}
						
					}else{
						
						break;
						
					}
				
				}
				
				socket.close();
				
			} catch (IOException e) {
				
				System.err.println("エラーが発生しました: " + e);
				
			}
		}
	}


	public static void main(String[] args) {
		MyGame net = new MyGame();
		net.setVisible(true);
		new SoundTestWav();
		
	}
  	
	public void mouseClicked(MouseEvent e) {//ボタンをクリックしたときの処理
	
		
		
		JButton theButton = (JButton)e.getComponent();//クリックしたオブジェクトを得る．型が違うのでキャストする
		String theArrayIndex = theButton.getActionCommand();//ボタンの配列の番号を取り出す
		
		//タイトル画面をクリックした際に非表示にする
		if(theArrayIndex.equals("11")){
			isTitle = false;
			
			buttonArray[11].setVisible(false);
		}
		
		Point theMLoc = e.getPoint();
		System.out.println(theMLoc);//theIconには，現在のボタンに設定されたアイコンが入る
		Point theBtnLocation = theButton.getLocation();
		
		//送信情報を作成する（受信時には，この送った順番にデータを取り出す．スペースがデータの区切りとなる）
		String msg = "MOVE"+" "+theArrayIndex;
		System.out.println(theArrayIndex);
		//サーバーに情報を送る
		out.println(msg);//送信データをバッファに書き出す
		out.flush();//送信データをフラッシュ（ネットワーク上にはき出す）する
		
		
		//選んだ手札の配列番号を
		if(isCheck){
			if(theBNum < 5){
				
				se.play();//ボタンクリック時のSEの再生
				
				if(theArrayIndex.equals("5")){
					
					
					myPlay = true;
					//System.out.println("theENum"+theENum+"theSNum"+theSNum);
					
					play();//選択したカードの移動
					
					isCheck = false;
				}
				
			}
		
		}

		repaint();//オブジェクトの再描画を行う
	}
	
	public void mouseEntered(MouseEvent e) {
	}
	
	public void mouseExited(MouseEvent e) {
	}
	
	public void mousePressed(MouseEvent e) {
	}
	
	public void mouseReleased(MouseEvent e) {
	}
	
	public void mouseDragged(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
		
		// int theMLocX = e.getX();//マウスのx座標
		// int theMLocY = e.getY();//マウスのy座標
		//System.out.println(theMLocX+","+theMLocY);
	}
	
	
	//プレイヤーの手札を場に出すための関数（ターン数とプレイヤーが何を出すかによってによってカードの配置が決まる）
	public void play(){
		
		if(myPlay == true){
			int k = 0;
			
			
			int cardX = 870;
			int cardY = 500;
			int cardZ = 250;
			
			if(theBNum != 0 && theBNum < 5){
				if(myStatus == 0){
					switch(emperorTurn){
						
						case 1:
						
							for(k=0;k < 4;k++){
								buttonArray[k].setBounds(k*200 + 560,760,180,230);
								System.out.println(buttonArray[k].getLocation());
							}

							buttonArray[4].setLocation(cardX,cardY);
							
							if(myStatus == 0){
								
								//送信情報を作成する（受信時には，この送った順番にデータを取り出す．スペースがデータの区切りとなる）
								String msg = "SLAVERECEIVE"+" "+4+" "+theBNum;
								//サーバーに情報を送る
								out.println(msg);//送信データをバッファに書き出す
								out.flush();//送信データをフラッシュ（ネットワーク上にはき出す）する
								
							}
							
							theBNum = 12;
							myPlay = false;
							break;
							
						case 2:

							for(k=0;k < 3;k++){
								buttonArray[k].setLocation(k*200 + 660,760);
							}
							
							buttonArray[3].setLocation(cardX,cardY);
							
							if(myStatus == 0){
								
								//送信情報を作成する（受信時には，この送った順番にデータを取り出す．スペースがデータの区切りとなる）
								String msg = "SLAVERECEIVE"+" "+3+" "+theBNum;
								//サーバーに情報を送る
								out.println(msg);//送信データをバッファに書き出す
								out.flush();//送信データをフラッシュ（ネットワーク上にはき出す）する
								
							}

							myPlay = false;
							theBNum = 12;
							break;
						
						case 3:
						
							for(k=0;k < 2;k++){
								buttonArray[k].setLocation(k*200 + 760,760);
							}
							
							buttonArray[2].setLocation(cardX,cardY);
							
							if(myStatus == 0){
								
								//送信情報を作成する（受信時には，この送った順番にデータを取り出す．スペースがデータの区切りとなる）
								String msg = "SLAVERECEIVE"+" "+2+" "+theBNum;
								//サーバーに情報を送る
								out.println(msg);//送信データをバッファに書き出す
								out.flush();//送信データをフラッシュ（ネットワーク上にはき出す）する
								
							}
							
							theBNum = 12;
							myPlay = false;
							break;
						
						case 4:
							buttonArray[0].setLocation(cardX,760);
							
							buttonArray[1].setLocation(cardX,cardY);
							
							if(myStatus == 0){
								
								//送信情報を作成する（受信時には，この送った順番にデータを取り出す．スペースがデータの区切りとなる）
								String msg = "SLAVERECEIVE"+" "+1+" "+theBNum;
								//サーバーに情報を送る
								out.println(msg);//送信データをバッファに書き出す
								out.flush();//送信データをフラッシュ（ネットワーク上にはき出す）する
								
							}
							
							theBNum = 12;
							myPlay = false;
							break;
						
					}
					emperorTurn++;
				}
				
				if(myStatus == 1){
					
					switch(slaveTurn){
						
						case 1:
						
							for(k=0;k < 4;k++){
								buttonArray[k].setBounds(k*200 + 560,760,180,230);
								System.out.println(buttonArray[k].getLocation());
							}

							buttonArray[4].setLocation(cardX,cardY);
							
							if(myStatus == 1){
								
								//送信情報を作成する（受信時には，この送った順番にデータを取り出す．スペースがデータの区切りとなる）
								String msg = "EMPERORRECEIVE"+" "+4+" "+theBNum;
								//サーバーに情報を送る
								out.println(msg);//送信データをバッファに書き出す
								out.flush();//送信データをフラッシュ（ネットワーク上にはき出す）する
								
							}
							
							theBNum = 12;
							myPlay = false;
							break;
							
						case 2:

							for(k=0;k < 3;k++){
								buttonArray[k].setLocation(k*200 + 660,760);
							}
							
							buttonArray[3].setLocation(cardX,cardY);
							
							if(myStatus == 1){
								
								//送信情報を作成する（受信時には，この送った順番にデータを取り出す．スペースがデータの区切りとなる）
								String msg = "EMPERORRECEIVE"+" "+3+" "+theBNum;
								//サーバーに情報を送る
								out.println(msg);//送信データをバッファに書き出す
								out.flush();//送信データをフラッシュ（ネットワーク上にはき出す）する
								
							}
							
							myPlay = false;
							theBNum = 12;
							break;
						
						case 3:
						
							for(k=0;k < 2;k++){
								buttonArray[k].setLocation(k*200 + 760,760);
							}
							
							buttonArray[2].setLocation(cardX,cardY);
							
							if(myStatus == 1){
								
								//送信情報を作成する（受信時には，この送った順番にデータを取り出す．スペースがデータの区切りとなる）
								String msg = "EMPERORRECEIVE"+" "+2+" "+theBNum;
								//サーバーに情報を送る
								out.println(msg);//送信データをバッファに書き出す
								out.flush();//送信データをフラッシュ（ネットワーク上にはき出す）する
								
							}

							theBNum = 12;
							myPlay = false;
							break;
						
						case 4:
							buttonArray[0].setLocation(cardX,760);
							
							buttonArray[1].setLocation(cardX,cardY);
							
							if(myStatus == 1){
								
								//送信情報を作成する（受信時には，この送った順番にデータを取り出す．スペースがデータの区切りとなる）
								String msg = "EMPERORRECEIVE"+" "+1+" "+theBNum;
								//サーバーに情報を送る
								out.println(msg);//送信データをバッファに書き出す
								out.flush();//送信データをフラッシュ（ネットワーク上にはき出す）する
								
							}

							theBNum = 12;
							myPlay = false;
							break;
							
					}
					slaveTurn++;
				}
			}
			
			if(theBNum == 0){
				if(myStatus == 0){
					switch(emperorTurn){
						
						case 1:
						
							for(k=1;k < 5;k++){
								buttonArray[k].setBounds((k-1)*200 + 560,760,180,230);
								System.out.println(buttonArray[k].getLocation());
							}

							buttonArray[0].setLocation(cardX,cardY);
							
							if(myStatus == 0){
								
								//送信情報を作成する（受信時には，この送った順番にデータを取り出す．スペースがデータの区切りとなる）
								String msg = "SLAVERECEIVE"+" "+4+" "+theBNum;
								//サーバーに情報を送る
								out.println(msg);//送信データをバッファに書き出す
								out.flush();//送信データをフラッシュ（ネットワーク上にはき出す）する
								
							}
							
							theBNum = 12;
							myPlay = false;
							break;
							
						case 2:
						
							for(k=1;k < 4;k++){
								buttonArray[k].setLocation((k-1)*200 + 660,760);
							}
							
							buttonArray[0].setLocation(cardX,cardY);
							
							if(myStatus == 0){
								
								//送信情報を作成する（受信時には，この送った順番にデータを取り出す．スペースがデータの区切りとなる）
								String msg = "SLAVERECEIVE"+" "+3+" "+theBNum;
								//サーバーに情報を送る
								out.println(msg);//送信データをバッファに書き出す
								out.flush();//送信データをフラッシュ（ネットワーク上にはき出す）する
								
							}

							myPlay = false;
							theBNum = 12;
							break;
						
						case 3:
							System.out.println("通ったよ2");
							for(k=1;k < 3;k++){
								buttonArray[k].setLocation((k-1)*200 + 760,760);
							}
							
							buttonArray[0].setLocation(cardX,cardY);
							
							if(myStatus == 0){
								
								//送信情報を作成する（受信時には，この送った順番にデータを取り出す．スペースがデータの区切りとなる）
								String msg = "SLAVERECEIVE"+" "+2+" "+theBNum;
								//サーバーに情報を送る
								out.println(msg);//送信データをバッファに書き出す
								out.flush();//送信データをフラッシュ（ネットワーク上にはき出す）する
								
							}

							theBNum = 12;
							myPlay = false;
							break;
						
						case 4:
							buttonArray[0].setLocation(cardX,cardY);
							
							buttonArray[1].setLocation(cardX,760);
							
							if(myStatus == 0){
								
								//送信情報を作成する（受信時には，この送った順番にデータを取り出す．スペースがデータの区切りとなる）
								String msg = "SLAVERECEIVE"+" "+1+" "+theBNum;
								//サーバーに情報を送る
								out.println(msg);//送信データをバッファに書き出す
								out.flush();//送信データをフラッシュ（ネットワーク上にはき出す）する
								
							}

							theBNum = 12;
							myPlay = false;
							break;
						
						case 5:
							buttonArray[0].setLocation(cardX,cardY);
							if(myStatus == 0){
								
								//送信情報を作成する（受信時には，この送った順番にデータを取り出す．スペースがデータの区切りとなる）
								String msg = "SLAVERECEIVE"+" "+0+" "+theBNum;
								//サーバーに情報を送る
								out.println(msg);//送信データをバッファに書き出す
								out.flush();//送信データをフラッシュ（ネットワーク上にはき出す）する
								
							}
							
							theBNum = 12;
							myPlay = false;
							break;
							
					}
					emperorTurn++;
				}
				
				if(myStatus == 1){
					
					switch(slaveTurn){
						
						case 1:
						
							for(k=1;k < 5;k++){
								buttonArray[k].setBounds((k-1)*200 + 560,760,180,230);
								System.out.println(buttonArray[k].getLocation());
							}

							buttonArray[0].setLocation(cardX,cardY);
							
							if(myStatus == 1){
								
								//送信情報を作成する（受信時には，この送った順番にデータを取り出す．スペースがデータの区切りとなる）
								String msg = "EMPERORRECEIVE"+" "+4+" "+theBNum;
								//サーバーに情報を送る
								out.println(msg);//送信データをバッファに書き出す
								out.flush();//送信データをフラッシュ（ネットワーク上にはき出す）する
								
							}

							theBNum = 12;
							myPlay = false;
							break;
							
						case 2:

							for(k=1;k < 4;k++){
								buttonArray[k].setLocation((k-1)*200 + 660,760);
							}
							
							buttonArray[0].setLocation(cardX,cardY);
							
							if(myStatus == 1){
								
								//送信情報を作成する（受信時には，この送った順番にデータを取り出す．スペースがデータの区切りとなる）
								String msg = "EMPERORRECEIVE"+" "+3+" "+theBNum;
								//サーバーに情報を送る
								out.println(msg);//送信データをバッファに書き出す
								out.flush();//送信データをフラッシュ（ネットワーク上にはき出す）する
								
							}
							
							myPlay = false;
							theBNum = 12;
							break;
						
						case 3:
						
							for(k=1;k < 3;k++){
								buttonArray[k].setLocation((k-1)*200 + 760,760);
							}
							
							buttonArray[0].setLocation(cardX,cardY);
							
							if(myStatus == 1){
								
								//送信情報を作成する（受信時には，この送った順番にデータを取り出す．スペースがデータの区切りとなる）
								String msg = "EMPERORRECEIVE"+" "+2+" "+theBNum;
								//サーバーに情報を送る
								out.println(msg);//送信データをバッファに書き出す
								out.flush();//送信データをフラッシュ（ネットワーク上にはき出す）する
								
							}

							theBNum = 12;
							myPlay = false;
							break;
						
						case 4:
							buttonArray[0].setLocation(cardX,cardY);
							
							buttonArray[1].setLocation(cardX,760);
							
							if(myStatus == 1){
								
								//送信情報を作成する（受信時には，この送った順番にデータを取り出す．スペースがデータの区切りとなる）
								String msg = "EMPERORRECEIVE"+" "+1+" "+theBNum;
								//サーバーに情報を送る
								out.println(msg);//送信データをバッファに書き出す
								out.flush();//送信データをフラッシュ（ネットワーク上にはき出す）する
								
							}

							theBNum = 12;
							myPlay = false;
							break;
							
						case 5:
							buttonArray[0].setLocation(cardX,cardY);
							System.out.println("通りましたで2");
							if(myStatus == 1){
								
								//送信情報を作成する（受信時には，この送った順番にデータを取り出す．スペースがデータの区切りとなる）
								String msg = "EMPERORRECEIVE"+" "+0+" "+theBNum;
								//サーバーに情報を送る
								out.println(msg);//送信データをバッファに書き出す
								out.flush();//送信データをフラッシュ（ネットワーク上にはき出す）する
								
							}

							theBNum = 12;
							myPlay = false;
							break;
					}
					slaveTurn++;
				}
			}
		}
		
		repaint();//オブジェクトの再描画を行う

	}


	//相手の手札を自動で場に出すための関数（ターン数と相手が何を出したかによって配置が決まる）
	public void receive(){
		int k = 0;
		
		int cardX = 870;
		int cardY = 500;
		int cardZ = 265;
		
		if(emperorPlay){
			
			if(myStatus == 0){

				if(slaveTurn==1){
					for(k=6;k < 10;k++){
						buttonArray[k].setLocation((k%6)*200 + 560,10);
						System.out.println(buttonArray[k].getLocation());
					}

					buttonArray[slaveNum].setLocation(cardX,cardZ);
					
					emperorPlay = false;
					
				}
				if(slaveTurn==2){
					for(k=6;k < 9;k++){
						buttonArray[k].setLocation((k%6)*200 + 660,10);
					}
					
					buttonArray[slaveNum].setLocation(cardX,cardZ);
					
					emperorPlay = false;
					
				}
				if(slaveTurn == 3){
					for(k=6;k < 8;k++){
						buttonArray[k].setLocation((k%6)*200 + 760,10);
					}
					
					buttonArray[slaveNum].setLocation(cardX,cardZ);
					
					emperorPlay = false;
					
				}
				if(slaveTurn == 4){
					buttonArray[6].setLocation(cardX,10);
					
					buttonArray[slaveNum].setLocation(cardX,cardZ);
					
					emperorPlay = false;
					
				}
				if(slaveTurn == 5){
					buttonArray[6].setLocation(cardX,cardZ);
					
					emperorPlay = false;
					
				}
				
				slaveTurn++;
			}

		}
		
		if(slavePlay){
			
			if(myStatus == 1){
				
					if(emperorTurn==1){
						for(k=6;k < 10;k++){
							buttonArray[k].setLocation((k%6)*200 + 560,10);
							System.out.println(buttonArray[k].getLocation());
						}

						buttonArray[emperorNum].setLocation(cardX,cardZ);
						
						slavePlay = false;
						
					}
					if(emperorTurn==2){
						for(k=6;k < 9;k++){
							buttonArray[k].setLocation((k%6)*200 + 660,10);
						}
						
						buttonArray[emperorNum].setLocation(cardX,cardZ);
						
						slavePlay = false;
						
					}
					if(emperorTurn == 3){
						for(k=6;k < 8;k++){
							buttonArray[k].setLocation((k%6)*200 + 760,10);
						}
						
						buttonArray[emperorNum].setLocation(cardX,cardZ);
						
						slavePlay = false;
						
					}
					if(emperorTurn == 4){
						buttonArray[6].setLocation(cardX,10);
						
						buttonArray[emperorNum].setLocation(cardX,cardZ);
						
						slavePlay = false;
						
					}
					if(emperorTurn == 5){
						buttonArray[6].setLocation(cardX,cardZ);
						
						slavePlay = false;
						
					}
					
					emperorTurn++;
					
				}

			}
		repaint();
	}
	
	
	//勝利判定
	public void judge(int emperorSelect, int slaveSelect){
		
		//どちらも市民を出した場合
		if(emperorSelect != 0 && slaveSelect != 0){
			
			if(myStatus == 0){
				
				buttonArray[slaveNum].setIcon(citizenIcon);
				
				try{//Thread.sleepはTry-catchでないとつかえないため
					Thread.sleep(2000); //2000ミリ秒Sleepする
				}catch(InterruptedException ee){}//エラーがでてもなにもしない
				
				//Playしたカードを消す
				buttonArray[slaveNum].setVisible(false);
				buttonArray[theSNum].setVisible(false);
				
			}
			if(myStatus == 1){
				
				buttonArray[emperorNum].setIcon(citizenIcon);
				
				try{//Thread.sleepはTry-catchでないとつかえないため
					Thread.sleep(2000); //2000ミリ秒Sleepする
				}catch(InterruptedException ee){}//エラーがでてもなにもしない
				
				//Playしたカードを消す
				buttonArray[theENum].setVisible(false);
				buttonArray[emperorNum].setVisible(false);
				
			}
		}
		
		//皇帝側が皇帝を出した時
		if(emperorSelect == 0){
			
			//奴隷側が奴隷を出した時
			if(slaveSelect == 0){
				
				if(myStatus == 0){
					
					buttonArray[slaveNum].setIcon(slaveIcon);
					
					try{//Thread.sleepはTry-catchでないとつかえないため
						Thread.sleep(1000); //1000ミリ秒Sleepする
					}catch(InterruptedException ee){}//エラーがでてもなにもしない
					
					buttonArray[slaveNum].setEnabled(false);
					buttonArray[emperorSelect].setEnabled(false);

					loseLabel.setVisible(true);
					
					System.out.println("あなたの負け");
					
				}
				if(myStatus == 1){
					
					buttonArray[emperorNum].setIcon(emperorIcon);
					
					try{//Thread.sleepはTry-catchでないとつかえないため
						Thread.sleep(1000); //1000ミリ秒Sleepする
					}catch(InterruptedException ee){}//エラーがでてもなにもしない
					
					buttonArray[emperorNum].setEnabled(false);
					buttonArray[slaveSelect].setEnabled(false);
					
					winLabel.setVisible(true);
					
					System.out.println("あなたの勝ち");
					
				}
				
			}else{//奴隷側が市民を出したとき
				
				if(myStatus == 0){
					
					buttonArray[slaveNum].setIcon(citizenIcon);
					
					try{//Thread.sleepはTry-catchでないとつかえないため
						Thread.sleep(1000); //1000ミリ秒Sleepする
					}catch(InterruptedException ee){}//エラーがでてもなにもしない
					
					buttonArray[slaveNum].setEnabled(false);
					buttonArray[emperorSelect].setEnabled(false);

					winLabel.setVisible(true);
					
					System.out.println("あなたの勝ち");//勝利時のポップアップの画像
					
				}
				if(myStatus == 1){
					
					buttonArray[emperorNum].setIcon(emperorIcon);
					
					try{//Thread.sleepはTry-catchでないとつかえないため
						Thread.sleep(1000); //1000ミリ秒Sleepする
					}catch(InterruptedException ee){}//エラーがでてもなにもしない
					
					buttonArray[emperorNum].setEnabled(false);
					buttonArray[slaveSelect].setEnabled(false);

					loseLabel.setVisible(true);//敗北時のポップアップの画像
					
					System.out.println("あなたの負け");
					
				}
				
				
			}
		}

		//奴隷側が奴隷を出した時
		if(slaveSelect == 0){
			
			//皇帝側が皇帝を出した時
			if(emperorSelect == 0){
				
			}else{//皇帝側が市民を出したとき
			
				if(myStatus == 0){
					
					buttonArray[slaveNum].setIcon(slaveIcon);
					
					try{//Thread.sleepはTry-catchでないとつかえないため
						Thread.sleep(1000); //1000ミリ秒Sleepする
					}catch(InterruptedException ee){}//エラーがでてもなにもしない
					
					System.out.println("あなたの勝ち");
					
					buttonArray[slaveNum].setEnabled(false);
					buttonArray[emperorSelect].setEnabled(false);
					
					
					winLabel.setVisible(true);//勝利時のポップアップの画像
					
				}else{
					
					buttonArray[emperorNum].setIcon(citizenIcon);
					
					try{//Thread.sleepはTry-catchでないとつかえないため
						Thread.sleep(1000); //1000ミリ秒Sleepする
					}catch(InterruptedException ee){}//エラーがでてもなにもしない
					
					buttonArray[emperorNum].setEnabled(false);
					buttonArray[slaveSelect].setEnabled(false);
					
					System.out.println("あなたの負け");
					
					loseLabel.setVisible(true);//敗北時のポップアップの画像
					
				}

			}
		}
		
		return;
	}
	
}