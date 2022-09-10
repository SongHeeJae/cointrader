package kukekyakya.cointrader.service;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * WinRate
 *
 * @author heejae.song
 * @since 2022. 09. 09.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WinRate {
	private int winCount;
	private int loseCount;
	private int finishCount;

	public static WinRate init(int finishCount) {
		return new WinRate(finishCount);
	}

	private WinRate(int finishCount) {
		this.finishCount = finishCount;
	}

	public void win() {
		winCount++;
	}

	public void lose() {
		loseCount++;
	}

	public boolean isFinished() {
		return winCount + loseCount >= finishCount;
	}

	public boolean isWin() {
		return winCount > loseCount;
	}

	public boolean isLose() {
		return winCount < loseCount;
	}

	public boolean isDraw() {
		return winCount == loseCount;
	}

	public double getRate() {
		return (double)winCount / loseCount;
	}

	public int getTotalCount() {
		return winCount + loseCount;
	}

	public void clear() {
		winCount = 0;
		loseCount = 0;
	}
}
