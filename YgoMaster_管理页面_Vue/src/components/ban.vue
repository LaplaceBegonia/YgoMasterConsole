<template>
	<div style="width: 100%;height: 100%;overflow-x: hidden;background-color: #fafafa;user-select:text">
		<div style="width:95%;padding-left: 1%;padding-right: 2.5%;margin-top:1.5%;height:95%;background-color: white;margin-left: 2.5%;margin-right: 2.5%;overflow-y: auto;padding-bottom: 20px;margin-bottom: 20px;"
        v-if="show2"
        >
            <div style="width: 100%;display: flex;flex-direction: row;align-items: center;">
                <p style="font-weight: 700;">禁止卡列表</p>
                <el-button style="width: 150px;margin-left: auto;margin-right:0px;" type="primary" @click="addcard()">添加卡牌</el-button>
            </div>
            <el-table
              
                :data="list[sub]['available']['a0']"
                border
                style="width: 100%">
                <el-table-column
                label="编号"
                
                >
                    <template slot-scope="scope">
                        {{ scope.row }}
                    </template>
                </el-table-column>
                <el-table-column
                label="卡名"
                >
                    <template slot-scope="scope">
                        {{ banlist['a0'][scope.$index] }}
                    </template>
                </el-table-column>
                <el-table-column
                    fixed="right"
                    label="操作"
                    width="120">
                    <template slot-scope="scope">
                        <el-button @click="getinfo(scope.row,scope.$index,'a0')" type="text" size="small">查看</el-button>
                        <el-button @click="delete1(scope.$index,'a0')" type="text" size="small">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <p style="font-weight: 700;">限制卡列表</p>
            <el-table
                :data="list[sub]['available']['a1']"
                border
                style="width: 100%">
                <el-table-column
                label="编号"
                
                >
                    <template slot-scope="scope">
                        {{ scope.row }}
                    </template>
                </el-table-column>
                <el-table-column
                label="卡名"
                >
                    <template slot-scope="scope">
                        {{ banlist['a1'][scope.$index] }}
                    </template>
                </el-table-column>
                <el-table-column
                    fixed="right"
                    label="操作"
                    width="120">
                    <template slot-scope="scope">
                        <el-button @click="getinfo(scope.row,scope.$index,'a1')" type="text" size="small">查看</el-button>
                        <el-button @click="delete1(scope.$index,'a1')" type="text" size="small">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <p style="font-weight: 700;">准限制卡列表</p>
            <el-table
                :data="list[sub]['available']['a2']"
                border
                style="width: 100%">
                <el-table-column
                label="编号"
                
                >
                    <template slot-scope="scope">
                        {{ scope.row }}
                    </template>
                </el-table-column>
                <el-table-column
                label="卡名"
                >
                    <template slot-scope="scope">
                        {{ banlist['a2'][scope.$index] }}
                    </template>
                </el-table-column>
                <el-table-column
                    fixed="right"
                    label="操作"
                    width="120">
                    <template slot-scope="scope">
                        <el-button @click="getinfo(scope.row,scope.$index,'a2')" type="text" size="small">查看</el-button>
                        <el-button @click="delete1(scope.$index,'a2')" type="text" size="small">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <el-dialog
            title="添加卡牌"
            :visible.sync="show"
            width="40%"
           >
           <div>
            <el-form ref="form" :model="cardinfo" label-width="80px">
                <el-radio-group v-model="cardinfo.index" style="margin-bottom: 30px;"
                @change="change"
                >
                    <el-radio-button label="根据卡牌名称"></el-radio-button>
                    <el-radio-button label="根据卡牌ID"></el-radio-button>
                </el-radio-group>
                <el-form-item label="卡牌名称" v-if="cardinfo.index=='根据卡牌名称'">
                    <el-input v-model="cardinfo.name" @change="searchbyname()">
                        <el-button slot="append" icon="el-icon-search" @click="searchbyname()"></el-button>
                    </el-input>
                </el-form-item>
                <el-form-item label="卡牌ID"  v-if="cardinfo.index=='根据卡牌ID'">
                    <el-input v-model="cardinfo.id" @change="searchbyid()">
                        <el-button slot="append" icon="el-icon-search" @click="searchbyid()"></el-button>
                    </el-input>
                </el-form-item>
                <el-descriptions class="margin-top" title="卡牌信息" :column="1"  border v-if="cardinfo.value.cardName!=null">
                    <el-descriptions-item>
                        <template slot="label">
                            <p style="width: 50px;">卡名</p>
                            
                        </template>
                        <div>{{ cardinfo.value.cardName }}</div>
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template slot="label">
                            <p style="width: 50px;">编号</p>
                        </template>
                        <div>{{ cardinfo.value.cardId }}</div>
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template slot="label">
                            <p style="width: 50px;">属性</p>
                        </template>
                        <div style="display: flex;flex-direction: row;">
                            {{ getcardinfo(cardinfo.value) }}
                        </div>
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template slot="label">
                            图片
                        </template>
                        <div>
                            <el-image :src='"https://yxwdbapi.windoent.com/konami/getImageAction?type=1&ciid=1&cid="+cardinfo.id+"&enc="+cardinfo.value.imageKey+"&lang="+cardinfo.value.lang'
                            style="height: 250px;width: auto;"
                            >
                            <el-image slot="placeholder" style="height: 250px;width: auto;" :src="bg"></el-image>
                        </el-image>
                        </div>
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template slot="label">
                            <p style="width: 60px;">效果文本</p>
                        </template>
                        <div v-html=" cardinfo.value.cardText "></div>
                    </el-descriptions-item>
                    <el-descriptions-item v-if="show&&cardinfo.value.relationCardList.length>0">
                        <template slot="label">
                            相关卡牌
                        </template>
                        <div>
                            <el-table
                                :data=" cardinfo.value.relationCardList"
                                style="width: 100%"
                                max-height="200"
                                >
                                <el-table-column
                                    prop="cardName"
                                    label="卡牌名称"
                                    >
                                </el-table-column>
                                <el-table-column
                                    prop="cardId"
                                    label="卡牌编号"
                                    >
                                </el-table-column>
                                <el-table-column
                                    fixed="right"
                                    label="操作"
                                    width="80">
                                    <template slot-scope="scope">
                                        <el-button @click="getinfo2(scope.row.cardId)" type="text" size="small">查看</el-button>
                                    </template>
                                </el-table-column>
                                </el-table>
                        </div>
                    </el-descriptions-item>
                    <el-descriptions-item v-if="cardinfo.value.packList.length>0">
                        <template slot="label">
                            <p style="width: 50px;">发售情况</p>
                        </template>
                        <div style="display: flex;flex-direction: column;align-items: flex-start">
                            <el-table
                                :data="cardinfo.value.packList"
                                style="width: 100%"
                                max-height="200"
                                >
                                <el-table-column
                                    prop="cardName"
                                    label="发售日期"
                                    >
                                    <template slot-scope="scope">
                                        {{ scope.row.releaseDate.split(" ")[0]  }}
                                    </template>
                                </el-table-column>
                                <el-table-column
                                    prop="packName"
                                    label="发售卡包"
                                    >
                                </el-table-column>
                                <el-table-column
                                    prop="cardNo"
                                    label="编号"
                                    >
                                </el-table-column>
                                <el-table-column
                                    label="罕贵度"
                                    >
                                    <template slot-scope="scope">
                                        {{ scope.row.rarityKey+scope.row.rarity}}
                                    </template>
                                </el-table-column>
                            </el-table>
                        </div>
                    </el-descriptions-item>
                </el-descriptions>
               
                <el-form-item  label="设置为"  v-if="cardinfo.value.cardName!=null">
                    <el-radio v-model="cardinfo.type" :label="0">禁止卡</el-radio>
                    <el-radio v-model="cardinfo.type" :label="1">限制卡</el-radio>
                    <el-radio v-model="cardinfo.type" :label="2">准限制卡</el-radio>
                </el-form-item>
                <el-form-item v-if="cardinfo.value.cardName!=null">
                    <el-button type="primary" @click="save()">保存</el-button>
                </el-form-item>
            </el-form>
           </div>
        </el-dialog>

        <el-dialog
            title="卡牌信息"
            :visible.sync="show1"
            width="40%"
           >
           <div>
            <el-form ref="form" :model="info" label-width="80px">
                <el-descriptions class="margin-top" :column="1"  border>
                    <el-descriptions-item>
                        <template slot="label">
                            卡名
                        </template>
                        <div>{{ info.cardName }}</div>
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template slot="label">
                            编号
                        </template>
                        <div>{{ info.cardId }}</div>
                    </el-descriptions-item>
                    <el-descriptions-item v-if="show1">
                        <template slot="label">
                            <p style="width: 50px;">属性</p>
                        </template>
                        <div style="display: flex;flex-direction: row;">
                            {{ getcardinfo(info) }}
                        </div>
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template slot="label">
                            图片
                        </template>
                        <div>
                            <el-image @load="loadimage" :src='"https://yxwdbapi.windoent.com/konami/getImageAction?type=1&ciid=1&cid="+info.cardId+"&enc="+info.imageKey+"&lang="+info.lang'
                            style="height: 250px;width: auto;"
                            >
                                <el-image slot="placeholder" style="height: 250px;width: auto;" :src="bg"></el-image>
                            </el-image>
                        </div>
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template slot="label">
                            效果文本
                        </template>
                        <div v-html=" info.cardText "></div>
                    </el-descriptions-item>
                    <el-descriptions-item v-if="show1&&info.relationCardList.length>0">
                        <template slot="label">
                            相关卡牌
                        </template>
                        <div>
                            <el-table
                                :data="info.relationCardList"
                                style="width: 100%"
                                max-height="200"
                                >
                                <el-table-column
                                    prop="cardName"
                                    label="卡牌名称"
                                    >
                                </el-table-column>
                                <el-table-column
                                    prop="cardId"
                                    label="卡牌编号"
                                    >
                                </el-table-column>
                                <el-table-column
                                    fixed="right"
                                    label="操作"
                                    width="80">
                                    <template slot-scope="scope">
                                        <el-button @click="getinfo1(scope.row.cardId)" type="text" size="small">查看</el-button>
                                    </template>
                                </el-table-column>
                                </el-table>
                        </div>
                    </el-descriptions-item>
                    <el-descriptions-item v-if="info.packList!=null&&info.packList.length>0">
                        <template slot="label">
                            <p style="width: 60px;">发售情况</p>
                        </template>
                        <div style="display: flex;flex-direction: column;align-items: flex-start;width:100%;overflow-x: auto">
                            <el-table
                                :data="info.packList"
                                style="width: 100%"
                                max-height="200"
                                >
                                <el-table-column
                                    prop="cardName"
                                    label="发售日期"
                                    >
                                    <template slot-scope="scope">
                                        {{ scope.row.releaseDate.split(" ")[0]  }}
                                    </template>
                                </el-table-column>
                                <el-table-column
                                    prop="packName"
                                    label="发售卡包"
                                    >
                                </el-table-column>
                                <el-table-column
                                    prop="cardNo"
                                    label="编号"
                                    >
                                </el-table-column>
                                <el-table-column
                                    label="罕贵度"
                                    >
                                    <template slot-scope="scope">
                                        {{ scope.row.rarityKey+scope.row.rarity}}
                                    </template>
                                </el-table-column>
                            </el-table>
                        </div>
                    </el-descriptions-item>
                </el-descriptions>
                <el-form-item  label="设置为" prop="info" v-if="info!=null&&info.type!=null">
                        <el-radio v-model="info.type" :label="0">禁止卡</el-radio>
                        <el-radio v-model="info.type" :label="1">限制卡</el-radio>
                        <el-radio v-model="info.type" :label="2">准限制卡</el-radio>
                </el-form-item>
                <el-form-item>
                        <el-button type="primary" @click="save1()">保存</el-button>
                </el-form-item>
            </el-form>
           </div>
        </el-dialog>
	</div>
