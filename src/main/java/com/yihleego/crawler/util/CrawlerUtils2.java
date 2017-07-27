package com.yihleego.crawler.util;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by YihLeego on 2017-5-11.
 */
public class CrawlerUtils2 {

    public static final int DEFAULT_TIMEOUT = 60 * 1000;

    public interface WEB_TYPE {
        public static final int STATIC = 0;
        public static final int DYNAMIC = 1;
        public static final int HTTP_CLIENT = 2;

    }

    public interface SELECTOR_TYPE {
        public static final int TEXT = 0;
        public static final int OWN_TEXT = 1;
        public static final int HTML = 2;
        public static final int OUTER_HTML = 3;
        public static final int SRC = 4;
        public static final int HREF = 5;
        public static final int ABS_HREF = 6;
    }


    /**
     * 获取JSON字符串
     *
     * @param url url
     * @return String
     * @throws Exception
     */
    public static String getJSONString(String url) throws Exception {
        return getJSONString(url, DEFAULT_TIMEOUT);
    }

    /**
     * 获取JSON字符串
     *
     * @param url     url
     * @param timeout timeout
     * @return String
     * @throws Exception
     */
    public static String getJSONString(String url, int timeout) throws Exception {
        Document document = getUrlDocument(url, WEB_TYPE.STATIC, timeout);
        if (null == document) {
            return null;
        }
        Element body = document.select("body").first();
        if (null == body) {
            return null;
        }
        return body.text();

    }

    /**
     * 获取DOM字符串
     *
     * @param url
     * @param webType
     * @return String
     * @throws Exception
     */
    public static String getDocument(String url, int webType) throws Exception {
        return getDocument(url, webType, DEFAULT_TIMEOUT);
    }

    /**
     * 获取DOM字符串
     *
     * @param url
     * @param webType
     * @param timeout
     * @return String
     * @throws Exception
     */
    public static String getDocument(String url, int webType, int timeout) throws Exception {
        Document document = getUrlDocument(url, webType, timeout);
        if (null == document) {
            return null;
        }
        return document.text();
    }


    /**
     * 获取元素
     *
     * @param documentString
     * @param selector
     * @return
     * @throws Exception
     */
    public static String getElements(String documentString, String selector) throws Exception {
        if (null == documentString || null == selector) {
            return null;
        }
        Document document = Jsoup.parse(documentString);
        if (null == document) {
            return null;
        }
        Elements elements = document.select(selector);
        if (null == elements) {
            return null;
        }
        return elements.text();
    }


    public static Element getElement(String elementsString, int index) throws Exception {
        if (null == elementsString) {
            return null;
        }
        if (index < 0) {
            index = 0;
        }
        Document document = Jsoup.parse(elementsString);
        if (null == document) {
            return null;
        }
        Elements elements = document.getAllElements();
        if (null == elements || elements.size() <= 0) {
            return null;
        }
        return elements.get(index);
    }


    /**
     * 获取字符串
     *
     * @param documentString
     * @param selector
     * @param selectorType
     * @return
     * @throws Exception
     */
    public static String getText(String documentString, String selector, int selectorType) throws Exception {
        if (null == documentString || null == selector) {
            return null;
        }
        Document document = Jsoup.parse(documentString);
        if (null == document) {
            return null;
        }
        Elements elements = document.select(selector);
        if (null == elements || elements.size() <= 0) {
            return null;
        }
        Element element = elements.first();
        if (null == element) {
            return null;
        }
        String result = null;
        switch (selectorType) {
            case SELECTOR_TYPE.TEXT:
                result = element.text();
                break;
            case SELECTOR_TYPE.OWN_TEXT:
                result = element.ownText();
                break;
            case SELECTOR_TYPE.HTML:
                result = element.html();
                break;
            case SELECTOR_TYPE.OUTER_HTML:
                result = element.outerHtml();
                break;
            case SELECTOR_TYPE.SRC:
                result = element.attr("src");
                break;
            case SELECTOR_TYPE.HREF:
                result = element.attr("href");
                break;
            case SELECTOR_TYPE.ABS_HREF:
                result = element.attr("abs:href");
                break;
            default:
                result = element.text();
                break;
        }
        return result;
    }

