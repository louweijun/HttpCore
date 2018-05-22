package core.android.xuele.net.crhlibcore.xml;

import android.content.Context;
import android.support.annotation.Nullable;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by louweijun on 2018-05-15.
 * xml解析器
 *设计思路：
 * XmlDocument 表示文档整个xml文档
 * XmlTag 表示每个节点的信息，XmlTag如果有子节点，则
 */
public class XmlResolve {

    public static XmlDocument readXml(InputStream inputStream, String tag) {
        XmlDocument document = null;
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            // 获取XmlPullParser实例
            XmlPullParser pullParser = factory.newPullParser();
            pullParser.setInput(inputStream, "UTF-8");
            // 开始
            int eventType = pullParser.getEventType();
            XmlTag lastTag = null;

            xmlRead:
            while (true) {
                String nodeName = pullParser.getName();
                switch (eventType) {
                    // 文档开始
                    case XmlPullParser.START_DOCUMENT:
                        document = new XmlDocument(tag);
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        document.finish();
                        break xmlRead;
                    // 开始节点
                    case XmlPullParser.START_TAG:
                        lastTag = findXmlTag(document.getLastXmlTag());
                        XmlTag xmlTag = new XmlTag(nodeName);
                        if (lastTag == null) {
                            document.addTag(xmlTag);
                        } else {
                            lastTag.addTag(xmlTag);
                        }
                        break;
                    case XmlPullParser.TEXT:
                        lastTag = findXmlTag(document.getLastXmlTag());
                        if (lastTag != null) {
                            lastTag.setValue(pullParser.getText());
                        }
                        break;
                    // 结束节点
                    case XmlPullParser.END_TAG:
                        lastTag = findXmlTag(document.getLastXmlTag());
                        if (lastTag != null) {
                            lastTag.end();
                        }
                        break;

                }
                eventType = pullParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return document;
    }

    //寻找应该添加的父节点
    //寻找逻辑：
    //如果是根节点已经添加结束,则返回null
    //如果未结束，寻找其子节点，如果子节点都已经结束，那么返回其父节点。
    //循环搜索，直到找到对应节点
    @Nullable
    private static XmlTag findXmlTag(@Nullable XmlTag srcXmlTag) {
        if (srcXmlTag == null || srcXmlTag.isTagEnd())
            return null;

        XmlTag childTag = srcXmlTag.getLastXmlTag();

        if (childTag == null || childTag.isTagEnd())
            return srcXmlTag;

        return findXmlTag(childTag);
    }

    @Nullable
    public static XmlDocument readXmlAssets(Context context, String fileName) {
        try {
            return readXml(context.getAssets().open(fileName), fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

//    public void readXmlAssets(String fileName) {
//        try {
//            InputStream inputStream = context.getAssets().open(fileName);
//            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
//            // 获取XmlPullParser实例
//            XmlPullParser pullParser = factory.newPullParser();
//            pullParser.setInput(inputStream, "UTF-8");
//            // 开始
//            int eventType = pullParser.getEventType();
//
//            while (eventType != XmlPullParser.END_DOCUMENT) {
//                String nodeName = pullParser.getName();
//                switch (eventType) {
//                    // 文档开始
//                    case XmlPullParser.START_DOCUMENT:
//                        configBeans = new ArrayList<ConfigBean>();
//                        break;
//                    // 开始节点
//                    case XmlPullParser.START_TAG:
//                        if ("server".equals(nodeName)) {
//                            currentConfigBean = new ConfigBean();
//                        }
//                        if ("crh_app_name".equals(nodeName)) {
//                            currentConfigBean.setCrh_app_name(pullParser.nextText().trim());
//                        } else if ("crh_app_type".equals(nodeName)) {
//                            String type = pullParser.nextText().trim();
//                            if (type == null || "".equals(type)) {
//                                type = "0";
//                            }
//                            currentConfigBean.setCrh_app_type(Integer.valueOf(type));
//                        } else if ("crh_server_domain".equals(nodeName)) {
//                            currentConfigBean.setCrh_server_domain(pullParser.nextText().trim());
//                        } else if ("crh_server_url".equals(nodeName)) {
//                            currentConfigBean.setCrh_server_url(pullParser.nextText().trim());
//                        } else if ("crh_upload_url".equals(nodeName)) {
//                            currentConfigBean.setCrh_upload_url(pullParser.nextText().trim());
//                        } else if ("crh_cookie_path".equals(nodeName)) {
//                            currentConfigBean.setCrh_cookie_path(pullParser.nextText().trim());
//                        } else if ("crh_video_server_ip".equals(nodeName)) {
//                            currentConfigBean.setCrh_video_server_ip(pullParser.nextText().trim());
//                        } else if ("crh_video_server_port".equals(nodeName)) {
//                            String port = pullParser.nextText().trim();
//                            if (port == null || "".equals(port)) {
//                                port = "8906";
//                            }
//                            currentConfigBean.setCrh_video_server_port(Integer.valueOf(port));
//                        } else if ("crh_exit_tip_msg".equals(nodeName)) {
//                            String port = pullParser.nextText().trim();
//                            if (TextUtils.isEmpty(nodeName)) {
//                                port = "尊敬的用户,您是否退出此功能?";
//                            }
//                            currentConfigBean.setCrh_exit_tip_msg(port);
//                        } else if ("crh_app_value".equals(nodeName)) {
//                            currentConfigBean.setCrh_app_value(pullParser.nextText().trim());
//                        } else if ("crh_login_url".equals(nodeName)) {
//                            currentConfigBean.setCrh_login_url(pullParser.nextText().trim());
//                        } else if ("crh_check_login_url".equals(nodeName)) {
//                            currentConfigBean.setCrh_check_login_url(pullParser.nextText().trim());
//                        } else if ("crh_status_bar_color".equals(nodeName)) {
//                            currentConfigBean.setCrh_status_bar_color(pullParser.nextText().trim());
//                        }
//                        break;
//                    // 结束节点
//                    case XmlPullParser.END_TAG:
//                        if ("server".equals(nodeName)) {
//                            configBeans.add(currentConfigBean);
//                        }
//                        break;
//                    default:
//                        break;
//                }
//                // 手动的触发下一个事件
//                eventType = pullParser.next();
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//        return configBeans;
//    }
//
//    /****
//     * 读取xml配置文件
//     * @param RId
//     * @return
//     */
//    public List<ConfigBean> readLocalXml(File file) {
//        List<ConfigBean> configBeans = null;
//        ConfigBean currentConfigBean = null;
//        try {
//            FileInputStream inputStream = new FileInputStream(file);
//            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
//            // 获取XmlPullParser实例
//            XmlPullParser pullParser = factory.newPullParser();
//            pullParser.setInput(inputStream, "UTF-8");
//            // 开始
//            int eventType = pullParser.getEventType();
//
//            while (eventType != XmlPullParser.END_DOCUMENT) {
//                String nodeName = pullParser.getName();
//                switch (eventType) {
//                    // 文档开始
//                    case XmlPullParser.START_DOCUMENT:
//                        configBeans = new ArrayList<ConfigBean>();
//                        break;
//                    // 开始节点
//                    case XmlPullParser.START_TAG:
//                        if ("server".equals(nodeName)) {
//                            currentConfigBean = new ConfigBean();
//                        }
//                        if ("crh_app_name".equals(nodeName)) {
//                            currentConfigBean.setCrh_app_name(pullParser.nextText().trim());
//                        } else if ("crh_app_type".equals(nodeName)) {
//                            String type = pullParser.nextText().trim();
//                            if (type == null || "".equals(type)) {
//                                type = "0";
//                            }
//                            currentConfigBean.setCrh_app_type(Integer.valueOf(type));
//                        } else if ("crh_server_domain".equals(nodeName)) {
//                            currentConfigBean.setCrh_server_domain(pullParser.nextText().trim());
//                        } else if ("crh_server_url".equals(nodeName)) {
//                            currentConfigBean.setCrh_server_url(pullParser.nextText().trim());
//                        } else if ("crh_upload_url".equals(nodeName)) {
//                            currentConfigBean.setCrh_upload_url(pullParser.nextText().trim());
//                        } else if ("crh_cookie_path".equals(nodeName)) {
//                            currentConfigBean.setCrh_cookie_path(pullParser.nextText().trim());
//                        } else if ("crh_video_server_ip".equals(nodeName)) {
//                            currentConfigBean.setCrh_video_server_ip(pullParser.nextText().trim());
//                        } else if ("crh_video_server_port".equals(nodeName)) {
//                            String port = pullParser.nextText().trim();
//                            if (port == null || "".equals(port)) {
//                                port = "8906";
//                            }
//                            currentConfigBean.setCrh_video_server_port(Integer.valueOf(port));
//                        } else if ("crh_exit_tip_msg".equals(nodeName)) {
//                            String port = pullParser.nextText().trim();
//                            if (TextUtils.isEmpty(nodeName)) {
//                                port = "尊敬的用户,您是否退出此功能?";
//                            }
//                            currentConfigBean.setCrh_exit_tip_msg(port);
//                        } else if ("crh_app_value".equals(nodeName)) {
//                            currentConfigBean.setCrh_app_value(pullParser.nextText().trim());
//                        } else if ("crh_login_url".equals(nodeName)) {
//                            currentConfigBean.setCrh_login_url(pullParser.nextText().trim());
//                        } else if ("crh_check_login_url".equals(nodeName)) {
//                            currentConfigBean.setCrh_check_login_url(pullParser.nextText().trim());
//                        }
//                        break;
//                    // 结束节点
//                    case XmlPullParser.END_TAG:
//                        if ("server".equals(nodeName)) {
//                            configBeans.add(currentConfigBean);
//                        }
//                        break;
//                    default:
//                        break;
//                }
//                // 手动的触发下一个事件
//                eventType = pullParser.next();
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return configBeans;
//    }
//
//    public byte[] readInputStream(InputStream inStream) throws Exception {
//        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
//        byte[] buffer = new byte[inStream.available()];
//        int rLen = 0;
//        int wLen = 0;
//        while ((rLen = inStream.read(buffer)) != -1) {
//            outSteam.write(buffer, wLen, rLen);
//            wLen = wLen + rLen;
//        }
//        outSteam.flush();
//        outSteam.close();
//        inStream.close();
//        return outSteam.toByteArray();
//    }

}
