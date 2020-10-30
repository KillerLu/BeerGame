package com.jnu.constant;

/**
 * @author ：Killer
 * @date ：Created in 20-10-23 上午10:53
 * @description：${description}
 * @modified By：
 * @version: version
 */
public interface GameConstant {


    /**
     * 游戏状态--未开始
     */
    public static final int UNSTART=0;

    /**
     * 游戏状态--进行中
     */
    public static final int RUNNING=1;

    /**
     * 游戏状态--终止(未完成强行结束)
     */
    public static final int TERMINATE=2;


    /**
     * 游戏状态--结束
     */
    public static final int FINISH=3;

    /**
     * 游戏角色--零售商
     */
    public static final String RETAILER = "RETAILER";

    /**
     * 游戏角色--批发商
     */
    public static final String WHOLESALER = "WHOLESALER";


    /**
     * 游戏角色--分销商
     */
    public static final String RESELLER = "RESELLER";


    /**
     * 游戏角色--零售商
     */
    public static final String MANUFACTURER = "MANUFACTURER";

    /**
     * 游戏角色--观看者
     */
    public static final String OB = "OB";

    /**
     * 游戏角色进度--刚开始
     */
    public static final int INIT=0;


    /**
     * 游戏角色进度--收到下游供应商订货请求
     */
    public static final int HAVE_RECEIVE_ORDER=1;


    /**
     * 游戏角色进度--已发货给下游供应商
     */
    public static final int HAVE_SEND_GOODS=2;


    /**
     * 游戏角色进度--已向上游供应商发出订货请求
     */
    public static final int HAVE_ORDER_GOODS=3;

    /**
     * 游戏角色进度--已收到上游供应商的发货
     */
    public static final int HAVE_RECEIVED_GOODS=4;

    /**
     * 游戏回合进度--刚开始
     */
    public static final int INIT_TURN=0;

    /**
     * 游戏回合进度--零售商回合
     */
    public static final int RETAILER_TURN=1;


    /**
     * 游戏回合进度--批发商回合
     */
    public static final int WHOLESALER_TURN=2;


    /**
     * 游戏回合进度--分销商回合
     */
    public static final int RESELLER_TURN=3;


    /**
     * 游戏回合进度--厂商回合
     */
    public static final int MANUFACTURER_TURN=4;

}
