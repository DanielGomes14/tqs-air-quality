import {API_BASE} from "config/urls";

class WeatherBitService{

    searchbyCity(city_name){

        let url = 'current_quality';

        if (city_name != null) {
            alert("entrei")
            url+='?city_name=' + city_name
        }

        return fetch(API_BASE + url, {
            method: 'GET',
            mode: 'cors',
        }) 
    }
}

export default new WeatherBitService();