    /**
     * 是否不为数字
     * 检查字符串是否是非数字值,整数返回false,非整数返回true.
     *
     * @param numValue 数字字符串
     * @return boolean
     * @throws Exception
     */
    public static boolean isNaN(String numValue) throws Exception {
        //长度小于等于0时直接返回true
        if (numValue.length() <= 0) {
            return true;
        }
        for (int i = 0, len = numValue.length(); i < len; i++) {
            if (!Character.isDigit(numValue.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取DOM
     *
     * @param url     url
     * @param webType 网页类型
     * @param timeout 超时时间
     * @return Document
     * @throws Exception
     */
    private static Document getUrlDocument(String url, int webType, int timeout) throws Exception {
        if (null == url) {
            return null;
        }
        Document document = null;
        //设置请求头
        Map<String, String> header = new HashMap<String, String>();
        header.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        header.put("Accept-Encoding", "gzip, deflate, sdch, br");
        header.put("Accept-Language", "zh-CN,zh;q=0.8");
        header.put("Cache-Control:", "max-age=0");
        header.put("Connection", "keep-alive");
        header.put("Upgrade-Insecure-Requests", "1");
        header.put("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.82 Safari/537.36");

        switch (webType) {
            case WEB_TYPE.STATIC:
                Connection connect = Jsoup.connect(url);
                document = connect
                        // 设置请求头
                        .data(header)
                        // 设置超市时间
                        .timeout(timeout)
                        // 无视编码类型
                        .ignoreContentType(true)
                        // 获取当前页面
                        .get();
                break;
            case WEB_TYPE.DYNAMIC:
                WebClient webClient = new WebClient(BrowserVersion.CHROME, url, 9052);
                // 设置请求头
                for (String key : header.keySet()) {
                    webClient.addRequestHeader(key, header.get(key));
                }
                // 启动重定向
                webClient.getOptions().setRedirectEnabled(true);
                // 启用js渲染,默认为true
                webClient.getOptions().setJavaScriptEnabled(true);
                // 禁用css渲染
                webClient.getOptions().setCssEnabled(false);
                // js运行错误时,是否抛出异常
                webClient.getOptions().setThrowExceptionOnScriptError(false);
                // 错误代码时，是否抛出异常
                webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
                // 启动ajax代理
                webClient.setAjaxController(new NicelyResynchronizingAjaxController());
                // 设置超时时间
                webClient.getOptions().setTimeout(timeout);
                // 打开url
                HtmlPage page = webClient.getPage(url);
                // 等待js加载
                webClient.waitForBackgroundJavaScript(timeout);
                webClient.setJavaScriptTimeout(0);
                // 获取DOM
                String pageXml = page.asXml();
                // 将String DOM转为Document
                document = Jsoup.parse(pageXml);
                // 关闭webClient 释放资源
                webClient.close();
                break;
            case WEB_TYPE.HTTP_CLIENT:
                CloseableHttpClient httpclient = HttpClients.createDefault();
                HttpGet httpget = new HttpGet(url);
                for (String key : header.keySet()) {
                    httpget.setHeader(key, header.get(key));
                }
                CloseableHttpResponse response = httpclient.execute(httpget);
                HttpEntity entity = response.getEntity();
                InputStream in = entity.getContent();
                StringBuffer out = new StringBuffer();
                byte[] b = new byte[4096];
                for (int n; (n = in.read(b)) != -1; ) {
                    out.append(new String(b, 0, n));
                }
                document = Jsoup.parse(out.toString());
                httpclient.close();
                break;
        }
        return document;
    }



    /*
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>fastjson</artifactId>
        <version>1.1.41</version>
    </dependency>
    <dependency>
        <groupId>org.jsoup</groupId>
        <artifactId>jsoup</artifactId>
        <version>1.10.2</version>
    </dependency>
    <dependency>
        <groupId>net.sourceforge.htmlunit</groupId>
        <artifactId>htmlunit</artifactId>
        <version>2.26</version>
    </dependency>
    <dependency>
        <groupId>org.apache.httpcomponents</groupId>
        <artifactId>httpclient</artifactId>
        <version>4.5.3</version>
    </dependency>
    */

}
