<template>
  <div style="height:100%;width:100vw;display: flex;flex-direction: row">
	  <el-row style="height:100%;width:15vw;">
	    <el-col :span="12" style="height: 100%;overflow: auto;width:100%;" class="demo">
	         <div style="width:15vw;height:8vh;display: flex;align-items: center;justify-content: center;background-color: #191a23;color:white;position: fixed;z-index: 99;top:0;">
				Yu-Gi-Oh!  Master Duel
			</div>
			<div style="width:100%;height:8vh;display: flex;align-items: center;justify-content: center;background-color: #191a23;color:white">
				
			</div>
	        <el-menu
			  style="height: 92vh;width:100%"
	          default-active="0"
	          class="el-menu-vertical-demo"
	          background-color="#191a23"
	          text-color="#fff"
	          active-text-color="#3c9cff">  
			  <div  v-for="(item,index) in pagelist" :key="index">
	          <el-submenu  v-if="item.type==2" :index="index+''"
			
			  >
					<template slot="title">
					  <i :class="item.icon"></i>
					  <span>{{item.title}}</span>
					</template>
					<el-menu-item-group>
					  <el-menu-item   @click="page=item1.page" :index="(index+1)+'-'+(index1+1)" v-for="(item1,index1) in item.list">{{item1.title}}</el-menu-item>
					</el-menu-item-group>
					<!---->
	           </el-submenu>
				<el-menu-item :index="index+''"  v-if="item.type==1"   @click="page=item.page">
				    <i :class="item.icon"></i>
				    <span slot="title">{{item.title}}</span>
				</el-menu-item> 
				</div>
	        </el-menu>
	      </el-col>
	  </el-row>
	<div style="width:85vw;heigth:100vh;display: flex;flex-direction: column;">
		<div style="width: 100%;height: 10%;background-color: white;display: flex;align-items: center;flex-direction: row;">
			<div v-if="online" style="display: flex;flex-direction: row;align-items: center;margin-left: auto;">
				<div style="height: 5px;width: 5px;border-radius: 50%;background-color: #5ac725;margin-right: 8px;"></div>
				<p>运行中</p>
			</div>
			<div v-else  style="display: flex;flex-direction: row;align-items: center;margin-left: auto;"> 
				<div style="height: 5px;width: 5px;border-radius: 50%;background-color: #f9ae3d;margin-right: 8px;"></div>
				<p>未运行</p>
			</div>
			<el-button style="width: 120px;margin-left: 15px;margin-right:20px;" type="primary" @click="close()">关闭服务端</el-button>
			<el-button style="width: 120px;margin-left: 0px;margin-right:20px;" type="primary" @click="restart()">重启服务端</el-button>

		</div>
		<setting v-if="page=='setting'" style="height: 90%;overflow-y: hidden;"></setting>
		<player v-if="page=='player'"  style="height: 90%;overflow-y: hidden;"></player>
		<notice v-if="page=='notice'" style="height: 90%;overflow-y: hidden;"></notice>
		<ban v-if="page=='ban'" style="height: 90%;overflow-y: hidden;"></ban>
	</div>
		

</div>
</template>
<script>
import { post } from "../axios/request.js"
import setting from "@/components/setting.vue"
import player from "@/components/player.vue"
import notice from "@/components/notice.vue"
import ban from "@/components/ban.vue"
export default {
components: { 
	setting,player,notice,ban
},
  data() {
    return {
	  ip:"",
	  port:"",
	  page:"setting",
	  visible:false,
	  form:{
		password:"",
		repassword:""
	  },
	  pagelist:[
		{
			type:1,
			title:"服务器配置",
			page:"setting",
			icon:"el-icon-menu"
		},
		{
			type:1,
			title:"玩家列表",
			page:"player",
			icon:"el-icon-menu"
		},
		{
			type:1,
			title:"禁卡列表",
			icon:"el-icon-menu",
			page:"ban"
		},
		{
			type:1,
			title:"公告管理",
			icon:"el-icon-menu",
			page:"notice"
		},
	  ],
	  sub:0,
	  timer:null,
	  online:false,
    }
  },
  mounted() {
	post("jugeonline",{}).then((res)=>{
			if(res.data.code==200){
				that.online=true;
			}else{
				that.online=false;
			}
		})
	post("getsetting",{}).then((res)=>{
		this.ip=res.data.data.BaseIP;
		this.port=res.data.data.BasePort;
    })
	let that=this;
	this.timer= setInterval(() => {
		post("jugeonline",{}).then((res)=>{
			if(res.data.code==200){
				that.online=true;
			}else{
				that.online=false;
			}
		})
	}, 2000);
  },
  methods: {
	close(){
		let that=this;
		this.$confirm('是否关闭服务端?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'info'
            }).then(() => {
                post("close",{}).then((res)=>{
                    if(res.data.code==200){
                        this.$message({
                        type: 'success',
                        message: '关闭成功'
						
          	          });
						post("jugeonline",{}).then((res)=>{
							if(res.data.code==200){
								that.online=true;
							}else{
								that.online=false;
							}
						})
                    }else{
                        this.$message({
                        type: 'error',
                        message:res.data.msg
                    });
                    }
                })
            }).catch(() =>{    
        });
	},
	restart(){
		this.$confirm('是否重启服务端?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'info'
                    }).then(() => {
                        post("restart",{}).then((res)=>{
                            if(res.data.code==200){
                                this.$message({
                                type: 'success',
                                message: '重启成功'
                            });
                            }else{
                                this.$message({
                                type: 'error',
                                message:res.data.msg
                            });
                            }
                        })
                    }).catch(() => {
       
                });
	}
  },
}
</script>
<style>
.demo {
  scrollbar-width: none; /* firefox */
  -ms-overflow-style: none; /* IE 10+ */
  overflow-x: hidden;
  overflow-y: auto;
}
body{
	height:100vh;
	padding:0;
	margin:0;
	overflow: hidden;
}
</style>
