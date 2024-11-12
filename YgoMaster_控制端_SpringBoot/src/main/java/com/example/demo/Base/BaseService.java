package com.example.demo.Base;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class BaseService {
    @Value("${filepath}")
    private String filepath;

    @Value("${exepath}")
    private String exepath;

    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();

    public HashMap getnotice(){
        HashMap map1 = new HashMap();
        try{
            File folder = new File(filepath+"\\Notifications");
            File[] files = folder.listFiles();
            List list = new ArrayList();
            List<String> filenames = new ArrayList();
            for(File file : files) {
                String tempname = file.getName();
                if(tempname.substring(tempname.length()-4,tempname.length()).equals("json"));
                filenames.add(tempname);
            }
            for(String i : filenames){
                HashMap file = new HashMap();
                file.put("filename",i);
                HashMap value = objectMapper.readValue(new File(filepath+"\\Notifications\\"+i), HashMap.class);
                file.put("value",value);
                list.add(file);
            }
            map1.put("code",200);
            map1.put("msg","success");
            map1.put("data",list);
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            return map1;
        }
    }

    public HashMap addnotice(HashMap map){
        HashMap map1 = new HashMap();
        try{
            File folder = new File(filepath+"\\Notifications");
            File[] files = folder.listFiles();
            String filename = "Notice_"+files.length+".json";
            List<HashMap> Content = (List)map.get("Contents");
            for(int i=0;i<Content.size();i++){
                HashMap temp = Content.get(i);
                HashMap text = (HashMap) temp.get("text");
                String cn = text.get("zh_CN").toString();

                text.put("zh_CN",cn);
                temp.put("text",text);
                Content.set(i,temp);
            }
            objectMapper.writeValue(new File(filepath+"\\Notifications\\"+filename), map);
            map1.put("code",200);
            map1.put("msg","success");
        }catch (Exception e){
            e.printStackTrace();
            map1.put("code",500);
            map1.put("msg",e.toString());
        }
        finally {
            return map1;
        }
    }

    public HashMap updatenotice(HashMap map){
        HashMap map1 = new HashMap();
        try{
            HashMap list = (HashMap) map.get("data");
            List<HashMap> Content = (List)list.get("Contents");
            String jsonString = JSON.toJSONString(list);
            jsonString = getUnicode(jsonString);
            PrintStream stream=new PrintStream(filepath+"\\Notifications\\"+map.get("filename"));//写入的文件path
            stream.print(jsonString);
            stream.close();
            map1.put("code",200);
            map1.put("msg","success");
        }catch (Exception e){
            e.printStackTrace();
            map1.put("code",500);
            map1.put("msg",e.toString());
        }
        finally {
            return map1;
        }
    }
    private static String getUnicode(String s){
        String result = "";
        char[] c = s.toCharArray();
        for(char tmp:c){
            if(tmp > 255){
                result += "\\u" + Integer.toHexString(tmp);

            }else
                result += tmp;

        }
        return result;
    }
    public HashMap deletenotice(HashMap map){
        HashMap map1 = new HashMap();
        try{
            File file = new File(filepath+"\\Notifications\\"+map.get("filename"));
            if(file.exists()){
                file.delete();
                map1.put("code",200);
                map1.put("msg","success");
            }else{
                map1.put("code",200);
                map1.put("msg","success");
            }
        }catch (Exception e){
            e.printStackTrace();
            map1.put("code",500);
            map1.put("msg",e.toString());
        }
        finally {
            return map1;
        }
    }

    public HashMap getsetting(){
        HashMap map1 = new HashMap();
        try{
            HashMap value = objectMapper.readValue(new File(filepath+"\\Settings.json"), HashMap.class);
            map1.put("code",200);
            map1.put("msg","success");
            map1.put("data",value);
        }catch (Exception e){
            e.printStackTrace();
            map1.put("code",500);
            map1.put("msg",e.toString());
        }
        finally {
            return map1;
        }
    }

    public HashMap updatesetting(HashMap map){
        HashMap map1 = new HashMap();
        try{
            objectMapper.writeValue(new File(filepath+"\\Settings.json"), map);
            map1.put("code",200);
            map1.put("msg","success");
        }catch (Exception e){
            e.printStackTrace();
            map1.put("code",500);
            map1.put("msg",e.toString());
        }
        finally {
            return map1;
        }
    }

    public HashMap getplayerlist(){
        HashMap map1 = new HashMap();
        try{
            File folder = new File(filepath+"\\Players");
            File[] files = folder.listFiles();
            List list = new ArrayList();
            List<String> player = new ArrayList();
            for(File file : files) {
                String tempname = file.getName();
                if(!tempname.equals("Local"));
                    player.add(tempname);
            }
            for(String i : player){
                HashMap playerinfo = new HashMap();
                File deck = new File(filepath+"\\Players\\"+i+"\\Decks");
                File[] decksfile =  deck.listFiles();
                File Replay = new File(filepath+"\\Players\\"+i+"\\Replays");
                File[] Replaysfile = Replay.listFiles();
                List decks = new ArrayList();
                List replays = new ArrayList();
                if(Replaysfile!=null)
                for(File file : Replaysfile){
                    HashMap temp = new HashMap();
                    String filename = file.getName();
                    HashMap duelinfo = objectMapper.readValue(new File(filepath+"\\Players\\"+i+"\\Replays\\"+filename), HashMap.class);
                    List decklist = (List)duelinfo.get("Deck");
                    HashMap deck1 = (HashMap) decklist.get(0);
                    HashMap deck2 = (HashMap) decklist.get(1);
                    deck1.remove("Main");deck2.remove("Main");
                    deck1.remove("Extra");deck2.remove("Extra");
                    decklist.set(0,deck1);decklist.set(1,deck2);
                    duelinfo.put("Deck",decklist);
                    Long timestamp = Long.valueOf(filename.substring(0,filename.length()-5))*1000;
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String time1 = sdf.format(new Date(timestamp));
                    temp.put("filename",filename);
                    temp.put("time",time1);
                    temp.put("duelinfo",duelinfo);
                    replays.add(temp);
                }
                if(decksfile!=null)
                for(File file : decksfile){
                    HashMap temp = new HashMap();
                    String deckname = file.getName();
                    HashMap decklist = objectMapper.readValue(new File(filepath+"\\Players\\"+i+"\\Decks\\"+file.getName()), HashMap.class);
                    temp.put("deckname",deckname);
                    temp.put("cardlist",decklist);
                    decks.add(temp);
                }
                playerinfo.put("decks",decks);
                playerinfo.put("replays",replays);
                File info = new File(filepath+"\\Players\\"+i+"\\Player.json");
                HashMap tempplayerinfo = objectMapper.readValue(info, HashMap.class);
                tempplayerinfo.remove("Cards");
                tempplayerinfo.remove("Items");
                tempplayerinfo.remove("ShopState");
                tempplayerinfo.remove("SoloChapters");
                tempplayerinfo.remove("CardFavorites");
                playerinfo.put("info",tempplayerinfo);
                list.add(playerinfo);
            }
            map1.put("code",200);
            map1.put("msg","success");
            map1.put("data",list);
            return map1;

        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            return map1;
        }
    }
    public HashMap updateplayer(HashMap map){
        HashMap map1 = new HashMap();
        try{
            objectMapper.writeValue(new File(filepath+"\\Players\\"+map.get("Code")+"\\Player.json"), map);
            map1.put("code",200);
            map1.put("msg","success");
        }catch (Exception e){
            e.printStackTrace();
            map1.put("code",500);
            map1.put("msg",e.toString());
        }
        finally {
            return map1;
        }
    }
    public HashMap getplayerreplay(HashMap map){
        HashMap map1 = new HashMap();
        try{

        }catch (Exception e){
            e.printStackTrace();
            map1.put("code",500);
            map1.put("msg",e.toString());
        }
        finally {
            return map1;
        }
    }
    public HashMap deleteplayerreplay(HashMap map){
        HashMap map1 = new HashMap();
        try{
            File file = new File(filepath+"\\Players\\"+map.get("player")+"\\Replays\\"+map.get("filename"));
            if(file.exists()){
                file.delete();
                map1.put("code",200);
                map1.put("msg","success");
            }
        }catch (Exception e){
            e.printStackTrace();
            map1.put("code",500);
            map1.put("msg",e.toString());
        }
        finally {
            return map1;
        }
    }

    public HashMap getbancard(HashMap map){
        HashMap map1 = new HashMap();
        try{
            HashMap value = objectMapper.readValue(new File(filepath+"\\Regulation.json"), HashMap.class);
            map1.put("code",200);
            map1.put("msg","success");
            map1.put("data",value);
        }catch (Exception e){
            e.printStackTrace();
            map1.put("code",500);
            map1.put("msg",e.toString());
        }
        finally {
            return map1;
        }
    }

    public HashMap updateban(HashMap map){
        HashMap map1 = new HashMap();
        try{
            objectMapper.writeValue(new File(filepath+"\\Regulation.json"), map);
            map1.put("code",200);
            map1.put("msg","success");
        }catch (Exception e){
            e.printStackTrace();
            map1.put("code",500);
            map1.put("msg",e.toString());
        }
        finally {
            return map1;
        }
    }

    public HashMap restart(HashMap map){
        HashMap map1 = new HashMap();
        try{
            try {
                String procName = "YgoMaster.exe";
                Process proc = Runtime.getRuntime().exec("tasklist -fi " + '"' + "imagename eq " + procName +'"');
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    if (line.contains(procName)) {
                        System.out.println("服务重启中...");
                        Runtime.getRuntime().exec("tskill YgoMaster");
                    }else{

                    }
                }
            } catch (Exception ex) {

            }
            try {
                Runtime.getRuntime().exec(new String[]{exepath+"\\start.bat"});
            }
            catch (Exception e){
                e.printStackTrace();
            }

            map1.put("code",200);
            map1.put("msg","success");
        }catch (Exception e){
            e.printStackTrace();
            map1.put("code",500);
            map1.put("msg",e.toString());
        }
        finally {
            return map1;
        }
    }

    public HashMap jugeonline(){
        HashMap map1 = new HashMap();
        try{
            String procName = "YgoMaster.exe";
            Process proc = Runtime.getRuntime().exec("tasklist -fi " + '"' + "imagename eq " + procName +'"');
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains(procName)) {
                    map1.put("code",200);
                    map1.put("msg","exist");
                    return map1;
                }else{
                    map1.put("code",300);
                    map1.put("msg","unexist");
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            map1.put("code",500);
            map1.put("msg",e.toString());
        }
        finally {
            return map1;
        }
    }

    public HashMap close(){
        HashMap map1 = new HashMap();
        try{
            String procName = "YgoMaster.exe";
            Process proc = Runtime.getRuntime().exec("tasklist -fi " + '"' + "imagename eq " + procName +'"');
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains(procName)) {
                    Runtime.getRuntime().exec("tskill YgoMaster");
                    System.out.println("服务已关闭");
                    map1.put("code",200);
                    map1.put("msg","success");
                    return map1;
                }else{
                    map1.put("code",300);
                    map1.put("msg","unexist");
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            map1.put("code",500);
            map1.put("msg",e.toString());
        }
        finally {
            return map1;
        }
    }
}
