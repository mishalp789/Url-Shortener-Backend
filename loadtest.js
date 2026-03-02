import http from 'k6/http';
import {sleep} from 'k6';

export const options = {
    vus:20,
    duration: '30s'
};

export default function (){
    let res = http.get('http://localhost:8080/api/v1/XFROMo448');
    console.log(res.status);
    sleep(1);
}