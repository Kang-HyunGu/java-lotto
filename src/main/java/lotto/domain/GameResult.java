package lotto.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class GameResult {

    private final WinningLotto winningLotto;
    private final Map<Rank, Integer> rank;

    public GameResult(List<String> winningNumbers, String bonusNumber) {
        this.winningLotto = new WinningLotto(winningNumbers, bonusNumber);
        this.rank = new HashMap<>();
        rank.put(Rank.FIRST, 0);
        rank.put(Rank.SECOND, 0);
        rank.put(Rank.BONUS, 0);
        rank.put(Rank.THIRD, 0);
        rank.put(Rank.FORTH, 0);
    }

    public void record(final List<Lotto> lottos) {
        rank.forEach((key, value) -> rank.put(key, match(lottos, key)));
    }

    int match(final List<Lotto> lottos, final Rank matchRank) {
        return (int) lottos.stream()
                .filter(lotto -> matchRank.equals(winningLotto.match(lotto)))
                .count();
    }

    long totalPrizeMoney() {
        AtomicLong totalPrizeMoney = new AtomicLong(0L);
        rank.forEach((key, count) -> totalPrizeMoney.addAndGet(key.totalPrizeMoney(count)));
        return totalPrizeMoney.longValue();
    }

    public int rateOfReturn(long amount) {
        return (int)(((double)totalPrizeMoney() / (double)amount) * 100);
    }

    public WinningLotto getWinning() {
        return winningLotto;
    }

    public Map<Rank, Integer> getRank() {
        return rank;
    }
}
