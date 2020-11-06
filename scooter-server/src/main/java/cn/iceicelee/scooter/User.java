package cn.iceicelee.scooter;

/**
 * @author: Yao Shuai
 * @date: 2020/11/5 18:15
 */
/**
 * 用户
 */
public class User {
    /**
     * 用户 Id
     */
    private int userId;
    /**
     * 英雄形象
     */
    private String heroAvatar;

    public User(int userId, String heroAvatar) {
        this.userId = userId;
        this.heroAvatar = heroAvatar;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getHeroAvatar() {
        return heroAvatar;
    }

    public void setHeroAvatar(String heroAvatar) {
        this.heroAvatar = heroAvatar;
    }
}
