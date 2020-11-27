package cn.lanink.wxnotice;

import cn.lanink.wxnotice.utils.Network;
import cn.nukkit.Server;
import cn.nukkit.plugin.PluginBase;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * @author lt_name
 */
public class WXNotice extends PluginBase {

    public static final String VERSION = ".*";
    private static String SCKEY;
    private static boolean canUse = false;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        SCKEY = this.getConfig().getString("SCKEY").trim();
        if (!"".equals(SCKEY)) {
            canUse = true;
            //使用Task保证在服务器启动完成后执行
            this.getServer().getScheduler().scheduleTask(this, () -> sendMessage("服务器启动完成！",
                    "服务器信息:  " +
                            "名称:" + this.getServer().getName() + "  " +
                            "Motd:" + this.getServer().getMotd() + "  " +
                            "版本:" + this.getServer().getVersion(), Type.POST));
        }
    }

    /**
     * 发送消息
     * @param title 标题
     * @param message 内容
     * @return 发送结果(json)
     */
    public static String sendMessage(String title, String message) {
        return sendMessage(title, message, Type.POST);
    }

    /**
     * 发送消息
     * @param title 标题
     * @param message 内容
     * @param type 通信类型
     * @return 发送结果(json)
     */
    public static String sendMessage(String title, String message, Type type) {
        String returnString = "";
        if (canUse) {
            try {
                title = URLEncoder.encode(title, "UTF-8");
                message = URLEncoder.encode(message, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String url = "https://sc.ftqq.com/" + SCKEY + ".send";
            switch (type) {
                case GET:
                    url += "?text=" + title + "&desp=" + message;
                    returnString = Network.get(url);
                    break;
                case POST:
                    HashMap<String, String> map = new HashMap<>();
                    map.put("text", title);
                    map.put("desp", message);
                    returnString = Network.post(url, map);
                    break;
            }
        }
        Server.getInstance().getLogger().info(returnString);
        return returnString;
    }

    public enum Type {
        GET,
        POST
    }

}
