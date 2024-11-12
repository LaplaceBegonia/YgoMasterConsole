
import axios from "axios";
//let baseURL = "http://127.0.0.1:8080/api/";
let baseURL = "https://m.begonia.cafe/api/";
//封装对应的get请求
export const get = (url, params) => {
    return new Promise((resolve, reject) => {
        axios.get(url,
            {
                param:{
                    
                },
               
            },
           
        )
        .then(res => resolve(res))
        .catch(err => reject(err));
    });
}
//封装对应的post请求
export const post = (url, param) => {
    return new Promise((resolve, reject) => {
        axios.post(baseURL+url, param,{
           
        })
        .then(res => resolve(res))
        .catch(err => reject(err));
    });
}
export const get1 = (url, params) => {
    return new Promise((resolve, reject) => {
        axios.get(url,
            {
                param:{
                    
                },
               
            },
           
        )
        .then(res => resolve(res))
        .catch(err => reject(err));
    });
}