</template>

<script>
import {post , get } from "../axios/request.js"
import cardlist from "../cards/zh_cards.json"
import cardlist1 from "../cards/jp_cards.json"
export default {
  name:"ban",
  data() {
    return {
        banlist:[],
        list:[],
        typelist:[],
        info:{
            type:3,
        },
        show:false,
        bg:null,
        sub:'',
        show1:false,
        show2:false,
        cardinfo:{
            name:"",
            id:"",
            type:0,
            index:"根据卡牌名称",
            value:{
                packList:[]
            }
        }
    }
  },
  mounted() {
    this.bg=require("@/assets/css/bg.png");
    post("getbancard",{}).then((res)=>{
        this.list=JSON.parse(JSON.stringify(res.data.data));
        var sub='';
        for(const key in res.data.data){
            if(parseInt(key)>1000&&parseInt(key)<2000){
                this.sub=key
                break;
            }
        }
        var temp = JSON.parse(JSON.stringify(res.data.data[this.sub]["available"]));
        console.log(temp)
        var temp1 = JSON.parse(JSON.stringify(temp));
        for(var x=0;x<cardlist.length;x++){
            if(temp.a0.indexOf(cardlist[x].id)!=-1){
                temp.a0[temp.a0.indexOf(cardlist[x].id)]=cardlist[x].name;
                temp1.a0[temp1.a0.indexOf(cardlist[x].id)]="cn"
            }
            if(temp.a1.indexOf(cardlist[x].id)!=-1){
                temp.a1[temp.a1.indexOf(cardlist[x].id)]=cardlist[x].name;
                temp1.a1[temp1.a1.indexOf(cardlist[x].id)]="cn"
            }
            if(temp.a2.indexOf(cardlist[x].id)!=-1){
                temp.a2[temp.a2.indexOf(cardlist[x].id)]=cardlist[x].name;
                temp1.a2[temp1.a2.indexOf(cardlist[x].id)]="cn"
            }
        }

        for(var x=0;x<cardlist1.length;x++){
            if(temp.a0.indexOf(cardlist1[x].id)!=-1){
                temp.a0[temp.a0.indexOf(cardlist1[x].id)]=cardlist1[x].name;
                temp1.a0[temp1.a0.indexOf(cardlist1[x].id)]="ja"
            }
            if(temp.a1.indexOf(cardlist1[x].id)!=-1){
                temp.a1[temp.a1.indexOf(cardlist1[x].id)]=cardlist1[x].name;
                 temp1.a1[temp1.a1.indexOf(cardlist1[x].id)]="ja"
            }
            if(temp.a2.indexOf(cardlist1[x].id)!=-1){
                temp.a2[temp.a2.indexOf(cardlist1[x].id)]=cardlist1[x].name;
                temp1.a2[temp1.a2.indexOf(cardlist1[x].id)]="ja"
            }
        }
        this.typelist=temp1;
        this.banlist=temp;
        this.show2=true;
    })
  },
  methods: {
    save1(){
        var id = this.info.cardId;
        var temp1 =  this.list[this.sub]["available"]['a0'];
        var temp2 =  this.list[this.sub]["available"]['a1'];
        var temp3 =  this.list[this.sub]["available"]['a2'];
        if(temp1.indexOf(id)!=-1){
            temp1.splice(temp1.indexOf(id),1);
            this.$forceUpdate();
        }
        if(temp2.indexOf(id)!=-1){
            temp2.splice(temp2.indexOf(id),1);
            this.$forceUpdate();
        }
        if(temp3.indexOf(id)!=-1){
            temp3.splice(temp3.indexOf(id),1);
            this.$forceUpdate();
        }
        if(this.info.type==0){
            temp1.push(id);
            this.banlist['a0'][temp1.length-1]=this.info.cardName;
            this.$forceUpdate();
        }
        if(this.info.type==1){
            temp2.push(id);
            this.banlist['a1'][temp2.length-1]=this.info.cardName;
            this.$forceUpdate();
        }
        if(this.info.type==2){
            temp3.push(id);
            this.banlist['a2'][temp3.length-1]=this.info.cardName;
            this.$forceUpdate();
        }
        post("updateban",this.list).then((res)=>{
            if(res.data.code==200){
                this.$message.success("保存成功，重启服务端后生效");
                this.$forceUpdate();
                this.show1=false;
            }
            else{
                this.$message.error(res.data.msg);
            }
        })
    },
    getcardinfo(e){
        var str=" ";
        if(e.attributeName!=null&&e.attributeName.length>0){
            str+=e.attributeName;
        }
        if(e.effectName!=null&&e.effectName.length>0&&str.indexOf(e.effectName)==-1){
            str+=" "+e.effectName;
        }
        if(e.speciesName!=null&&e.speciesName.length>0&&str.indexOf(e.speciesName)==-1){
            str+=" "+e.speciesName;
        }
        if(e.otherItemNameList!=null)
        for(var x=0;x<e.otherItemNameList.length;x++){
            if(str.indexOf(e.otherItemNameList[x])==-1){
                str+=" "+e.otherItemNameList[x];
            }
        }
        if(str.indexOf("超量")!=-1&&e.starchip!=null){
            str+=" 阶级"+e.starchip;
        }
        else if(e.starchip!=null){
            str+=" 等级"+e.starchip;
        }
        return str;
    },
    loadimage(e){

    },
    getinfo(id,x,y){
        var type=this.typelist[y][x];
        get("https://yxwdbapi.windoent.com/konami/card/detail?titleId=1&cardId="+id+"&lang="+type+"&pageSize=10&page=1&sort=1",{}).then(((res)=>{
            if(y=='a0')
                res.data.response.type=0;
            if(y=='a1')
                res.data.response.type=1;
            if(y=='a2')
                res.data.response.type=2;
            this.info=res.data.response;
            this.$forceUpdate();
            this.show1=true;
        }))
    },
    getinfo1(id){
        get("https://yxwdbapi.windoent.com/konami/card/detail?titleId=1&cardId="+id+"&lang=cn&pageSize=10&page=1&sort=1",{}).then(((res)=>{
            if(res.data.response==null){
                get("https://yxwdbapi.windoent.com/konami/card/detail?titleId=1&cardId="+id+"&lang=ja&pageSize=10&page=1&sort=1",{}).then((res)=>{
                    res.data.response.type=3;
                    this.info=res.data.response;
                    this.$forceUpdate();
                })
            }
            else{
                res.data.response.type=3;
                this.info=res.data.response;
                this.$forceUpdate();
            }
        }))
    },
    getinfo2(id){
        get("https://yxwdbapi.windoent.com/konami/card/detail?titleId=1&cardId="+id+"&lang=cn&pageSize=10&page=1&sort=1",{}).then(((res)=>{
            if(res.data.response==null){
                get("https://yxwdbapi.windoent.com/konami/card/detail?titleId=1&cardId="+id+"&lang=ja&pageSize=10&page=1&sort=1",{}).then((res)=>{
                    res.data.response.type=3;
                    this.cardinfo.value=res.data.response;
                    this.cardinfo.id=this.cardinfo.value.cardId;
                    this.cardinfo.name=this.cardinfo.value.cardName;
                    this.$forceUpdate();
                })
            }
            else{
                res.data.response.type=3;
                this.cardinfo.value=res.data.response;
                this.cardinfo.id=this.cardinfo.value.cardId;
                this.cardinfo.name=this.cardinfo.value.cardName;
                this.cardinfo.type=3;
                this.$forceUpdate();
            }
        }))
    },
    delete1(e,i){
        var cardname=this.banlist[i][e];
        var type='';
        var id=this.list[this.sub]["available"][i][e];
        if(i=='a0')type='禁止卡表';if(i=='a1')type='限制卡表';if(i=='a2')type='准限制卡表';
        this.$confirm('是否将 '+cardname+' 从'+type+"移出", '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
            this.list[this.sub]["available"][i].splice(e,1);
            this.banlist[i].splice(e,1);
            post("updateban",this.list).then((res)=>{
            if(res.data.code==200){
                this.$message({
                    type: 'success',
                    message: '删除成功，重启服务端后生效'
                });
            }
            else{
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
    save(){
        var id=this.cardinfo.id;
       
        if(this.cardinfo.type==0){
            let data = this.list[this.sub]["available"]['a0'];
            if(data.indexOf(id)!=-1){
                this.$message.error("此卡已存在于禁止卡表")
                return;
            }
            else{
                data.push(id);
                this.banlist['a0'].push(this.cardinfo.name);
            }
        }
        if(this.cardinfo.type==1){
            let data = this.list[this.sub]["available"]['a1'];
            if(data.indexOf(id)!=-1){
                this.$message.error("此卡已存在于限制卡表")
                return;
            }
            else{
                data.push(id);
                this.banlist['a1'].push(this.cardinfo.name);
            }
        }
        if(this.cardinfo.type==2){
            let data = this.list[this.sub]["available"]['a2'];
            if(data.indexOf(id)!=-1){
                this.$message.error("此卡已存在于准限制卡表")
                return;
            }
            else{
                data.push(id);
                this.banlist['a2'].push(this.cardinfo.name);
            }
        }
        post("updateban",this.list).then((res)=>{
            if(res.data.code==200){
                this.$message.success("添加成功，重启服务端后生效");
                this.show=false;
            }
            else{
                this.$message.error(res.data.msg);
            }
        })
    },
    searchbyid(){
        var id = this.cardinfo.id;
        for(var x=0;x<cardlist.length;x++){
            if(cardlist[x].id==id){
                this.cardinfo.name=cardlist[x].name;
                get("https://yxwdbapi.windoent.com/konami/card/detail?titleId=1&cardId="+this.cardinfo.id+"&lang=cn&pageSize=10&page=1&sort=1",{}).then(((res)=>{
                    this.cardinfo.value=res.data.response;
                }))
                break;
            }
        }
        for(var x=0;x<cardlist1.length;x++){
            if(cardlist1[x].id==id){
                this.cardinfo.name=cardlist1[x].name;
                get("https://yxwdbapi.windoent.com/konami/card/detail?titleId=1&cardId="+this.cardinfo.id+"&lang=cn&pageSize=10&page=1&sort=1",{}).then(((res)=>{
                    this.cardinfo.value=res.data.response;
                }))
                break;
            }
        }
    },
    searchbyname(){
        var name = this.cardinfo.name;
        for(var x=0;x<cardlist.length;x++){
            if(cardlist[x].name==name){
                this.cardinfo.id=cardlist[x].id;
                get("https://yxwdbapi.windoent.com/konami/card/detail?titleId=1&cardId="+this.cardinfo.id+"&lang=cn&pageSize=10&page=1&sort=1",{}).then(((res)=>{
                    this.cardinfo.value=res.data.response;
                }))
                break;
            }
        }
        for(var x=0;x<cardlist1.length;x++){
            if(cardlist1[x].name==name){
                this.cardinfo.id=cardlist1[x].id;
                get("https://yxwdbapi.windoent.com/konami/card/detail?titleId=1&cardId="+this.cardinfo.id+"&lang=cn&pageSize=10&page=1&sort=1",{}).then(((res)=>{
                    this.cardinfo.value=res.data.response;
                }))
                break;
            }
        }
    },
    change(){
        this.cardinfo.name='';
        this.cardinfo.id='';
        this.cardinfo. value={
                packList:[]
        };
    },
    addcard(){
        this.cardinfo={
            name:"",
            id:"",
            type:0,
            index:"根据卡牌名称",
            value:{
                packList:[]
            }
        },
        this.show=true;
    }
  }
}
</script>
<style scoped>

</style>