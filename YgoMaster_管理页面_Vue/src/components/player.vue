<template>
	<div style="width: 100%;height: 100%;overflow-x: hidden;background-color: #fafafa;">
		<div style="width:95%;padding-left: 1%;padding-right: 2.5%;margin-top:1.5%;height:95%;background-color: white;margin-left: 2.5%;margin-right: 2.5%;overflow-y: auto;padding-bottom: 20px;margin-bottom: 20px;">
            <el-table
                :data="list"
                size="medium"
                style="width: 100%">
                <el-table-column
                    type="index"
                    width="50">
                </el-table-column>
                <el-table-column
                    prop="info.Name"
                    label="玩家昵称"
                    width="200">
                </el-table-column>
                <el-table-column
                    prop="info.Code"
                    label="玩家代码"
                    width="200">
                </el-table-column>
                <el-table-column
                    prop="info.Token"
                    label="玩家凭证"
                    width="200">
                </el-table-column>
                <el-table-column
                    label="牌组数"
                    width="100">
                    <template slot-scope="scope">
                        {{ scope.row.decks.length }}
                    </template>
                </el-table-column>
                <el-table-column
                    label="近期对战次数"
                    width="120">
                    <template slot-scope="scope">
                        {{ scope.row.replays.length }}
                    </template>
                </el-table-column>
                <el-table-column
                    prop="info.Gems"
                    label="剩余钻石"
                    width="100">
                </el-table-column>
                <el-table-column
                    prop="info.CraftPoints.3"
                    label="剩余N尘"
                    width="100">
                </el-table-column>
                <el-table-column
                    prop="info.CraftPoints.4"
                    label="剩余R尘"
                    width="100">
                </el-table-column>
                <el-table-column
                    prop="info.CraftPoints.5"
                    label="剩余SR尘"
                    width="100">
                </el-table-column>
                <el-table-column
                    prop="info.CraftPoints.6"
                    label="剩余UR尘"
                    width="100">
                </el-table-column>
                <el-table-column
                    fixed="right"
                    label="操作"
                    width="200">
                    <template slot-scope="scope">
                        <el-button @click="huifang(scope.row.replays,scope.$index)" type="text" size="small">回放列表</el-button>
                        <el-button @click="getdecks(scope.row.decks)" type="text" size="small">牌组列表</el-button>
                        <el-button @click="playerinfo=scope.row.info,show3=true" type="text" size="small">编辑</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>

        <el-dialog
            title="回放列表"
            :visible.sync="show2"
            width="80%"
           >
           <div>
                <el-card class="box-card" style="max-height:50vh;overflow-y: auto">
                    <el-table
                        :data="replay"
                        size="medium"
                        style="width: 100%">
                        <el-table-column
                            type="index"
                            width="50">
                        </el-table-column>
                        <el-table-column
                            label="对战日期"
                            prop="time"
                            width="200">
                        </el-table-column>
                        <el-table-column
                            label="对战模式"
                            width="100">
                            <template slot-scope="scope">
                                {{ scope.row.duelinfo.is_pvp?"PVP":"PVE" }}
                            </template>
                        </el-table-column>
                        <el-table-column
                            label="先手方"
                            width="100">
                            <template slot-scope="scope">
                                {{ scope.row.duelinfo.name[scope.row.duelinfo.FirstPlayer] }}
                            </template>
                        </el-table-column>
                        <el-table-column
                            label="后手方"
                            width="100">
                            <template slot-scope="scope">
                                {{  scope.row.duelinfo.name[scope.row.duelinfo.FirstPlayer==0?1:0] }}
                            </template>
                        </el-table-column>
                        <el-table-column
                            label="对战模式"
                            width="100">
                            <template slot-scope="scope">
                                {{ scope.row.duelinfo.is_pvp?"PVP":"PVE" }}
                            </template>
                        </el-table-column>
                        <el-table-column
                            label="回合数"
                            width="100">
                            <template slot-scope="scope">
                                {{ scope.row.duelinfo.turn }}
                            </template>
                        </el-table-column>
                        <el-table-column
                            label="对局时长"
                            width="100">
                            <template slot-scope="scope">
                                {{ scope.row.duelinfo.dueltime }}
                            </template>
                        </el-table-column>
                        <el-table-column
                            label="对局情况"
                            width="130">
                            <template slot-scope="scope">
                                {{ jugeduiju(scope.row.duelinfo)+"获胜" }}
                            </template>
                        </el-table-column>
                        <el-table-column
                            label="是否投降"
                            width="100">
                            <template slot-scope="scope">
                                {{ scope.row.duelinfo.surrender?"是":"否" }}
                            </template>
                        </el-table-column>
                        <el-table-column
                            label="先手方牌组"
                            width="100">
                            <template slot-scope="scope">
                                {{ scope.row.duelinfo.Deck[0].name }}
                            </template>
                        </el-table-column>
                        <el-table-column
                            label="后手方牌组"
                            width="100">
                            <template slot-scope="scope">
                                {{ scope.row.duelinfo.Deck[1].name }}
                            </template>
                        </el-table-column>
                        <el-table-column
                            fixed="right"
                            label="操作"
                            width="80">
                            <template slot-scope="scope">
                                <el-button @click="deletereply(scope.row.filename)" type="text" size="small">删除</el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                </el-card>
           </div>
        </el-dialog>

        <el-dialog
            title="牌组列表"
            :visible.sync="show1"
            width="30%"
           >
           <div>
                <el-card class="box-card" >
                    <div style="margin-top:-20px"></div>
                    <div v-for="(item,index) in decks" :key="o"
                    style="margin-bottom: 10px;font-size: 16px;border-bottom: 0.5px solid #efefef;padding-bottom:20px">

                        <div style=" user-select:text">
                            <p style="font-size:22px;font-weight:700">{{item.deckname.split(".")[0]}}</p>
                            <p style="font-weight: 700;">主牌组</p>
                            <div style="font-weight:200" v-for="(item1,index1) in item.cardlist.m.ids "
                            v-if="(index1==0||item.cardlist.m.ids[index1]!=item.cardlist.m.ids[index1-1])"
                            >
                                {{ (item1)+" *"+item.cardlist.m.r[index1] }}
                            </div>
                            <p style="font-weight: 700;">额外牌组</p>
                            <div style="font-weight:200" v-for="(item1,index1) in item.cardlist.e.ids "
                            v-if="(index1==0||item.cardlist.e.ids[index1]!=item.cardlist.e.ids[index1-1])"
                            >
                                {{ (item1)+" *"+item.cardlist.e.r[index1] }}
                            </div>
                        </div>
                    </div>
                </el-card>
           </div>
        </el-dialog>

        <el-dialog
            title="玩家信息"
            :visible.sync="show3"
            width="40%"
           >
           <div>
                <el-card class="box-card">
                    <el-form ref="form" :model="playerinfo" label-width="100px" style="width:80%;margin-left: 2.5%;">
                        <el-form-item label="玩家昵称">
                            <el-input v-model="playerinfo.Name"></el-input>
                        </el-form-item>
                        <el-form-item label="玩家凭证">
                            <el-input v-model="playerinfo.Token"></el-input>
                        </el-form-item>
                        <el-form-item label="玩家代码">
                            <el-input v-model="playerinfo.Code" disabled></el-input>
                        </el-form-item>
                        <el-form-item label="玩家经验值">
                            <el-input-number v-model="playerinfo.Exp"  :min="1"></el-input-number>
                        </el-form-item>
                        <el-form-item label="玩家钻石数量">
                            <el-input-number v-model="playerinfo.Gems"  :min="1" ></el-input-number>
                        </el-form-item>
                        <el-form-item label="玩家N尘数量" v-if="show3">
                            <el-input-number v-model="playerinfo.CraftPoints['3']"  :min="1" ></el-input-number>
                        </el-form-item>
                        <el-form-item label="玩家R尘数量" v-if="show3">
                            <el-input-number v-model="playerinfo.CraftPoints['4']"  :min="1" ></el-input-number>
                        </el-form-item>
                        <el-form-item label="玩家SR尘数量" v-if="show3">
                            <el-input-number v-model="playerinfo.CraftPoints['5']"  :min="1" ></el-input-number>
                        </el-form-item>
                        <el-form-item label="玩家UR尘数量" v-if="show3">
                            <el-input-number v-model="playerinfo.CraftPoints['6']"  :min="1" ></el-input-number>
                        </el-form-item>
                        <el-form-item label="玩家头像ID">
                            <el-input v-model="playerinfo.AvatarId" disabled></el-input>
                        </el-form-item>
                        <el-form-item label="玩家壁纸ID">
                            <el-input v-model="playerinfo.Wallpaper" disabled></el-input>
                        </el-form-item>
                        <el-button style="width: 150px;margin-bottom:20px;" type="primary" @click="save()">保存玩家信息</el-button>
                    </el-form>

                </el-card>
           </div>
        </el-dialog>
	</div>
