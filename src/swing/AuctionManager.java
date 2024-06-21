package swing;

import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Vector;

import dao.AuctionDAO;
import dto.AuctionDTO;
import dto.CardDTO;
import dto.UserDTO;
import lombok.Data;
import manager.Server;

@Data
public class AuctionManager extends Thread {

	private Server mContext;
	private int roomId;
	private String h;
	private String m;
	private int startBid;
	private int highbid;
	private int morebid;
	private CardDTO cardDTO;
	private UserDTO maker;
	private AuctionDTO auctionDTO = new AuctionDTO();

	// 시간 관련 변수
	LocalTime time = LocalTime.now();
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH시 mm분 ss초");
	// BID 관련 변수

	public AuctionManager(CardDTO cardDTO, Server mContext, int moreBid) {
		this.mContext = mContext;
		this.cardDTO = cardDTO;
		highbid = startBid;
		// DTO

		Vector<UserDTO> userID = new Vector<>();
		start();
	}

	@Override
	public void run() {

		while (true) {
			time = LocalTime.now();
			System.out.println(time.format(formatter));
			try {
				Thread.sleep(1000);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (time.format(formatter).equals(h + "시" + m + "분" + "00초")) {
				timeOver();

				break;
			}
			;
		}

	}

	public void timeOver() {
		System.out.println("경매 종료");
		AuctionDAO dao = new AuctionDAO();
		auctionDTO.setBidPrice(highbid);
		auctionDTO.setEndDate(h + "시" + m + "분" + "00초");
		try {
			dao.addAuction(auctionDTO);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}