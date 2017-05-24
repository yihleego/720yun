package com.yihleego.crawler.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by YihLeego on 2017-5-11.
 */
public class CrawlerUtils {


    public JSONObject getJSON(String url) throws Exception {
        JSONObject jsonObject = null;
        try {
            Document document = getDocument(url, 0);
            Element body = document.select("body").first();
            jsonObject = JSON.parseObject(body.text());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;

    }

    public String getJSONString(String url) throws Exception {
        String jsonString = null;
        try {
            Document document = getDocument(url, 0);
            Element body = document.select("body").first();
            jsonString = body.text();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonString;

    }

    public List<String> getHrefList(String url, String listSelector, String rowSelector, String hrefSelector, int pageType) throws Exception {
        List<String> hrefList = null;
        try {
            Document document = getDocument(url, pageType);
            System.out.println(document.toString());
            Elements listElements = getListElements(document, listSelector);
            Elements rowElements = getRowElements(listElements, rowSelector);
            hrefList = getHrefList(rowElements, hrefSelector);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hrefList;
    }

    public String[] getItemArray(String url, String[] selectorArray, int[] typeArray, int pageType) throws Exception {
        String[] itemArray = null;
        Document document = getDocument(url, pageType);
        itemArray = getItemArray(document, selectorArray, typeArray);
        return itemArray;
    }

    public List<String> getItemList(String url, List<String> selectorList, List<Integer> typeList, int pageType) throws Exception {
        List<String> itemList = null;
        Document document = getDocument(url, pageType);
        itemList = getItemList(document, selectorList, typeList);
        return itemList;
    }

    public String testList(String url, String listSelector, int pageType) throws Exception {
        Document document = getDocument(url, pageType);
        Elements listElements = getListElements(document, listSelector);
        return listElements.outerHtml();
    }

    public String testRow(String url, String listSelector, String rowSelector, int pageType) throws Exception {
        Document document = getDocument(url, pageType);
        Elements listElements = getListElements(document, listSelector);
        Elements rowElements = getRowElements(listElements, rowSelector);
        return rowElements.outerHtml();
    }

    public Object[] analysisUrl(String url) throws Exception {
        String patternStr = "(.*?)\\[(.*?)-(.*?)\\](.*)";
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(url);
        Object[] result = new Object[3];

        result[0] = url;
        result[1] = -1;
        result[2] = -1;

        if (matcher.find()) {
            int size = matcher.groupCount();
            String[] urlArray = new String[size];
            for (int i = 0; i < size; i++) {
                urlArray[i] = matcher.group(i + 1).trim();
            }
            if (!isNaN(urlArray[1]) && !isNaN(urlArray[2])) {
                int start = Integer.parseInt(urlArray[1]);
                int end = Integer.parseInt(urlArray[2]);
                if (start > end) {
                    int whatever = start;
                    start = end;
                    end = whatever;
                }
                result[0] = urlArray[0] + "{0}" + urlArray[3];
                result[1] = start;
                result[2] = end;
            }
        }
        return result;

    }




    /**
     * 是否不为数字(is Not a Number)
     * author:YihLeego
     * desc:检查其参数是否是非数字值,整数返回false,非整数返回true.
     *
     * @param numValue
     * @return boolean
     * @throws Exception
     */


    private boolean isNaN(String numValue) throws Exception {
        //长度小于等于0时直接返回true
        if (numValue.length() <= 0)
            return true;

        for (int i = 0, len = numValue.length(); i < len; i++) {
            if (!Character.isDigit(numValue.charAt(i))) {
                return true;
            }
        }
        return false;
    }


    /**
     * 重构getDocument方法
     * author:YihLeego
     * desc:重构getDocument方法,设置defaultTimeout
     *
     * @param url
     * @param pageType
     * @return document
     * @throws Exception
     */
    public Document getDocument(String url, int pageType) throws Exception {
        int defaultTimeout = 60000;
        return getDocument(url, pageType, defaultTimeout);
    }

    /**
     * 获取目标url的DOM
     * author:YihLeego
     * desc:
     *
     * @param url
     * @param pageType
     * @param timeout
     * @return document
     * @throws Exception
     */
    public Document getDocument(String url, int pageType, int timeout) throws Exception {
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

            /*
            *pageType 0-静态页面;指的是爬取内容不需要js动态加载,使用jsoup;
            *pageType 1-动态页面;模拟浏览器,渲染js和css,可以爬取js加载的数据,使用htmlunit;
            */
        if (pageType == 0) {
            //创建Connection
            Connection connect = Jsoup.connect(url);

                /*
                *data;设置请求头;
                *timeout;设置超市时间;
                *ignoreContentType;无视编码类型;
                *get;获取当前页面打DOM;
                */
            document = connect.data(header).timeout(timeout).ignoreContentType(true).get();

        } else if (pageType == 1) {
            // 创建webClient
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

        } else if (pageType == 2) {
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
        }


        return document;
    }

    public Elements getListElements(Document document, String listSelector) throws Exception {
        return document == null ? null : document.select(listSelector);
    }

    public Elements getRowElements(Elements listElements, String rowSelector) throws Exception {
        return listElements == null ? null : listElements.select(rowSelector);
    }

    public List<String> getHrefList(Elements rowElements, String hrefSelector) throws Exception {

        List<String> hrefList = new ArrayList();

        if (rowElements == null)
            return hrefList;

        for (Element hrefElement : rowElements) {

            if (hrefElement != null) {

                if (hrefSelector == null || "".equals(hrefSelector) || hrefSelector == "href" || hrefSelector == "default") {
                    hrefSelector = "abs:href";
                }

                String value = hrefElement.attr(hrefSelector);

                if (value == null || "".equals(value)) {
                    value = hrefElement.attr("href");
                }


                if (!(value.startsWith("http://") || value.startsWith("https://"))) {
                    if (value.startsWith("//"))
                        value = "http:" + value;
                    else
                        value = "http://" + value;
                }

                hrefList.add(value);
            }
        }
        return hrefList;
    }


    public String[] getItemArray(Document document, String[] selectorArray, int[] typeArray) throws Exception {
        List<String> selectorList = Arrays.asList(selectorArray);
        List<Integer> typeList = new ArrayList<Integer>();
        for (int type : typeArray)
            typeList.add(type);
        return (String[]) getItemList(document, selectorList, typeList).toArray();
    }


    public List<String> getItemList(Document document, List<String> selectorList, List<Integer> typeList) throws Exception {

        int size = selectorList.size();

        List<String> itemList = new ArrayList<String>();
        if (document == null)
            return itemList;


        for (int i = 0; i < size; i++) {
            Elements itemElements = document.select(selectorList.get(i));
            int type = typeList.get(i);

            StringBuffer item = new StringBuffer();

            if (itemElements.size() <= 0)
                continue;

            for (int j = 0, len = itemElements.size(); j < len; j++) {
                Element itemElement = itemElements.get(j);
                if (j > 0)
                    item.append(",");
                switch (type) {
                    case 0:
                        item.append(itemElement.text());
                        break;
                    case 1:
                        item.append(itemElement.ownText());
                        break;
                    case 2:
                        item.append(itemElement.html());
                        break;
                    case 3:
                        item.append(itemElement.outerHtml());
                        break;
                    case 4:
                        item.append(itemElement.attr("src"));
                        break;
                    case 5:
                        item.append(itemElement.attr("href"));
                        break;
                    case 6:
                        item.append(itemElement.attr("abs:href"));
                        break;
                    default:
                        item.append(itemElement.text());
                        break;
                }
            }

            itemList.add(item.toString().replace("\"", "\\\""));

        }
        return itemList;
    }


}
