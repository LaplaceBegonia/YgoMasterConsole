package com.example.demo.Base;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.util.*;

@RestController
@ResponseBody
@CrossOrigin
@RequestMapping("/api")
public class BaseController {
    @Autowired
    private BaseService service;

    @PostMapping("/getnotice")
    public HashMap getnotice(@RequestBody HashMap map) {
        return service.getnotice();
    }

    @PostMapping("/addnotice")
    public HashMap addnotice(@RequestBody HashMap map) {
        return service.addnotice(map);
    }

    @PostMapping("/updatenotice")
    public HashMap updatenotice(@RequestBody HashMap map) {
        return service.updatenotice(map);
    }

    @PostMapping("/deletenotice")
    public HashMap deletenotice(@RequestBody HashMap map) {
        return service.deletenotice(map);
    }

    @PostMapping("/getsetting")
    public HashMap getsetting(@RequestBody HashMap map) {
        return service.getsetting();
    }

    @PostMapping("/updatesetting")
    public HashMap updatesetting(@RequestBody HashMap map) {
        return service.updatesetting(map);
    }

    @PostMapping("/getplayerlist")
    public HashMap getplayerlist(@RequestBody HashMap map) {
        return service.getplayerlist();
    }

    @PostMapping("/updateplayer")
    public HashMap updateplayer(@RequestBody HashMap map) {
        return service.updateplayer(map);
    }

    @PostMapping("/getplayerreplay")
    public HashMap getplayerreplay(@RequestBody HashMap map) {
        return service.getplayerreplay(map);
    }



    @PostMapping("/deleteplayerreplay")
    public HashMap deleteplayerreplay(@RequestBody HashMap map) {
        return service.deleteplayerreplay(map);
    }

    @PostMapping("/restart")
    public HashMap restart(@RequestBody HashMap map) {
        return service.restart(map);
    }

    @PostMapping("/getbancard")
    public HashMap getbancard(@RequestBody HashMap map) {
        return service.getbancard(map);
    }

    @PostMapping("/updateban")
    public HashMap updateban(@RequestBody HashMap map) {
        return service.updateban(map);
    }

    @PostMapping("/jugeonline")
    public HashMap jugeonline(@RequestBody HashMap map) {
        return service.jugeonline();
    }
    @PostMapping("/close")
    public HashMap close(@RequestBody HashMap map) {
        return service.close();
    }

    @PostMapping("/getcardlist")
    public HashMap getcardlist(@RequestBody HashMap map) {
        HashMap map1 = new HashMap();
        Map map2 = new HashMap();
        map2.put("sm_and_keyword","true");
        map2.put("page","1");
        List list = new ArrayList();
        for(int i=1;i<=156;i++) {
            Map temp = (Map) JSON.parse(postParams("https://www.pokemon-card.com/card-search/resultAPI.php", i));
            List cardlist = (List)temp.get("cardList");
            for(int x=0;x<cardlist.size();x++){
                Map temp1 = (Map) cardlist.get(x);
                list.add(temp1);
            }
        }
        map1.put("list",list);
        return map1;
    }

    public static String postParams(String url, Integer page) {
        HttpResponse response;
        HttpClient client = HttpClients.createDefault();
        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        entityBuilder.addTextBody("regulation_sidebar_form", "XY");
        entityBuilder.addTextBody("sm_and_keyword", "true");
        entityBuilder.addTextBody("page", page.toString());
        HttpEntity entity = entityBuilder.build();
        HttpPost post = new HttpPost(url);
        post.setEntity(entity);
        try {
            response = client.execute(post);
            return  EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
