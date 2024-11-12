<template>
	<div style="width: 100%;height: 100%;">
		<div v-if="!show" style="width:95%;margin-left: 2.5%;margin-top:1.5%;height:95%;background-color: white;">
      <el-button style="width: 150px;margin-bottom:20px;" type="primary" @click="info=addinfo,show=true,sub=1">添加公告</el-button>
      <el-table
        :data="list"
        :border="true"
        style="width: 100%">
        <el-table-column
        type="index"
          >
        </el-table-column>
        <el-table-column
          label="标题"
          prop="value.Title"
          >
        </el-table-column>
        <el-table-column
                    fixed="right"
                    label="操作"
                    width="120">
                    <template slot-scope="scope">
                        <el-button @click="info=scope.row.value,show=true,sub=2,index=scope.$index" type="text" size="small">编辑</el-button>
                        <el-button @click="delete1(scope.row)" type="text" size="small">删除</el-button>
                    </template>
                </el-table-column>
      </el-table>
		</div>
    <div v-if="show" style="width:95%;margin-left: 2.5%;margin-top:1.5%;height:95%;background-color: white;">
      <el-form ref="form" :model="info" label-width="80px">
        <el-form-item label="公告标题">
          <el-input v-model="info.Title"></el-input>
        </el-form-item>
        <el-form-item label="背景图片">
          <el-select v-model="info.BackImage" placeholder="请选择" style="width:30%">
            <el-option
              v-for="item in imagelist"
              :key="item"
              :label="item"
              :value="item">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="item.tp=='H1'?'小标题':'正文'" v-for="(item,index) in info.Contents">
          <el-input style="width:93%" v-model="item.text['zh_CN']"></el-input>
          <el-button v-if="index>=2" style="margin-left:10px" type="primary" plain @click="delete2(index)">删除</el-button>
        </el-form-item>
      </el-form>
      <div style="display:flex;flex-direction:column;">
        <div style="display:flex;flex-direction:row;">
          <el-button style="width: 150px;margin-bottom:20px;" type="primary" plain @click="addtitle()">添加小标题</el-button>
          <el-button style="width: 150px;margin-bottom:20px;" type="primary" @click="addtext()">添加正文</el-button>
        </div>
        <div style="display:flex;flex-direction:row;align-items:center;margin-top:15px">
          <el-button style="width: 150px;margin-bottom:20px;" type="primary" plain @click="show=false">返回公告列表</el-button>
          <el-button v-if="sub==2" style="width: 150px;margin-bottom:20px;" type="primary" @click="save()">保存公告</el-button>
          <el-button v-else style="width: 150px;margin-bottom:20px;" type="primary" @click="newnotice()">发布公告</el-button>
        </div>
      </div>
    </div>
	</div>
</template>

<script>
import {post} from "../axios/request.js"
export default {
  name:"notice",
  data() {
    return {
      show:false,
      index:0,
      list:[],
      imagelist:[
        "Images/Notification/System/Gate001",
        "Images/Notification/System/Notice001",
        "Images/Notification/System/Notice002",
        "Images/Notification/System/Notice003",
        "Images/Notification/System/Notice004",
        "Images/Notification/System/Notice005"
      ],
      info:{},
      addinfo:{
        "Title": "",
        "Pattern": "NOTIFY",
        "Sort": 1591200,
        "BackImage": "Images/Notification/System/Gate001",
        "Contents": [
          {
            "tp": "H1",
            "text": { 
              "zh_CN":"" 
            }
          },
          {
            "tp": "Text",
            "text": { 
              "zh_CN": "" 
            },
            "indent": -1
          }
        ]
      },
      text: {
            "tp": "Text",
            "text": { 
              "zh_CN": "" 
            },
            "indent": -1
          },
      title:{
        
            "tp": "H1",
            "text": { 
              "zh_CN":"" 
            }
      }
    }
  },
  mounted() {
    post("getnotice",{}).then((res)=>{
      this.list=res.data.data;
    })
  },
  methods: {
    delete2(e){
      this.info.Contents.splice(e,1);
    },
    addtitle(){
      this.info.Contents.push(JSON.parse(JSON.stringify(this.title)));
    },
    addtext(){
      this.info.Contents.push(JSON.parse(JSON.stringify(this.text)));
    },
    delete1(e){
      this.$confirm('是否删除公告'+e.value.Title, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          post("deletenotice",{filename:e.filename}).then((res)=>{
            if(res.data.code==200){
              this.$message({
                type: 'success',
                message: '删除成功'
              });
              post("getnotice",{}).then((res)=>{
                this.list=res.data.data;
              })
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
    save(){
      if(this.info.Title==''||this.info.Title==null){
        this.$notify({
          title: '警告',
          message: '公告标题不能为空',
          type: 'warning'
        });
        return;
      }
      for(var i=0;i<this.info.Contents.length;i++){
        if(this.info.Contents[i].text['zh_CN']==''||this.info.Contents[i].text['zh_CN']==null){
          this.$notify({
          title: '警告',
          message: '小标题和正文内容不能为空',
          type: 'warning'
        });
          return;
        }
      }
      post("updatenotice",{filename:this.list[this.index].filename,data:this.info}).then((res)=>{
        if(res.data.code==200){
          this.$notify({
          title: '成功',
          message: '保存成功',
          type: 'success'
        });
        post("getnotice",{}).then((res)=>{
          this.list=res.data.data;
          this.show=false;
        })
          
        }else{
          this.$notify.error({
          title: '错误',
          message: res.data.msg
        });
        }
      })
    },
    newnotice(){
      if(this.info.Title==''||this.info.Title==null){
        this.$notify({
          title: '警告',
          message: '公告标题不能为空',
          type: 'warning'
        });
        return;
      }
      for(var i=0;i<this.info.Contents.length;i++){
        if(this.info.Contents[i].text['zh_CN']==''||this.info.Contents[i].text['zh_CN']==null){
          this.$notify({
          title: '警告',
          message: '小标题和正文内容不能为空',
          type: 'warning'
        });
          return;
        }
      }
      post("addnotice",this.info).then((res)=>{
        if(res.data.code==200){
          this.$notify({
          title: '成功',
          message: '发布成功',
          type: 'success'
        });
        post("getnotice",{}).then((res)=>{
          this.list=res.data.data;
          this.show=false;
        })
          
        }else{
          this.$notify.error({
          title: '错误',
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