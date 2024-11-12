<template>
	<div style="width: 100%;height: 100%;overflow-x: hidden;background-color: #fafafa;">
		<div style="width:95%;padding-left: 1%;padding-right: 2.5%;margin-top:1.5%;height:95%;background-color: white;margin-left: 2.5%;margin-right: 2.5%;overflow-y: auto;padding-bottom: 20px;margin-bottom: 20px;">
            <p style="font-size: 16px;font-weight: 700;margin-top: 10px;">IP端口配置</p>
			<el-form ref="form" :model="info" label-width="200px" style="width: 50%;"
            v-if="code==200"
            >
                <el-form-item label="服务端基础IP">
                    <el-input v-model="info.BaseIP"></el-input>
                </el-form-item>
                <el-form-item label="服务端基础端口">
                    <el-input v-model="info.BasePort"></el-input>
                </el-form-item>
                <el-form-item label="对战TCP服务IP">
                    <el-input v-model="info.SessionServerIP" disabled></el-input>
                </el-form-item>
                <el-form-item label="对战TCP服务端口">
                    <el-input v-model="info.SessionServerPort"></el-input>
                </el-form-item>
                <el-form-item label="绑定IP">
                    <el-input v-model="info.BindIP" disabled></el-input>
                </el-form-item>
            </el-form>
            <p style="font-size: 16px;font-weight: 700;margin-top: 10px;">游戏配置</p>
            <el-form ref="form" :model="info" label-width="200px" style="width: 50%;"
            v-if="code==200"
            >
                <el-form-item label="PVP模式">
                    <el-radio v-model="info.UnlockAllItems" :label="true">开启</el-radio>
                    <el-radio v-model="info.UnlockAllItems" :label="false">关闭</el-radio>
                </el-form-item>
                <el-form-item label="玩家回放对局上限">
                    <el-input-number v-model="info.DuelReplaySaveFileLimit"  :min="1" ></el-input-number>
                </el-form-item>
            </el-form>
            <p style="font-size: 16px;font-weight: 700;margin-top: 10px;">用户默认数据</p>
            <el-form ref="form" :model="info" label-width="200px" style="width: 50%;"
            v-if="code==200"
            >
                <el-form-item label="默认钻石数量">
                    <el-input-number v-model="info.DefaultGems"  :min="1" ></el-input-number>
                </el-form-item>
                <el-form-item label="默认N尘数量">
                    <el-input-number v-model="info.DefaultCraftPoints['3']"  :min="1" ></el-input-number>
                </el-form-item>
                <el-form-item label="默认R尘数量">
                    <el-input-number v-model="info.DefaultCraftPoints['4']"  :min="1" ></el-input-number>
                </el-form-item>
                <el-form-item label="默认SR尘数量">
                    <el-input-number v-model="info.DefaultCraftPoints['5']"  :min="1" ></el-input-number>
                </el-form-item>
                <el-form-item label="默认UR尘数量">
                    <el-input-number v-model="info.DefaultCraftPoints['6']"  :min="1" ></el-input-number>
                </el-form-item>
                <el-form-item label="解锁全部物品">
                    <el-radio v-model="info.UnlockAllItems" :label="true">是</el-radio>
                    <el-radio v-model="info.UnlockAllItems" :label="false">否</el-radio>
                </el-form-item>
                <el-form-item label="解锁全部卡牌">
                    <el-radio v-model="info.UnlockAllCards" :label="true">是</el-radio>
                    <el-radio v-model="info.UnlockAllCards" :label="false">否</el-radio>
                </el-form-item>
                <el-form-item label="解锁全部面闪卡牌">
                    <el-radio v-model="info.UnlockAllCardsShine" :label="true">是</el-radio>
                    <el-radio v-model="info.UnlockAllCardsShine" :label="false">否</el-radio>
                </el-form-item>
                <el-form-item label="解锁全部镜碎卡牌">
                    <el-radio v-model="info.UnlockAllCardsHighestRarity" :label="true">是</el-radio>
                    <el-radio v-model="info.UnlockAllCardsHighestRarity" :label="false">否</el-radio>
                </el-form-item>
                <el-form-item label="解锁全部商店出售物品">
                    <el-radio v-model="info.UnlockAllSoloChapters" :label="true">是</el-radio>
                    <el-radio v-model="info.UnlockAllSoloChapters" :label="false">否</el-radio>
                </el-form-item>
                <el-form-item label="玩家可以合成全部卡牌">
                    <el-radio v-model="info.CardCraftableAll" :label="true">是</el-radio>
                    <el-radio v-model="info.CardCraftableAll" :label="false">否</el-radio>
                </el-form-item>
            </el-form>
            <el-button style="width: 150px;margin-bottom:20px;" type="primary" @click="save()">保存配置</el-button>
		</div>
	</div>
</template>

<script>
import {post} from "../axios/request.js"
export default {
  name:"setting",
  data() {
    return {
        info:{},
        code:0,
    }
  },
  mounted() {
    post("getsetting",{}).then((res)=>{
        if(res.data.code==200){
            this.info=res.data.data;
            this.code=200
        }else{
            this.code=500;
        }
    })
  },
  methods: {
    save(){
        post("/updatesetting",this.info).then((res)=>{
            if(res.data.code==200){
                this.$confirm('配置保存成功，是否重启服务端加载配置?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'success'
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
            else{
                this.$notify.error({
                    title: '保存失败',
                    message: res.data.msg
                });
            }
        })
    }
  }
}
</script>
<style scoped>

</style>