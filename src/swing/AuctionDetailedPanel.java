package swing;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import dto.CardDTO;
import dto.UserDTO;
import manager.AuctionManager;
import manager.Client;
import manager.Server;

public class AuctionDetailedPanel extends JPanel {

	private Client mContext;

	private AuctionPanel auctionPanel;

	private JPanel backgroundPanel;
	private JPanel backgroundPanel1;
	private JLabel title;
	private JScrollPane scrollPane;
	private CardDTO card;
	private UserDTO user;
	private JButton buyCard;
	private JButton goBackButton;
	private BuyFrame buyFrame;

	// 옥션 매니저
	private AuctionManager auctionManager;
	boolean flag = true;
	SellProductPanel sell;
	String time; // 시간
	private int bid;

	public AuctionDetailedPanel(Client mContext, AuctionManager auctionManager) {
		this.mContext = mContext;
		auctionManager = new AuctionManager(card, mContext, bid);
		this.auctionManager = auctionManager;
		sell = new SellProductPanel(mContext);
		initData();
		setInitLayout();
		initListener();
	}

	private void initData() {
		backgroundPanel1 = new JPanel();
	}

	private void setInitLayout() {
		setSize(1920, 630);
		setLocation(0, 400);
		setLayout(null);
		setBackground(Color.WHITE);
		add(backgroundPanel1);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(4, 4, 950, 330);

		JLabel title = new JLabel("경매 상품 상세 보기");
		title.setFont(new Font("Freesentation 7 Bold", Font.BOLD, 32));
		title.setBounds(860, 10, 800, 50);
		add(title);

		JLabel cardId = new JLabel(" 카드 ID : " + card.getId());
		JLabel cardName = new JLabel(" 카드명 : " + card.getName());
		JLabel cardPrice = new JLabel(" 현재 카드 가격 : " + auctionManager.getHighbid());
		JLabel cardIcon = new JLabel(new ImageIcon(card.getUrl()));
		JLabel endTime = new JLabel("종료시간" + auctionManager.getCurrent_time());
		buyCard = new JButton("가격 제시하기");
		goBackButton = new JButton("뒤로 가기");

		cardId.setFont(new Font("Freesentation 7 Bold", Font.BOLD, 24));
		cardName.setFont(new Font("Freesentation 7 Bold", Font.BOLD, 24));
		cardPrice.setFont(new Font("Freesentation 7 Bold", Font.BOLD, 24));
		cardIcon.setFont(new Font("Freesentation 7 Bold", Font.BOLD, 24));
		endTime.setFont(new Font("Freesentation 7 Bold", Font.BOLD, 24));
		buyCard.setFont(new Font("Freesentation 7 Bold", Font.BOLD, 24));
		goBackButton.setFont(new Font("Freesentation 7 Bold", Font.BOLD, 24));

		cardId.setBounds(900, 100, 400, 100);
		cardName.setBounds(900, 150, 400, 100);
		cardPrice.setBounds(900, 200, 400, 100);
		cardIcon.setBounds(600, 150, 150, 200);
		endTime.setBounds(900, 0, 400, 200);
		buyCard.setBounds(900, 300, 200, 70);
		buyCard.setBackground(new Color(255, 204, 3));
		buyCard.setBorderPainted(false);
		goBackButton.setBounds(600, 20, 130, 50);
		goBackButton.setBackground(new Color(255, 204, 3));
		goBackButton.setBorderPainted(false);

		add(cardId);
		add(cardName);
		add(cardPrice);
		add(cardIcon);
		add(buyCard);
		add(goBackButton);
// 시간 확인 쓰레드
		time = "19시 55분 50초";
		new Thread(() -> {
			while (flag) {
				String s = Long.toString(auctionManager.getCurrent_time());
				if (!s.equals(time)) {
					time = s;
					endTime.setText("남은시간 :" + time + "초");
					if (time.equals("0")) {
						endTime.setText("경매 종료");
						endTime.setForeground(Color.RED);
						buyCard.setVisible(false);
						flag = false;
					}
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}).start();

	}

	private void initListener() {
		// 진행중인 경매 이동
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {

			}

		});
		buyCard.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				buyFrame = new BuyFrame(card, user);
			}

		});
		goBackButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				setVisible(false);
				backgroundPanel.add(auctionPanel);
			}

		});
	}

}