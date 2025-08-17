package model;

/**
 * File: Rating.java
 * Author: [团队名称]
 * Date: [当前日期]
 *
 * Description: 评分数据模型，管理用户对图书的评分记录
 */
public class Rating 
{
    private String strUserId;    // 用户 ID
    private String strBookId;    // 图书 ID
    private int    intRating;    // 评分值 (1–5)

    /**
     * 构造函数，带范围校验
     * @param strUserId  用户 ID
     * @param strBookId  图书 ID
     * @param intRating  评分值，必须在 1 到 5 之间
     */
    public Rating(String strUserId, String strBookId, int intRating) 
    {
        if (intRating < 1 || intRating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        this.strUserId = strUserId;
        this.strBookId = strBookId;
        this.intRating = intRating;
    }

    // === Getter 方法 ===
    public String getStrUserId()  { return strUserId; }
    public String getStrBookId()  { return strBookId; }
    public int    getIntRating()  { return intRating; }

    @Override
    public String toString() 
    {
        return String.format("Rating[user=%s, book=%s, score=%d]",
                             strUserId, strBookId, intRating);
    }
}
