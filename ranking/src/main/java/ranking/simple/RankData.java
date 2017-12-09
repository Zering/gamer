package ranking.simple;

/**
 * Created by zhanghaojie on 2017/12/9.
 *
 * 小数据量排行榜数据父类
 *
 */
public abstract class RankData<T> implements Comparable<T> {

  // 排行数据唯一id
  private int uniqueId;
  // 排行依据主数据
  private int score;
  // 当前排行
  private short rank;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    RankData<?> rankData = (RankData<?>) o;

    return uniqueId == rankData.uniqueId;
  }

  @Override
  public int hashCode() {
    return uniqueId;
  }

  public int getUniqueId() {
    return uniqueId;
  }

  public void setUniqueId(int uniqueId) {
    this.uniqueId = uniqueId;
  }

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }

  public short getRank() {
    return rank;
  }

  public void setRank(short rank) {
    this.rank = rank;
  }
}