</template>

<script>
import {post , get } from "../axios/request.js"
import cardlist from "../cards/zh_cards.json"
import cardlist1 from "../cards/jp_cards.json"
export default {
  name:"player",
  data() {
    return {
        list:[],
        info:{},
        code:0,
        decks:[],
        replay:[],
        playerinfo:{},
        show1:false,
        show2:false,
        show3:false,
        sub:0,
        cardlist:[]
    }
  },
  mounted() {
    post("getplayerlist",{}).then((res)=>{
        if(res.data.code==200){
            this.list=res.data.data;
            for(var i=0;i<this.list.length;i++){
                if(this.list[i].info.Token==''||this.list[i].info.Token==null){
                    this.list.splice(i,1);
                    break;
                }
            }
            this.code=200
        }else{
            this.code=500;
        }
    })
     cardlist;
  },
  methods: {
    jugeduiju(e){
        console.log(e)
        var code = e.code;
        var sub = e.pcode.indexOf(code);
        if(e.res==1)
        return e.name[sub];
        else if(e.res==2){
            if(sub==0)return e.name[1];
            else return e.name[0];
        }
    },
    getdecks(a){
        let e = JSON.parse(JSON.stringify(a));
        for(var x=0;x<e.length;x++){
            let data1=e[x].cardlist.m.ids;
            let data2=e[x].cardlist.m.r;
            data2[0]=1;
            for(var i=1;i<data1.length;i++){
                if(data1[i]==data1[i-1]){
                    data1.splice(i,1);
                    data2[i-1]++;
                    data2.splice(i,1);
                    i--;
                }else{
                    data2[i]=1;
                }
            }
            let data3=e[x].cardlist.e.ids;
            let data4=e[x].cardlist.e.r;
            data4[0]=1;
            for(var i=1;i<data3.length;i++){
                if(data3[i]==data3[i-1]){
                    data3.splice(i,1);
                    data4[i-1]++;
                    data4.splice(i,1);
                    i--;
                }else{
                    data4[i]=1;
                }
            }
        }
        for(var i=0;i<cardlist.length;i++){
            var id = cardlist[i].id;
            for(var x=0;x<e.length;x++){
                if(e[x].cardlist.m.ids.indexOf(id)!=-1){
                    var sub = e[x].cardlist.m.ids.indexOf(id);
                    e[x].cardlist.m.ids[sub] = cardlist[i].name;
                }
                if(e[x].cardlist.e.ids.indexOf(id)!=-1){
                    var sub = e[x].cardlist.e.ids.indexOf(id);
                    e[x].cardlist.e.ids[sub] = cardlist[i].name;
                }
            }
        }
        for(var i=0;i<cardlist1.length;i++){
            var id = cardlist1[i].id;
            for(var x=0;x<e.length;x++){
                if(e[x].cardlist.m.ids.indexOf(id)!=-1){
                    var sub = e[x].cardlist.m.ids.indexOf(id);
                    e[x].cardlist.m.ids[sub] = cardlist1[i].name;
                }
                if(e[x].cardlist.e.ids.indexOf(id)!=-1){
                    var sub = e[x].cardlist.e.ids.indexOf(id);
                    e[x].cardlist.e.ids[sub] = cardlist1[i].name;
                }
            }
        }
        this.decks = e;
        this.show1=true;
    },
    getcardname(id){
        for(var i=0;i<this.cardlist.length;i++){
            if(this.cardlist[i].CardId==id)return this.cardlist[i].CardName;
        }
    },
    save(){
        post("updateplayer",this.playerinfo).then((res)=>{
            if(res.data.code==200){
                this.$message({
                    type: 'success',
                    message: '修改成功'
                });
            }
            else{
                this.$message({
                    type: 'error',
                    message: res.data.msg
                });
            }
        })
    },
    deletereply(e){
        this.$confirm('是否删除此记录?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
            post("deleteplayerreplay",{player:this.list[this.sub].info.Code,filename:e}).then((res)=>{
                if(res.data.code==200){
                    this.$message({
                        type: 'success',
                        message: '删除成功'
                    });
                    let data = this.list[this.sub].replays;
                    for(var i=0;i<data.length;i++){
                        if(data[i].filename==e){
                            data.splice(i,1);
                            break;
                        }
                    }
                }else{
                    this.$message({
                        type: 'error',
                        message: res.data.msg
                    });
                }
            })
         
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });          
        });
    },
    huifang(e,i){
        this.sub=i;
        var name = this.list[i].info.Name;
        var code = this.list[i].info.Code;
        for(var x=0;x<e.length;x++){
           
            let data = e[x].duelinfo;

            if(data.name[0]==name){
                data.duifangname=data.name[1];
                
            }else {
                data.duifangname=data.name[0];
                if(data.FirstPlayer==0)data.FirstPlayer=1;
                else data.FirstPlayer=0;
            }
            data.code=code;
            data.wofangname=name;
            var startime = data.DuelBeginTime;
            var endtime = data.DuelEndTime;
            var time = (endtime-startime)*1;
            let hours = time/3600;
            let minute = time%3600/60
            let second =time%3600%60
            time = "";
            if(hours>=1)time=parseInt(hours)+'小时';
            if(minute>0)time+=parseInt(minute)+'分';
            time+=parseInt(second)+'秒';
            data.dueltime = time;
        }
        this.replay=JSON.parse(JSON.stringify(e)).reverse();
        //this.replay.reverse();
        this.show2=true;
    }
  }
}
</script>
<style scoped>

</style>