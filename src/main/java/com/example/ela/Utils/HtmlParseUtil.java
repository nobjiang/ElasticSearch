package com.example.ela.Utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @program: ela
 * @description:
 * @author: zhaol
 * @create: 2020-10-08 15:48
 **/
public class HtmlParseUtil {
    public static void main(String[] args) throws IOException {
        //获取请求
        String url="https://search.jd.com/Search?keyword=java";
        //解析网页,返回的就是浏览器的Document对象
        Document document = Jsoup.parse(new URL(url),3000);
        //所有在js中能使用的方法，这里都能用
        Element element=document.getElementById("J_goodsList");
        //System.out.println(element.html());
        Elements elements=element.getElementsByTag("li");
        //获取元素中的内容
        for(Element el:elements){
            String img = el.getElementsByTag("img").eq(0).attr("data-lazy-img");
            String price = el.getElementsByClass("p-price").eq(0).text();
            String pname = el.getElementsByClass("p-name").eq(0).text();
            System.out.println("=========================================================");
            System.out.println(img);
            System.out.println(price);
            System.out.println(pname);
        }




    }
}